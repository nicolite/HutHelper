package cn.nicolite.huthelper.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 搜索历史
 * Created by nicolite on 17-11-10.
 */

@Entity
public class SearchHistory {
    @Id
    private Long id;
    private int type; //历史类型， 标识是属于哪个的
    private String history; //历史数据

    @Generated(hash = 1744059450)
    public SearchHistory(Long id, int type, String history) {
        this.id = id;
        this.type = type;
        this.history = history;
    }

    @Generated(hash = 1905904755)
    public SearchHistory() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }
}
