package org.flysky.coder.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.flysky.coder.entity.UserVoteArticle;

@Mapper
public interface UserVoteArticleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserVoteArticle record);

    int insertSelective(UserVoteArticle record);

    UserVoteArticle selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserVoteArticle record);

    int updateByPrimaryKey(UserVoteArticle record);

    UserVoteArticle getUserVoteArticleByUserIdAndArticleId(@Param("userId") int userId, @Param("articleId") int articleId);

}