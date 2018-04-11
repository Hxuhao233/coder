package org.flysky.coder.service;

import org.flysky.coder.entity.Home;
import org.flysky.coder.entity.Room;
import org.flysky.coder.entity.User;
import org.flysky.coder.entity.wrapper.RecordWrapper;
import org.flysky.coder.vo.chat.ChatMessage;

import java.util.List;

/**
 * Created by hxuhao233 on 2018/3/28.
 */
public interface IChatService {
    int createHome(Home home);

    int modifyHome(Home home);

    int deleteHome(int homeId);

    Home getHomeById(int homeId);

    List<Home> getHomeByUserId(int userId);

    int createRoom(Room room);

    int modifyRoom(Room room);

    int deleteRoom(int roomId);

    List<Room> searchRoom(String info);

    List<Room> getRoomByHomeId(int homeId);

    Room getRoomById(int roomId);

    ChatMessage chat(User user, ChatMessage chatMessage);

    ChatMessage enterRoom(User user, ChatMessage chatMessage);

    ChatMessage exitRoom(User user, ChatMessage chatMessage);

    List<RecordWrapper> getRecord(int roomId, int pageNum, int pageSize);
}
