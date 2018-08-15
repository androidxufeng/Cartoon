/*
 *
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-7-27, xufeng, Create file
 */
package com.tplink.cartoon.application;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.tplink.cartoon.db.DaoManager;

public class CartoonApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        //初始化GreenDao
        DaoManager.getInstance(this.getApplicationContext());
    }
}
