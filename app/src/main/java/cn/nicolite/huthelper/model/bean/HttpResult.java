package cn.nicolite.huthelper.model.bean;

/**
 * bean封装
 * Created by nicolite on 17-10-13.
 */

public class HttpResult<T> {
    private String msg;
    private int code;
    private String status;
    private String remember_code_app;
    private T data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getRemember_code_app() {
        return remember_code_app;
    }

    public void setRemember_code_app(String remember_code_app) {
        this.remember_code_app = remember_code_app;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
