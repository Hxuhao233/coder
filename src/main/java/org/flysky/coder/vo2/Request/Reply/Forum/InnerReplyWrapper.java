package org.flysky.coder.vo2.Request.Reply.Forum;

public class InnerReplyWrapper {
    private Integer postId;
    private String content;
    private Integer floorCnt;

    public InnerReplyWrapper() {

    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getFloorCnt() {
        return floorCnt;
    }

    public void setFloorCnt(Integer floorCnt) {
        this.floorCnt = floorCnt;
    }
}
