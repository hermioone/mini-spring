package org.hermione.minis.web.view;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

@SuppressWarnings("deprecation")
@Slf4j
public class InternalResourceViewResolver implements ViewResolver{
    @Getter
    @Setter
    private Class<?> viewClass = null;
    @Getter
    private String viewClassName = "";
    @Getter
    @Setter
    private String prefix = "";
    @Getter
    @Setter
    private String suffix = "";

    @Getter
    @Setter
    private String contentType;

    public InternalResourceViewResolver() {
        this.viewClass = JstlView.class;
    }

    public void setViewClassName(String viewClassName) {
        this.viewClassName = viewClassName;
        Class<?> clz = null;
        try {
            clz = Class.forName(viewClassName);
        } catch (ClassNotFoundException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        setViewClass(clz);
    }

    @Override
    public View resolveViewName(String viewName) throws Exception {
        return buildView(viewName);
    }

    protected View buildView(String viewName) throws Exception {
        Class<?> viewClass = this.viewClass;

        View view = (View) viewClass.newInstance();
        view.setUrl(getPrefix() + viewName + getSuffix());

        String contentType = getContentType();
        view.setContentType(contentType);

        return view;
    }
}
