package org.flysky.coder.vo.chat;

import java.util.List;

/**
 * Created by hxuhao233 on 2018/4/10.
 */
public class RoomInfo {
    private String name;

    private String description;

    private List<String> tips;

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


    public List<String> getTips() {
        return tips;
    }

    public void setTips(List<String> tips) {
        this.tips = tips;
    }
}
