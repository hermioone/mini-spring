package org.hermione.minis.beans.factory.support;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hermione.minis.beans.BeansException;
import org.hermione.minis.beans.factory.FactoryBean;

@Slf4j
public abstract class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry{
    protected Class<?> getTypeForFactoryBean(final FactoryBean<?> factoryBean) {
        return factoryBean.getObjectType();
    }
    protected Object getObjectFromFactoryBean(FactoryBean<?> factory, String beanName) {
        Object object = doGetObjectFromFactoryBean(factory, beanName);
        try {
            object = postProcessObjectFromFactoryBean(object, beanName);
        } catch (BeansException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return object;
    }

    //从factory bean中获取内部包含的对象
    private Object doGetObjectFromFactoryBean(final FactoryBean<?> factory, final String beanName) {
        Object object = null;
        try {
            object = factory.getObject();
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return object;
    }

    protected Object postProcessObjectFromFactoryBean(Object object, String beanName) throws BeansException {
        return object;
    }
}
