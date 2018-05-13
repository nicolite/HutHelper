package cn.nicolite.huthelper.model.bean;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by nicolite on 17-10-17.
 */
@Entity
public class User {
    @Id
    @SerializedName(value = "user_id", alternate = {"id"})
    private String user_id;
    private String studentKH;
    private String school;
    private String TrueName;
    private String username;
    private String dep_name;
    private String class_name;
    private String address;
    private String active;
    private String login_cnt;
    private String head_pic;
    @SerializedName(value = "last_login", alternate = {"last_use"})
    private String last_login;
    private String sex;
    private String head_pic_thumb;
    private String bio;

    @Generated(hash = 481135458)
    public User(String user_id, String studentKH, String school, String TrueName,
            String username, String dep_name, String class_name, String address,
            String active, String login_cnt, String head_pic, String last_login,
            String sex, String head_pic_thumb, String bio) {
        this.user_id = user_id;
        this.studentKH = studentKH;
        this.school = school;
        this.TrueName = TrueName;
        this.username = username;
        this.dep_name = dep_name;
        this.class_name = class_name;
        this.address = address;
        this.active = active;
        this.login_cnt = login_cnt;
        this.head_pic = head_pic;
        this.last_login = last_login;
        this.sex = sex;
        this.head_pic_thumb = head_pic_thumb;
        this.bio = bio;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStudentKH() {
        return studentKH;
    }

    public void setStudentKH(String studentKH) {
        this.studentKH = studentKH;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getTrueName() {
        return TrueName;
    }

    public void setTrueName(String trueName) {
        TrueName = trueName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDep_name() {
        return dep_name;
    }

    public void setDep_name(String dep_name) {
        this.dep_name = dep_name;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getLogin_cnt() {
        return login_cnt;
    }

    public void setLogin_cnt(String login_cnt) {
        this.login_cnt = login_cnt;
    }

    public String getHead_pic() {
        return head_pic;
    }

    public void setHead_pic(String head_pic) {
        this.head_pic = head_pic;
    }

    public String getLast_login() {
        return last_login;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHead_pic_thumb() {
        return head_pic_thumb;
    }

    public void setHead_pic_thumb(String head_pic_thumb) {
        this.head_pic_thumb = head_pic_thumb;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
