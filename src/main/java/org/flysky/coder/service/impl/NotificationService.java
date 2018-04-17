package org.flysky.coder.service.impl;

import org.flysky.coder.entity.Message;
import org.flysky.coder.entity.Reply;
import org.flysky.coder.mapper.MessageMapper;
import org.flysky.coder.mapper.ReplyMapper;
import org.flysky.coder.service.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService implements INotificationService{
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private ReplyMapper replyMapper;

    private final String unreadMessageNotificationPrefix="NewMessageNotification-";
    private final String unreadReplyNotificationPrefix="NewReplyNotification-";
    private final String unreadPostDeletedNotificationPrefix="NewPostDeletedNotification-";
    private final String unreadPostRecoveredNotificationPrefix="NewPostRecoveredNotification-";

    private final String oldMessageNotificationPrefix="OldMessageNotification-";
    private final String oldReplyNotificationPrefix="OldReplyNotification-";
    private final String oldPostDeletedNotificationPrefix="OldPostNotification-";
    private final String oldPostRecoveredNotificationPrefix="OldPostRecoveredNotification-";

    @Override
    public void newMessageNotification(int uid, int messageId) {
        redisTemplate.opsForList().leftPush(unreadMessageNotificationPrefix+String.valueOf(uid),String.valueOf(messageId));
    }

    @Override
    public void newReplyNotification(int uid, int replyId) {
        redisTemplate.opsForList().leftPush(unreadReplyNotificationPrefix+String.valueOf(uid),String.valueOf(replyId));
    }

    @Override
    public void newPostDeleted(int uid, String postName) {
        redisTemplate.opsForList().leftPush(unreadPostDeletedNotificationPrefix+String.valueOf(uid),postName);
    }

    public void newPostRecovered(int uid,String postName){
        redisTemplate.opsForList().leftPush(unreadPostRecoveredNotificationPrefix+String.valueOf(uid),postName);
    }

    public void beforeViewMessageNoticications(int uid) {
        String unreadMessageQueueName=unreadMessageNotificationPrefix+String.valueOf(uid);
        String oldMessageQueueName=oldMessageNotificationPrefix+String.valueOf(uid);

        List<String> msgList=redisTemplate.opsForList().range(unreadMessageQueueName,0,-1);
        redisTemplate.delete(unreadMessageQueueName);
        for(int i=msgList.size()-1;i>=0;i--){
            redisTemplate.opsForList().leftPush(oldMessageQueueName,msgList.get(i));
        }

    }


    public void beforeViewReplyNotifications(int uid) {
        String unreadReplyQueueName=unreadReplyNotificationPrefix+String.valueOf(uid);
        String oldReplyQueueName=oldReplyNotificationPrefix+String.valueOf(uid);

        List<String> replyList=redisTemplate.opsForList().range(unreadReplyQueueName,0,-1);
        redisTemplate.delete(unreadReplyQueueName);
        for(int i=replyList.size()-1;i>=0;i--){
            redisTemplate.opsForList().leftPush(oldReplyQueueName,replyList.get(i));
        }
    }


    public void beforeViewPostDeletedNotifications(int uid) {
        String unreadPostDeletedQueueName=unreadPostDeletedNotificationPrefix+String.valueOf(uid);
        String oldPostDeletedQueueName=oldPostDeletedNotificationPrefix+String.valueOf(uid);

        List<String> postDeletedList=redisTemplate.opsForList().range(unreadPostDeletedQueueName,0,-1);
        redisTemplate.delete(unreadPostDeletedQueueName);
        for(int i=postDeletedList.size()-1;i>=0;i--){
            redisTemplate.opsForList().leftPush(oldPostDeletedQueueName,postDeletedList.get(i));
        }
    }

    public void beforeViewPostRecoveredNotifications(int uid){
        String unreadPostRecoveredQueueName=unreadPostRecoveredNotificationPrefix+String.valueOf(uid);
        String oldPostRecoveredQueueName=oldPostRecoveredNotificationPrefix+String.valueOf(uid);

        List<String> postRecoveredList=redisTemplate.opsForList().range(unreadPostRecoveredQueueName,0,-1);
        redisTemplate.delete(unreadPostRecoveredQueueName);
        for(int i=postRecoveredList.size()-1;i>=0;i--){
            redisTemplate.opsForList().leftPush(oldPostRecoveredQueueName,postRecoveredList.get(i));
        }
    }

    /*
    @Override
    public void viewMessageNoticications(int uid) {
        String oldMessageQueueName=oldMessageNotificationPrefix+String.valueOf(uid);
        List<String> oldMsgNotificationStringList=redisTemplate.opsForList().range(oldMessageQueueName,0,-1);
        List<Message> msgList=new ArrayList<Message>();
        for(String s:oldMsgNotificationStringList){
            int messageId=Integer.parseInt(s);
            Message msg=messageMapper.selectByPrimaryKey(messageId);
            if(msg!=null) {
                msgList.add(msg);
            }
        }

    }*/

    @Override
    public List<Reply> viewReplyNotifications(int uid) {
        String oldReplyQueueName=oldReplyNotificationPrefix+String.valueOf(uid);
        List<String> oldReplyNotificationStringList=redisTemplate.opsForList().range(oldReplyQueueName,0,-1);
        List<Reply> replyList=new ArrayList<Reply>();
        for(String s:oldReplyNotificationStringList){
            int replyId=Integer.parseInt(s);
            Reply reply=replyMapper.selectByPrimaryKey(replyId);
            if(reply!=null) {
                replyList.add(reply);
            }
        }
        return replyList;
    }

    @Override
    public List<String> viewPostDeletedNotifications(int uid) {
        String oldPostDeletedQueueName=oldPostDeletedNotificationPrefix+String.valueOf(uid);
        List<String> oldPostDeletedNotificationList=redisTemplate.opsForList().range(oldPostDeletedQueueName,0,-1);

        return oldPostDeletedNotificationList;
    }

    @Override
    public List<String> viewPostRecoveredNotifications(int uid){
        String oldPostRecoveredQueueName=oldPostRecoveredNotificationPrefix+String.valueOf(uid);
        List<String> oldPostRecoveredNotificationList=redisTemplate.opsForList().range(oldPostRecoveredQueueName,0,-1);

        return oldPostRecoveredNotificationList;
    }
}
