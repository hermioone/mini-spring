package org.hermione.minis.context;

import lombok.Getter;
import org.hermione.minis.beans.factory.BeanFactory;
import org.hermione.minis.beans.BeansException;
import org.hermione.minis.beans.factory.ConfigurableListableBeanFactory;
import org.hermione.minis.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.hermione.minis.beans.factory.config.BeanFactoryPostProcessor;
import org.hermione.minis.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.hermione.minis.beans.factory.support.DefaultListableBeanFactory;
import org.hermione.minis.beans.factory.xml.XmlBeanDefinitionReader;
import org.hermione.minis.core.ClassPathXmlResource;
import org.hermione.minis.core.Resource;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class ClassPathXmlApplicationContext extends AbstractApplicationContext {
    DefaultListableBeanFactory beanFactory;

    public ClassPathXmlApplicationContext(String fileName) {
        this(fileName, true);
    }

    public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) {
        Resource resource = new ClassPathXmlResource(fileName);
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(resource);
        this.beanFactory = beanFactory;
        if (isRefresh) {
            try {
                refresh();
            } catch (BeansException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void registerListeners() {
        ApplicationListener listener = new ApplicationListener();
        this.getApplicationEventPublisher().addApplicationListener(listener);
    }

    @Override
    public void initApplicationEventPublisher() {
        ApplicationEventPublisher aep = new SimpleApplicationEventPublisher();
        this.setApplicationEventPublisher(aep);
    }

    @Override
    protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        this.getApplicationEventPublisher().publishEvent(event);
    }

    @Override
    public void addApplicationListener(ApplicationListener listener) {
        this.getApplicationEventPublisher().addApplicationListener(listener);
    }

    public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor) {
        this.getBeanFactoryPostProcessors().add(postProcessor);
    }

    @Override
    public void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
    }

    @Override
    protected void onRefresh() {
        this.beanFactory.refresh();
    }

    @Override
    public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
        return this.beanFactory;
    }

    @Override
    public void finishRefresh() {
        publishEvent(new ContextRefreshEvent("Context Refreshed..."));
    }
}

