package cn.nicolite.huthelper.model.bean;

import java.util.List;

/**
 * 宣讲会详情
 * Created by nicolite on 17-11-5.
 */

public class CareerTalkItem {
    private int id;
    private String company;
    private String title;
    private String holdtime;
    private int univ_id;
    private String universityName;
    private String address;
    private String logoUrl;
    private Object apply_url;
    private Object applyUrl;
    private int is_cancel;
    private int is_official;
    private String web;
    private int totalClicks;
    private String content;
    private int assocLiveId;
    private boolean isSaved;
    private ChatGroupBean chatGroup;
    private List<?> positions;
    private List<XjhsBean> xjhs;
    private List<?> albums;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHoldtime() {
        return holdtime;
    }

    public void setHoldtime(String holdtime) {
        this.holdtime = holdtime;
    }

    public int getUniv_id() {
        return univ_id;
    }

    public void setUniv_id(int univ_id) {
        this.univ_id = univ_id;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Object getApply_url() {
        return apply_url;
    }

    public void setApply_url(Object apply_url) {
        this.apply_url = apply_url;
    }

    public Object getApplyUrl() {
        return applyUrl;
    }

    public void setApplyUrl(Object applyUrl) {
        this.applyUrl = applyUrl;
    }

    public int getIs_cancel() {
        return is_cancel;
    }

    public void setIs_cancel(int is_cancel) {
        this.is_cancel = is_cancel;
    }

    public int getIs_official() {
        return is_official;
    }

    public void setIs_official(int is_official) {
        this.is_official = is_official;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public int getTotalClicks() {
        return totalClicks;
    }

    public void setTotalClicks(int totalClicks) {
        this.totalClicks = totalClicks;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getAssocLiveId() {
        return assocLiveId;
    }

    public void setAssocLiveId(int assocLiveId) {
        this.assocLiveId = assocLiveId;
    }

    public boolean isIsSaved() {
        return isSaved;
    }

    public void setIsSaved(boolean isSaved) {
        this.isSaved = isSaved;
    }

    public ChatGroupBean getChatGroup() {
        return chatGroup;
    }

    public void setChatGroup(ChatGroupBean chatGroup) {
        this.chatGroup = chatGroup;
    }

    public List<?> getPositions() {
        return positions;
    }

    public void setPositions(List<?> positions) {
        this.positions = positions;
    }

    public List<XjhsBean> getXjhs() {
        return xjhs;
    }

    public void setXjhs(List<XjhsBean> xjhs) {
        this.xjhs = xjhs;
    }

    public List<?> getAlbums() {
        return albums;
    }

    public void setAlbums(List<?> albums) {
        this.albums = albums;
    }

    public static class ChatGroupBean {

        private int id;
        private String name;
        private String avatar;
        private int version;
        private int userCnt;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public int getUserCnt() {
            return userCnt;
        }

        public void setUserCnt(int userCnt) {
            this.userCnt = userCnt;
        }
    }

    public static class XjhsBean {


        private int id;
        private String title;
        private String company;
        private int univ_id;
        private String holdtime;
        private String address;
        private String universityName;
        private String universityShortName;
        private String logoUrl;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public int getUniv_id() {
            return univ_id;
        }

        public void setUniv_id(int univ_id) {
            this.univ_id = univ_id;
        }

        public String getHoldtime() {
            return holdtime;
        }

        public void setHoldtime(String holdtime) {
            this.holdtime = holdtime;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getUniversityName() {
            return universityName;
        }

        public void setUniversityName(String universityName) {
            this.universityName = universityName;
        }

        public String getUniversityShortName() {
            return universityShortName;
        }

        public void setUniversityShortName(String universityShortName) {
            this.universityShortName = universityShortName;
        }

        public String getLogoUrl() {
            return logoUrl;
        }

        public void setLogoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
        }
    }
}
