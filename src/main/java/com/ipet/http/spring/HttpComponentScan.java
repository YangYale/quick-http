package com.ipet.http.spring;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/5 14:02
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(HttpComponentScanRegistrar.class)
public @interface HttpComponentScan {

    String value() default "";

    String[] basePackages() default "";
}
