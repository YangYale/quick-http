package com.ipet.http.annotation;

import java.lang.annotation.*;

/**
 * 参数注解 不使用此注解时参数名称为方法参数定义名称，使用此参数可重写
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/6 13:46
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {
    /**
     * 参数名称
     * @return
     */
    String value() default "";

    /**
     * 参数是否必须(为true状态下 若为空则会抛出NullPointerException)
     * @return
     */
    boolean required() default true;
}
