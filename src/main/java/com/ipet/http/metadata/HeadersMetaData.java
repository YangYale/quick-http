package com.ipet.http.metadata;

import com.ipet.http.annotation.Header;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/6 11:06
 */
public class HeadersMetaData {
    private List<HeaderMetaData> headerMetaDataList;

    public List<HeaderMetaData> getHeaderMetaDataList() {
        return headerMetaDataList;
    }

    public HeadersMetaData() {
        headerMetaDataList = new ArrayList<>();
    }

    public HeadersMetaData(Header ...headers){
        headerMetaDataList = new ArrayList<>(headers.length);
        for (Header header: headers) {
            HeaderMetaData headerMetaData = new HeaderMetaData(header);
            if(headerMetaData.legal()){
                headerMetaDataList.add(headerMetaData);
            }
        }
    }

    public HeadersMetaData(List<HeaderMetaData> headerMetaDataList) {
        this.headerMetaDataList = headerMetaDataList;
    }

    public void addHeaderMetaDatas(Collection<HeaderMetaData> headerMetaDataCollection){
        this.headerMetaDataList.addAll(headerMetaDataCollection);
    }

    public void addHeaderMetaData(HeaderMetaData headerMetaData){
        this.headerMetaDataList.add(headerMetaData);
    }
}
