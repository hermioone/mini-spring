package org.hermione.minis.web.servlet;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hermione.minis.web.RequestMapping;
import org.hermione.minis.web.WebApplicationContext;

import java.lang.reflect.Method;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;


@Slf4j
public class RequestMappingHandlerMapping implements HandlerMapping {
    private final WebApplicationContext wac;
    private final MappingRegistry mappingRegistry = new MappingRegistry();

    public RequestMappingHandlerMapping(WebApplicationContext wac) {
        this.wac = wac;
        initMapping();
    }

    //建立URL与调用方法和实例的映射关系，存储在mappingRegistry中
    private void initMapping() {
        Class<?> clz = null;
        Object obj = null;
        String[] controllerNames = this.wac.getBeanDefinitionNames();
        //扫描WAC中存放的所有bean
        for (String controllerName : controllerNames) {
            try {
                clz = Class.forName(controllerName);
                obj = this.wac.getBean(controllerName);
            } catch (Exception e) {
                log.error(ExceptionUtils.getStackTrace(e));
            }
            Method[] methods = Objects.requireNonNull(clz).getDeclaredMethods();
            //检查每一个方法声明
            for (Method method : methods) {
                boolean isRequestMapping = method.isAnnotationPresent(RequestMapping.class);
                //如果该方法带有@RequestMapping注解,则建立映射关系
                if (isRequestMapping) {
                    String methodName = method.getName();
                    String url = method.getAnnotation(RequestMapping.class).value();

                    this.mappingRegistry.getUrlMappingNames().add(url);
                    this.mappingRegistry.getMappingObjs().put(url, obj);
                    this.mappingRegistry.getMappingMethods().put(url, method);
                }
            }
        }
    }

    //根据访问URL查找对应的调用方法
    @Override
    public HandlerMethod getHandler(HttpServletRequest request) throws Exception {
        String sPath = request.getServletPath();
        if (!this.mappingRegistry.getUrlMappingNames().contains(sPath)) {
            return null;
        }
        Method method = this.mappingRegistry.getMappingMethods().get(sPath);
        Object obj = this.mappingRegistry.getMappingObjs().get(sPath);
        return new HandlerMethod(method, obj);
    }
}
