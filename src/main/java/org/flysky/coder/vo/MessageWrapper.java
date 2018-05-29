package org.flysky.coder.vo;

public class MessageWrapper {

    private Integer toUid;

    private String content;

    public MessageWrapper(){

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getToUid() {
        return toUid;
    }

    public void setToUid(Integer toUid) {
        this.toUid = toUid;
    }
}
