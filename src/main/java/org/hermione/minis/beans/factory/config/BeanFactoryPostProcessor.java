package org.hermione.minis.beans.factory.config;

import org.hermione.minis.beans.BeansException;
import org.hermione.minis.beans.factory.BeanFactory;

public interface BeanFactoryPostProcessor {
    void postProcessBeanFactory(BeanFactory beanFactory) throws BeansException;
}
