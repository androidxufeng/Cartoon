/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-15, xufeng, Create file
 */
package com.tplink.cartoon.db;

import android.content.Context;

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.greendao.DaoMaster;
import com.tplink.cartoon.greendao.DaoSession;

import java.lang.reflect.ParameterizedType;
import java.util.List;


public class DaoHelper<T> {

    private DaoManager mDaoManager;
    private Class<T> clazz;

    public DaoHelper(Context context) {
        mDaoManager = DaoManager.getInstance(context);
    }

    private Class<T> getClazz() {
        if (clazz == null) {//获取泛型的Class对象
            clazz = ((Class<T>) (((ParameterizedType) (this.getClass().getGenericSuperclass())).getActualTypeArguments()[0]));
        }
        return clazz;
    }

    public boolean insert(T t) {
        return mDaoManager.getDaoSession().insert(t) != -1;
    }

    //插入集合
    public boolean insertList(final List<T> datas) {
        boolean flag = false;
        try {
            mDaoManager.getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (T t : datas) {
                        mDaoManager.getDaoSession().insertOrReplace(t);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    //删除
    public boolean delete(T t) {
        try {
            mDaoManager.getDaoSession().delete(t);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 删除所有
    public boolean deleteAll() {
        try {
            mDaoManager.getDaoSession().deleteAll(clazz);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //列出所有
    public List<T> listAll() {
        return (List<T>) mDaoManager.getDaoSession().loadAll(getClazz());
    }

    public List<Comic> listComicAll() {
        return mDaoManager.getDaoSession().getComicDao().loadAll();
    }

    public T find(long id) {
        return mDaoManager.getDaoSession().load(clazz, id);
    }

    public T findComic(long id) {
        return (T) mDaoManager.getDaoSession().getComicDao().load(id);
    }

    //更新
    public boolean update(T t) {
        try {
            mDaoManager.getDaoSession().update(t);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //queryRaw查询
    public List<T> queryAll(String where, String... selectionArgs) {
        List<T> list = mDaoManager.getDaoSession().queryRaw(clazz, where, selectionArgs);
        return list;
    }

    //biuld查询
    public List<T> queryBuilder() {
        List<T> list = mDaoManager.getDaoSession().queryBuilder(clazz).list();
        return list;
    }

    //查询全部，dao查询
    public List<T> queryDaoAll(Class clazz) {
        DaoMaster daoMaster = mDaoManager.getDaoMaster();
        DaoSession session = daoMaster.newSession();
        List<T> list = session.loadAll(clazz);
        return list;
    }
}
