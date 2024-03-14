package org.hermione.minis.beans.property;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class PropertyEditorRegistrySupport {
    private static final Map<Class<?>, PropertyEditor> defaultEditors = new HashMap<>();
    private static final Map<Class<?>, PropertyEditor> customEditors = new HashMap<>();

    //注册默认的转换器editor
    static {
        // Default instances of collection editors.
        // 目前只支持了 Number 和 String 类型的转换
        defaultEditors.put(int.class, new CustomNumberEditor(Integer.class, false));
        defaultEditors.put(Integer.class, new CustomNumberEditor(Integer.class, true));
        defaultEditors.put(long.class, new CustomNumberEditor(Long.class, false));
        defaultEditors.put(Long.class, new CustomNumberEditor(Long.class, true));
        defaultEditors.put(float.class, new CustomNumberEditor(Float.class, false));
        defaultEditors.put(Float.class, new CustomNumberEditor(Float.class, true));
        defaultEditors.put(double.class, new CustomNumberEditor(Double.class, false));
        defaultEditors.put(Double.class, new CustomNumberEditor(Double.class, true));
        defaultEditors.put(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, true));
        defaultEditors.put(BigInteger.class, new CustomNumberEditor(BigInteger.class, true));
        defaultEditors.put(String.class, new StringEditor(String.class, true));
    }

    public static PropertyEditor getEditor(Class<?> requiredType) {
        if (defaultEditors.containsKey(requiredType)) {
            return defaultEditors.get(requiredType);
        }
        return customEditors.getOrDefault(requiredType, defaultEditors.get(String.class));
    }

    //获取默认的转换器editor
    public PropertyEditor getDefaultEditor(Class<?> requiredType) {
        return defaultEditors.get(requiredType);
    }

    //注册客户化转换器
    public void registerCustomEditor(Class<?> requiredType, PropertyEditor propertyEditor) {
        customEditors.put(requiredType, propertyEditor);
    }

    //查找客户化转换器
    public PropertyEditor findCustomEditor(Class<?> requiredType) {
        return getCustomEditor(requiredType);
    }

    public boolean hasCustomEditorForElement(Class<?> elementType) {
        return elementType != null && customEditors.containsKey(elementType);
    }

    //获取客户化转换器
    private PropertyEditor getCustomEditor(Class<?> requiredType) {
        if (requiredType == null) {
            return null;
        }
        return customEditors.get(requiredType);
    }
}

