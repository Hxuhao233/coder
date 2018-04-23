package org.flysky.coder.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.flysky.coder.entity.Article;
import org.flysky.coder.entity.wrapper.ArticleWrapper;

import java.util.List;

@Mapper
public interface ArticleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Article record);

    int insertSelective(Article record);

    Article selectByPrimaryKey(Integer id);

    ArticleWrapper getArticleWrapperById(int id);

    int updateByPrimaryKeySelective(Article record);

    int updateByPrimaryKey(Article record);

    boolean hasArticleName(String name);

    List<ArticleWrapper> getArticleWrapperByInfo(String info);

    List<ArticleWrapper> getArticleWrapperByColumnId(int columnId);
}