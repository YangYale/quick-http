package com.ipet.http.annotation;

import java.lang.annotation.*;

/**
 * 注解在参数上时，值为参数传递值
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/5 11:34
 */
@Documented
@Target(value = {ElementType.METHOD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(value = Headers.class)
public @interface Header {
    String key();

    String value() default "";
}
