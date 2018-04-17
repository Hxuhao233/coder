package org.flysky.coder.entity.wrapper;

import org.flysky.coder.entity.Article;

import java.util.List;

/**
 * Created by hxuhao233 on 2018/4/17.
 */
public class ArticleWrapper extends Article {
    private String column;

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

    public String getcolumn() {
        return column;
    }

    public void setcolumn(String column) {
        this.column = column;
    }
}
