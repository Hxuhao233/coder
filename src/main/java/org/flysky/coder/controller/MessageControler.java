package org.flysky.coder.controller;

import org.flysky.coder.entity.Message;
import org.flysky.coder.service.IMessageService;
import org.flysky.coder.vo.MessageWrapper;
import org.flysky.coder.vo.ResultWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MessageControler {
    @Autowired
    private IMessageService messageService;

    @RequestMapping("/message/createMessage")
    public ResultWrapper createMessage(@RequestBody MessageWrapper msgWrapper){
        Integer fromUid=msgWrapper.getFromUid();
        Integer toUid=msgWrapper.getToUid();
        String content=msgWrapper.getContent();
        if(fromUid==null||toUid==null||content==null){
            return new ResultWrapper(0);
        }

        Integer result=messageService.createMessage(fromUid,toUid,content);

        return new ResultWrapper(result);
    }

    @RequestMapping("/message/viewConversations/{fromUid}/{toUid}")
    public List<Message> viewConversations(@PathVariable Integer fromUid, @PathVariable Integer toUid){
        if(fromUid==null||toUid==null){
            return null;
        }

        return messageService.showConversations(fromUid,toUid);
    }
}
