package org.hermione.minis.aop;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TracingInterceptor implements MethodInterceptor {
    public Object invoke(MethodInvocation i) throws Throwable {
        log.info("method {} is called on {} with args {}", i.getMethod(), i.getThis(), i.getArguments());

        // 真正调用目标方法
        Object ret=i.proceed();
        log.info("method {} returns {}", i.getMethod(), ret);
        return ret;
    }
}
