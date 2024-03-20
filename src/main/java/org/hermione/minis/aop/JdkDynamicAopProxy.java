package org.hermione.minis.aop;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK 动态代理
 * 动态代理和静态代理的区别就是：动态代理可以代理任何类的对象；而静态代理每一个代理类只能代理某一个具体的类
 */
@Slf4j
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {
    Object target;
    Advisor advisor;

    public JdkDynamicAopProxy(Object target, Advisor advisor) {
        this.target = target;
        this.advisor = advisor;
    }

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(JdkDynamicAopProxy.class.getClassLoader(), target.getClass().getInterfaces(), this);
    }


    /*
    这种方式代码耦合性太高了，代理的业务逻辑也整合在 invoke 方法中，重构将它抽成接口
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("log ******");
        return method.invoke(target, args);
    }*/

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> targetClass = (target != null ? target.getClass() : null);
        MethodInterceptor interceptor = this.advisor.getMethodInterceptor();
        MethodInvocation invocation = new ReflectiveMethodInvocation(proxy, target, method, args, targetClass);
        return interceptor.invoke(invocation);
    }

}
