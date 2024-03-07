package org.hermione.minis.beans.factory.config;

import lombok.Getter;
import lombok.Setter;

/**
 * 表示一个构造器参数
 */
@Getter
@Setter
public class ConstructorArgumentValue {
    private Object value;
    private String type;
    private String name;
    public ConstructorArgumentValue(Object value, String type) {
        this.value = value;
        this.type = type;
    }
    public ConstructorArgumentValue(String type, String name, Object value) {
        this.value = value;
        this.type = type;
        this.name = name;
    }
    //省略getter和setter
}
