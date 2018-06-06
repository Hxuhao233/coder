package org.flysky.coder.vo.chat;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * Created by hxuhao233 on 2018/3/22.
 * 在线交流的基础消息类
 */
public class ChatMessage {

    private String content;

    private int toId;

    private LocalDateTime createdAt;

    private int fromId;

    private String username;

    private String icon;

    private int type;

    private int contentType;

    public static int TYPE_ROOM_CHAT = 1;

    public static int TYPE_USER_CHAT = 2;

    public static int TYPE_ENTER = 2;

    public static int TYPE_EXIT = 3;

    public static int CONTENT_TYPE_TEXT = 1;

    public static int CONTENT_TYPE_IMG = 2;

    public ChatMessage() {

    }

    public ChatMessage(String message) {
        content = message;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int userId) {
        this.fromId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime time) {
        this.createdAt = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int content_type) {
        this.contentType = content_type;
    }
}
