package org.flysky.coder.entity.wrapper;

import org.flysky.coder.entity.Room;

import java.util.List;

/**
 * Created by hxuhao233 on 2018/4/12.
 */
public class RoomWrapper extends Room{

    private String home;

    private List<String> tags;

    private String username;

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }
}
