package org.flysky.coder.service.impl;

import org.flysky.coder.entity.Message;
import org.flysky.coder.entity.User;
import org.flysky.coder.mapper.MessageMapper;
import org.flysky.coder.mapper.UserMapper;
import org.flysky.coder.service.IMessageService;
import org.flysky.coder.service.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService implements IMessageService{
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private INotificationService notificationService;

    @Override
    public Integer createMessage(Integer fromUid, Integer toUid, String content) {
        if(fromUid==null||toUid==null||content==null){
            return 0;
        }

        if(fromUid==toUid){
            return 0;
        }

        if(fromUid<=0||toUid<=0){
            return 0;
        }

        User u1=userMapper.selectByPrimaryKey(fromUid);
        User u2=userMapper.selectByPrimaryKey(toUid);
        if(u1==null||u2==null){
            return 0;
        }

        Message message=new Message();
        message.setFromUid(fromUid);
        message.setToUid(toUid);
        message.setContent(content);
        message.setCreatedAt(LocalDateTime.now());

        messageMapper.insert(message);

        notificationService.newMessageNotification(toUid,message.getId());
        return 1;
    }

    @Override
    public List<Message> showConversations(Integer uid1, Integer uid2) {
        return messageMapper.viewConversations(uid1,uid2);
    }

    @Override
    public List<Message> getMessageByContentAndTime(String content, LocalDateTime time1, LocalDateTime time2) {
        return messageMapper.getMessageByContentAndTime(content,time1,time2);
    }

    @Override
    public Integer deleteMessage(Integer id) {
        return messageMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Message getMessageById(Integer id) {
        return messageMapper.selectByPrimaryKey(id);
    }


}
