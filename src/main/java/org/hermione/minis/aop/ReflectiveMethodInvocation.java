package org.hermione.minis.aop;

import lombok.Getter;

import java.lang.reflect.Method;

@SuppressWarnings("FieldCanBeLocal")
public class ReflectiveMethodInvocation implements MethodInvocation{
    protected final Object proxy;
    protected final Object target;
    @Getter
    protected final Method method;
    @Getter
    protected Object[] arguments;
    private Class<?> targetClass;
    protected ReflectiveMethodInvocation(Object proxy,  Object target, Method method,  Object[] arguments, Class<?> targetClass) {
        this.proxy = proxy;
        this.target = target;
        this.targetClass = targetClass;
        this.method = method;
        this.arguments = arguments;
    }


    @Override
    public Object getThis() {
        return null;
    }

    @Override
    public Object proceed() throws Throwable {
        return this.method.invoke(this.target, this.arguments);
    }
}
