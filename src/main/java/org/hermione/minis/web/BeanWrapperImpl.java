package org.hermione.minis.web;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hermione.minis.beans.property.PropertyEditor;
import org.hermione.minis.beans.property.PropertyEditorRegistrySupport;
import org.hermione.minis.beans.PropertyValue;
import org.hermione.minis.beans.PropertyValues;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 这个类的核心在于利用反射对目标 Bean 对象的属性值进行读写，具体是通过 setter 和 getter 方法
 */
@Slf4j
public class BeanWrapperImpl extends PropertyEditorRegistrySupport {
    /**
     * 目标对象
     */
    private final Object wrappedObject;
    /**
     * 目标对象的 class
     */
    private final Class<?> clz;

    private final Map<String, Field> fields;

    /**
     * 目标对象中的 <参数名，参数值>
     */
    PropertyValues pvs;

    public BeanWrapperImpl(Object object, Class<?> clazz) {
        this.wrappedObject = object;
        this.clz = clazz;
        this.fields = Stream.of(clazz.getDeclaredFields()).collect(Collectors.toMap(Field::getName, field -> field));
    }

    public Object getBeanInstance() {
        return wrappedObject;
    }

    //绑定参数值
    public void setPropertyValues(PropertyValues pvs) {
        this.pvs = pvs;
        for (PropertyValue pv : this.pvs.getPropertyValues()) {
            setPropertyValue(pv);
        }
    }

    //绑定具体某个参数
    public void setPropertyValue(PropertyValue pv) {
        //拿到参数处理器
        BeanPropertyHandler propertyHandler = new BeanPropertyHandler(pv.getName());
        //找到对该参数类型的editor
        PropertyEditor pe = getEditor(propertyHandler.getPropertyClz());
        //设置参数值
        pe.setAsText((String) pv.getValue());
        // 利用反射将值 set 进 wrappedObject 的相应 field 中
        propertyHandler.setValue(pe.getValue());
    }

    //一个内部类，用于处理参数，通过getter()和setter()操作属性
    class BeanPropertyHandler {
        Method writeMethod = null;
        Method readMethod = null;
        @Getter
        Class<?> propertyClz = null;

        public BeanPropertyHandler(String propertyName) {
            try {
                //获取参数对应的属性及类型
                if (fields.containsKey(propertyName)) {
                    Field field = fields.get(propertyName);
                    propertyClz = field.getType();
                    //获取设置属性的方法，按照约定为setXxxx（）
                    this.writeMethod = clz.getDeclaredMethod("set" +
                            propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1), propertyClz);
                    //获取读属性的方法，按照约定为getXxxx（）
                    this.readMethod = clz.getDeclaredMethod("get" +
                            propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1));
                }
            } catch (Exception e) {
                log.error(ExceptionUtils.getStackTrace(e));
            }
        }

        //调用getter读属性值
        public Object getValue() {
            Object result = null;
            writeMethod.setAccessible(true);
            try {
                result = readMethod.invoke(wrappedObject);
            } catch (Exception e) {
                log.error(ExceptionUtils.getStackTrace(e));
            }
            return result;
        }

        //调用setter设置属性值
        public void setValue(Object value) {
            writeMethod.setAccessible(true);
            try {
                writeMethod.invoke(wrappedObject, value);
            } catch (Exception e) {
                log.error(ExceptionUtils.getStackTrace(e));
            }
        }
    }
}
