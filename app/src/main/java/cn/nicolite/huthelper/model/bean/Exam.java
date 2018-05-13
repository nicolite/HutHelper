package cn.nicolite.huthelper.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 考试计划
 * Created by nicolite on 17-11-2.
 */

@Entity
public class Exam {
    @Id(autoincrement = true)
    private Long id;
    private String userId;
    private String KKDepNo;
    private String CourseName;
    private String Starttime;
    private String EndTime;
    private String Week_Num;
    private String isset;
    private String RoomName;

    @Generated(hash = 569887472)
    public Exam(Long id, String userId, String KKDepNo, String CourseName,
                String Starttime, String EndTime, String Week_Num, String isset,
                String RoomName) {
        this.id = id;
        this.userId = userId;
        this.KKDepNo = KKDepNo;
        this.CourseName = CourseName;
        this.Starttime = Starttime;
        this.EndTime = EndTime;
        this.Week_Num = Week_Num;
        this.isset = isset;
        this.RoomName = RoomName;
    }

    @Generated(hash = 945526930)
    public Exam() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getKKDepNo() {
        return KKDepNo;
    }

    public void setKKDepNo(String KKDepNo) {
        this.KKDepNo = KKDepNo;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String courseName) {
        CourseName = courseName;
    }

    public String getStarttime() {
        return Starttime;
    }

    public void setStarttime(String starttime) {
        Starttime = starttime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getWeek_Num() {
        return Week_Num;
    }

    public void setWeek_Num(String week_Num) {
        Week_Num = week_Num;
    }

    public String getIsset() {
        return isset;
    }

    public void setIsset(String isset) {
        this.isset = isset;
    }

    public String getRoomName() {
        return RoomName;
    }

    public void setRoomName(String roomName) {
        RoomName = roomName;
    }
}
