package org.flysky.coder.service;

public interface IReplyService {
    Integer replyToPost(Integer postId,Integer uid);
    Integer deleteReply(Integer replyId);
}
