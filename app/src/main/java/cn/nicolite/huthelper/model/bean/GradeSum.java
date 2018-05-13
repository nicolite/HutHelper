package cn.nicolite.huthelper.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by nicolite on 17-12-6.
 * 成绩统计
 */

@Entity
public class GradeSum {
    @Id
    private Long id;
    private String userId;
    private String zxf;
    private String gks;
    private String wdxf;
    private String zhjd;
    private String pjf;

    @Generated(hash = 1021518218)
    public GradeSum(Long id, String userId, String zxf, String gks, String wdxf,
            String zhjd, String pjf) {
        this.id = id;
        this.userId = userId;
        this.zxf = zxf;
        this.gks = gks;
        this.wdxf = wdxf;
        this.zhjd = zhjd;
        this.pjf = pjf;
    }

    @Generated(hash = 1246884358)
    public GradeSum() {
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

    public String getZxf() {
        return zxf;
    }

    public void setZxf(String zxf) {
        this.zxf = zxf;
    }

    public String getGks() {
        return gks;
    }

    public void setGks(String gks) {
        this.gks = gks;
    }

    public String getWdxf() {
        return wdxf;
    }

    public void setWdxf(String wdxf) {
        this.wdxf = wdxf;
    }

    public String getZhjd() {
        return zhjd;
    }

    public void setZhjd(String zhjd) {
        this.zhjd = zhjd;
    }

    public String getPjf() {
        return pjf;
    }

    public void setPjf(String pjf) {
        this.pjf = pjf;
    }
}
