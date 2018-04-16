package org.flysky.coder.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.flysky.coder.entity.RoomTag;

import java.util.List;

@Mapper
public interface RoomTagMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RoomTag record);

    int insertSelective(RoomTag record);

    RoomTag selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoomTag record);

    int updateByPrimaryKey(RoomTag record);

    List<Integer> getTagIdsByRoomId(int roomId);

    void deleteByRoomId(int roomId);

    void deleteByTagId(Integer id);
}