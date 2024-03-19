package org.hermione.minis.web.test;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class User {

    private long id;

    private String name;

    private int age;

    private Date birthday;
}
