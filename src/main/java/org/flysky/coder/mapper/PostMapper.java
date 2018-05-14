package org.flysky.coder.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.flysky.coder.entity.Post;

import java.time.LocalDateTime;
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

    List<Post> searchPostByTitleAndContentAndType(@Param("title") String title,@Param("content") String content,@Param("type") Integer type);

    List<Post> searchPostByUsernameAndType(@Param("username") String username,@Param("type") Integer type);

    List<Post> getPostByTitleAndTimeAndType(@Param("title")String title, @Param("time1") LocalDateTime time1, @Param("time2")LocalDateTime time2, @Param("type") Integer type);

    List<Post> searchPost(@Param("title")String title,@Param("content") String content,@Param("username") String username,
                          @Param("type") Integer type);
}