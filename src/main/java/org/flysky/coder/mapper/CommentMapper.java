package org.flysky.coder.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.flysky.coder.entity.Comment;
import org.flysky.coder.entity.wrapper.CommentWrapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Integer id);

    CommentWrapper getCommentWrapperById(int id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    List<CommentWrapper> getCommentWrapperByArticleId(@Param(value = "commentedId")int commentedId, @Param(value = "commentedType") int commentedType);
}