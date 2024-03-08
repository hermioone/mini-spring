package org.hermione.minis.beans.factory;

import org.hermione.minis.beans.BeansException;

public interface AutowireCapableBeanFactory  extends BeanFactory{
    int AUTOWIRE_NO = 0;
    int AUTOWIRE_BY_NAME = 1;
    int AUTOWIRE_BY_TYPE = 2;
    Object applyBeanPostProcessorBeforeInitialization(Object existingBean, String beanName) throws BeansException;
    Object applyBeanPostProcessorAfterInitialization(Object existingBean, String beanName) throws BeansException;
}
