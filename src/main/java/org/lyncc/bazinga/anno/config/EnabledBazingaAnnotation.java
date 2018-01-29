package org.lyncc.bazinga.anno.config;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
//@Import({CreateAnnotationBeanPostProcessor.class})
public @interface EnabledBazingaAnnotation {


    String[] basePackages();


}
