package org.hermione.minis.web.view;


public interface ViewResolver {
    View resolveViewName(String viewName) throws Exception;
}
