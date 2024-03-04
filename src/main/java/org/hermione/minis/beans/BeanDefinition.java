package org.hermione.minis.beans;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BeanDefinition {
    String SCOPE_SINGLETON = "singleton";
    String SCOPE_PROTOTYPE = "prototype";

    /**
     * 是否懒加载
     */
    @Getter
    @Setter
    private boolean lazyInit = false;
    @Getter
    @Setter
    private String[] dependsOn;
    @Getter
    private ArgumentValues constructorArgumentValues;
    @Getter
    private PropertyValues propertyValues;
    @Getter
    @Setter
    private String initMethodName;
    private volatile Object beanClass;
    private String id;
    private String className;

    /**
     * Bean 是单例还是原型
     */
    @Getter
    @Setter
    private String scope = SCOPE_SINGLETON;

    public BeanDefinition(String id, String className) {
        this.id = id;
        this.className = className;
    }

    public boolean hasBeanClass() {
        return (this.beanClass instanceof Class);
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public Class<?> getBeanClass() {
        return (Class<?>) this.beanClass;
    }

    public boolean isSingleton() {
        return SCOPE_SINGLETON.equals(scope);
    }

    public boolean isPrototype() {
        return SCOPE_PROTOTYPE.equals(scope);
    }

    public void setConstructorArgumentValues(ArgumentValues constructorArgumentValues) {
        this.constructorArgumentValues =
                (constructorArgumentValues != null ? constructorArgumentValues : new ArgumentValues());
    }

    public boolean hasConstructorArgumentValues() {
        return !this.constructorArgumentValues.isEmpty();
    }

    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = (propertyValues != null ? propertyValues : new PropertyValues());
    }


}
