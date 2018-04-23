package org.flysky.coder.service;

import com.github.pagehelper.PageInfo;
import org.flysky.coder.entity.Article;
import org.flysky.coder.entity.Column;
import org.flysky.coder.entity.Comment;
import org.flysky.coder.entity.wrapper.ArticleWrapper;
import org.flysky.coder.entity.wrapper.ColumnWrapper;
import org.flysky.coder.entity.wrapper.CommentWrapper;

import java.util.List;

/**
 * Created by hxuhao233 on 2018/4/17.
 */
public interface IArticleService {
    int createColumn(Column column);

    int modifyColumn(Column column, boolean needCheckName);

    int deleteColumn(int columnId);

    Column getColumnById(int columnId);

    ColumnWrapper getColumnWrapperById(int columnId);

    PageInfo<Column> getColumnByUserId(int userId, int pageNum, int pageSize);

    int createArticle(Article article, List<String> tips);

    int modifyArticle(Article article, boolean needCheckName, List<String> tips);

    int deleteArticle(int articleId);

    PageInfo<ArticleWrapper> getArticleByColumnId(int columnId, int pageNum, int pageSize);

    PageInfo<ArticleWrapper> getArticleWrapperByInfo(String info, int pageNum, int pageSize);

    Article getArticleById(int articleId);

    ArticleWrapper getArticleWrapperById(int articleId);

    int createComment(Comment comment);

    PageInfo<CommentWrapper> getCommentWrapperByArticleId(int articleId, int pageNum, int pageSize);
}
