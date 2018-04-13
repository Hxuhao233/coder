package org.flysky.coder.entity;

import java.time.LocalDateTime;

public class Reply {
    private Integer id;

    private Integer userId;

    private Integer type;

    private Integer postid;

    private Integer floorCnt;

    private LocalDateTime createdAt;

    private Integer innerReplyFloor;

    private String content;

    private Integer anonymous;

    private String anonymousName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPostid() {
        return postid;
    }

    public void setPostid(Integer postid) {
        this.postid = postid;
    }

    public Integer getFloorCnt() {
        return floorCnt;
    }

    public void setFloorCnt(Integer floorCnt) {
        this.floorCnt = floorCnt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getInnerReplyFloor() {
        return innerReplyFloor;
    }

    public void setInnerReplyFloor(Integer innerReplyFloor) {
        this.innerReplyFloor = innerReplyFloor;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Integer anonymous) {
        this.anonymous = anonymous;
    }

    public String getAnonymousName() {
        return anonymousName;
    }

    public void setAnonymousName(String anonymousName) {
        this.anonymousName = anonymousName;
    }
}