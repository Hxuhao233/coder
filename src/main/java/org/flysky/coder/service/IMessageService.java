package org.flysky.coder.service;

import org.flysky.coder.entity.Message;

import java.time.LocalDateTime;
import java.util.List;

public interface IMessageService {
    Integer createMessage(Integer fromUid,Integer toUid,String content);
    List<Message> showConversations(Integer uid1,Integer uid2);
    List<Message> getMessageByContentAndTime(String content,LocalDateTime time1,LocalDateTime time2);
}
