package org.flysky.coder.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.flysky.coder.entity.Post;

import java.util.List;

@Mapper
public interface PostMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Post record);

    int insertSelective(Post record);

    Post selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Post record);

    int updateByPrimaryKeyWithBLOBs(Post record);

    int updateByPrimaryKey(Post record);

    List<Post> selectBySectorAndType(@Param("sectorId") Integer sectorId,@Param("type") Integer type);

    List<Post> searchPostByTitleAndContent(@Param("title") String title,@Param("content") String content,@Param("type") Integer type);

    List<Post> searchPostByUsername(@Param("username") String username,@Param("type") Integer type);
}