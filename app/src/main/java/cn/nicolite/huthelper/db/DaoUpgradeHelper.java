package cn.nicolite.huthelper.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.yuweiguocn.library.greendao.MigrationHelper;

import org.greenrobot.greendao.database.Database;

import cn.nicolite.huthelper.BuildConfig;
import cn.nicolite.huthelper.db.dao.ConfigureDao;
import cn.nicolite.huthelper.db.dao.DaoMaster;
import cn.nicolite.huthelper.db.dao.ExamDao;
import cn.nicolite.huthelper.db.dao.ExpLessonDao;
import cn.nicolite.huthelper.db.dao.GradeDao;
import cn.nicolite.huthelper.db.dao.GradeRankDao;
import cn.nicolite.huthelper.db.dao.GradeSumDao;
import cn.nicolite.huthelper.db.dao.LessonDao;
import cn.nicolite.huthelper.db.dao.MenuDao;
import cn.nicolite.huthelper.db.dao.NoticeDao;
import cn.nicolite.huthelper.db.dao.SearchHistoryDao;
import cn.nicolite.huthelper.db.dao.TimeAxisDao;
import cn.nicolite.huthelper.db.dao.UserDao;

/**
 * Created by nicolite on 17-12-6.
 * greenDao升级类, 防止数据库升级数据被清空
 * TODO 每次创建新的表都需要在这里添加
 */

public class DaoUpgradeHelper extends DaoMaster.OpenHelper {
    public DaoUpgradeHelper(Context context, String name) {
        super(context, name);
    }

    public DaoUpgradeHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);

        //是否开启日志
        MigrationHelper.DEBUG = BuildConfig.LOG_DEBUG;

        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
                    @Override
                    public void onCreateAllTables(Database db, boolean ifNotExists) {
                        DaoMaster.createAllTables(db, ifNotExists);
                    }

                    @Override
                    public void onDropAllTables(Database db, boolean ifExists) {
                        DaoMaster.dropAllTables(db, ifExists);
                    }
                }, ConfigureDao.class, UserDao.class, ExamDao.class, ExpLessonDao.class,
                GradeDao.class, GradeSumDao.class, GradeRankDao.class, LessonDao.class,
                MenuDao.class, NoticeDao.class, SearchHistoryDao.class, TimeAxisDao.class);
    }
}
