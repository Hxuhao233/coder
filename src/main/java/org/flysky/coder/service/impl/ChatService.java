package org.flysky.coder.service.impl;

import com.github.pagehelper.PageHelper;
import org.flysky.coder.entity.Home;
import org.flysky.coder.entity.Record;
import org.flysky.coder.entity.Room;
import org.flysky.coder.entity.User;
import org.flysky.coder.entity.wrapper.RecordWrapper;
import org.flysky.coder.mapper.HomeMapper;
import org.flysky.coder.mapper.RecordMapper;
import org.flysky.coder.mapper.RoomMapper;
import org.flysky.coder.service.IChatService;
import org.flysky.coder.vo.chat.ChatMessage;
import org.flysky.coder.vo.chat.HomeInfo;
import org.flysky.coder.vo.chat.RoomInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hxuhao233 on 2018/3/28.
 */
@Service
public class ChatService implements IChatService{

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private HomeMapper homeMapper;


    @Override
    public int createHome(Home home) {
        if (!homeMapper.hasHomeName(home.getName())) {
            return homeMapper.insertSelective(home);
        }
        return 0;
    }

    @Override
    public int modifyHome(Home home) {
        if (!homeMapper.hasHomeName(home.getName())) {
            return homeMapper.updateByPrimaryKeySelective(home);
        }
        return 0;
    }

    @Override
    public int deleteHome(int homeId) {
        return homeMapper.deleteByPrimaryKey(homeId);
    }

    @Override
    public Home getHomeById(int homeId) {
        return homeMapper.selectByPrimaryKey(homeId);
    }

    @Override
    public int createRoom(Room room) {
        return 0;
    }

    @Override
    public int modifyRoom(Room room) {
        return 0;
    }

    @Override
    public int deleteRoom(int roomId) {
        return 0;
    }

    @Override
    public List<Room> searchRoom(String info) {
        return null;
    }

    @Override
    public List<Room> getRoomByHomeId(int homeId) {
        return null;
    }

    @Override
    public List<Home> getHomeByUserId(int userId) {
        return null;
    }

    @Override
    public Room getRoomById(int roomId) {
        return null;
    }

    @Override
    public ChatMessage chat(User user, ChatMessage chatMessage) {
        LocalDateTime time = LocalDateTime.now();

        Record record = new Record();
        record.setRoomId(chatMessage.getRoomId());
        record.setUserId(user.getId());
        record.setCreatedAt(time);
        record.setContent(chatMessage.getContent());
        recordMapper.insertSelective(record);

        chatMessage.setCreatedAt(time);
        chatMessage.setUsername(user.getUsername());
        chatMessage.setType(ChatMessage.TYPE_CHAT);
        return chatMessage;
    }

    @Override
    public ChatMessage enterRoom(User user, ChatMessage enterRoomMessage) {
        enterRoomMessage.setCreatedAt(LocalDateTime.now());
        enterRoomMessage.setUsername(user.getUsername());
        enterRoomMessage.setType(ChatMessage.TYPE_ENTER);
        enterRoomMessage.setContent("加入交流室");
        return enterRoomMessage;
    }

    @Override
    public ChatMessage exitRoom(User user, ChatMessage exitRoomMessage) {
        exitRoomMessage.setCreatedAt(LocalDateTime.now());
        exitRoomMessage.setUsername(user.getUsername());
        exitRoomMessage.setType(ChatMessage.TYPE_EXIT);
        exitRoomMessage.setContent("退出交流室");
        return exitRoomMessage;
    }

    @Override
    public List<RecordWrapper> getRecord(int roomId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        List<RecordWrapper> recordList = recordMapper.getRecordWrapperByRoomId(roomId);
        return recordList;
    }
}
