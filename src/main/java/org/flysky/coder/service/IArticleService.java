package org.flysky.coder.service;

import com.github.pagehelper.PageInfo;
import org.flysky.coder.entity.Article;
import org.flysky.coder.entity.Column;
import org.flysky.coder.entity.wrapper.ArticleWrapper;

import java.util.List;

/**
 * Created by hxuhao233 on 2018/4/17.
 */
public interface IArticleService {
    int createColumn(Column Column);

    int modifyColumn(Column Column, boolean needCheckName);

    int deleteColumn(int ColumnId);

    Column getColumnById(int ColumnId);

    PageInfo<Column> getColumnByUserId(int userId, int pageNum, int pageSize);

    int createArticle(Article Article, List<String> tips);

    int modifyArticle(Article Article, boolean needCheckName, List<String> tips);

    int deleteArticle(int ArticleId);

    PageInfo<Article> searchArticle(String info);

    PageInfo<ArticleWrapper> getArticleByColumnId(int ColumnId, int pageNum, int pageSize);

    PageInfo<ArticleWrapper> getArticleByInfo(String info, int pageNum, int pageSize);

    ArticleWrapper getArticleById(int ArticleId);
}
