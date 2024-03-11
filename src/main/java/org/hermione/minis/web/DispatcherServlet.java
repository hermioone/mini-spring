package org.hermione.minis.web;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hermione.minis.beans.BeansException;
import org.hermione.minis.beans.factory.annotation.Autowired;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * DispatcherServlet 就是 MVC 容器，在 DispatcherServlet 中可以访问子容器 WebApplicationContext
 * 但是在 子容器中不能访问父容器 DispatcherServlet
 */
@SuppressWarnings({"FieldCanBeLocal", "deprecation", "UnusedReturnValue"})
@Slf4j
public class DispatcherServlet extends HttpServlet {

    private String sContextConfigLocation;

    private WebApplicationContext wac;


    /**
     * 存储需要扫描的 package 列表
     */
    private final List<String> packageNames = new ArrayList<>();

    /**
     * 存储 controller 的名字和 Bean 的映射关系
     */
    private final Map<String, Object> controllerObjs = new HashMap<>();

    /**
     * 存储 controller 的名字
     */
    private final List<String> controllerNames = new ArrayList<>();

    /**
     * 存储 controller 的名字和类的映射关系
     */
    private final Map<String, Class<?>> controllerClasses = new HashMap<>();

    /**
     * 保存自定义 @RequestMapping 名称（URL的名称）的列表
     */
    private final List<String> urlMappingNames = new ArrayList<>();

    /**
     * 保存 URL 名称和 Bean 的映射关系
     */
    private final Map<String, Object> mappingObjs = new HashMap<>();

    /**
     * 保存 URL 名称和方法的映射关系
     */
    private final Map<String, Method> mappingMethods = new HashMap<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.wac = (WebApplicationContext) this.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

        // 1. 解析 minis-servlet.xml 中定义的 Bean
        // 获取 web.xml 中 servlet 的 <init-param> 标签
        sContextConfigLocation = config.getInitParameter("contextConfigLocation");
        URL xmlPath = null;
        try {
            xmlPath = this.getServletContext().getResource(sContextConfigLocation);
        } catch (MalformedURLException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }

        this.packageNames.addAll(XmlScanComponentHelper.getNodeValue(xmlPath));

        // 2. 将 Bean 实例化
        refresh();
    }

    protected void refresh() {
        // 初始化 controller
        initController();
        // 初始化映射关系
        initMapping();
    }

    protected void initController() {
        this.controllerNames.addAll(scanPackages(this.packageNames));

        for (String controllerName : this.controllerNames) {
            Object obj = null;
            Class<?> clz = null;
            try {
                clz = Class.forName(controllerName);
                this.controllerClasses.put(controllerName,clz);
            } catch (ClassNotFoundException e) {
                log.error(ExceptionUtils.getStackTrace(e));
            }
            try {
                // 实例化 Controller
                // 事实上这里也应该考虑通过将 Controller 的实例化和初始化放在 IoC 容器中，因为 Controller 可能只有有参构造函数
                obj = Objects.requireNonNull(clz).newInstance();
                // 渲染 Controller 中的 bean field
                populateBean(obj,controllerName);

                this.controllerObjs.put(controllerName, obj);
            } catch (InstantiationException | IllegalAccessException | BeansException e) {
                log.error(ExceptionUtils.getStackTrace(e));
            }
        }
    }

    protected Object populateBean(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            boolean isAutowired = field.isAnnotationPresent(Autowired.class);
            if (isAutowired) {
                String fieldBeanName = field.getAnnotation(Autowired.class).value();
                Object autowiredObj = this.wac.getBean(fieldBeanName);
                try {
                    field.setAccessible(true);
                    field.set(bean, autowiredObj);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    log.error(ExceptionUtils.getStackTrace(e));
                }

            }
        }

        return bean;
    }

    private List<String> scanPackages(List<String> packages) {
        List<String> tempControllerNames = new ArrayList<>();
        for (String packageName : packages) {
            tempControllerNames.addAll(scanPackage(packageName));
        }
        return tempControllerNames;
    }

    private List<String> scanPackage(String packageName) {
        List<String> tempControllerNames = new ArrayList<>();
        URI uri = null;
        //将以.分隔的包名换成以/分隔的uri
        try {
            uri = Objects.requireNonNull(
                    this.getClass().getResource("/" + packageName.replaceAll("\\.", "/"))
            ).toURI();
        } catch (Exception ignored) {
        }
        File dir = new File(Objects.requireNonNull(uri));
        //处理对应的文件目录
        for (File file : Objects.requireNonNull(dir.listFiles())) { //目录下的文件或者子目录
            if (file.isDirectory()) { //对子目录递归扫描
                scanPackage(packageName + "." + file.getName());
            } else { //类文件
                String controllerName = packageName + "." + file.getName().replace(".class", "");
                tempControllerNames.add(controllerName);
            }
        }
        return tempControllerNames;
    }


    private void initMapping() {
        for (String controllerName : this.controllerNames) {
            Class<?> clazz = this.controllerClasses.get(controllerName);
            Object obj = this.controllerObjs.get(controllerName);
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                //检查所有的方法
                boolean isRequestMapping = method.isAnnotationPresent(RequestMapping.class);
                if (isRequestMapping) { //有RequestMapping注解
                    String methodName = method.getName();
                    //建立方法名和URL的映射
                    String urlMapping = method.getAnnotation(RequestMapping.class).value();
                    this.urlMappingNames.add(urlMapping);
                    this.mappingObjs.put(urlMapping, obj);
                    this.mappingMethods.put(urlMapping, method);
                }
            }
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sPath = request.getServletPath();
        if (!this.urlMappingNames.contains(sPath)) {
            return;
        }
        Object obj = null;
        Object objResult = null;
        try {
            Method method = this.mappingMethods.get(sPath);
            obj = this.mappingObjs.get(sPath);
            objResult = method.invoke(obj);
            response.getWriter().append(Objects.requireNonNull(objResult).toString());
        } catch (Exception ignored) {
        }
    }
}
