package org.flysky.coder.controller;

import org.flysky.coder.vo.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/join")
    @SendTo("/topic/chat")
    public ChatMessage join(@RequestBody ChatMessage name) throws Exception {
        return new ChatMessage(name.getContent() + "进入聊天室");
    }


    @MessageMapping("/message")
    public void chat(@RequestBody ChatMessage message) {
        template.convertAndSend("/topic/chat",message);
    }

}
