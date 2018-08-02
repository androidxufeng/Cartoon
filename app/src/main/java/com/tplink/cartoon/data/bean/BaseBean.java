/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-7-27, xufeng, Create file
 */

package com.tplink.cartoon.data.bean;

import java.io.Serializable;

public class BaseBean implements Serializable {
    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    protected String mId;

}
