package org.hermione.minis.aop;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK 动态代理
 */
@Slf4j
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {
    Object target;

    public JdkDynamicAopProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(JdkDynamicAopProxy.class.getClassLoader(), target.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("log ******");
        return method.invoke(target, args);
    }
}
