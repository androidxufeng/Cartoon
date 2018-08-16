/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-16, xufeng, Create file
 */
package com.tplink.cartoon.data.bean;

public class HomeTitle extends Comic {
    //标题名字
    public String itemTitle;
    //标题种类
    public int TitleType;

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public int getTitleType() {
        return TitleType;
    }

    public void setTitleType(int titleType) {
        TitleType = titleType;
    }
}
