package org.hermione.minis.beans;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BeanDefinition {
    private String id;
    private String className;

    public BeanDefinition(String id, String className) {
        this.id = id;
        this.className = className;
    }
}
