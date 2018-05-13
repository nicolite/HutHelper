package cn.nicolite.huthelper.model.bean;

import java.util.List;

/**
 * 考试计划实体类
 * Created by nicolite on 17-11-2.
 */

public class ExamResult {

    private int code;
    private String status;
    private String message;
    private ResBean res;
    private String stuclass;
    private String stuname;
    private int count;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResBean getRes() {
        return res;
    }

    public void setRes(ResBean res) {
        this.res = res;
    }

    public String getStuclass() {
        return stuclass;
    }

    public void setStuclass(String stuclass) {
        this.stuclass = stuclass;
    }

    public String getStuname() {
        return stuname;
    }

    public void setStuname(String stuname) {
        this.stuname = stuname;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public static class ResBean {
        private List<Exam> exam;
        private List<Exam> cxexam;

        public List<Exam> getExam() {
            return exam;
        }

        public void setExam(List<Exam> exam) {
            this.exam = exam;
        }

        public List<Exam> getCxexam() {
            return cxexam;
        }

        public void setCxexam(List<Exam> cxexam) {
            this.cxexam = cxexam;
        }
    }
}
