package cn.nicolite.huthelper.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 主页功能菜单
 * Created by nicolite on 17-10-17.
 */
@Entity
public class Menu{
    @Id
    private Long id;  //主键
    private int index; //在第几位显示
    private int imgId;  //图片Id
    private int type;  //类型，是网页还是其他
    private String title; //标题
    private String path;  //启动路径
    private boolean isMain; //是否在主页显示
    private String userId; //属于哪个用户

    public Menu(int index, int imgId, int type, String title, String path,
                boolean isMain) {
        this.index = index;
        this.imgId = imgId;
        this.type = type;
        this.title = title;
        this.path = path;
        this.isMain = isMain;
    }

    @Generated(hash = 129860286)
    public Menu(Long id, int index, int imgId, int type, String title, String path,
            boolean isMain, String userId) {
        this.id = id;
        this.index = index;
        this.imgId = imgId;
        this.type = type;
        this.title = title;
        this.path = path;
        this.isMain = isMain;
        this.userId = userId;
    }

    @Generated(hash = 1631206187)
    public Menu() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean getIsMain() {
        return this.isMain;
    }

    public void setIsMain(boolean isMain) {
        this.isMain = isMain;
    }
}
