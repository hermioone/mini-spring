package org.hermione.minis.web;


import lombok.Getter;
import lombok.Setter;

@Getter
public class MappingValue {

    @Setter
    String uri;

    @Setter
    String clz;

    @Setter
    String method;

    public MappingValue(String uri, String clz, String method) {
        this.uri = uri;
        this.clz = clz;
        this.method = method;
    }
}
