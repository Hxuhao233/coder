package org.flysky.coder.vo2.Request.Reply.Forum;

public class PostReplyWrapper {
    private Integer postId;
    private String content;

    public PostReplyWrapper(){

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

}
