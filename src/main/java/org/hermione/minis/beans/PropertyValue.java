package org.hermione.minis.beans;

import lombok.Getter;

/**
 * 表示一个 setter 注入的参数
 */
@Getter
public class PropertyValue {
    private final String type;
    private final String name;
    private final Object value;
    private final boolean isRef;

    public PropertyValue(String type, String name, Object value, boolean isRef) {
        this.type = type;
        this.name = name;
        this.value = value;
        this.isRef = isRef;
    }

    public PropertyValue(String name, Object value) {
        this("", name, value, false);
    }

    public boolean getIsRef() {
        return isRef;
    }
}
