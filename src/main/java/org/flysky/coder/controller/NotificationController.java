package org.flysky.coder.controller;

import org.flysky.coder.entity.Message;
import org.flysky.coder.entity.Notification;
import org.flysky.coder.entity.Reply;
import org.flysky.coder.entity.User;
import org.flysky.coder.service.INotificationService;
import org.flysky.coder.service.IUserService;
import org.flysky.coder.token.RedisTokenService;
import org.flysky.coder.vo.Result;
import org.flysky.coder.vo.ResultWrapper;
import org.flysky.coder.vo2.Response.AllReplyNotificationsWrapper;
import org.flysky.coder.vo2.Response.MessageNotificationWrapper;
import org.flysky.coder.vo2.Response.ReplyNotificationWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.core.AbstractMessageSendingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
public class NotificationController {
    @Autowired
    private INotificationService notificationService;

    @Autowired
    private RedisTokenService redisTokenService;

    @Autowired
    private IUserService userService;


    @RequestMapping("/notification/afterViewMessageNotifications")
    public void afterViewMessageNotifications(HttpSession session){
        User u=(User)session.getAttribute("user");

        notificationService.afterViewMessageNoticications(u.getId());
    }

//    @RequestMapping("/notification/beforeViewPostDeletedNotifications/{uid}")
//    public void beforeViewPostDeletedNotifications(@PathVariable int uid){
//        notificationService.beforeViewPostDeletedNotifications(uid);
//    }
//
//    @RequestMapping("/notification/beforeViewPostRecoveredNotifications/{uid}")
//    public void beforeViewPostRecoveredNotifications(@PathVariable int uid){
//        notificationService.beforeViewPostRecoveredNotifications(uid);
//    }

    @RequestMapping("/notification/afterViewReplyNotifications")
    public void afterViewReplyNotifications(HttpSession session){
        User u=(User)session.getAttribute("user");
        notificationService.afterViewReplyNotifications(u.getId());
    }

    @RequestMapping("/notification/viewReplyNotifications")
    public ResultWrapper viewReplyNotifications(HttpSession session){
        List<Reply> replyList=notificationService.viewReplyNotifications(2);
        ResultWrapper resultWrapper=new ResultWrapper();
        if(replyList.isEmpty()||replyList==null){
            resultWrapper.setCode(0);
            resultWrapper.setPayload(null);
        }else{
            resultWrapper.setCode(1);
            resultWrapper.setPayload(replyList);
        }
        return resultWrapper;
    }

//    @RequestMapping("/notification/viewPostDeletedNotifications/{token}")
//    public ResultWrapper viewPostDeletedNotifications(@PathVariable String token){
//        List<String> postDeletedInfoList=notificationService.viewPostDeletedNotifications(redisTokenService.getIdByToken(token));
//        ResultWrapper resultWrapper=new ResultWrapper();
//        if(postDeletedInfoList.isEmpty()||postDeletedInfoList==null){
//            resultWrapper.setCode(0);
//            resultWrapper.setPayload(null);
//        }else{
//            resultWrapper.setCode(1);
//            resultWrapper.setPayload(postDeletedInfoList);
//        }
//        return resultWrapper;
//    }
//
//    @RequestMapping("/notification/viewPostRecoveredNotifications/{token}")
//    public ResultWrapper viewPostRecoveredNotifications(@PathVariable String token){
//        List<String> postRecoveredInfoList=notificationService.viewPostRecoveredNotifications(redisTokenService.getIdByToken(token));
//        ResultWrapper resultWrapper=new ResultWrapper();
//        if(postRecoveredInfoList.isEmpty()||postRecoveredInfoList==null){
//            resultWrapper.setCode(0);
//            resultWrapper.setPayload(null);
//        }else{
//            resultWrapper.setCode(1);
//            resultWrapper.setPayload(postRecoveredInfoList);
//        }
//        return resultWrapper;
//    }

    @RequestMapping("/notification/getReplyNotificationNum")
    public Long getReplyNotificationNum(HttpSession session){
        return notificationService.getNewReplyNotificationNum(2);
    }

//    @RequestMapping("/notification/getPostDeletedNotificationNum/{token}")
//    public Long getPostDeletedNotificationNum(@PathVariable String token){
//        return notificationService.getNewPostDeletedNotificationNum(redisTokenService.getIdByToken(token));
//    }
//
//    @RequestMapping("/notification/getPostRecoveredNotificationNum/{token}")
//    public Long getPostRecoveredNotificationNum(@PathVariable String token){
//        return notificationService.getNewPostRecoveredNotificaionNum(redisTokenService.getIdByToken(token));
//    }

    @RequestMapping("/notification/getMessageNotificationNum")
    public Long getMessageNotificationNum(HttpSession session){
        return notificationService.getNewMessageNotificationNum(2);
    }

    @RequestMapping("/notification/getNewestMessageNotifications")
    public ResultWrapper getNewestMessageNotifications(HttpSession session){
        User u=(User)session.getAttribute("user");
        Integer uid=u.getId();
        //Integer uid=2;
        ResultWrapper rw=new ResultWrapper();
        List<Message> msg=notificationService.getNewMessageNotifications(uid);
        if(msg.isEmpty()){
            rw.setPayload(null);
            return null;
        }
        Message m=msg.get(0);
        MessageNotificationWrapper mnw=new MessageNotificationWrapper();
        mnw.setContent(m.getContent());
        mnw.setFromUsername(userService.getUserById(m.getFromUid()).getUsername());
        mnw.setTime(m.getCreatedAt());
        mnw.setId(m.getId());
        mnw.setNewNotificationNum(notificationService.getNewMessageNotificationNum(uid));
        rw.setPayload(mnw);
        return rw;
    }

    @RequestMapping("/notification/getNewestReplyNotifications")
    public ResultWrapper getNewestReplyNotifications(HttpSession session){
        User u=(User)session.getAttribute("user");
        //Integer uid=2;
        Integer uid=u.getId();
        ResultWrapper rw=new ResultWrapper();
        List<Reply> replyList=notificationService.getReplyNotifications(uid);
        if(replyList.isEmpty()){
            rw.setPayload(null);
            return rw;
        }
        Reply r=replyList.get(0);
        ReplyNotificationWrapper rnw=new ReplyNotificationWrapper();
        rnw.setId(r.getId());
        rnw.setNewReplyNum(notificationService.getNewReplyNotificationNum(uid));
        rnw.setReplyUsername(userService.getUserById(r.getUserId()).getUsername());
        rnw.setTime(r.getCreatedAt());
        rnw.setContent(r.getContent());
        rw.setPayload(rnw);
        return rw;
    }

    @RequestMapping("/notification/getReplyNotifications")
    public ResultWrapper getReplyNotifications(HttpSession session){
        User u=(User)session.getAttribute("user");
        //Integer uid=2;
        Integer uid=u.getId();
        ResultWrapper rw=new ResultWrapper();

        List<ReplyNotificationWrapper> newList=new ArrayList<>();
        List<Reply> replyList=notificationService.getReplyNotifications(uid);
        for(Reply r:replyList) {
            ReplyNotificationWrapper rnw = new ReplyNotificationWrapper();
            rnw.setId(r.getId());
            rnw.setNewReplyNum(notificationService.getNewReplyNotificationNum(uid));
            rnw.setReplyUsername(userService.getUserById(r.getUserId()).getUsername());
            rnw.setTime(r.getCreatedAt());
            rnw.setContent(r.getContent());
            newList.add(rnw);
        }

        List<ReplyNotificationWrapper> oldList=new ArrayList<>();
        List<Reply> replyList1=notificationService.viewReplyNotifications(uid);

        for(Reply r:replyList1) {
            ReplyNotificationWrapper rnw = new ReplyNotificationWrapper();
            rnw.setId(r.getId());
            rnw.setNewReplyNum(notificationService.getNewReplyNotificationNum(uid));
            rnw.setReplyUsername(userService.getUserById(r.getUserId()).getUsername());
            rnw.setTime(r.getCreatedAt());
            rnw.setContent(r.getContent());
            oldList.add(rnw);
        }

        AllReplyNotificationsWrapper allReplyNotificationsWrapper=new AllReplyNotificationsWrapper();
        allReplyNotificationsWrapper.setNewReplyNotifications(newList);
        allReplyNotificationsWrapper.setOldReplyNotifications(oldList);

        rw.setPayload(allReplyNotificationsWrapper);
        return rw;
    }
}
