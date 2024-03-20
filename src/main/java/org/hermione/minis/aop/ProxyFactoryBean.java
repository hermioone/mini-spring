package org.hermione.minis.aop;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hermione.minis.beans.BeansException;
import org.hermione.minis.beans.factory.BeanFactory;
import org.hermione.minis.beans.factory.BeanFactoryAware;
import org.hermione.minis.beans.factory.FactoryBean;
import org.hermione.minis.util.ClassUtils;

@Slf4j
public class ProxyFactoryBean implements FactoryBean<Object>, BeanFactoryAware {
    private AopProxyFactory aopProxyFactory;

    @Setter
    private BeanFactory beanFactory;
    /**
     * 在 applicationContext.xml 中设置
     */
    @Setter
    private String interceptorName;
    private Advisor advisor;
    /**
     * 被代理对象，在 applicationContext.xml 中设置
     */
    @Setter
    private Object target;
    private final ClassLoader proxyClassLoader = ClassUtils.getDefaultClassLoader();
    /**
     * 代理对象
     */
    private Object singletonInstance;

    public ProxyFactoryBean() {
        this.aopProxyFactory = new DefaultAopProxyFactory();
    }

    @Override
    public Object getObject() throws Exception {//获取内部对象
        initializeAdvisor();
        return getSingletonInstance();
    }

    private synchronized void initializeAdvisor() {
        Object advice = null;
        MethodInterceptor mi = null;
        try {
            advice = this.beanFactory.getBean(this.interceptorName);
        } catch (BeansException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        if (advice instanceof BeforeAdvice) {
            mi = new MethodBeforeAdviceInterceptor((MethodBeforeAdvice) advice);
        } else if (advice instanceof AfterAdvice) {
            mi = new AfterReturningAdviceInterceptor((AfterReturningAdvice) advice);
        } else if (advice instanceof MethodInterceptor) {
            mi = (MethodInterceptor) advice;
        }
        advisor = new DefaultAdvisor();
        advisor.setMethodInterceptor(mi);
    }

    /**
     * @return 代理对象
     */
    private synchronized Object getSingletonInstance() {
        if (this.singletonInstance == null) {
            this.singletonInstance = getProxy(createAopProxy());
        }
        return this.singletonInstance;
    }

    /**
     * 生成代理对象
     *
     * @return 代理对象
     */
    private Object getProxy(AopProxy aopProxy) {
        return aopProxy.getProxy();
    }

    protected AopProxy createAopProxy() {
        return this.aopProxyFactory.createAopProxy(this.target, this.advisor);
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }
}
