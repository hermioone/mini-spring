package org.hermione.minis.beans.property;


public interface PropertyEditor {
    void setAsText(String text);

    void setValue(Object value);

    Object getValue();

    Object getAsText();
}
