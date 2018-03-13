package com.ipet.http.annotation;

import java.lang.annotation.*;

/**
 * 声明接口为http接口
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/5 13:50
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpComponent {
}
