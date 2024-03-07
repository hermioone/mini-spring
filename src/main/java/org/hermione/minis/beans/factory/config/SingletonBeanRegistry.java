package org.hermione.minis.beans.factory.config;

/**
 * 单例 Bean对象 的注册器
 */
public interface SingletonBeanRegistry {
    void registerSingleton(String beanName, Object singletonObject);
    Object getSingleton(String beanName);
    boolean containsSingleton(String beanName);
    String[] getSingletonNames();
}
