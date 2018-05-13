package cn.nicolite.huthelper.model.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 带分页的bean封装
 * Created by nicolite on 17-11-9.
 */

public class HttpPageResult<T> {
    private int code;
    private String current_page;
    private int pageination;
    private String msg;
    @SerializedName(value = "goods", alternate = {"data", "statement"})
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(String current_page) {
        this.current_page = current_page;
    }

    public int getPageination() {
        return pageination;
    }

    public void setPageination(int pageination) {
        this.pageination = pageination;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
