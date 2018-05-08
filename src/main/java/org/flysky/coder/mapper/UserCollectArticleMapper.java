package org.flysky.coder.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.flysky.coder.entity.UserCollectArticle;

@Mapper
public interface UserCollectArticleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserCollectArticle record);

    int insertSelective(UserCollectArticle record);

    UserCollectArticle selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserCollectArticle record);

    int updateByPrimaryKey(UserCollectArticle record);

    UserCollectArticle getUserCollectArticleByUserIdAndArticleId(@Param("userId") int userId, @Param("articleId") int articleId);

}