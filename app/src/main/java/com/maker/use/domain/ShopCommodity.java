package com.maker.use.domain;

/**
 * Created by XISEVEN on 2016/10/25.
 */

public class ShopCommodity {
    private String type;
    private String name;
    private String desc;
    private String price;
    private String imgUri;


    public ShopCommodity(String type, String name, String desc, String price, String imgUri) {
        this.type = type;
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.imgUri = imgUri;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

}