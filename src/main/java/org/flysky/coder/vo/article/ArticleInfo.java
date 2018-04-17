package org.flysky.coder.vo.article;

import java.util.List;

/**
 * Created by hxuhao233 on 2018/4/17.
 */
public class ArticleInfo {
    private String name;

    private String description;

    private List<String> tags;

    private int ColumnId;

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

    public int getColumnId() {
        return ColumnId;
    }

    public void setColumnId(int columnId) {
        ColumnId = columnId;
    }
}
