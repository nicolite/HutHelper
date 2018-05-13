package cn.nicolite.huthelper.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by nicolite on 17-11-12.
 */

@Entity
public class Grade {

    @Id
    private Long id;
    private String userId;
    private String xh;
    private String xn;
    private String xq;
    private String kcmc;
    private String xf;
    private String cj;
    private String zscj;
    private String bkcj;
    private String jd;
    private String cxbj;
    private String kcxz;
    private String bj;

    @Generated(hash = 1209206556)
    public Grade(Long id, String userId, String xh, String xn, String xq,
                 String kcmc, String xf, String cj, String zscj, String bkcj, String jd,
                 String cxbj, String kcxz, String bj) {
        this.id = id;
        this.userId = userId;
        this.xh = xh;
        this.xn = xn;
        this.xq = xq;
        this.kcmc = kcmc;
        this.xf = xf;
        this.cj = cj;
        this.zscj = zscj;
        this.bkcj = bkcj;
        this.jd = jd;
        this.cxbj = cxbj;
        this.kcxz = kcxz;
        this.bj = bj;
    }

    @Generated(hash = 2042976393)
    public Grade() {
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

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
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

    public String getKcmc() {
        return kcmc;
    }

    public void setKcmc(String kcmc) {
        this.kcmc = kcmc;
    }

    public String getXf() {
        return xf;
    }

    public void setXf(String xf) {
        this.xf = xf;
    }

    public String getCj() {
        return cj;
    }

    public void setCj(String cj) {
        this.cj = cj;
    }

    public String getZscj() {
        return zscj;
    }

    public void setZscj(String zscj) {
        this.zscj = zscj;
    }

    public String getBkcj() {
        return bkcj;
    }

    public void setBkcj(String bkcj) {
        this.bkcj = bkcj;
    }

    public String getJd() {
        return jd;
    }

    public void setJd(String jd) {
        this.jd = jd;
    }

    public String getCxbj() {
        return cxbj;
    }

    public void setCxbj(String cxbj) {
        this.cxbj = cxbj;
    }

    public String getKcxz() {
        return kcxz;
    }

    public void setKcxz(String kcxz) {
        this.kcxz = kcxz;
    }

    public String getBj() {
        return bj;
    }

    public void setBj(String bj) {
        this.bj = bj;
    }
}
