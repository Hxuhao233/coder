package org.flysky.coder.controller;

import org.flysky.coder.entity.Message;
import org.flysky.coder.entity.User;
import org.flysky.coder.service.IMessageService;
import org.flysky.coder.token.RedisTokenService;
import org.flysky.coder.vo.MessageWrapper;
import org.flysky.coder.vo.Result;
import org.flysky.coder.vo.ResultWrapper;
import org.flysky.coder.vo.message.SearchMessageWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.session.SessionProperties;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class MessageController {
    @Autowired
    private IMessageService messageService;

    @Autowired
    private RedisTokenService redisTokenService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @RequestMapping("/message/createMessage")
    public ResultWrapper createMessage(@RequestBody MessageWrapper msgWrapper,HttpSession session){
        User u=(User) session.getAttribute("user");
        //Integer fromUid=u.getId();
        Integer fromUid=1;
        Integer toUid=msgWrapper.getToUid();
        String content=msgWrapper.getContent();
        if(fromUid==null||toUid==null||content==null){
            return new ResultWrapper(0);
        }

        Integer result=messageService.createMessage(fromUid,toUid,content);

        return new ResultWrapper(result);
    }

    @RequestMapping("/message/viewConversations/{toUid}")
    public ResultWrapper viewConversations(@PathVariable Integer toUid,HttpSession session){
        User u=(User) session.getAttribute("user");
       // Integer fromUid=u.getId();
        Integer fromUid=1;
        if(toUid==null){
            return null;
        }
        ResultWrapper rw=new ResultWrapper();
        rw.setPayload(messageService.showConversations(fromUid,toUid));
        return rw;
    }

    @RequestMapping("/message/viewConversations/{toUid}/{token}")
    public ResultWrapper viewConversationsWithToUid(@PathVariable Integer toUid,HttpSession session){
        User u=(User) session.getAttribute("user");
        Integer fromUid=u.getId();
        if(fromUid==null||toUid==null){
            return null;
        }
        ResultWrapper rw=new ResultWrapper();
        rw.setPayload(messageService.showConversations(fromUid,toUid));
        return rw;
    }

    @RequestMapping("/message/getMessageByContentAndTime")
    public ResultWrapper getMessageByContentAndTime(@RequestBody SearchMessageWrapper smw){
        ResultWrapper rw=new ResultWrapper();
        rw.setPayload(messageService.getMessageByContentAndTime(smw.getContent(),smw.getTime1(),smw.getTime2()));
        return rw;
    }

    @RequestMapping("/message/deleteMessage/{id}")
    public Result deleteMessage(@PathVariable int id){
        Result result=new Result();
        result.setCode(messageService.deleteMessage(id));
        return result;
    }

    @RequestMapping("/message/getMessageById/{id}")
    public Message getMessageById(@PathVariable int id){
        return messageService.getMessageById(id);
    }

}
