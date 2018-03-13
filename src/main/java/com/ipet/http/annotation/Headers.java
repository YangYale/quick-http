package com.ipet.http.annotation;

import java.lang.annotation.*;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/5 11:36
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD,ElementType.PARAMETER})
public @interface Headers {
    Header[] value();
}
