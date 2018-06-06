package org.flysky.coder.entity.wrapper;

import org.flysky.coder.entity.Comment;

/**
 * Created by hxuhao233 on 2018/4/19.
 */
public class CommentWrapper extends Comment {
    private String username;

    private String icon;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
