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
    /**
     * 请求host，可不指定，由method提供全路径，指定时method需要配置path,与host二选一
     * 可用${}注入
     * @return
     * @see HttpRequest
     */
    String value() default "";

    /**
     * 请求host，可不指定，由method提供全路径，指定时method需要配置path,与value二选一
     * 可用${}注入
     * @return
     * @see HttpRequest
     */
    String host() default "";
}
