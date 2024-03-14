package org.hermione.minis.web.servlet;


import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

public class HandlerMethod {
    @Getter
    @Setter
    private  Object bean;
    private  Class<?> beanType;
    @Getter
    @Setter
    private  Method method;
    @Getter
    @Setter
    private  MethodParameter[] parameters;

    @Getter
    @Setter
    private  Class<?> returnType;

    @Getter
    @Setter
    private  String description;

    @Getter
    @Setter
    private  String className;

    @Getter
    @Setter
    private  String methodName;

    public HandlerMethod(Method method, Object obj) {
        this.setMethod(method);
        this.setBean(obj);
    }
}
