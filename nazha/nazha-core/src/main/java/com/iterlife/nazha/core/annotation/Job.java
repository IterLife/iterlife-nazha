package com.iterlife.nazha.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
*
* @desc An user defined annotation used to identify an "Job" object
* @author Lu Jie
* @date 2019 2019年2月13日 上午11:43:51
* @tags 
*/
@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Job {

    String id();

    String name();

    String desc();
}
