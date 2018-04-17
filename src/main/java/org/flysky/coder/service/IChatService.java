package org.flysky.coder.service;

import com.github.pagehelper.PageInfo;
import org.flysky.coder.entity.Home;
import org.flysky.coder.entity.Room;
import org.flysky.coder.entity.User;
import org.flysky.coder.entity.wrapper.RecordWrapper;
import org.flysky.coder.entity.wrapper.RoomWrapper;
import org.flysky.coder.vo.chat.ChatMessage;

import java.util.List;

/**
 * Created by hxuhao233 on 2018/3/28.
 */
public interface IChatService {
    int createHome(Home home);

    int modifyHome(Home home, boolean needCheckName);

    int deleteHome(int homeId);

    Home getHomeById(int homeId);

    PageInfo<Home> getHomeByUserId(int userId, int pageNum, int pageSize);

    int createRoom(Room room, List<String> tips);

    int modifyRoom(Room room, boolean needCheckName, List<String> tips);

    int deleteRoom(int roomId);

    PageInfo<Room> searchRoom(String info);

    PageInfo<RoomWrapper> getRoomByHomeId(int homeId, int pageNum, int pageSize);

    PageInfo<RoomWrapper> getRoomByInfo(String info, int pageNum, int pageSize);

    RoomWrapper getRoomById(int roomId);

    ChatMessage chat(User user, ChatMessage chatMessage);

    ChatMessage enterRoom(User user, ChatMessage chatMessage);

    ChatMessage exitRoom(User user, ChatMessage chatMessage);

    PageInfo<RecordWrapper> getRecord(int roomId, int pageNum, int pageSize);
}
