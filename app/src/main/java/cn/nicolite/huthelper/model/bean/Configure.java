package cn.nicolite.huthelper.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

import cn.nicolite.huthelper.db.dao.DaoSession;
import cn.nicolite.huthelper.db.dao.UserDao;
import cn.nicolite.huthelper.db.dao.ConfigureDao;

/**
 * 配置表
 * Created by nicolite on 17-10-22.
 */
@Entity
public class Configure {
    @Id(autoincrement = true)
    private Long id;
    private String appRememberCode; //code
    private String token; //融云token
    private String city; //城市
    private String tmp; //温度
    private String content; //天气，多云、阴等
    private String userId; //用户唯一Id
    private String studentKH; //学号
    private String lou; //电费 楼
    private String hao; //电费 寝室号
    private String libraryUrl; //图书馆地址
    private String testPlanUrl; //考试计划地址
    private String newTermDate; //开学时间

    @ToOne(joinProperty = "userId")
    private User user;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 978496343)
    private transient ConfigureDao myDao;

    @Generated(hash = 2021069969)
    public Configure(Long id, String appRememberCode, String token, String city,
            String tmp, String content, String userId, String studentKH, String lou,
            String hao, String libraryUrl, String testPlanUrl, String newTermDate) {
        this.id = id;
        this.appRememberCode = appRememberCode;
        this.token = token;
        this.city = city;
        this.tmp = tmp;
        this.content = content;
        this.userId = userId;
        this.studentKH = studentKH;
        this.lou = lou;
        this.hao = hao;
        this.libraryUrl = libraryUrl;
        this.testPlanUrl = testPlanUrl;
        this.newTermDate = newTermDate;
    }

    @Generated(hash = 1672402550)
    public Configure() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppRememberCode() {
        return this.appRememberCode;
    }

    public void setAppRememberCode(String appRememberCode) {
        this.appRememberCode = appRememberCode;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTmp() {
        return this.tmp;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStudentKH() {
        return this.studentKH;
    }

    public void setStudentKH(String studentKH) {
        this.studentKH = studentKH;
    }

    public String getLou() {
        return this.lou;
    }

    public void setLou(String lou) {
        this.lou = lou;
    }

    public String getHao() {
        return this.hao;
    }

    public void setHao(String hao) {
        this.hao = hao;
    }

    public String getLibraryUrl() {
        return this.libraryUrl;
    }

    public void setLibraryUrl(String libraryUrl) {
        this.libraryUrl = libraryUrl;
    }

    public String getTestPlanUrl() {
        return this.testPlanUrl;
    }

    public void setTestPlanUrl(String testPlanUrl) {
        this.testPlanUrl = testPlanUrl;
    }

    public String getNewTermDate() {
        return this.newTermDate;
    }

    public void setNewTermDate(String newTermDate) {
        this.newTermDate = newTermDate;
    }

    @Generated(hash = 1867105156)
    private transient String user__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 538271798)
    public User getUser() {
        String __key = this.userId;
        if (user__resolvedKey == null || user__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserDao targetDao = daoSession.getUserDao();
            User userNew = targetDao.load(__key);
            synchronized (this) {
                user = userNew;
                user__resolvedKey = __key;
            }
        }
        return user;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 34413221)
    public void setUser(User user) {
        synchronized (this) {
            this.user = user;
            userId = user == null ? null : user.getUser_id();
            user__resolvedKey = userId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 52764996)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getConfigureDao() : null;
    }
    
}
