package org.hermione.minis.beans;

public class BeansException extends Exception {

    public BeansException() {

    }

    public BeansException(Exception e) {
        super(e);
    }

    public BeansException(String msg) {
        super(msg);
    }
}
