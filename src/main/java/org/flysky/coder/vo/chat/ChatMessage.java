package org.flysky.coder.vo.chat;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * Created by hxuhao233 on 2018/3/22.
 * 在线交流的基础消息类
 */
public class ChatMessage {

    private String content;

    private int roomId;

    private LocalDateTime createdAt;

    private String username;

    public ChatMessage() {

    }

    public ChatMessage(String message) {
        content = message;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int room) {
        this.roomId = room;
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
}
