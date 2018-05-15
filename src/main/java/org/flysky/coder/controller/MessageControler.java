package org.flysky.coder.controller;

import org.flysky.coder.entity.Message;
import org.flysky.coder.service.IMessageService;
import org.flysky.coder.token.RedisTokenService;
import org.flysky.coder.vo.MessageWrapper;
import org.flysky.coder.vo.ResultWrapper;
import org.flysky.coder.vo.message.SearchMessageWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.session.SessionProperties;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MessageControler {
    @Autowired
    private IMessageService messageService;

    @Autowired
    private RedisTokenService redisTokenService;

    @RequestMapping("/message/createMessage/{token}")
    public ResultWrapper createMessage(@RequestBody MessageWrapper msgWrapper,@PathVariable String token){
        Integer fromUid=redisTokenService.getIdByToken(token);
        Integer toUid=msgWrapper.getToUid();
        String content=msgWrapper.getContent();
        if(fromUid==null||toUid==null||content==null){
            return new ResultWrapper(0);
        }

        Integer result=messageService.createMessage(fromUid,toUid,content);

        return new ResultWrapper(result);
    }

    @RequestMapping("/message/viewConversations/{fromUid}/{toUid}")
    public ResultWrapper viewConversations(@PathVariable Integer fromUid, @PathVariable Integer toUid){
        if(fromUid==null||toUid==null){
            return null;
        }
        ResultWrapper rw=new ResultWrapper();
        rw.setPayload(messageService.showConversations(fromUid,toUid));
        return rw;
    }

    @RequestMapping("/message/viewConversations/{toUid}/{token}")
    public ResultWrapper viewConversationsWithToUid(@PathVariable Integer toUid,@PathVariable String token){
        Integer fromUid=redisTokenService.getIdByToken(token);
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
}
