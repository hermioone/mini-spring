package org.hermione.minis.beans;

import lombok.Getter;
import lombok.Setter;

/**
 * 表示一个构造器参数
 */
@Getter
@Setter
public class ArgumentValue {
    private Object value;
    private String type;
    private String name;
    public ArgumentValue(Object value, String type) {
        this.value = value;
        this.type = type;
    }
    public ArgumentValue(String type, String name, Object value) {
        this.value = value;
        this.type = type;
        this.name = name;
    }
    //省略getter和setter
}
