package org.flysky.coder.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.flysky.coder.entity.PostTag;

@Mapper
public interface PostTagMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PostTag record);

    int insertSelective(PostTag record);

    PostTag selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PostTag record);

    int updateByPrimaryKey(PostTag record);
}