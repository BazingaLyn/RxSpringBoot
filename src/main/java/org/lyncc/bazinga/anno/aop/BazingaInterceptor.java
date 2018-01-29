package org.lyncc.bazinga.anno.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * bazinga aop拦截器
 *
 * @author liguolin
 * @create 2018-01-29 13:46
 **/
public class BazingaInterceptor implements MethodInterceptor, ApplicationContextAware {


    private static final Logger logger = LoggerFactory.getLogger(BazingaInterceptor.class);

    private ApplicationContext applicationContext;


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        logger.info("bazinga interceptor invoke <<<<<<<<<<>>>>>>>>>>>>>>");
        return invocation.proceed();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
