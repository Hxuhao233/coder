package org.flysky.coder.service;

import com.github.pagehelper.PageInfo;
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
    PageInfo<Reply> searchForumPostReply(String content, String username, String title,Integer page,Integer pageSize);
    PageInfo<Reply> searchAnonymousPostReply(String content,String title,Integer page,Integer pageSize);
    Reply getReplyById(Integer id);
    Integer getReplyNumByPostId(Integer postId);
}
