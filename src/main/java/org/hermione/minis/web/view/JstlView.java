package org.hermione.minis.web.view;


import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Getter
public class JstlView implements View {
    public static final String DEFAULT_CONTENT_TYPE = "text/html;charset=UTF-8";
    private String contentType = DEFAULT_CONTENT_TYPE;
    private String requestContextAttribute;

    @Setter
    private String beanName;
    @Setter
    private String url;

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setRequestContextAttribute(String requestContextAttribute) {
        this.requestContextAttribute = requestContextAttribute;
    }


    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        for (Entry<String, ?> e : model.entrySet()) {
            request.setAttribute(e.getKey(), e.getValue());
        }
        // Tomcat 会自动转发 /jsp/hellpJsp.jsp 请求到 WebContent/jsp/helloJsp.jsp 页面
        request.getRequestDispatcher(getUrl()).forward(request, response);
    }
}
