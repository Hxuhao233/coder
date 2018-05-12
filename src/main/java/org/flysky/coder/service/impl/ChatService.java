package org.flysky.coder.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.flysky.coder.entity.*;
import org.flysky.coder.entity.wrapper.HomeWrapper;
import org.flysky.coder.entity.wrapper.RecordWrapper;
import org.flysky.coder.entity.wrapper.RoomWrapper;
import org.flysky.coder.mapper.*;
import org.flysky.coder.service.IChatService;
import org.flysky.coder.vo.chat.ChatMessage;
import org.flysky.coder.vo.chat.OnlineUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by hxuhao233 on 2018/3/28.
 */
@Service
public class ChatService implements IChatService{

    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private HomeMapper homeMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private RoomTagMapper roomTagMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public int createHome(Home home) {
        if (homeMapper.hasHomeName(home.getName())) {
            return 0;
        }

        return homeMapper.insertSelective(home);
    }

    @Override
    public int modifyHome(Home home, boolean needCheckName) {
        if (needCheckName && homeMapper.hasHomeName(home.getName())) {
            return 0;
        }
        return homeMapper.updateByPrimaryKeySelective(home);
    }

    @Override
    public int deleteHome(int homeId) {
        Home home = new Home();
        home.setId(homeId);
        home.setIsDeleted(true);
        homeMapper.updateByPrimaryKeySelective(home);

        List<Room> rooms = roomMapper.getAllRoomsByHomeId(homeId);
        for (Room room : rooms) {
            deleteRoom(room.getId());
        }

        return 1;
    }

    @Override
    public Home getHomeById(int homeId) {
        return homeMapper.selectByPrimaryKey(homeId);
    }

    @Override
    public HomeWrapper getHomeWrapperById(int homeId) {
        return homeMapper.getHomeWrapperById(homeId);
    }

    @Override
    public PageInfo<RoomWrapper> getRoomByHomeId(int homeId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        PageInfo<RoomWrapper> roomWrappers = new PageInfo<>(roomMapper.getRoomWrappersByHomeId(homeId));
        List<RoomWrapper> roomWrapperList = roomWrappers.getList();
        for (RoomWrapper roomWrapper : roomWrapperList) {
            List<Integer> tagIds = roomTagMapper.getTagIdsByRoomId(roomWrapper.getId());
            List<String> tagNames = new ArrayList<>();
            for (Integer tagId : tagIds) {
                tagNames.add(tagMapper.selectByPrimaryKey(tagId).getName());
            }
            roomWrapper.setTags(tagNames);
        }
        return roomWrappers;
    }

    @Override
    public PageInfo<RoomWrapper> getRoomByInfo(String info, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        PageInfo<RoomWrapper> roomWrappers = new PageInfo<>(roomMapper.getRoomWrappersByInfo(info));
        List<RoomWrapper> roomWrapperList = roomWrappers.getList();
        for (RoomWrapper roomWrapper : roomWrapperList) {
            List<Integer> tagIds = roomTagMapper.getTagIdsByRoomId(roomWrapper.getId());
            List<String> tagNames = new ArrayList<>();
            for (Integer tagId : tagIds) {
                tagNames.add(tagMapper.selectByPrimaryKey(tagId).getName());
            }
            roomWrapper.setTags(tagNames);
        }

        return roomWrappers;
    }

    @Override
    public Room getRoomById(int roomId) {
        return roomMapper.selectByPrimaryKey(roomId);
    }


    @Override
    public int createRoom(Room room, List<String> tags) {
        if (roomMapper.hasRoomName(room.getName())) {
            return 0;
        }

        int result = roomMapper.insertSelective(room);

        for (String tagName : tags) {
            Tag tag = tagMapper.getTagByTagNameAndType(tagName, Tag.TYPE_ROOM);
            if (tag == null) {
                tag = new Tag();
                tag.setName(tagName);
                tag.setType(Tag.TYPE_ROOM);
                tagMapper.insertSelective(tag);
            }

            RoomTag roomTag = new RoomTag();
            roomTag.setRoomId(room.getId());
            roomTag.setTagId(tag.getId());
            roomTagMapper.insertSelective(roomTag);
        }


        return result;

    }

    @Override
    public int modifyRoom(Room room, boolean needCheckName, List<String> tags) {
        if (needCheckName && roomMapper.hasRoomName(room.getName())) {
            return 0;
        }
        int result = roomMapper.updateByPrimaryKeySelective(room);

        List<Integer> tagIdList = roomTagMapper.getTagIdsByRoomId(room.getId());
        //List<Integer> deleteTagIdList = new ArrayList<>();
        for (Integer tagId : tagIdList) {
            Tag tag = tagMapper.selectByPrimaryKey(tagId);
            int index = tags.indexOf(tag.getName());
            if (index == -1) {
                roomTagMapper.deleteByTagId(tag.getId());
            } else {
                tags.remove(index);
            }
        }

        for (String tagName : tags) {
            Tag tag = new Tag();
            tag.setName(tagName);
            tag.setType(Tag.TYPE_ROOM);
            tagMapper.insertSelective(tag);

            RoomTag roomTag = new RoomTag();
            roomTag.setRoomId(room.getId());
            roomTag.setTagId(tag.getId());
            roomTagMapper.insertSelective(roomTag);
        }


        return result;
    }

    @Override
    public int deleteRoom(int roomId) {
        Room room = new Room();
        room.setId(roomId);
        room.setIsDeleted(true);
        roomMapper.updateByPrimaryKeySelective(room);
        roomTagMapper.deleteByRoomId(roomId);
        return 1;
    }

    @Override
    public PageInfo getHomeByUserId(int userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Home> homes = homeMapper.getHomesByUserId(userId);
        PageInfo page = new PageInfo(homes);
        return page;
    }

    @Override
    public RoomWrapper getRoomWrapperById(int roomId) {
        RoomWrapper roomWrapper = roomMapper.getRoomWrapperById(roomId);
        if (roomWrapper == null) {
            return null;
        }
        List<Integer> tagIds = roomTagMapper.getTagIdsByRoomId(roomId);
        List<String> tagNames = new ArrayList<>();
        for (Integer tagId : tagIds) {
            tagNames.add(tagMapper.selectByPrimaryKey(tagId).getName());
        }
        roomWrapper.setTags(tagNames);

        return roomWrapper;
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
    public int addHistoryRoom(int userId, int roomId) {
        redisTemplate.opsForSet().add(userId + ":historyRooms", String.valueOf(roomId));
        return 0;
    }

    @Override
    public List<Room> getHistoryRoom(int userId) {
        Set<String> roomIds = redisTemplate.opsForSet().members(userId + ":historyRooms");
        List<Room> rooms = new ArrayList<>();

        for (String roomId : roomIds) {
            Room room = roomMapper.selectByPrimaryKey(Integer.valueOf(roomId));
            rooms.add(room);
        }

        return rooms;
    }

    @Override
    public int deleteHistoryRoom(int userId, int roomId) {
        redisTemplate.opsForSet().remove(userId + ":historyRooms", String.valueOf(roomId));
        return 0;
    }

    @Override
    public List<OnlineUser> getOnlineUsersByRoomId(int roomId) {
        List<OnlineUser> onlineUsers = new ArrayList<>();
        Set<String> userIds = redisTemplate.opsForSet().members(roomId + ":onlineUsers");
        for (String userId : userIds) {
            User user = userMapper.selectByPrimaryKey(Integer.valueOf(userId));
            OnlineUser onlineUser = new OnlineUser();
            onlineUser.setId(user.getId());
            onlineUser.setUsername(user.getUsername());
            onlineUser.setIcon(user.getIcon());
            onlineUsers.add(onlineUser);
        }
        return onlineUsers;
    }

    @Override
    public ChatMessage enterRoom(User user, ChatMessage enterRoomMessage) {
        addHistoryRoom(user.getId(), enterRoomMessage.getRoomId());

        redisTemplate.opsForSet().add(enterRoomMessage.getRoomId() + ":onlineUsers",String.valueOf(user.getId()));

        enterRoomMessage.setCreatedAt(LocalDateTime.now());
        enterRoomMessage.setUsername(user.getUsername());
        enterRoomMessage.setType(ChatMessage.TYPE_ENTER);
        enterRoomMessage.setContent("加入交流室");
        return enterRoomMessage;
    }

    @Override
    public ChatMessage exitRoom(User user, ChatMessage exitRoomMessage) {
        redisTemplate.opsForSet().remove(exitRoomMessage.getRoomId() + ":onlineUsers",String.valueOf(user.getId()));

        exitRoomMessage.setCreatedAt(LocalDateTime.now());
        exitRoomMessage.setUsername(user.getUsername());
        exitRoomMessage.setType(ChatMessage.TYPE_EXIT);
        exitRoomMessage.setContent("退出交流室");
        return exitRoomMessage;
    }

    @Override
    public PageInfo<RecordWrapper> getRecord(int roomId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        List<RecordWrapper> recordList = recordMapper.getRecordWrapperByRoomId(roomId);
        return new PageInfo<>(recordList);
    }
}
