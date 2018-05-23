package org.flysky.coder.entity.wrapper;

import org.flysky.coder.entity.Article;
import org.flysky.coder.entity.User;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hxuhao233 on 2018/4/17.
 */
public class ArticleWrapper extends Article implements Serializable {
    private String column;

    private List<String> tags;

    private String username;

    private String icon;

    public ArticleWrapper() {

    }

    public static ArticleWrapper build(Article article, List<String> tags, User user, String column) {
        ArticleWrapper articleWrapper = new ArticleWrapper();
        articleWrapper.setTags(tags);
        articleWrapper.setColumn(column);
        articleWrapper.setUsername(user.getUsername());
        articleWrapper.setCollectCount(article.getCollectCount());
        articleWrapper.setVoteCount(article.getVoteCount());
        articleWrapper.setContent(article.getContent());
        articleWrapper.setDescription(article.getDescription());
        articleWrapper.setIsDeleted(article.getIsDeleted());
        articleWrapper.setName(article.getName());
        articleWrapper.setParseType(article.getParseType());
        articleWrapper.setUpdatedAt(article.getUpdatedAt());
        articleWrapper.setColumnId(article.getColumnId());
        articleWrapper.setCreatedAt(article.getCreatedAt());
        articleWrapper.setId(article.getId());
        articleWrapper.setUserId(article.getUserId());
        articleWrapper.setIcon(user.getIcon());

        return articleWrapper;
    }

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

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
