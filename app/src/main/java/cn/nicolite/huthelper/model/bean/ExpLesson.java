package cn.nicolite.huthelper.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 实验课表实体类
 * Created by nicolite on 17-11-4.
 */

@Entity
public class ExpLesson {

    @Id
    private Long id;
    private String userId;
    private String teacher;
    private String lesson;
    private String obj;
    private String locate;
    private String weeks_no;
    private String week;
    private String lesson_no;
    private String period;
    private String real_time;

    @Generated(hash = 842610022)
    public ExpLesson(Long id, String userId, String teacher, String lesson,
                     String obj, String locate, String weeks_no, String week,
                     String lesson_no, String period, String real_time) {
        this.id = id;
        this.userId = userId;
        this.teacher = teacher;
        this.lesson = lesson;
        this.obj = obj;
        this.locate = locate;
        this.weeks_no = weeks_no;
        this.week = week;
        this.lesson_no = lesson_no;
        this.period = period;
        this.real_time = real_time;
    }

    @Generated(hash = 1800159136)
    public ExpLesson() {
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

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    public String getLocate() {
        return locate;
    }

    public void setLocate(String locate) {
        this.locate = locate;
    }

    public String getWeeks_no() {
        return weeks_no;
    }

    public void setWeeks_no(String weeks_no) {
        this.weeks_no = weeks_no;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getLesson_no() {
        return lesson_no;
    }

    public void setLesson_no(String lesson_no) {
        this.lesson_no = lesson_no;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getReal_time() {
        return real_time;
    }

    public void setReal_time(String real_time) {
        this.real_time = real_time;
    }
}
