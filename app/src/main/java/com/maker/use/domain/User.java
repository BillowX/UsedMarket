package com.maker.use.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息实体类
 * Created by XT on 2016/10/8.
 */
public class User implements Serializable {

    /**
     * 用户信息表主键
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机
     */
    private String phone;

    /**
     * 1：男；0：女
     */
    private int sex;

    /**
     * 头像
     */
    private String headPortrait;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 身份证号码
     */
    private String IDNum;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 注册时间
     */
    private Date registrationDate;

    /**
     * 所属学校
     */
    private String school;
    /**
     * 收货地址
     */
    private String shippingAddress;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getIDNum() {
        return IDNum;
    }

    public void setIDNum(String IDNum) {
        this.IDNum = IDNum;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", sex=" + sex +
                ", headPortrait='" + headPortrait + '\'' +
                ", birthday=" + birthday +
                ", IDNum='" + IDNum + '\'' +
                ", realName='" + realName + '\'' +
                ", registrationDate=" + registrationDate +
                ", school='" + school + '\'' +
                ", shippingAddress='" + shippingAddress + '\'' +
                '}';
    }
}
