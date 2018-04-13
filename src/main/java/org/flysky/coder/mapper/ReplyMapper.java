package org.flysky.coder.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.flysky.coder.entity.Reply;

import java.util.List;

@Mapper
public interface ReplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Reply record);

    int insertSelective(Reply record);

    Reply selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Reply record);

    int updateByPrimaryKey(Reply record);

    int getInnerReplyCountByPostIdAndFloor(@Param("postId") Integer postId, @Param("floor")Integer floor);

    List<Reply> getAllReplyByPostIdAndFloor(@Param("postId") Integer postId,@Param("floor") Integer floor);

    List<Reply> getRepliesByPostId(Integer postId);
}