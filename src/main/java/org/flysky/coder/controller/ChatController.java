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

        int roomId = enterRoomMessage.getRoomId();
        ChatMessage response = new ChatMessage("加入交流室!");
        response.setRoomId(roomId);
        response.setCreatedAt(LocalDateTime.now());
        response.setUsername(user.getUsername());
        template.convertAndSend("/chat/" + roomId,response);
    }


    @MessageMapping("/exit")
    public void exit(SimpMessageHeaderAccessor headerAccessor, @RequestBody ChatMessage exitRoomMessage) throws Exception {
        User user = (User) headerAccessor.getSessionAttributes().get("user");
        int roomId = exitRoomMessage.getRoomId();
        ChatMessage response = new ChatMessage("退出交流室!");
        response.setRoomId(roomId);
        response.setCreatedAt(LocalDateTime.now());
        response.setUsername(user.getUsername());
        template.convertAndSend("/chat/" + roomId,response);
    }

    /**
     * 交流
     * @param chatMessage
     */
    @MessageMapping("/chat")
    public void chat(SimpMessageHeaderAccessor headerAccessor, @RequestBody ChatMessage chatMessage) {
        User user = (User) headerAccessor.getSessionAttributes().get("user");
        chatService.chat(user,chatMessage);
    }

    @MessageMapping("/getRecord")
    @SendToUser("/self")
    public List<RecordWrapper> getMessageRecord(@RequestBody RecordPage recordPage) {
        List<RecordWrapper> recordLists = chatService.getRecord(recordPage.getRoomId(), recordPage.getPageNum(), recordPage.getPageSize());
        return recordLists;
    }


}
