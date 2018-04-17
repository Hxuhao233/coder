package org.flysky.coder.controller;

import org.flysky.coder.entity.Notification;
import org.flysky.coder.entity.Reply;
import org.flysky.coder.service.INotificationService;
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

    @RequestMapping("/notification/viewReplyNotifications/{uid}")
    public ResultWrapper viewReplyNotifications(@PathVariable int uid){
        List<Reply> replyList=notificationService.viewReplyNotifications(uid);
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

    @RequestMapping("/notification/viewPostDeletedNotifications/{uid}")
    public ResultWrapper viewPostDeletedNotifications(@PathVariable int uid){
        List<String> postDeletedInfoList=notificationService.viewPostDeletedNotifications(uid);
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

    @RequestMapping("/notification/viewPostRecoveredNotifications/{uid}")
    public ResultWrapper viewPostRecoveredNotifications(@PathVariable int uid){
        List<String> postRecoveredInfoList=notificationService.viewPostRecoveredNotifications(uid);
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

    @RequestMapping("/notification/getReplyNotificationNum/{uid}")
    public Long getReplyNotificationNum(@PathVariable int uid){
        return notificationService.getNewReplyNotificationNum(uid);
    }

    @RequestMapping("/notification/getPostDeletedNotificationNum/{uid}")
    public Long getPostDeletedNotificationNum(@PathVariable int uid){
        return notificationService.getNewPostDeletedNotificationNum(uid);
    }

    @RequestMapping("/notification/getPostRecoveredNotificationNum/{uid}")
    public Long getPostRecoveredNotificationNum(@PathVariable int uid){
        return notificationService.getNewPostRecoveredNotificaionNum(uid);
    }

    @RequestMapping("/notification/getMessageNotificationNum/{uid}")
    public Long getMessageNotificationNum(@PathVariable int uid){
        return notificationService.getNewMessageNotificationNum(uid);
    }
}
