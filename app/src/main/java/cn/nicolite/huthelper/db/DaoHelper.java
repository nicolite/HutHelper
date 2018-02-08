package cn.nicolite.huthelper.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import cn.nicolite.huthelper.db.dao.DaoMaster;
import cn.nicolite.huthelper.db.dao.DaoSession;
import cn.nicolite.huthelper.model.Constants;

/**
 * Created by nicolite on 17-9-6.
 */

public class DaoHelper {
    private volatile static DaoHelper instance;
    private DaoMaster.OpenHelper helper;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    private DaoHelper(Context context) {
        //helper = new DaoMaster.DevOpenHelper(context, Constants.DBNAME);
        helper = new DaoUpgradeHelper(context, Constants.DBNAME);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public static DaoHelper getDaoHelper(Context context) {
        if (instance == null) {
            synchronized (DaoHelper.class) {
                if (instance == null) {
                    instance = new DaoHelper(context);
                }
            }
            instance = new DaoHelper(context);
        }
        return instance;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }
}
