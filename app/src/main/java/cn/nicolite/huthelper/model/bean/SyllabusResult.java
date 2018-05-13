package cn.nicolite.huthelper.model.bean;

import java.util.List;

/**
 * Created by nicolite on 17-12-2.
 * 课程表
 */

public class SyllabusResult {

    private int code;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {

        private int dsz;
        private String xqj;
        private String djj;
        private String name;
        private String teacher;
        private String room;
        private List<Integer> zs;

        public int getDsz() {
            return dsz;
        }

        public void setDsz(int dsz) {
            this.dsz = dsz;
        }

        public String getXqj() {
            return xqj;
        }

        public void setXqj(String xqj) {
            this.xqj = xqj;
        }

        public String getDjj() {
            return djj;
        }

        public void setDjj(String djj) {
            this.djj = djj;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTeacher() {
            return teacher;
        }

        public void setTeacher(String teacher) {
            this.teacher = teacher;
        }

        public String getRoom() {
            return room;
        }

        public void setRoom(String room) {
            this.room = room;
        }

        public List<Integer> getZs() {
            return zs;
        }

        public void setZs(List<Integer> zs) {
            this.zs = zs;
        }
    }
}
