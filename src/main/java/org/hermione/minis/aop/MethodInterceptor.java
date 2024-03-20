package org.hermione.minis.aop;


/**
 * 调用方法上的拦截器，也就是它实现在某个方法上的增强
 * 拦截器不仅仅会增强逻辑，它内部也会调用业务逻辑方法
 */
public interface MethodInterceptor extends Interceptor {
    Object invoke(MethodInvocation invocation) throws Throwable;
}
