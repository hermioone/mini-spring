package org.hermione.minis.web;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hermione.minis.web.servlet.HandlerAdapter;
import org.hermione.minis.web.servlet.HandlerMapping;
import org.hermione.minis.web.servlet.HandlerMethod;
import org.hermione.minis.web.servlet.RequestMappingHandlerAdapter;
import org.hermione.minis.web.servlet.RequestMappingHandlerMapping;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings({"FieldCanBeLocal", "deprecation", "UnusedReturnValue"})
@Slf4j
public class DispatcherServlet extends HttpServlet {

    private String sContextConfigLocation;

    /**
     * 子容器
     * 子容器中持有对父容器的引用
     */
    private WebApplicationContext wac;

    /**
     * 父容器
     * Listener 启动的 IoC 容器，因为这个容器启动在前，所以是 parent
     * 这个是 XmlWebApplicationContext
     */
    private WebApplicationContext parentApplicationContext;

    private HandlerMapping handlerMapping;
    private HandlerAdapter handlerAdapter;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.parentApplicationContext = (WebApplicationContext) this.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

        // 获取 web.xml 中 servlet 的 <init-param> 标签
        sContextConfigLocation = config.getInitParameter("contextConfigLocation");

        // 子容器持有对父容器的引用
        // 子容器中加载并初始化 Controller 层的 Bean 对象
        this.wac = new AnnotationConfigWebApplicationContext(sContextConfigLocation, this.parentApplicationContext);

        refresh();
    }

    protected void refresh() {
        // 初始化映射关系
        initHandlerMappings(this.wac);
        initHandlerAdapters(this.wac);
    }

    protected void initHandlerMappings(WebApplicationContext wac) {
        this.handlerMapping = new RequestMappingHandlerMapping(wac);
    }

    protected void initHandlerAdapters(WebApplicationContext wac) {
        this.handlerAdapter = new RequestMappingHandlerAdapter(wac);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse
            response) {
        request.setAttribute(WebApplicationContext.WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.wac);
        try {
            doDispatch(request, response);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }

    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandlerMethod handlerMethod = this.handlerMapping.getHandler(request);
        if (handlerMethod == null) {
            throw new RuntimeException("No matched url: " + request.getServletPath());
        }
        this.handlerAdapter.handle(request, response, handlerMethod);
    }
}
