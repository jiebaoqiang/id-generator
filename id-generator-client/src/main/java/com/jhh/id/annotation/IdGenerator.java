package com.jhh.id.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mybatis id 生成器注解<br />
 * 用于拦截Mybatis insert 操作，调用<p>id-generator-service</p>工程中的接口生成Id
 *
 * @author siguiyang
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IdGenerator {
    /**
     * 指定请求入参的bizName
     */
    String value() default "";

    /**
     * 指定字段的名称
     */
    String column() default "id";
}
