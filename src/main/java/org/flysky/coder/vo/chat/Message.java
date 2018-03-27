package org.flysky.coder.vo.chat;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * Created by hxuhao233 on 2018/3/22.
 * 在线交流的基础消息类
 */
public class Message {

    private String content;

    private String room;

    private LocalDateTime time;

    public Message() {

    }

    public Message(String message) {
        content = message;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
