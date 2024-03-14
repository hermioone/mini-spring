package org.hermione.minis.web.test;


import org.hermione.minis.beans.property.CustomDateEditor;
import org.hermione.minis.web.WebBindingInitializer;
import org.hermione.minis.web.WebDataBinder;

import java.util.Date;

public class DateInitializer implements WebBindingInitializer {

    @Override
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(Date.class,"yyyy-MM-dd", false));
    }
}

