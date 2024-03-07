package org.hermione.minis.beans.factory.config;

import lombok.Getter;
import lombok.Setter;
import org.hermione.minis.beans.PropertyValues;

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
    private boolean lazyInit = true;
    @Getter
    @Setter
    private String[] dependsOn;
    @Getter
    private ConstructorArgumentValues constructorArgumentValues;
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

    public void setConstructorArgumentValues(ConstructorArgumentValues constructorArgumentValues) {
        this.constructorArgumentValues =
                (constructorArgumentValues != null ? constructorArgumentValues : new ConstructorArgumentValues());
    }

    public boolean hasConstructorArgumentValues() {
        return !this.constructorArgumentValues.isEmpty();
    }

    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = (propertyValues != null ? propertyValues : new PropertyValues());
    }


}
