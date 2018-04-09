package org.flysky.coder.controller;

import org.flysky.coder.entity.User;
import org.flysky.coder.entity.wrapper.RecordWrapper;
import org.flysky.coder.service.IChatService;
import org.flysky.coder.vo.chat.ChatMessage;
import org.flysky.coder.vo.chat.RecordPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;


@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private IChatService chatService;

    @MessageMapping("/enter")
    public void enter(SimpMessageHeaderAccessor headerAccessor, @RequestBody ChatMessage enterRoomMessage) throws Exception {
        User user = (User) headerAccessor.getSessionAttributes().get("user");

        ChatMessage response = chatService.enterRoom(user,enterRoomMessage);
        template.convertAndSend("/chat/" + response.getRoomId(),response);

        // 获取最近10条聊天记录
        List<RecordWrapper> recordLists = chatService.getRecord(enterRoomMessage.getRoomId(),1,10);
        template.convertAndSendToUser(headerAccessor.getSessionId(),"/self",recordLists);
    }


    @MessageMapping("/exit")
    public void exit(SimpMessageHeaderAccessor headerAccessor, @RequestBody ChatMessage exitRoomMessage) throws Exception {
        User user = (User) headerAccessor.getSessionAttributes().get("user");

        ChatMessage response = chatService.exitRoom(user,exitRoomMessage);
        template.convertAndSend("/chat/" + response.getRoomId(),response);
    }

    /**
     * 交流
     * @param chatMessage
     */
    @MessageMapping("/chat")
    public void chat(SimpMessageHeaderAccessor headerAccessor, @RequestBody ChatMessage chatMessage) {
        User user = (User) headerAccessor.getSessionAttributes().get("user");

        ChatMessage response = chatService.chat(user,chatMessage);
        template.convertAndSend("/chat/" + response.getRoomId(), response);
    }

    @MessageMapping("/getRecord")
    public void getMessageRecord(SimpMessageHeaderAccessor headerAccessor, @RequestBody RecordPage recordPage) {
        List<RecordWrapper> recordLists = chatService.getRecord(recordPage.getRoomId(), recordPage.getPageNum(), recordPage.getPageSize());
        template.convertAndSendToUser(headerAccessor.getSessionId(),"/self",recordLists);
    }


}
