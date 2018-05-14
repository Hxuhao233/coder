package org.flysky.coder.service;

import org.flysky.coder.entity.Reply;
import org.flysky.coder.vo2.Response.ReplyWrapper;


import java.time.LocalDateTime;
import java.util.List;

public interface IReplyService {
    Integer replyToPost(Integer postId,Integer uid,String content,boolean isAnonymous,String anonymousName,Integer type);
    Integer createInnerReply(Integer postId,Integer uid,String content,Integer floorCount,boolean isAnonymous,String anonymousName,Integer type);
    Integer deleteReply(Integer postId,Integer replyId);
    List<ReplyWrapper> getRepliesByPostId(int postId);
    List<Reply> getReplyByContentAndTimeAndType(String content, LocalDateTime time1, LocalDateTime time2, Integer type);
    List<Reply> searchForumPostReply(String content,String username,String title);
    List<Reply> searchAnonymousPostReply(String content,String title);
    Reply getReplyById(Integer id);
}
