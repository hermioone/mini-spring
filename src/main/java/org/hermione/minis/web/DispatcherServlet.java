package org.hermione.minis.web;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hermione.minis.beans.BeansException;
import org.hermione.minis.web.servlet.HandlerAdapter;
import org.hermione.minis.web.servlet.HandlerMapping;
import org.hermione.minis.web.servlet.HandlerMethod;
import org.hermione.minis.web.servlet.RequestMappingHandlerAdapter;
import org.hermione.minis.web.servlet.RequestMappingHandlerMapping;
import org.hermione.minis.web.view.ModelAndView;
import org.hermione.minis.web.view.View;
import org.hermione.minis.web.view.ViewResolver;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings({"FieldCanBeLocal", "deprecation", "UnusedReturnValue", "CommentedOutCode"})
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
    private ViewResolver viewResolver;


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
        initViewResolvers(this.wac);
    }

    protected void initHandlerMappings(WebApplicationContext wac) {
        this.handlerMapping = new RequestMappingHandlerMapping(wac);
    }

    protected void initHandlerAdapters(WebApplicationContext wac) {
        this.handlerAdapter = new RequestMappingHandlerAdapter(wac);
    }

    protected void initViewResolvers(WebApplicationContext wac) {
        try {
            this.viewResolver = (ViewResolver) wac.getBean("viewResolver");
        } catch (BeansException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
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
        ModelAndView mav = this.handlerAdapter.handle(request, response, handlerMethod);
        render(request, response, mav);
    }

    /* 原始的渲染程序是这样的：
            1. 获取 model，将 model 设置到 request 的 attribute 中
            2. 将请求转发到相应的 jsp

        但这样写存在以下问题：
            1. 转发路径不可配置，必须是 viewName.jsp
            2. 不能处理其他后缀名的转发，比如 .html
            3. DispatcherServlet 也负责前端页面的渲染工作，过于耦合了
    protected void render( HttpServletRequest request, HttpServletResponse response,ModelAndView mv) throws Exception {
        //获取model，写到request的Attribute中：
        Map<String, Object> modelMap = mv.getModel();
        for (Map.Entry<String, Object> e : modelMap.entrySet()) {
            request.setAttribute(e.getKey(),e.getValue());
        }
        //输出到目标JSP
        String sTarget = mv.getViewName();
        String sPath = "/" + sTarget + ".jsp";
        request.getRequestDispatcher(sPath).forward(request, response);
    }*/

    private void render( HttpServletRequest request, HttpServletResponse response,ModelAndView mv) throws Exception {
        if (mv == null) {
            response.getWriter().flush();
            response.getWriter().close();
            return;
        }
        String sTarget = mv.getViewName();
        Map<String, Object> modelMap = mv.getModel();
        View view = resolveViewName(sTarget, modelMap, request);
        Objects.requireNonNull(view).render(modelMap, request, response);
    }

    private View resolveViewName(String viewName, Map<String, Object> model,
                                   HttpServletRequest request) throws Exception {
        if (this.viewResolver != null) {
            return viewResolver.resolveViewName(viewName);
        }
        return null;
    }

}
