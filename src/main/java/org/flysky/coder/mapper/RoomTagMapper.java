package org.flysky.coder.mapper;

import org.flysky.coder.entity.RoomTag;

public interface RoomTagMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RoomTag record);

    int insertSelective(RoomTag record);

    RoomTag selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoomTag record);

    int updateByPrimaryKey(RoomTag record);
}