package org.flysky.coder.controller;

import org.flysky.coder.vo.chat.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;


@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/enter")
    public void enter(@RequestBody Message enterRoomMessage) throws Exception {
        String username = enterRoomMessage.getContent();     // 获取用户名，后续实现改为从session中获取
        String room = enterRoomMessage.getRoom();
        Message response = new Message(username + "加入交流室!");
        response.setRoom(room);
        response.setTime(LocalDateTime.now());
        template.convertAndSend("/chat/" + room,response);
    }


    @MessageMapping("/exit")
    public void exit(@RequestBody Message exitRoomMessage) throws Exception {
        String username = exitRoomMessage.getContent();     // 获取用户名，后续实现改为从session中获取
        String room = exitRoomMessage.getRoom();
        Message response = new Message(username + "退出交流室!");
        response.setRoom(room);
        response.setTime(LocalDateTime.now());
        template.convertAndSend("/chat/" + room,response);
    }


    @MessageMapping("/message")
    public void chat(@RequestBody Message chatMessage) {
        chatMessage.setTime(LocalDateTime.now());
        template.convertAndSend("/chat/" + chatMessage.getRoom(),chatMessage);
    }

}
