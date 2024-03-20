package org.hermione.minis.aop;

import lombok.Getter;
import lombok.Setter;

@Getter
public class DefaultAdvisor implements Advisor{

    @Setter
    private MethodInterceptor methodInterceptor;
    public DefaultAdvisor() {
    }
}
