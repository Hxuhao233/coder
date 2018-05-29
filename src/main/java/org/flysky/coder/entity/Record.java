package org.flysky.coder.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class Record {
    private Integer id;

    private Integer fromId;

    private Integer toId;

    private String content;

    private LocalDateTime createdAt;

    private Integer type;

    private Integer contentType;

    public static int TYPE_ROOM_CHAT = 1;

    public static int TYPE_USER_CHAT = 2;

    public static int CONTENT_TYPE_TEXT = 1;

    public static int CONTENT_TYPE_IMG = 2;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFromId() {
        return fromId;
    }

    public void setFromId(Integer fromId) {
        this.fromId = fromId;
    }

    public Integer getToId() {
        return toId;
    }

    public void setToId(Integer toId) {
        this.toId = toId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getContentType() {
        return contentType;
    }

    public void setContentType(Integer content_type) {
        this.contentType = content_type;
    }
}