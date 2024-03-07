package org.hermione.minis.beans.factory.support;

import org.hermione.minis.beans.factory.config.BeanDefinition;

/**
 * 管理 BeanDefinition 的注册器
 */
public interface BeanDefinitionRegistry {
    void registerBeanDefinition(String name, BeanDefinition bd);
    void removeBeanDefinition(String name);
    BeanDefinition getBeanDefinition(String name);
    boolean containsBeanDefinition(String name);
}
