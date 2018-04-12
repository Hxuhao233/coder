package org.flysky.coder.vo;

public class MessageWrapper {
    private Integer fromUid;

    private Integer toUid;

    private String content;

    public MessageWrapper(Integer fromUid, Integer toUid, String content) {
        this.fromUid = fromUid;
        this.toUid = toUid;
        this.content = content;
    }

    public Integer getFromUid() {
        return fromUid;
    }

    public void setFromUid(Integer fromUid) {
        this.fromUid = fromUid;
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
