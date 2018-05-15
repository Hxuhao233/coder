package org.flysky.coder.vo2.Response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class SearchPostWrapper {
    private String username;
    private String title;
    private String content;

    public SearchPostWrapper() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
