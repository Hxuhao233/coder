package org.flysky.coder.controller;

import org.flysky.coder.entity.Notification;
import org.flysky.coder.entity.Reply;
import org.flysky.coder.service.INotificationService;
import org.flysky.coder.token.RedisTokenService;
import org.flysky.coder.vo.Result;
import org.flysky.coder.vo.ResultWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NotificationController {
    @Autowired
    private INotificationService notificationService;

    @Autowired
    private RedisTokenService redisTokenService;

    @RequestMapping("/notification/beforeViewMessageNotifications/{uid}")
    public void beforeViewMessageNotifications(@PathVariable int uid){
        notificationService.beforeViewMessageNoticications(uid);
    }

    @RequestMapping("/notification/beforeViewPostDeletedNotifications/{uid}")
    public void beforeViewPostDeletedNotifications(@PathVariable int uid){
        notificationService.beforeViewPostDeletedNotifications(uid);
    }

    @RequestMapping("/notification/beforeViewPostRecoveredNotifications/{uid}")
    public void beforeViewPostRecoveredNotifications(@PathVariable int uid){
        notificationService.beforeViewPostRecoveredNotifications(uid);
    }

    @RequestMapping("/notification/beforeViewReplyNotifications/{uid}")
    public void beforeViewReplyNotifications(@PathVariable int uid){
        notificationService.beforeViewReplyNotifications(uid);
    }

    @RequestMapping("/notification/viewReplyNotifications/{token}")
    public ResultWrapper viewReplyNotifications(@PathVariable String token){
        List<Reply> replyList=notificationService.viewReplyNotifications(redisTokenService.getIdByToken(token));
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

    @RequestMapping("/notification/viewPostDeletedNotifications/{token}")
    public ResultWrapper viewPostDeletedNotifications(@PathVariable String token){
        List<String> postDeletedInfoList=notificationService.viewPostDeletedNotifications(redisTokenService.getIdByToken(token));
        ResultWrapper resultWrapper=new ResultWrapper();
        if(postDeletedInfoList.isEmpty()||postDeletedInfoList==null){
            resultWrapper.setCode(0);
            resultWrapper.setPayload(null);
        }else{
            resultWrapper.setCode(1);
            resultWrapper.setPayload(postDeletedInfoList);
        }
        return resultWrapper;
    }

    @RequestMapping("/notification/viewPostRecoveredNotifications/{token}")
    public ResultWrapper viewPostRecoveredNotifications(@PathVariable String token){
        List<String> postRecoveredInfoList=notificationService.viewPostRecoveredNotifications(redisTokenService.getIdByToken(token));
        ResultWrapper resultWrapper=new ResultWrapper();
        if(postRecoveredInfoList.isEmpty()||postRecoveredInfoList==null){
            resultWrapper.setCode(0);
            resultWrapper.setPayload(null);
        }else{
            resultWrapper.setCode(1);
            resultWrapper.setPayload(postRecoveredInfoList);
        }
        return resultWrapper;
    }

    @RequestMapping("/notification/getReplyNotificationNum/{token}")
    public Long getReplyNotificationNum(@PathVariable String token){
        return notificationService.getNewReplyNotificationNum(redisTokenService.getIdByToken(token));
    }

    @RequestMapping("/notification/getPostDeletedNotificationNum/{token}")
    public Long getPostDeletedNotificationNum(@PathVariable String token){
        return notificationService.getNewPostDeletedNotificationNum(redisTokenService.getIdByToken(token));
    }

    @RequestMapping("/notification/getPostRecoveredNotificationNum/{token}")
    public Long getPostRecoveredNotificationNum(@PathVariable String token){
        return notificationService.getNewPostRecoveredNotificaionNum(redisTokenService.getIdByToken(token));
    }

    @RequestMapping("/notification/getMessageNotificationNum/{token}")
    public Long getMessageNotificationNum(@PathVariable String token){
        return notificationService.getNewMessageNotificationNum(redisTokenService.getIdByToken(token));
    }
}
