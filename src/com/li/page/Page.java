package com.li.page;

import java.util.List;

public class Page<T> {
    private int pageCode;//当前页码
    private int totalRecord;//总记录数
    private int perPageRecord;//每页记录数
    private List<T> pageRecord;//每页所记录数据
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTotalPage(){
        int totalPage=totalRecord/perPageRecord;
        return totalRecord%perPageRecord==0?totalPage:totalPage+1;
    }

    public int getPageCode() {
        return pageCode;
    }

    public void setPageCode(int pageCode) {
        this.pageCode = pageCode;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public int getPerPageRecord() {
        return perPageRecord;
    }

    public void setPerPageRecord(int perPageRecord) {
        this.perPageRecord = perPageRecord;
    }

    public List<T> getPageRecord() {
        return pageRecord;
    }

    public void setPageRecord(List<T> pageRecord) {
        this.pageRecord = pageRecord;
    }
}
