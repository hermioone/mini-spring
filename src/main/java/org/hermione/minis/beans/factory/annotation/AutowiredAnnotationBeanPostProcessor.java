package org.hermione.minis.beans.factory.annotation;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hermione.minis.beans.BeansException;
import org.hermione.minis.beans.factory.config.BeanPostProcessor;
import org.hermione.minis.beans.factory.support.AutowireCapableBeanFactory;

import java.lang.reflect.Field;

@Slf4j
@Getter
@Setter
public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {
    private AutowireCapableBeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        //对每一个属性进行判断，如果带有@Autowired注解则进行处理
        for (Field field : fields) {
            boolean isAutowired = field.isAnnotationPresent(Autowired.class);
            if (isAutowired) {
                // 根据属性名查找同名的bean
                Autowired annotation = field.getDeclaredAnnotation(Autowired.class);
                String fieldName = annotation.value();
                Object autowiredObj = this.getBeanFactory().getBean(fieldName);
                //设置属性值，完成注入
                try {
                    field.setAccessible(true);
                    field.set(bean, autowiredObj);
                    log.info("autowire {} for bean {}", fieldName, beanName);
                } catch (IllegalAccessException e) {
                    throw new BeansException(e);
                }
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }
}

