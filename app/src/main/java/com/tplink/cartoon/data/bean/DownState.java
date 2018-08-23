/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-22, xufeng, Create file
 */
package com.tplink.cartoon.data.bean;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DownState {

    public static final int START = 0;
    public static final int DOWN = 1;
    public static final int PAUSE = 2;
    public static final int STOP = 3;
    public static final int ERROR = 4;
    public static final int FINISH = 5;
    public static final int NONE = 6;

    @IntDef({START, DOWN, PAUSE, STOP, ERROR, FINISH, NONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface state {

    }

    @state
    private int state;

    public int getState() {
        return state;
    }

    public void setState(@state int state) {
        this.state = state;
    }

    DownState(@state int state) {
        this.state = state;
    }
}
