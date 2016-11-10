package com.maker.use.domain;

import java.io.Serializable;
import java.util.List;
/**
 * Description：商品类
 * Created by Peivxuan on 2016/10/23.
 */
public class Commodity implements Serializable {


    /**
     * commodityId : a4f18a59c54f40e29775c368c3530c63
     * userId : 54cb98d1fd4c45c7a68a581f6960fe31
     * storeId : null
     * commodityName : hh
     * category : 其它
     * price : 1000
     * amount : 22
     * images : ["static/attachment/_b128de6a86c34442a4088457ddd6c83c.jpg","static/attachment/_1b8036594ed9476aa447f476b9c07ba2.jpg","static/attachment/_74ab4f6047cb413d9c678d27c10ff349.jpg","static/attachment/_cd61bef964c2473bae322a9b7010d393.jpg","static/attachment/_63950ff80af54e639a8fa67d732ccee3.jpg","static/attachment/_37490af5012f4fd48222af93d8d37a55.jpg","static/attachment/_2e6a8c1d98a349058ea25d94daed96bd.jpg","static/attachment/_8b500bb558e74a1abdea426c6cf14047.jpg","static/attachment/_69a2fde8cc434d408a53c65452777f76.jpg"]
     * description : hh
     * location : 广东省广州市九龙大道东150米广州商学院学生公寓21幢
     * preferNum : 0
     * launchDate : 1478694288000
     * salesVolume : 0
     * status : 2
     * commodityNum : null
     * username : obama
     * headPortrait : static/attachment/_a1d5e2b4a67847599112dab3dabf3112.jpg
     */

    private String commodityId;
    private String userId;
    private Object storeId;
    private String commodityName;
    private String category;
    private String price;
    private String amount;
    private String description;
    private String location;
    private String preferNum;
    private String launchDate;
    private String salesVolume;
    private String status;
    private Object commodityNum;
    private String username;
    private String headPortrait;
    private List<String> images;

    public Commodity() {
    }

    @Override
    public String toString() {
        return "Commodity{" +
                "commodityId='" + commodityId + '\'' +
                ", userId='" + userId + '\'' +
                ", storeId='" + storeId + '\'' +
                ", commodityName='" + commodityName + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", amount=" + amount +
                ", images='" + images + '\'' +
                ", description='" + description + '\'' +
                ", preferNum=" + preferNum +
                ", launchDate=" + launchDate +
                ", salesVolume=" + salesVolume +
                '}';
    }

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Object getStoreId() {
        return storeId;
    }

    public void setStoreId(Object storeId) {
        this.storeId = storeId;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPreferNum() {
        return preferNum;
    }

    public void setPreferNum(String preferNum) {
        this.preferNum = preferNum;
    }

    public String getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(String launchDate) {
        this.launchDate = launchDate;
    }

    public String getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(String salesVolume) {
        this.salesVolume = salesVolume;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getCommodityNum() {
        return commodityNum;
    }

    public void setCommodityNum(Object commodityNum) {
        this.commodityNum = commodityNum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
