package org.flysky.coder.vo.article;

import java.util.List;

/**
 * Created by hxuhao233 on 2018/4/17.
 */
public class ArticleInfo {
    private String name;

    private String description;

    private List<String> tags;

    private int columnId;

    private String content;

    private int parseType;

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
        return columnId;
    }

    public void setColumnId(int columnId) {
        this.columnId = columnId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getParseType() {
        return parseType;
    }

    public void setParseType(int parseType) {
        this.parseType = parseType;
    }
}
