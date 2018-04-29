package org.flysky.coder.vo.message;

import org.flysky.coder.vo.reply.SearchReplyWrapper;

import java.time.LocalDateTime;

public class SearchMessageWrapper {
    private String content;
    private LocalDateTime time1;
    private LocalDateTime time2;

    public SearchMessageWrapper(){

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTime1() {
        return time1;
    }

    public void setTime1(LocalDateTime time1) {
        this.time1 = time1;
    }

    public LocalDateTime getTime2() {
        return time2;
    }

    public void setTime2(LocalDateTime time2) {
        this.time2 = time2;
    }
}
