package org.hermione.minis.beans.property;


import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.hermione.minis.util.NumberUtils;

import java.text.NumberFormat;

public class CustomNumberEditor implements PropertyEditor{
    private final Class<? extends Number> numberClass; //数据类型
    private final NumberFormat numberFormat; //指定格式
    private final boolean allowEmpty;
    @Getter
    private Object value;
    public CustomNumberEditor(Class<? extends Number> numberClass, boolean allowEmpty) throws IllegalArgumentException {
        this(numberClass, null, allowEmpty);
    }
    public CustomNumberEditor(Class<? extends Number> numberClass, NumberFormat numberFormat, boolean allowEmpty) throws IllegalArgumentException {
        this.numberClass = numberClass;
        this.numberFormat = numberFormat;
        this.allowEmpty = allowEmpty;
    }

    //将一个字符串转换成number赋值
    @Override
    public void setAsText(String text) {
        if (this.allowEmpty && StringUtils.isBlank(text)) {
            setValue(null);
        }
        else if (this.numberFormat != null) {
            // 给定格式
            setValue(NumberUtils.parseNumber(text, this.numberClass, this.numberFormat));
        }
        else {
            setValue(NumberUtils.parseNumber(text, this.numberClass));
        }
    }

    //接收Object作为参数
    @Override
    public void setValue(Object value) {
        if (value instanceof Number) {
            this.value = (NumberUtils.convertNumberToTargetClass((Number) value, this.numberClass));
        }
        else {
            this.value = value;
        }
    }

    //将number表示成格式化串
    @Override
    public Object getAsText() {
        Object value = this.value;
        if (value == null) {
            return "";
        }
        if (this.numberFormat != null) {
            // 给定格式.
            return this.numberFormat.format(value);
        }
        else {
            return value.toString();
        }
    }
}
