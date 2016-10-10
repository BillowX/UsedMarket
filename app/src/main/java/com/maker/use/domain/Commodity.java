package com.maker.use.domain;

import java.io.Serializable;

/**
 * 商品数据Bean
 * Created by XT on 2016/10/8.
 */

public class Commodity implements Serializable {
    public int id;
    public String name;
    public String category;
    public double price;
    public int num;
    public String imgurl;
    public String description;
    public String username;
    public String time;

    @Override
    public String toString() {
        return id + "," + name + "," + category + "," + price + "," + num + "," +
                imgurl + "," + description + "," + username + "," + time;
    }
}
