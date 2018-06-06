package org.flysky.coder.controller;

import com.github.pagehelper.PageInfo;
import org.flysky.coder.entity.Message;
import org.flysky.coder.entity.User;
import org.flysky.coder.service.IMessageService;
import org.flysky.coder.service.IUserService;
import org.flysky.coder.token.RedisTokenService;
import org.flysky.coder.vo.MessageWrapper;
import org.flysky.coder.vo.Result;
import org.flysky.coder.vo.ResultWrapper;
import org.flysky.coder.vo.message.SearchMessageWrapper;
import org.flysky.coder.vo2.Request.Message.SearchMessageWrap;
import org.flysky.coder.vo2.Response.MessageResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.session.SessionProperties;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MessageController {
    @Autowired
    private IMessageService messageService;

    @Autowired
    private RedisTokenService redisTokenService;

    @Autowired
    private IUserService userService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @RequestMapping("/message/createMessage")
    public ResultWrapper createMessage(@RequestBody MessageWrapper msgWrapper,HttpSession session){
        User u=(User) session.getAttribute("user");
//        Integer fromUid=u.getId();
//        Integer toUid=msgWrapper.getToUid();
        Integer fromUid=1;
        Integer toUid=2;
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
       Integer fromUid=u.getId();

        if(toUid==null){
            return null;
        }
        List<Message> msg=messageService.showConversations(fromUid,toUid);
        List<MessageResponseWrapper> messageResponseWrappers=new ArrayList<>();
        for(Message m:msg){
            MessageResponseWrapper mrw=new MessageResponseWrapper();
            mrw.setId(m.getId());
            mrw.setTime(m.getCreatedAt());
            mrw.setSender(userService.getUserNameById(m.getFromUid()));
            mrw.setReceiver(userService.getUserNameById(m.getToUid()));
        }
        ResultWrapper rw=new ResultWrapper();
        rw.setPayload(messageResponseWrappers);
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

    @RequestMapping("/message/searchMessage")
    public ResultWrapper searchMessage(@RequestBody SearchMessageWrap searchMessageWrap,
                                       @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        ResultWrapper rw=new ResultWrapper();
        PageInfo messagePageInfo=messageService.searchMessage(searchMessageWrap.getContent(),searchMessageWrap.getUsername1(),searchMessageWrap.getUsername2(),pageNum,pageSize);
        List<Message> msgList=messagePageInfo.getList();
        List<MessageResponseWrapper> mrwList=new ArrayList<MessageResponseWrapper>();
        for(Message m:msgList){
            MessageResponseWrapper mrw=new MessageResponseWrapper();
            mrw.setContent(m.getContent());
            mrw.setSender(userService.getUserById(m.getFromUid()).getUsername());
            mrw.setReceiver(userService.getUserById(m.getToUid()).getUsername());
            mrw.setTime(m.getCreatedAt());
            mrw.setId(m.getId());
            mrwList.add(mrw);
        }
        messagePageInfo.setList(mrwList);
        rw.setPayload(messagePageInfo);
        return rw;
    }

}
