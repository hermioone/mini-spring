package org.hermione.minis.context;

import org.hermione.minis.beans.BeansException;
import org.hermione.minis.beans.factory.ConfigurableBeanFactory;
import org.hermione.minis.beans.factory.ConfigurableListableBeanFactory;
import org.hermione.minis.beans.factory.ListableBeanFactory;
import org.hermione.minis.beans.factory.config.BeanFactoryPostProcessor;
import org.hermione.minis.core.env.Environment;
import org.hermione.minis.core.env.EnvironmentCapable;

public interface ApplicationContext extends EnvironmentCapable, ListableBeanFactory, ConfigurableBeanFactory, ApplicationEventPublisher {
    String getApplicationName();

    long getStartupDate();

    ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;

    void setEnvironment(Environment environment);

    Environment getEnvironment();

    void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor);

    void refresh() throws BeansException, IllegalStateException;

    void close();

    boolean isActive();
}
