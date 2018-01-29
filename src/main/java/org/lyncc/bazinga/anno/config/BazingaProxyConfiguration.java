package org.lyncc.bazinga.anno.config;

import org.lyncc.bazinga.anno.aop.BazingaAdvisor;
import org.lyncc.bazinga.anno.aop.BazingaInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 代理配置
 *
 * @author liguolin
 * @create 2018-01-29 13:15
 **/
@Configuration
public class BazingaProxyConfiguration implements ImportAware, ApplicationContextAware {

    protected AnnotationAttributes enableMethodCache;
    private ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setImportMetadata(AnnotationMetadata annotationMetadata) {

        this.enableMethodCache = AnnotationAttributes.fromMap(
                annotationMetadata.getAnnotationAttributes(EnabledBazingaMethodAnnotation.class.getName(), false));
        if (this.enableMethodCache == null) {
            throw new IllegalArgumentException(
                    "@EnableMethodCache is not present on importing class " + annotationMetadata.getClassName());
        }
    }

    @Bean(name = BazingaAdvisor.BAZINGA_ADVISOR_BEAN_NAME)
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public BazingaAdvisor bazingaAdvisor() {

        if(this.enableMethodCache == null) return null;

        ConcurrentHashMap configMap = new ConcurrentHashMap();

        BazingaInterceptor bazingaInterceptor = new BazingaInterceptor();
        bazingaInterceptor.setApplicationContext(applicationContext);

        BazingaAdvisor advisor = new BazingaAdvisor();
        advisor.setAdviceBeanName(BazingaAdvisor.BAZINGA_ADVISOR_BEAN_NAME);
        advisor.setAdvice(bazingaInterceptor);
        advisor.setBasePackages(this.enableMethodCache.getStringArray("basePackages"));
        advisor.setCacheConfigMap(configMap);
        advisor.setOrder(this.enableMethodCache.<Integer>getNumber("order"));
        return advisor;
    }
}
