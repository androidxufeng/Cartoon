/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-20, xufeng, Create file
 */
package com.tplink.cartoon.data.bean;

import java.util.List;

public class SearchResult extends BaseBean {
    public int status;
    public List<SearchBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<SearchBean> getData() {
        return data;
    }

    public void setData(List<SearchBean> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "status=" + status +
                ", data=" + data +
                '}';
    }
}
