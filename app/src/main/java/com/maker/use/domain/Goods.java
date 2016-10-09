package com.maker.use.domain;

/**
 * Created by XISEVEN on 2016/9/27.
 */

public class Goods {
    private int imgId;
    private String msg;

    @Override
    public String toString() {
        return "Goods{" +
                "imgId=" + imgId +
                ", msg='" + msg + '\'' +
                '}';
    }

    public Goods(int imgId, String msg) {
        this.imgId = imgId;
        this.msg = msg;
    }
    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}
