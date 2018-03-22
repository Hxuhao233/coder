package org.flysky.coder.vo;

/**
 * Created by hxuhao233 on 2018/3/22.
 */
public class ChatMessage {

    String content;

    public ChatMessage() { }

    public ChatMessage(String message) {
        content = message;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
