package org.hermione.minis.web;


import org.hermione.minis.beans.PropertyValue;
import org.hermione.minis.beans.PropertyValues;
import org.hermione.minis.beans.property.PropertyEditor;
import org.hermione.minis.util.WebUtils;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * 对于 <a href="http://localhost:8080/test1?name=hermione&age=16">http://localhost:8080/test1?name=hermione&age=16</a> 这个请求来说，Spring MVC 有2种接收参数方式：<br/>
 *  1. String test1(@RequestParam() String name, @RequestParam() int age) <br/>
 *  2. String test1(@ModelAttribute() User user), @ModelAttribute() 通常可以省略 <br/>
 *  <b>这个类主要就是用于处理第2种情况</b><br/>
 */
public class WebDataBinder {

    /**
     * Controller 中被调用方法的参数对象
     */
    private final Object target;
    private final Class<?> targetClass;

    public WebDataBinder(Object target) {
        this.target = target;
        this.targetClass = this.target.getClass();
    }

    //核心绑定方法，将request里面的参数值绑定到目标对象的属性上
    public void bind(HttpServletRequest request) {
        PropertyValues mpvs = assignParameters(request);
        addBindValues(mpvs, request);
        doBind(mpvs);
    }

    private void doBind(PropertyValues mpvs) {
        applyPropertyValues(mpvs);
    }

    //实际将参数值与对象属性进行绑定的方法
    protected void applyPropertyValues(PropertyValues mpvs) {
        getPropertyAccessor().setPropertyValues(mpvs);
    }

    //设置属性值的工具
    protected BeanWrapperImpl getPropertyAccessor() {
        return new BeanWrapperImpl(this.target, this.targetClass);
    }

    //将Request参数解析成PropertyValues
    private PropertyValues assignParameters(HttpServletRequest request) {
        return new PropertyValues(WebUtils.getParameters(request));
    }

    protected void addBindValues(PropertyValues mpvs, HttpServletRequest request) {
    }

    public void registerCustomEditor(Class<?> requiredType, PropertyEditor propertyEditor) {
        getPropertyAccessor().registerCustomEditor(requiredType, propertyEditor);
    }
}
