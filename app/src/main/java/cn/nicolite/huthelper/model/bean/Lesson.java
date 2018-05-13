package cn.nicolite.huthelper.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by nicolite on 17-12-2.
 */

@Entity
public class Lesson {
    @Id
    private Long id;
    private String userId; //属于哪个用户
    private int dsz; //单双周
    private String xqj; //星期几
    private String djj; //第几节
    private String name; //名称
    private String teacher; //教师名
    private String room; //上课地点
    private String zs; //上课周数，用逗号隔开，比如1,2,3,4
    private boolean addByUser; //是否为用户自己添加

    @Generated(hash = 582435772)
    public Lesson(Long id, String userId, int dsz, String xqj, String djj,
            String name, String teacher, String room, String zs,
            boolean addByUser) {
        this.id = id;
        this.userId = userId;
        this.dsz = dsz;
        this.xqj = xqj;
        this.djj = djj;
        this.name = name;
        this.teacher = teacher;
        this.room = room;
        this.zs = zs;
        this.addByUser = addByUser;
    }

    @Generated(hash = 1669664117)
    public Lesson() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getDsz() {
        return dsz;
    }

    public void setDsz(int dsz) {
        this.dsz = dsz;
    }

    public String getXqj() {
        return xqj;
    }

    public void setXqj(String xqj) {
        this.xqj = xqj;
    }

    public String getDjj() {
        return djj;
    }

    public void setDjj(String djj) {
        this.djj = djj;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getZs() {
        return zs;
    }

    public void setZs(String zs) {
        this.zs = zs;
    }

    public boolean isAddByUser() {
        return addByUser;
    }

    public void setAddByUser(boolean addByUser) {
        this.addByUser = addByUser;
    }

    public boolean getAddByUser() {
        return this.addByUser;
    }
}
