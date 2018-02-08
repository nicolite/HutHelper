package cn.nicolite.huthelper.network.exception;

/**
 * 服务器定义错误
 * Created by nicolite on 17-10-17.
 */

public class ServerException extends RuntimeException{
    private int code;
    private String msg;

    public ServerException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
