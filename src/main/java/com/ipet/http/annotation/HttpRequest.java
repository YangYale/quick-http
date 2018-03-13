package com.ipet.http.annotation;

import com.ipet.http.AfterRequest;
import com.ipet.http.DefaultAfterRequest;
import com.ipet.http.DefaultPreRequest;
import com.ipet.http.PreRequest;
import com.ipet.http.enums.BodyType;
import com.ipet.http.enums.HttpMethod;
import org.apache.http.protocol.HTTP;

import java.lang.annotation.*;

/**
 * 注明当前请求的信息
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/5 11:52
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HttpRequest {

    /**
     * 请求链接(与url二选一)
     * @return
     */
    String value() default "";

    /**
     * 请求链接(与value二选一)
     * @return
     */
    String url() default "";

    /**
     * 请求方式
     * @return
     */
    HttpMethod method() default HttpMethod.GET;

    /**
     * 请求头部(复数)
     * @return
     */
    Headers headers() default @Headers({});

    /**
     * 请求头部
     * @return
     */
    Header header() default @Header(key = "");

    /**
     * 请求体
     * @return
     */
    BodyType body() default BodyType.DEFAULT;

    /**
     * 证书文件路径，请放在classpath下(当为ssl链接时填写生效)
     * @return
     */
    String certification() default "";

    /**
     * 请求前操作
     * @return
     */
    Class<? extends PreRequest> preScript() default DefaultPreRequest.class;

    /**
     * 请求后操作
     * @return
     */
    Class<? extends AfterRequest> afterScript() default DefaultAfterRequest.class;

    /**
     * 字符集
     * @return
     */
    String charset() default HTTP.UTF_8;

    /**
     * 缓冲区大小(字节)
     * @return
     */
    int bufferSize() default 8 * 1024;
}
