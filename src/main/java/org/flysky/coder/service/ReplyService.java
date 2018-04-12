package org.flysky.coder.service;

import org.flysky.coder.mapper.ReplyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReplyService implements IReplyService{
    @Autowired
    private ReplyMapper replyMapper;

    @Override
    public Integer replyToPost(Integer postId, Integer uid, String content) {
        return null;
    }

    @Override
    public Integer deleteReply(Integer replyId) {
        return null;
    }
}
