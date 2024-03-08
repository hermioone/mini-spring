package org.hermione.minis.beans.factory.config;

import org.hermione.minis.beans.BeansException;
import org.hermione.minis.beans.factory.BeanFactory;

public interface BeanPostProcessor {

    void setBeanFactory(BeanFactory beanFactory);

    Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;

    Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;

}
