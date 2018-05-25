package org.flysky.coder.service;

import com.github.pagehelper.PageInfo;
import org.flysky.coder.entity.Home;
import org.flysky.coder.entity.Room;
import org.flysky.coder.entity.User;
import org.flysky.coder.entity.wrapper.HomeWrapper;
import org.flysky.coder.entity.wrapper.RecordWrapper;
import org.flysky.coder.entity.wrapper.RoomWrapper;
import org.flysky.coder.vo.chat.ChatMessage;
import org.flysky.coder.vo.chat.OnlineUser;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by hxuhao233 on 2018/3/28.
 */
public interface IChatService {
    int createHome(Home home);

    int modifyHome(Home home, boolean needCheckName);

    int deleteHome(int homeId);

    Home getHomeById(int homeId);

    HomeWrapper getHomeWrapperById(int homeId);

    PageInfo<Home> getHomeByUserId(int userId, int pageNum, int pageSize);

    int createRoom(Room room, List<String> tips);

    int modifyRoom(Room room, boolean needCheckName, List<String> tips);

    int deleteRoom(int roomId);

    PageInfo<RoomWrapper> getRoomByHomeId(int homeId, int pageNum, int pageSize);

    PageInfo<RoomWrapper> getRoomByInfo(String info, int pageNum, int pageSize);

    Room getRoomById(int roomId);

    RoomWrapper getRoomWrapperById(int roomId);

    ChatMessage chat(User user, ChatMessage chatMessage);

    int addHistoryRoom(int userId, int roomId);

    List<Room> getHistoryRoom(int userId);

    int deleteHistoryRoom(int userId, int roomId);

    List<OnlineUser> getOnlineUsersByRoomId(int roomId);

    ChatMessage enterRoom(User user, ChatMessage chatMessage);

    ChatMessage exitRoom(User user, ChatMessage chatMessage);

    PageInfo<RecordWrapper> getRecord(int roomId, int pageNum, int pageSize);

    List<RecordWrapper> getRecord(int roomId, LocalDateTime time);

    PageInfo<RoomWrapper> getRoomWrappersByUserId(Integer id, int pageNum, int pageSize);
}
