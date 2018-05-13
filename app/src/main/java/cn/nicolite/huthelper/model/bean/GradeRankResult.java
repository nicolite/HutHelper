package cn.nicolite.huthelper.model.bean;

import java.util.List;

/**
 * Created by nicolite on 17-12-2.
 */

public class GradeRankResult {

    private int code;
    private String zxf;
    private String gks;
    private String wdxf;
    private String zhjd;
    private String pjf;
    private RankBean rank;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getZxf() {
        return zxf;
    }

    public void setZxf(String zxf) {
        this.zxf = zxf;
    }

    public String getGks() {
        return gks;
    }

    public void setGks(String gks) {
        this.gks = gks;
    }

    public String getWdxf() {
        return wdxf;
    }

    public void setWdxf(String wdxf) {
        this.wdxf = wdxf;
    }

    public String getZhjd() {
        return zhjd;
    }

    public void setZhjd(String zhjd) {
        this.zhjd = zhjd;
    }

    public String getPjf() {
        return pjf;
    }

    public void setPjf(String pjf) {
        this.pjf = pjf;
    }

    public RankBean getRank() {
        return rank;
    }

    public void setRank(RankBean rank) {
        this.rank = rank;
    }

    public static class RankBean {
        private List<GradeRank> xnrank;
        private List<GradeRank> xqrank;

        public List<GradeRank> getXnrank() {
            return xnrank;
        }

        public void setXnrank(List<GradeRank> xnrank) {
            this.xnrank = xnrank;
        }

        public List<GradeRank> getXqrank() {
            return xqrank;
        }

        public void setXqrank(List<GradeRank> xqrank) {
            this.xqrank = xqrank;
        }
    }
}
