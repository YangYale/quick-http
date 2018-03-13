package com.ipet.http.metadata;

import com.ipet.http.annotation.Header;
import org.apache.commons.lang3.StringUtils;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/6 10:51
 */
public class HeaderMetaData {

    private String key;
    private String value;

    public HeaderMetaData(Header header) {
        this.key = header.key();
        this.value = header.value();
    }

    public HeaderMetaData() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean legal(){
        return StringUtils.isNotBlank(this.key);
    }

    @Override
    public String toString() {
        return "HeaderMetaData{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
