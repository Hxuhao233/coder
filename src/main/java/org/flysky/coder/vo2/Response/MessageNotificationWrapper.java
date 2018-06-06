package org.flysky.coder.vo2.Response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class MessageNotificationWrapper {
    private int id;
    private String content;
    private LocalDateTime time;
    private String fromUsername;
    private Long newNotificationNum;

    public MessageNotificationWrapper() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getFromUsername() {
        return fromUsername;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    public Long getNewNotificationNum() {
        return newNotificationNum;
    }

    public void setNewNotificationNum(Long newNotificationNum) {
        this.newNotificationNum = newNotificationNum;
    }
}
