package org.hermione.minis.web.servlet;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hermione.minis.beans.BeansException;
import org.hermione.minis.beans.property.PropertyEditor;
import org.hermione.minis.beans.property.PropertyEditorRegistrySupport;
import org.hermione.minis.util.WebUtils;
import org.hermione.minis.web.WebApplicationContext;
import org.hermione.minis.web.WebBindingInitializer;
import org.hermione.minis.web.WebDataBinder;
import org.hermione.minis.web.WebDataBinderFactory;
import org.hermione.minis.web.common.ModelAttribute;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings({"FieldCanBeLocal", "deprecation", "JavadocLinkAsPlainText"})
@Slf4j
public class RequestMappingHandlerAdapter implements HandlerAdapter {
    private final WebApplicationContext wac;
    private final WebBindingInitializer webBindingInitializer;

    public RequestMappingHandlerAdapter(WebApplicationContext wac) {
        this.wac = wac;
        try {
            this.webBindingInitializer = (WebBindingInitializer) this.wac.getBean("webBindingInitializer");
        } catch (BeansException e) {
            throw new RuntimeException(e);
        }
    }

    public void handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        handleInternal(request, response, (HandlerMethod) handler);
    }

    private void handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
        try {
            invokeHandlerMethod(request, response, handler);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 对于 http://localhost:8080/test1?name=hermione&age=16 这个请求来说，Spring MVC 有2种接收参数方式：
     *  1. String test1(@RequestParam() String name, @RequestParam() int age)
     *  2. String test1(@ModelAttribute() User user), @ModelAttribute() 通常可以省略
     *  这个类主要就是用于处理第2种情况
     */
    protected void invokeHandlerMethod(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
        WebDataBinderFactory binderFactory = new WebDataBinderFactory();
        Parameter[] methodParameters = handlerMethod.getMethod().getParameters();
        Object[] methodParamObjs = new Object[methodParameters.length];
        int i = 0;
        //对调用方法里的每一个参数，处理绑定
        for (Parameter methodParameter : methodParameters) {
            if (methodParameter.isAnnotationPresent(ModelAttribute.class)) {
                // 处理 ModelAttribute 参数注入的情况
                Object methodParamObj = methodParameter.getType().newInstance();
                //给这个参数创建WebDataBinder
                WebDataBinder wdb = binderFactory.createBinder(request, methodParamObj);

                // 支持自定义的 PropertyEditor
                webBindingInitializer.initBinder(wdb);
                wdb.bind(request);
                methodParamObjs[i] = methodParamObj;
            } else {
                // 默认处理 RequestParam 参数注入的情况
                Object parameterValue = WebUtils.getParameter(request, methodParameter.getName());
                PropertyEditor propertyEditor = PropertyEditorRegistrySupport.getEditor(methodParameter.getType());
                propertyEditor.setAsText(String.valueOf(parameterValue));
                methodParamObjs[i] = propertyEditor.getValue();
            }
            i++;
        }
        Method invocableMethod = handlerMethod.getMethod();
        Object returnObj = invocableMethod.invoke(handlerMethod.getBean(), methodParamObjs);
        response.getWriter().append(returnObj.toString());
    }
}
