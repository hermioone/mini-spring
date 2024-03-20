package org.hermione.minis.batis;


import lombok.Getter;
import lombok.Setter;

@Getter
public class MapperNode {
    @Setter
    String namespace;
    @Setter
    String id;
    @Setter
    String parameterType;
    @Setter
    String resultType;
    @Setter
    String sql;
    @Setter
    String parameter;


    public String toString() {
        return this.namespace + "." + this.id + " : " + this.sql;
    }
}
