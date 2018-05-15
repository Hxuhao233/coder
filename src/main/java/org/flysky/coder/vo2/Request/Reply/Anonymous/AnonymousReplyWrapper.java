package org.flysky.coder.vo2.Request.Reply.Anonymous;

public class AnonymousReplyWrapper {
    private String content;
    private Integer postId;
    private Integer isAnonymous;

    public AnonymousReplyWrapper() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(Integer isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

}
