package cn.nicolite.huthelper.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 时间轴
 * Created by nicolite on 17-10-22.
 */
@Entity
public class TimeAxis {
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private String date;
    private int days;

    @Generated(hash = 181675545)
    public TimeAxis(Long id, String name, String date, int days) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.days = days;
    }

    @Generated(hash = 936677206)
    public TimeAxis() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }
}
