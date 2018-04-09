package org.flysky.coder.service;

import org.flysky.coder.entity.User;
import org.flysky.coder.entity.wrapper.RecordWrapper;
import org.flysky.coder.vo.chat.ChatMessage;

import java.util.List;

/**
 * Created by hxuhao233 on 2018/3/28.
 */
public interface IChatService {
    ChatMessage chat(User user, ChatMessage chatMessage);

    ChatMessage enterRoom(User user, ChatMessage chatMessage);

    ChatMessage exitRoom(User user, ChatMessage chatMessage);

    List<RecordWrapper> getRecord(int roomId, int pageNum, int pageSize);
}
