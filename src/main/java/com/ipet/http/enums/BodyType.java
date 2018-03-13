package com.ipet.http.enums;

import org.apache.http.entity.ContentType;

import java.nio.charset.Charset;

/**
 * 请求体更改可能会影响header中Content-Type的内容，一切以header中Content-Type的内容为准
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/5 11:46
 */
public enum BodyType {
    /**
     * 普通表单，不编码
     */
    DEFAULT(""),
    /**
     * url编码，相当于header[Content-Type:application/x-www-form-urlencoded]
     */
    FORM_URL_ENCODEED("application/x-www-form-urlencoded"),
    /**
     * 文件,相当于header[Content-Type:application/octet-stream]
     */
    BINARY("application/octet-stream"),
    /**
     * text/plain,相当于header[Content-Type:text/plain]
     */
    RAW_TEXT_PLAIN("text/plain"),
    /**
     * application/json,相当于header[Content-Type:application/json]
     */
    RAW_JSON("application/json"),
    /**
     * application/javascript,相当于header[Content-Type:application/javascript]
     */
    RAW_JAVASCRIPT("application/javascript"),
    /**
     * application/xml,相当于header[Content-Type:application/xml]
     */
    RAW_XML_APPLICATION("application/xml"),
    /**
     * text/xml,相当于header[Content-Type:text/xml]
     */
    RAW_XML("text/xml"),
    /**
     * text/html,相当于header[Content-Type:text/html]
     */
    RAW_HTML("text/html");
    private String value;

    public static final String KEY = "Content-Type";

    BodyType(String _value){
        this.value = _value;
    }

    public String ofValue(){
        return this.value;
    }

    public ContentType toContentType(Charset charset){
        return ContentType.create(this.value,charset);
    }

    public static BodyType toBodyType(String _value){
        for (int i = 0; i < BodyType.values().length; i++) {
            if(BodyType.values()[i].value.equalsIgnoreCase(_value)){
                return BodyType.values()[i];
            }
        }
        return BodyType.DEFAULT;
    }
}
