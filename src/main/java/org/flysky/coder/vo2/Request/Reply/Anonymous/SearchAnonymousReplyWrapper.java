package org.flysky.coder.vo2.Request.Reply.Anonymous;

public class SearchAnonymousReplyWrapper {
    private String title;
    private String content;
    private Integer id;

    public SearchAnonymousReplyWrapper() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
