package org.flysky.coder.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.flysky.coder.entity.ArticleTag;

import java.util.List;

@Mapper
public interface ArticleTagMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ArticleTag record);

    int insertSelective(ArticleTag record);

    ArticleTag selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ArticleTag record);

    int updateByPrimaryKey(ArticleTag record);

    List<ArticleTag> getTagsByArticleId(Integer id);

    void deleteByTagId(Integer id);
}