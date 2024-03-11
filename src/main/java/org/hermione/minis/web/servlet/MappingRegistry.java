package org.hermione.minis.web.servlet;


import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class MappingRegistry {

    /**
     * 保存自定义 @RequestMapping 名称（URL的名称）的列表
     */
    private final List<String> urlMappingNames = new ArrayList<>();

    /**
     * 保存 URL 名称和 Bean 的映射关系
     */
    private final Map<String,Object> mappingObjs = new HashMap<>();

    /**
     * 保存 URL 名称和方法的映射关系
     */
    private final Map<String,Method> mappingMethods = new HashMap<>();

}
