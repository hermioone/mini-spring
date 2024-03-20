package org.hermione.minis.aop;

public interface AopProxyFactory {
    AopProxy createAopProxy(Object target, Advisor advisor);
}
