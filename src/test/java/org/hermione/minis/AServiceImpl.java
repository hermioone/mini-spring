package org.hermione.minis;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings({"Lombok", "FieldCanBeLocal"})
public class AServiceImpl implements AService {
    private String name;
    private int level;
    @Getter
    @Setter
    private String property1;
    @Getter
    @Setter
    private String property2;
    @Getter
    @Setter
    private BaseService ref1;

    public AServiceImpl() {
    }

    public AServiceImpl(String name, int level) {
        this.name = name;
        this.level = level;
        System.out.println(this.name + "," + this.level);
    }

    @Override
    public void sayHello() {
        System.out.println("-------- AServiceImpl start --------");
        System.out.println("sayHello(): " + this.property1 + ", " + this.property2);
        ref1.sayHello();
        System.out.println("-------- AServiceImpl end --------");
    }
}
