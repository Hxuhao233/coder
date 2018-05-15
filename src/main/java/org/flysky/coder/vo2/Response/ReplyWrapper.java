package org.flysky.coder.vo2.Response;

import java.time.LocalDateTime;
import java.util.List;

public class ReplyWrapper {
    private int replyId;
    private List<ReplyWrapper> innerReplyList;
    private String content;
    private LocalDateTime time;
    private int floor;
    private String username;

    public ReplyWrapper() {

    }

    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    public List<ReplyWrapper> getInnerReplyList() {
        return innerReplyList;
    }

    public void setInnerReplyList(List<ReplyWrapper> innerReplyList) {
        this.innerReplyList = innerReplyList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
