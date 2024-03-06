package org.hermione.minis;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings({"Lombok", "FieldCanBeLocal"})
public class AServiceImpl implements AService {
    private String name;
    private int level;
    @Getter
    private String property1;
    @Getter
    private String property2;

    public AServiceImpl() {
    }

    public AServiceImpl(String name, int level) {
        this.name = name;
        this.level = level;
        System.out.println(this.name + "," + this.level);
    }

    public void setProperty1(String property1) {
        this.property1 = property1;
    }

    public void setProperty2(String property2) {
        this.property2 = property2;
    }

    @Override
    public void sayHello() {
        System.out.println("sayHello(): " + this.property1 + ", " + this.property2);
    }
}
