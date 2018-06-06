package org.flysky.coder.vo2.Response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class ReplyNotificationWrapper {
    private Integer id;
    private String replyUsername;
    private Long newReplyNum;
    private LocalDateTime time;
    private String content;

    public ReplyNotificationWrapper() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReplyUsername() {
        return replyUsername;
    }

    public void setReplyUsername(String replyUsername) {
        this.replyUsername = replyUsername;
    }

    public Long getNewReplyNum() {
        return newReplyNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setNewReplyNum(Long newReplyNum) {
        this.newReplyNum = newReplyNum;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
