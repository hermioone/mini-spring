package org.hermione.minis.beans.factory.support;

import org.hermione.minis.beans.factory.config.SingletonBeanRegistry;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DefaultListableBeanFactory 通过继承这个类实现了 ConfigurableBeanFactory 接口中的部分方法：
 *      registerDependentBean、getDependentBeans、getDependenciesForBean
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {
    // 容器中存放所有 bean的名称的列表
    protected List<String> beanNames = new ArrayList<>();
    // 容器中存放所有bean实例的map
    protected final Map<String, Object> singletons = new ConcurrentHashMap<>(256);

    /**
     * 用来维护 Bean 之间依赖关系
     */
    protected final Map<String,Set<String>> dependentBeanMap = new ConcurrentHashMap<>(64);
    protected final Map<String,Set<String>> dependenciesForBeanMap = new ConcurrentHashMap<>(64);

    public void registerSingleton(String beanName, Object singletonObject) {
        synchronized (this.singletons) {
            this.singletons.put(beanName, singletonObject);
            this.beanNames.add(beanName);
        }
    }

    @Override
    public Object getSingleton(String beanName) {
        return this.singletons.get(beanName);
    }

    @Override
    public boolean containsSingleton(String beanName) {
        return this.singletons.containsKey(beanName);
    }

    @Override
    public String[] getSingletonNames() {
        return this.beanNames.toArray(new String[0]);
    }
    protected void removeSingleton(String beanName) {
        synchronized (this.singletons) {
            this.beanNames.remove(beanName);
            this.singletons.remove(beanName);
        }
    }


    public void registerDependentBean(String beanName, String dependentBeanName) {
        Set<String> dependentBeans = this.dependentBeanMap.get(beanName);
        if (dependentBeans != null && dependentBeans.contains(dependentBeanName)) {
            return;
        }

        // No entry yet -> fully synchronized manipulation of the dependentBeans Set
        synchronized (this.dependentBeanMap) {
            dependentBeans = this.dependentBeanMap.computeIfAbsent(beanName, k -> new LinkedHashSet<String>(8));
            dependentBeans.add(dependentBeanName);
        }
        synchronized (this.dependenciesForBeanMap) {
            Set<String> dependenciesForBean = this.dependenciesForBeanMap.computeIfAbsent(dependentBeanName, k -> new LinkedHashSet<String>(8));
            dependenciesForBean.add(beanName);
        }

    }
    public String[] getDependentBeans(String beanName) {
        Set<String> dependentBeans = this.dependentBeanMap.get(beanName);
        if (dependentBeans == null) {
            return new String[0];
        }
        return dependentBeans.toArray(new String[0]);
    }
    public String[] getDependenciesForBean(String beanName) {
        Set<String> dependenciesForBean = this.dependenciesForBeanMap.get(beanName);
        if (dependenciesForBean == null) {
            return new String[0];
        }
        return dependenciesForBean.toArray(new String[0]);
    }
    public boolean hasDependentBean(String beanName) {
        return this.dependentBeanMap.containsKey(beanName);
    }
}
