/*
 * Author xufeng
 *
 * Ver 1.0, 18-8-21, xufeng, Create file
 */
package com.tplink.cartoon.data.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

@Entity
public class DBSearchResult extends BaseBean {
    @Id(autoincrement = true)
    protected Long id;
    @NotNull
    protected String title;
    @NotNull
    protected Long search_time;

    public Long getSearch_time() {
        return this.search_time;
    }

    public void setSearch_time(Long search_time) {
        this.search_time = search_time;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Generated(hash = 990650882)
    public DBSearchResult(Long id, @NotNull String title, @NotNull Long search_time) {
        this.id = id;
        this.title = title;
        this.search_time = search_time;
    }

    @Generated(hash = 567965572)
    public DBSearchResult() {
    }
}
