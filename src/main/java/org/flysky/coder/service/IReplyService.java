package org.flysky.coder.service;

import org.flysky.coder.vo.ReplyWrapper;

import java.util.List;

public interface IReplyService {
    Integer replyToPost(Integer postId,Integer uid,String content,boolean isAnonymous,String anonymousName);
    Integer createInnerReply(Integer postId,Integer uid,String content,Integer floorCount,boolean isAnonymous,String anonymousName);
    Integer deleteReply(Integer postId,Integer replyId);
    List<ReplyWrapper> getRepliesByPostId(int postId);
}
