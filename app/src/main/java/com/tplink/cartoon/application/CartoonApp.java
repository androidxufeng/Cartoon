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

import com.orhanobut.hawk.Hawk;
import com.squareup.leakcanary.LeakCanary;
import com.tplink.cartoon.data.common.Constants;
import com.tplink.cartoon.db.DaoManager;
import com.tplink.cartoon.utils.LogUtil;

import skin.support.SkinCompatManager;
import skin.support.app.SkinCardViewInflater;
import skin.support.constraint.app.SkinConstraintViewInflater;
import skin.support.design.app.SkinMaterialViewInflater;

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

        //Log开关
        LogUtil.init(LogUtil.VERBOSE, "Dr_xu");

        //SharedPreferences
        Hawk.init(this).build();

        //换皮肤
        SkinCompatManager.withoutActivity(this)                         // Basic Widget support
                .addInflater(new SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
                .addInflater(new SkinConstraintViewInflater())          // ConstraintLayout 控件换肤初始化[可选]
                .addInflater(new SkinCardViewInflater())
                .setSkinStatusBarColorEnable(false)                     // Disable statusBarColor skin support，default true   [selectable]
                .setSkinWindowBackgroundEnable(false)                   // Disable windowBackground skin support，default true [selectable]
                .loadSkin();
        try {
            if (!(boolean) Hawk.get(Constants.MODEL)) {
                SkinCompatManager.getInstance().restoreDefaultTheme();
            } else {
                SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN); // load by suffix
            }
        } catch (Exception e) {
            // 默认加载默认模式
            SkinCompatManager.getInstance().restoreDefaultTheme();
        }
    }
}
