package org.lyncc.bazinga.anno.config;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.matcher.ElementMatchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 代理bean
 *
 * @author liguolin
 * @create 2018-01-29 17:04
 **/
@Configuration
public class ProxyBeanPostProcessor implements BeanPostProcessor,ApplicationContextAware {

    private static Logger logger = LoggerFactory.getLogger(ProxyBeanPostProcessor.class);

    private ApplicationContext applicationContext;

    private String[] proxyBasePackages;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        this.applicationContext = applicationContext;
        Map<String, Object> stringObjectMap = applicationContext.getBeansWithAnnotation(EnabledBazingaAnnotation.class);

        stringObjectMap.keySet().forEach((String eachBeanName) -> {
            logger.info("with @EnabledBazingaAnnotation is {}",stringObjectMap.get(eachBeanName).getClass().getName());
            EnabledBazingaAnnotation enabledBazingaAnnotation = stringObjectMap.get(eachBeanName).getClass().getAnnotation(EnabledBazingaAnnotation.class);
            if(null != enabledBazingaAnnotation){
                setProxyBasePackages(enabledBazingaAnnotation.basePackages());
            }
        });

    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        if(proxyBasePackages == null || !include(bean.getClass().getName())){
            return bean;
        }
        Object proxyObject = bean;

        try {
            Class<? extends Object> targetClass = bean.getClass();
            Class<?> proxyType = new ByteBuddy()
                    .subclass(targetClass)
                    .name(targetClass.getName() + "Subclass")
                    //.method(ElementMatchers.any())//if you want to match anything
                    .method(ElementMatchers.isDeclaredBy(targetClass))
                    .intercept(MethodDelegation.to(new Interceptor(bean)))
                    .make()
                    .load(getClass().getClassLoader())
                    .getLoaded();

            proxyObject =  proxyType.newInstance();

        } catch (Throwable t) {
            t.printStackTrace();
        }
        return proxyObject;
    }

    private boolean include(String name) {
        if (proxyBasePackages != null) {
            for (String p : proxyBasePackages) {
                if (name.startsWith(p)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static class Interceptor {

        private final Object target;

        private Interceptor(Object target) {
            this.target = target;
        }

        public @RuntimeType
        Object intercept(@Origin(cache = true) Method method, @Origin(cache = true) Class clazz,
                         @AllArguments Object[] arguments)
                throws Exception {
            long start = System.currentTimeMillis();

            logger.info("invoke begin {}ms clazz is {} methodName is {} params is {}",System.currentTimeMillis(),clazz.getName(),method.getName(),arguments);

            try {
                return method.invoke(target, arguments);
            } finally {
                logger.info("invocation of " + method.getName() + " in " + (System.currentTimeMillis() - start) + " ms");
            }
        }
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String s) throws BeansException {
        return bean;
    }

    public void setProxyBasePackages(String[] proxyBasePackages) {
        this.proxyBasePackages = proxyBasePackages;
    }
}
