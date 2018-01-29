package org.lyncc.bazinga.anno.aop;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 切面通知
 *
 * @author liguolin
 * @create 2018-01-29 13:29
 **/
public class BazingaAdvisor extends AbstractBeanFactoryPointcutAdvisor {


    public static final String BAZINGA_ADVISOR_BEAN_NAME = "lyncc.bazinga.advisor";

    private ConcurrentHashMap cacheConfigMap;

    private String[] basePackages;

    @Override
    public Pointcut getPointcut() {
        BazingaPointcut pointcut = new BazingaPointcut(basePackages);
        return pointcut;
    }

    public void setCacheConfigMap(ConcurrentHashMap cacheConfigMap) {
        this.cacheConfigMap = cacheConfigMap;
    }

    public void setBasePackages(String[] basePackages) {
        this.basePackages = basePackages;
    }
}
