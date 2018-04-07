package org.flysky.coder.service.impl;

import com.github.pagehelper.PageHelper;
import org.flysky.coder.entity.Record;
import org.flysky.coder.entity.User;
import org.flysky.coder.entity.wrapper.RecordWrapper;
import org.flysky.coder.mapper.RecordMapper;
import org.flysky.coder.service.IChatService;
import org.flysky.coder.vo.chat.ChatMessage;
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
