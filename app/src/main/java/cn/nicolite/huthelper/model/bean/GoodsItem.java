package cn.nicolite.huthelper.model.bean;

import java.util.List;

/**
 * 商品详情实体类
 * Created by nicolite on 17-10-13.
 */

public class GoodsItem {

    private String tit;
    private String content;
    private String prize;
    private String user_id;
    private String attr;
    private String created_on;
    private String phone;
    private String address;
    private String username;
    private List<String> pics;
    private List<String> pics_src;

    public String getTit() {
        return tit;
    }

    public void setTit(String tit) {
        this.tit = tit;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public List<String> getPics_src() {
        return pics_src;
    }

    public void setPics_src(List<String> pics_src) {
        this.pics_src = pics_src;
    }
}
