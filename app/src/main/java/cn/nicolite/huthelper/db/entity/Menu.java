package cn.nicolite.huthelper.db.entity;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * 主页功能菜单
 * Created by nicolite on 17-10-17.
 */
@Entity
public class Menu {
    @Id
    private long id;  //主见
    private int index; //在第几位显示
    private int imgId;  //图片Id
    private int type;  //类型，是网页还是其他
    private String title; //标题
    private String path;  //启动路径
    private boolean isMain; //是否在主页显示

    public Menu(long id, int index, int imgId, int type, String title, String path, boolean isMain) {
        this.id = id;
        this.index = index;
        this.imgId = imgId;
        this.type = type;
        this.title = title;
        this.path = path;
        this.isMain = isMain;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public boolean getIsMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }
}
