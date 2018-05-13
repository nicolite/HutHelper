package cn.nicolite.huthelper.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nicolite on 17-12-2.
 */

public class Video implements Serializable{
    private String msg;
    @SerializedName("480P")
    private String _$480P;
    @SerializedName("720P")
    private String _$720P;
    @SerializedName("1080P")
    private String _$1080P;
    private List<LinksBean> links;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String get_$480P() {
        return _$480P;
    }

    public void set_$480P(String _$480P) {
        this._$480P = _$480P;
    }

    public String get_$720P() {
        return _$720P;
    }

    public void set_$720P(String _$720P) {
        this._$720P = _$720P;
    }

    public String get_$1080P() {
        return _$1080P;
    }

    public void set_$1080P(String _$1080P) {
        this._$1080P = _$1080P;
    }

    public List<LinksBean> getLinks() {
        return links;
    }

    public void setLinks(List<LinksBean> links) {
        this.links = links;
    }

    public static class LinksBean implements Serializable{

        private String name;
        private String img;
        private List<VedioListBean> vedioList;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public List<VedioListBean> getVedioList() {
            return vedioList;
        }

        public void setVedioList(List<VedioListBean> vedioList) {
            this.vedioList = vedioList;
        }

        public static class VedioListBean implements Serializable{

            private String title;
            private String url;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
