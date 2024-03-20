package org.hermione.minis.aop;

import java.lang.reflect.Method;

public interface MethodInvocation {
    Method getMethod();

    Object[] getArguments();

    Object getThis();

    /**
     * 调用目标方法
     */
    Object proceed() throws Throwable;
}
