package com.ipet.http.enums;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/5 11:32
 */
public enum HttpMethod {
    GET("GET"),POST("POST"),PUT("PUT"),DELETE("DELETE"),HEAD("HEAD"),OPTIONS("OPTIONS");
    private String value;
    HttpMethod(String _value){
        this.value = _value;
    }

    public String ofValue(){
        return this.value;
    }
}
