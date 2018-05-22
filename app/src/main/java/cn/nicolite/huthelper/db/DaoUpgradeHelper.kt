package cn.nicolite.huthelper.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import cn.nicolite.huthelper.BuildConfig
import cn.nicolite.huthelper.db.dao.*
import com.github.yuweiguocn.library.greendao.MigrationHelper
import org.greenrobot.greendao.database.Database

/**
 * Created by nicolite on 17-12-6.
 * greenDao升级类, 防止数据库升级数据被清空
 * TODO 每次创建新的表都需要在这里添加
 */

class DaoUpgradeHelper : DaoMaster.OpenHelper {
    constructor(context: Context, name: String) : super(context, name)
    constructor(context: Context, name: String, factory: SQLiteDatabase.CursorFactory) : super(context, name, factory)

    override fun onUpgrade(db: Database?, oldVersion: Int, newVersion: Int) {
        super.onUpgrade(db, oldVersion, newVersion)

        //是否开启日志
        MigrationHelper.DEBUG = BuildConfig.LOG_DEBUG

        MigrationHelper.migrate(db, object : MigrationHelper.ReCreateAllTableListener {
            override fun onCreateAllTables(db: Database, ifNotExists: Boolean) {
                DaoMaster.createAllTables(db, ifNotExists)
            }

            override fun onDropAllTables(db: Database, ifExists: Boolean) {
                DaoMaster.dropAllTables(db, ifExists)
            }
        }, ConfigureDao::class.java, UserDao::class.java, ExamDao::class.java, ExpLessonDao::class.java,
                GradeDao::class.java, GradeSumDao::class.java, GradeRankDao::class.java, LessonDao::class.java,
                MenuDao::class.java, NoticeDao::class.java, SearchHistoryDao::class.java, TimeAxisDao::class.java)
    }
}
