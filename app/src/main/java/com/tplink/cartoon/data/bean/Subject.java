package com.tplink.cartoon.data.bean;
/*
 * Copyright (C), 2018, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * ${FILE_NAME}
 *
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-7-27, xufeng, Create file
 */

import java.util.List;

public class Subject extends BaseBean {
    protected List<String> comiclist;

    public List<String> getComiclist() {
        return comiclist;
    }

    public void setComiclist(List<String> comiclist) {
        this.comiclist = comiclist;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "comiclist=" + comiclist +
                '}';
    }
}
