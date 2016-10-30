package com.maker.use.domain;

import java.io.Serializable;

/**
 * 评论bean
 * Created by XT on 2016/10/29.
 */

public class Comment {
    News_luntan news_luntan;
    private int pid;
    private int pcid;
    private User user;
    private String plocation;
    private String ptime;
    private String pcontent;
    private String pzan;
    private String ispzan;


    public Comment(int pcid, User user, String plocation, String ptime, String pcontent, String pzan, String ispzan) {
        this.pcid = pcid;
        this.user = user;
        this.plocation = plocation;
        this.ptime = ptime;
        this.pcontent = pcontent;
        this.pzan = pzan;
        this.ispzan = ispzan;
    }

    public Comment(int pid, int pcid, User user, String plocation, String ptime, String pcontent, String pzan, String ispzan) {
        this.pid = pid;
        this.pcid = pcid;
        this.user = user;
        this.plocation = plocation;
        this.ptime = ptime;
        this.pcontent = pcontent;
        this.pzan = pzan;
        this.ispzan = ispzan;
    }

    public Comment() {
    }

    public News_luntan getNews_luntan() {
        return news_luntan;
    }

    public void setNews_luntan(News_luntan news_luntan) {
        this.news_luntan = news_luntan;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getPcid() {
        return pcid;
    }

    public void setPcid(int pcid) {
        this.pcid = pcid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPlocation() {
        return plocation;
    }

    public void setPlocation(String plocation) {
        this.plocation = plocation;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getPcontent() {
        return pcontent;
    }

    public void setPcontent(String pcontent) {
        this.pcontent = pcontent;
    }

    public String getPzan() {
        return pzan;
    }

    public void setPzan(String pzan) {
        this.pzan = pzan;
    }

    public String getIspzan() {
        return ispzan;
    }

    public void setIspzan(String ispzan) {
        this.ispzan = ispzan;
    }

    class News_luntan implements Serializable {
        private int lid;
        private User user;
        private String content;
        private String image;
        private String time;
        private String pinglun;
        private String location;


        public News_luntan() {
            super();

        }

        public int getLid() {
            return lid;
        }

        public void setLid(int lid) {
            this.lid = lid;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getPinglun() {
            return pinglun;
        }

        public void setPinglun(String pinglun) {
            this.pinglun = pinglun;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

    }
}
