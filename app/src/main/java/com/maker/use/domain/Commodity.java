package com.maker.use.domain;

import java.io.Serializable;
import java.util.List;
/**
 * {
 * "commodityId": "cd34e098b0d84dac938841021a78c6c1",
 * "userId": "65feeb68a1fa4606a51a977f117895c9",
 * "storeId": "12",
 * "commodityName": "正培轩",
 * "category": "12",
 * "price": 12,
 * "amount": 12,
 * "images": "static/commodityImages/344f5848832e427e856d3e77cf450fea_.jpg;static/commodityImages/1900aa84f3c842daa744ea08850ecafd_.jpg",
 * "description": "12",
 * "preferNum": 0,
 * "launchDate": 1477404978000,
 * "salesVolume": 0,
 * "username": "谢焘",
 * "headPortrait": "static/headportrait/9a09c2eea68b419bbd6c3fa0e180ab3c_.png"
 * }
 * {"commodityId":"abd3cf87717d4d61b8741efe5960319e","userId":"f7fa0cb006a9449c8b1e86c8e3d723a2","storeId":null,"commodityName":"哈哈哈哈哈哈哈哈哈哈哈哈","category":"书刊","price":55666.0,"amount":1,"images":"static/commodityImages/bccd0abf718145d8bbcac8f63bf820f9_.jpg;static/commodityImages/5450be745a834c439f3aa067d6bd1fb7_.jpg;static/commodityImages/a14d6a2de57d4160979465ca24cfc383_.jpg;static/commodityImages/28023aa1202c43cbb36d7c25d9066662_.jpg;static/commodityImages/68dd106c8d634d7bba117b567750119c_.jpg","description":null,"preferNum":0,"launchDate":1477409380000,"salesVolume":0,"username":"123","headPortrait":"static/headportrait/6b5b447e432247df815065ea2904f5dd_.jpg"}
 */

/**
 * Description：商品类
 * Created by Peivxuan on 2016/10/23.
 */
public class Commodity implements Serializable {

    /**
     * 商品id
     */
    public String commodityId;

    /**
     * 用户id
     */
    public String userId;
    /**
     * 用户名
     */
    public String username;
    /**
     * 用户头像地址
     */
    public String headPortrait;
    /**
     * 商店id
     */
    public String storeId;
    /**
     * 商品名称
     */
    public String commodityName;
    /**
     * 分类
     */
    public String category;
    /**
     * 价格
     */
    public String price;
    /**
     * 数量
     */
    public String amount;
    /**
     * 定位
     */
    public String location;
    /**
     * 图片
     */
    public List<String> images;
    /**
     * 描述
     */
    public String description;
    /**
     * 收藏人数
     */
    public String preferNum;
    /**
     * 上架时间
     */
    public String launchDate;
    /**
     * 销售量
     */
    public String salesVolume;

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
}
