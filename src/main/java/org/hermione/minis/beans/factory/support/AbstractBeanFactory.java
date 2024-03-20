package org.hermione.minis.beans.factory.support;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hermione.minis.beans.BeansException;
import org.hermione.minis.beans.PropertyValue;
import org.hermione.minis.beans.PropertyValues;
import org.hermione.minis.beans.factory.BeanFactory;
import org.hermione.minis.beans.factory.ConfigurableBeanFactory;
import org.hermione.minis.beans.factory.config.BeanDefinition;
import org.hermione.minis.beans.factory.config.ConstructorArgumentValue;
import org.hermione.minis.beans.factory.config.ConstructorArgumentValues;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("UnusedReturnValue")
@Slf4j
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory, BeanDefinitionRegistry {
    protected final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
    protected final List<String> beanDefinitionNames = new ArrayList<>();
    protected final Map<String, Object> earlySingletonObjects = new HashMap<>(16);

    public AbstractBeanFactory() {
    }

    /**
     * Spring 对外提供了一个很重要的包装方法：refresh()。
     * 具体的包装方法也很简单，就是对所有的 Bean 调用了一次 getBean()，
     * 利用 getBean() 方法中的 createBean() 创建 Bean 实例，
     * 就可以只用一个方法把容器中所有的 Bean 的实例创建出来了。
     */
    public void refresh() {
        for (String beanName : beanDefinitionNames) {
            try {
                getBean(beanName);
            } catch (BeansException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Object getBean(String beanName) throws BeansException {
        // 先尝试直接从容器中获取bean实例
        Object singleton = this.getSingleton(beanName);
        if (singleton == null) {
            // 如果没有实例，则尝试从毛胚实例中获取
            singleton = this.earlySingletonObjects.get(beanName);
            if (singleton == null) {
                // 如果连毛胚都没有，则创建bean实例并注册
                log.info("Create bean: -------------- {}", beanName);
                BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
                if (beanDefinition == null) {
                    return null;
                }
                singleton = createBean(beanDefinition);
                this.registerBean(beanName, singleton);
                // 进行 beanpostprocessor 处理
                // step 1: postProcessBeforeInitialization
                applyBeanPostProcessorBeforeInitialization(singleton, beanName);
                // step 2: init-method
                if (StringUtils.isNotBlank(beanDefinition.getInitMethodName())) {
                    invokeInitMethod(beanDefinition, singleton);
                }
                // step 3: postProcessAfterInitialization
                applyBeanPostProcessorAfterInitialization(singleton, beanName);
            }
        }

        return singleton;
    }

    private void invokeInitMethod(BeanDefinition beanDefinition, Object obj) throws BeansException {
        try {
            Class<?> clz = Class.forName(beanDefinition.getClassName());
            Method method = null;
            try {
                method = clz.getMethod(beanDefinition.getInitMethodName());
                method.invoke(obj);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new BeansException(e);
            }
        } catch (Exception e) {
            throw new BeansException(e);
        }
    }

    @Override
    public boolean containsBean(String name) {
        return containsSingleton(name);
    }

    public void registerBean(String beanName, Object obj) {
        this.registerSingleton(beanName, obj);
    }

    @Override
    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(name, beanDefinition);
        this.beanDefinitionNames.add(name);
        // 在 registerBeanDefinition 时不能再 createBean，因为如果 bean 之间有相互依赖时这里会有问题
        // 所以先将所有的 BeanDefinition 注册完成后，再统一在 refresh() 方法中初始化 bean
        /*if (!beanDefinition.isLazyInit()) {
            try {
                getBean(name);
            } catch (BeansException ignored) {
            }
        }*/
    }

    @Override
    public void removeBeanDefinition(String name) {
        this.beanDefinitionMap.remove(name);
        this.beanDefinitionNames.remove(name);
        this.removeSingleton(name);
    }

    @Override
    public BeanDefinition getBeanDefinition(String name) {
        return this.beanDefinitionMap.get(name);
    }

    @Override
    public boolean containsBeanDefinition(String name) {
        return this.beanDefinitionMap.containsKey(name);
    }

    @Override
    public boolean isSingleton(String name) {
        return this.beanDefinitionMap.get(name).isSingleton();
    }

    @Override
    public boolean isPrototype(String name) {
        return this.beanDefinitionMap.get(name).isPrototype();
    }

    @Override
    public Class<?> getType(String name) {
        return this.beanDefinitionMap.get(name).getClass();
    }

    private Object createBean(BeanDefinition beanDefinition) throws BeansException {
        Class<?> clz = null;
        //创建毛胚 bean 实例，处理 constructor 注入属性
        Object obj = doCreateBean(beanDefinition);
        //存放到毛胚实例缓存中
        this.earlySingletonObjects.put(beanDefinition.getId(), obj);
        try {
            clz = Class.forName(beanDefinition.getClassName());
        } catch (ClassNotFoundException e) {
            throw new BeansException(e);
        }
        //完善bean，主要是处理 setter 注入属性
        populateBean(beanDefinition, clz, obj);
        return obj;
    }

    //doCreateBean创建毛胚实例，仅仅调用构造方法，没有进行属性处理
    @SuppressWarnings("deprecation")
    private Object doCreateBean(BeanDefinition beanDefinition) throws BeansException {
        Class<?> clz = null;
        Object obj = null;
        Constructor<?> con = null;
        try {
            clz = Class.forName(beanDefinition.getClassName());
            // handle constructor
            ConstructorArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();
            if (!constructorArgumentValues.isEmpty()) {
                Class<?>[] paramTypes = new Class<?>[constructorArgumentValues.getArgumentCount()];
                Object[] paramValues = new Object[constructorArgumentValues.getArgumentCount()];
                for (int i = 0; i < constructorArgumentValues.getArgumentCount(); i++) {
                    ConstructorArgumentValue constructorArgumentValue = constructorArgumentValues.getIndexedArgumentValue(i);
                    if ("String".equals(constructorArgumentValue.getType()) || "java.lang.String".equals(constructorArgumentValue.getType())) {
                        paramTypes[i] = String.class;
                        paramValues[i] = constructorArgumentValue.getValue();
                    } else if ("Integer".equals(constructorArgumentValue.getType()) || "java.lang.Integer".equals(constructorArgumentValue.getType())) {
                        paramTypes[i] = Integer.class;
                        paramValues[i] = Integer.valueOf((String) constructorArgumentValue.getValue());
                    } else if ("int".equals(constructorArgumentValue.getType())) {
                        paramTypes[i] = int.class;
                        paramValues[i] = Integer.valueOf((String) constructorArgumentValue.getValue());
                    } else {
                        paramTypes[i] = String.class;
                        paramValues[i] = constructorArgumentValue.getValue();
                    }
                }
                con = clz.getConstructor(paramTypes);
                obj = con.newInstance(paramValues);
            } else {
                obj = clz.newInstance();
            }
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException e) {
            throw new BeansException(e);
        }
        return obj;
    }

    private void populateBean(BeanDefinition beanDefinition, Class<?> clz, Object obj) throws BeansException {
        handleProperties(beanDefinition, clz, obj);
    }

    private void handleProperties(BeanDefinition beanDefinition, Class<?> clz, Object obj) throws BeansException {
        // handle properties
        log.info("handle properties for bean: {}", beanDefinition.getId());
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        //如果有属性
        if (!propertyValues.isEmpty()) {
            for (int i = 0; i < propertyValues.size(); i++) {
                PropertyValue propertyValue =
                        propertyValues.getPropertyValueList().get(i);
                String pType = propertyValue.getType();
                String pName = propertyValue.getName();
                Object pValue = propertyValue.getValue();
                boolean isRef = propertyValue.getIsRef();
                Class<?>[] paramTypes = new Class<?>[1];
                Object[] paramValues = new Object[1];
                if (!isRef) {
                    // 如果不是ref，只是普通属性
                    // 对每一个属性，分数据类型分别处理
                    if ("String".equals(pType) || "java.lang.String".equals(pType)) {
                        paramTypes[0] = String.class;
                        paramValues[0] = pValue.toString();
                    } else if ("Integer".equals(pType) || "java.lang.Integer".equals(pType)) {
                        paramTypes[0] = Integer.class;
                        paramValues[0] = Integer.parseInt(pValue.toString());
                    } else if ("int".equals(pType)) {
                        paramTypes[0] = int.class;
                        paramValues[0] = Integer.parseInt(pValue.toString());
                    } else {
                        paramTypes[0] = String.class;
                        paramValues[0] = pValue.toString();
                    }

                } else {
                    // is ref, create the dependent beans
                    try {
                        paramTypes[0] = Class.forName(pType);
                        // 再次调用 getBean 创建ref的bean实例
                        paramValues[0] = getBean((String) pValue);
                    } catch (ClassNotFoundException | BeansException e) {
                        throw new BeansException(e);
                    }
                }
                //按照setXxxx规范查找setter方法，调用setter方法设置属性
                String methodName = "set" + pName.substring(0, 1).toUpperCase()
                        + pName.substring(1);
                Method method = null;
                try {
                    method = clz.getMethod(methodName, paramTypes);
                    method.invoke(obj, paramValues);
                } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                    throw new BeansException(e);
                }
            }
        }
    }

    public abstract Object applyBeanPostProcessorBeforeInitialization(Object existingBean, String beanName) throws BeansException;

    public abstract Object applyBeanPostProcessorAfterInitialization(Object existingBean, String beanName) throws BeansException;
}
