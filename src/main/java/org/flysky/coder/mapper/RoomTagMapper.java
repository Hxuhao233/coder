package org.flysky.coder.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.flysky.coder.entity.RoomTag;

@Mapper
public interface RoomTagMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RoomTag record);

    int insertSelective(RoomTag record);

    RoomTag selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoomTag record);

    int updateByPrimaryKey(RoomTag record);
}