package cn.nicolite.huthelper.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by nicolite on 17-12-4.
 * 通知
 */

@Entity
public class Notice {
    @Id
    private Long id; //主键
    private String userId; //属于用户
    private String type; //类型
    private String time; // 时间
    private String content; //内容
    private String title; //标题

    @Generated(hash = 950456884)
    public Notice(Long id, String userId, String type, String time, String content,
            String title) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.time = time;
        this.content = content;
        this.title = title;
    }

    @Generated(hash = 1880392847)
    public Notice() {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
