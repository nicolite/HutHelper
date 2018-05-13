package cn.nicolite.huthelper.model.bean;

/**
 * 电费查询实体类
 * Created by nicolite on 17-10-31.
 */

public class Electric {

    private int code;
    private String ammeter;
    private String balance;
    private String time;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getAmmeter() {
        return ammeter;
    }

    public void setAmmeter(String ammeter) {
        this.ammeter = ammeter;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
