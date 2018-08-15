/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-14, xufeng, Create file
 */
package com.tplink.cartoon.db;

import android.content.Context;

import com.tplink.cartoon.data.common.Constants;
import com.tplink.cartoon.greendao.DaoMaster;
import com.tplink.cartoon.greendao.DaoSession;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;

public class DaoManager {

    private static DaoManager sDaoManager;

    private static DaoMaster sDaoMaster;

    private static DaoSession sDaoSession;

    private Context mContext;
    private DaoMaster.DevOpenHelper mDevOpenHelper;

    private DaoManager(Context context) {

        mContext = context;
    }

    public static DaoManager getInstance(Context context) {
        if (sDaoManager == null) {
            synchronized (DaoManager.class) {
                if (sDaoManager == null) {
                    sDaoManager = new DaoManager(context);
                }
            }
        }
        return sDaoManager;
    }

    public DaoMaster getDaoMaster() {
        if (sDaoMaster == null) {
            mDevOpenHelper = new DaoMaster.DevOpenHelper(mContext, Constants.DB_NAME, null);
            Database writableDb = mDevOpenHelper.getWritableDb();
            sDaoMaster = new DaoMaster(writableDb);
        }
        return sDaoMaster;
    }

    public DaoSession getDaoSession() {
        if (sDaoSession == null) {
            if (sDaoMaster == null) {
                sDaoMaster = getDaoMaster();
            }
            sDaoSession = sDaoMaster.newSession();
        }
        return sDaoSession;
    }

    //输出日志
    public void setDebug() {
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }

    public void close() {
        closeHelper();
        closeSession();
    }

    public void closeHelper() {
        if (mDevOpenHelper != null) {
            mDevOpenHelper.close();
            mDevOpenHelper = null;
        }
    }

    //关闭session
    public void closeSession() {
        if (sDaoSession != null) {
            sDaoSession.clear();
            sDaoSession = null;
        }
    }
}
