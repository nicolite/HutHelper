package cn.nicolite.huthelper.model.bean;

/**
 * 上传图片成功返回
 * Created by nicolite on 17-11-11.
 */

public class UploadImages {

    private int code;
    private String data;
    private String data_original;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData_original() {
        return data_original;
    }

    public void setData_original(String data_original) {
        this.data_original = data_original;
    }
}
