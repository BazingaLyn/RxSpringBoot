package org.lyncc.bazinga.anno.field;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.matcher.ElementMatchers;
import org.lyncc.bazinga.anno.config.EnabledBazingaAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 自定义注解解析类
 *
 * @author liguolin
 * @create 2018-01-29 10:39
 **/
public class CreateAnnotationBeanPostProcessor extends AutowiredAnnotationBeanPostProcessor implements ApplicationContextAware {

    private static Logger logger = LoggerFactory.getLogger(CreateAnnotationBeanPostProcessor.class);


    private ApplicationContext applicationContext;

    private String[] basePackages;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        Map<String, Object> stringObjectMap = applicationContext.getBeansWithAnnotation(EnabledBazingaAnnotation.class);

        stringObjectMap.keySet().forEach((String eachBeanName) -> {
            logger.info("with @EnabledBazingaAnnotation is {}",stringObjectMap.get(eachBeanName).getClass().getName());
            EnabledBazingaAnnotation enabledBazingaAnnotation = stringObjectMap.get(eachBeanName).getClass().getAnnotation(EnabledBazingaAnnotation.class);
            if(null != enabledBazingaAnnotation){
                setBasePackages(enabledBazingaAnnotation.basePackages());
            }
        });
    }



    @Override
    public PropertyValues postProcessPropertyValues(
            PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeanCreationException {


        Object proxyObject = bean;

        String completeBeanName = bean.getClass().getName();

        logger.info("current bean name is {}",completeBeanName);

        if(!include(completeBeanName)){

            return super.postProcessPropertyValues(pvs,pds,proxyObject,beanName);
        }
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
        return super.postProcessPropertyValues(pvs,pds,proxyObject,beanName+"Subclass");

    }




    private boolean include(String name) {
        if (basePackages != null) {
            for (String p : basePackages) {
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
        Object intercept(@Origin(cache = true) Method method,@Origin(cache = true) Class clazz,
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


    public void setBasePackages(String[] basePackages) {
        this.basePackages = basePackages;
    }
}
