package cn.nicolite.huthelper.model.bean;

/**
 * 宣讲会
 * Created by nicolite on 17-11-5.
 */

public class CareerTalkResult<T> {

    private String status;
    private int topAdCount;
    private T data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTopAdCount() {
        return topAdCount;
    }

    public void setTopAdCount(int topAdCount) {
        this.topAdCount = topAdCount;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
