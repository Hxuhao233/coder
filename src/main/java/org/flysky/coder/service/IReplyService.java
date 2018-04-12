package org.flysky.coder.service;

public interface IReplyService {
    Integer replyToPost(Integer postId,Integer uid,String content);
    Integer deleteReply(Integer replyId);
}
