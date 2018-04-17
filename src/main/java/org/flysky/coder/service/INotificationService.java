package org.flysky.coder.service;

import org.flysky.coder.entity.Message;
import org.flysky.coder.entity.Reply;

import java.util.List;

public interface INotificationService {
    void newMessageNotification(int uid,int messageId);
    void newReplyNotification(int uid,int replyId);
    void newPostDeleted(int uid,String postName);
    void newPostRecovered(int uid,String postName);
    void beforeViewMessageNoticications(int uid);
    void beforeViewReplyNotifications(int uid);
    void beforeViewPostDeletedNotifications(int uid);
    void beforeViewPostRecoveredNotifications(int uid);
    List<Reply> viewReplyNotifications(int uid);
    List<String> viewPostDeletedNotifications(int uid);
    List<String> viewPostRecoveredNotifications(int uid);
    Long getNewMessageNotificationNum(int uid);
    Long getNewReplyNotificationNum(int uid);
    Long getNewPostDeletedNotificationNum(int uid);
    Long getNewPostRecoveredNotificaionNum(int uid);
}
