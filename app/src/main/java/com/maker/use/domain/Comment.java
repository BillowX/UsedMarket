package com.maker.use.domain;

/**
 * Created by huangMP on 2016/10/23.
 * decription : 留言实体类
 */
public class Comment {
    /**
     * 主键
     */
    private String commentId;
    /**
     * 商品Id
     */
    private String commodityId;
    /**
     * 众筹Id
     */
    private String crowdfundingId;

    /**
     * 留言内容
     */
    private String commentText;

    /**
     * 添加时间
     */
    private String commentDate;
    /**
     * 添加人
     */
    private String userId;

    /**
     * 定位
     */
    private String commentLocation;
    /**
     * username : obama
     * headPortrait : static/attachment/_d2b7d52783034f51bbb33e7ecea526cc.jpg
     */

    private String username;
    private String headPortrait;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public String getCrowdfundingId() {
        return crowdfundingId;
    }

    public void setCrowdfundingId(String crowdfundingId) {
        this.crowdfundingId = crowdfundingId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommentLocation() {
        return commentLocation;
    }

    public void setCommentLocation(String commentLocation) {
        this.commentLocation = commentLocation;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId='" + commentId + '\'' +
                ", commodityId='" + commodityId + '\'' +
                ", crowdfundingId='" + crowdfundingId + '\'' +
                ", commentText='" + commentText + '\'' +
                ", commentDate=" + commentDate +
                ", userId='" + userId + '\'' +
                ", commentLocation='" + commentLocation + '\'' +
                '}';
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
}
