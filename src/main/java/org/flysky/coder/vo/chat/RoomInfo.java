package org.flysky.coder.vo.chat;

import java.util.List;

/**
 * Created by hxuhao233 on 2018/4/10.
 */
public class RoomInfo {
    private String name;

    private String description;

    private List<String> tags;

    private int homeId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getHomeId() {
        return homeId;
    }

    public void setHomeId(int homeId) {
        this.homeId = homeId;
    }
}
