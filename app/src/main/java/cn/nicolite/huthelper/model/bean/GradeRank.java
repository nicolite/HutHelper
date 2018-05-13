package cn.nicolite.huthelper.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by nicolite on 17-12-2.
 */
@Entity
public class GradeRank {

    @Id
    private Long id;
    private String userId;
    private String xn;
    private String xq;
    private String zhjd;
    private String bjrank;
    private String zyrank;
    private String pjf;
    private boolean isXq;

    @Generated(hash = 707992904)
    public GradeRank(Long id, String userId, String xn, String xq, String zhjd,
                     String bjrank, String zyrank, String pjf, boolean isXq) {
        this.id = id;
        this.userId = userId;
        this.xn = xn;
        this.xq = xq;
        this.zhjd = zhjd;
        this.bjrank = bjrank;
        this.zyrank = zyrank;
        this.pjf = pjf;
        this.isXq = isXq;
    }

    @Generated(hash = 198911312)
    public GradeRank() {
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

    public String getXn() {
        return xn;
    }

    public void setXn(String xn) {
        this.xn = xn;
    }

    public String getXq() {
        return xq;
    }

    public void setXq(String xq) {
        this.xq = xq;
    }

    public String getZhjd() {
        return zhjd;
    }

    public void setZhjd(String zhjd) {
        this.zhjd = zhjd;
    }

    public String getBjrank() {
        return bjrank;
    }

    public void setBjrank(String bjrank) {
        this.bjrank = bjrank;
    }

    public String getZyrank() {
        return zyrank;
    }

    public void setZyrank(String zyrank) {
        this.zyrank = zyrank;
    }

    public String getPjf() {
        return pjf;
    }

    public void setPjf(String pjf) {
        this.pjf = pjf;
    }

    public boolean isXq() {
        return isXq;
    }

    public void setXq(boolean xq) {
        isXq = xq;
    }

    public boolean getIsXq() {
        return this.isXq;
    }

    public void setIsXq(boolean isXq) {
        this.isXq = isXq;
    }
}
