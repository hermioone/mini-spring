package org.hermione.minis.aop;

import lombok.Getter;
import lombok.Setter;
import org.hermione.minis.beans.factory.FactoryBean;
import org.hermione.minis.util.ClassUtils;

public class ProxyFactoryBean implements FactoryBean<Object> {
    @Getter
    @Setter
    private AopProxyFactory aopProxyFactory;
    @Setter
    private String[] interceptorNames;
    @Setter
    private String targetName;
    /**
     * 被代理对象
     */
    @Setter
    private Object target;
    private final ClassLoader proxyClassLoader = ClassUtils.getDefaultClassLoader();
    /**
     * 代理对象
     */
    private Object singletonInstance;

    public ProxyFactoryBean() {
        this.aopProxyFactory = new DefaultAopProxyFactory();
    }

    @Override
    public Object getObject() throws Exception {//获取内部对象
        return getSingletonInstance();
    }

    /**
     * @return 代理对象
     */
    private synchronized Object getSingletonInstance() {
        if (this.singletonInstance == null) {
            this.singletonInstance = getProxy(createAopProxy());
        }
        return this.singletonInstance;
    }

    /**
     * 生成代理对象
     * @return  代理对象
     */
    private Object getProxy(AopProxy aopProxy) {
        return aopProxy.getProxy();
    }

    private AopProxy createAopProxy() {
        return this.aopProxyFactory.createAopProxy(target);
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }
}
