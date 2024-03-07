package org.hermione.minis.context;

import lombok.Getter;
import org.hermione.minis.beans.factory.BeanFactory;
import org.hermione.minis.beans.BeansException;
import org.hermione.minis.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.hermione.minis.beans.factory.config.BeanFactoryPostProcessor;
import org.hermione.minis.beans.factory.support.AbstractBeanFactory;
import org.hermione.minis.beans.factory.support.AutowireCapableBeanFactory;
import org.hermione.minis.beans.factory.xml.XmlBeanDefinitionReader;
import org.hermione.minis.core.ClassPathXmlResource;
import org.hermione.minis.core.Resource;

import java.util.ArrayList;
import java.util.List;

public class ClassPathXmlApplicationContext implements BeanFactory, ApplicationEventPublisher {
    private final AutowireCapableBeanFactory beanFactory;

    @Getter
    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();

    //context负责整合容器的启动过程，读外部配置，解析Bean定义，创建BeanFactory
    public ClassPathXmlApplicationContext(String fileName) {
        this(fileName, true);
    }

    public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) {
        Resource resource = new ClassPathXmlResource(fileName);
        AutowireCapableBeanFactory beanFactory = new AutowireCapableBeanFactory();
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

    //context再对外提供一个getBean，底下就是调用的BeanFactory对应的方法
    @Override
    public Object getBean(String beanName) throws BeansException {
        return this.beanFactory.getBean(beanName);
    }

    @Override
    public Boolean containsBean(String name) {
        return this.beanFactory.containsBean(name);
    }

    @Override
    public boolean isSingleton(String name) {
        return false;
    }

    @Override
    public boolean isPrototype(String name) {
        return false;
    }

    @Override
    public Class<?> getType(String name) {
        return null;
    }

    @Override
    public void publishEvent(ApplicationEvent event) {

    }

    public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor
                                                    postProcessor) {
        this.beanFactoryPostProcessors.add(postProcessor);
    }
    public void refresh() throws BeansException, IllegalStateException {
        // 注册 BeanPostProcessor
        registerBeanPostProcessors(this.beanFactory);
        // 实例化 Bean
        onRefresh();
    }
    private void registerBeanPostProcessors(AutowireCapableBeanFactory beanFactory) {
        beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
    }
    private void onRefresh() {
        this.beanFactory.refresh();
    }

}

