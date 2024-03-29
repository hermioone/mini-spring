package org.hermione.minis.beans.factory;

import org.hermione.minis.beans.factory.config.BeanPostProcessor;
import org.hermione.minis.beans.factory.config.SingletonBeanRegistry;

/**
 * 增强 BeanFactory 扩展性：维护 Bean 之间的依赖关系以及支持 Bean 处理器
 */
public interface ConfigurableBeanFactory extends BeanFactory, SingletonBeanRegistry {
    String SCOPE_SINGLETON = "singleton";
    String SCOPE_PROTOTYPE = "prototype";

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    int getBeanPostProcessorCount();

    void registerDependentBean(String beanName, String dependentBeanName);

    String[] getDependentBeans(String beanName);

    String[] getDependenciesForBean(String beanName);
}
