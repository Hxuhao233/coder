package org.flysky.coder.service.impl;

import com.github.pagehelper.PageInfo;
import org.flysky.coder.entity.Article;
import org.flysky.coder.entity.Column;
import org.flysky.coder.entity.wrapper.ArticleWrapper;
import org.flysky.coder.mapper.ColumnMapper;
import org.flysky.coder.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by hxuhao233 on 2018/4/17.
 */
@Service
public class ArticleService implements IArticleService {

    @Autowired
    private ColumnMapper columnMapper;

    @Override
    public int createColumn(Column Column) {
        if (columnMapper.hasColumnName(Column.getName())) {
            return 0;
        }

        return columnMapper.insertSelective(Column);
    }

    @Override
    public int modifyColumn(Column Column, boolean needCheckName) {
        return 0;
    }

    @Override
    public int deleteColumn(int ColumnId) {
        return 0;
    }

    @Override
    public Column getColumnById(int ColumnId) {
        return null;
    }

    @Override
    public PageInfo<Column> getColumnByUserId(int userId, int pageNum, int pageSize) {
        return null;
    }

    @Override
    public int createArticle(Article Article, List<String> tips) {
        return 0;
    }

    @Override
    public int modifyArticle(Article Article, boolean needCheckName, List<String> tips) {
        return 0;
    }

    @Override
    public int deleteArticle(int ArticleId) {
        return 0;
    }

    @Override
    public PageInfo<Article> searchArticle(String info) {
        return null;
    }

    @Override
    public PageInfo<ArticleWrapper> getArticleByColumnId(int ColumnId, int pageNum, int pageSize) {
        return null;
    }

    @Override
    public PageInfo<ArticleWrapper> getArticleByInfo(String info, int pageNum, int pageSize) {
        return null;
    }

    @Override
    public ArticleWrapper getArticleById(int ArticleId) {
        return null;
    }
}
