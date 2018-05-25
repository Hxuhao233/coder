package org.flysky.coder.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.flysky.coder.entity.Room;
import org.flysky.coder.entity.wrapper.RoomWrapper;

import java.util.List;

@Mapper
public interface RoomMapper {
    boolean hasRoomName(String name);

    int deleteByPrimaryKey(Integer id);

    int insert(Room record);

    int insertSelective(Room record);

    Room selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Room record);

    int updateByPrimaryKey(Room record);

    RoomWrapper getRoomWrapperById(int roomId);

    List<RoomWrapper> getRoomWrappersByHomeId(int homeId);

    List<RoomWrapper> getRoomWrappersByInfo(String info);

    List<Room> getAllRoomsByHomeId(int homeId);

    List<RoomWrapper> getRoomWrappersByUserId(Integer userId);
}