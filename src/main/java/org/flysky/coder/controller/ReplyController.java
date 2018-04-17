package org.flysky.coder.controller;

import org.apache.ibatis.javassist.runtime.Inner;
import org.flysky.coder.service.IReplyService;
import org.flysky.coder.vo.ReplyWrapper;
import org.flysky.coder.vo.Result;
import org.flysky.coder.vo.reply.InnerPostReplyWrapper;
import org.flysky.coder.vo.reply.PostReplyWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReplyController {
    @Autowired
    private IReplyService replyService;

    @RequestMapping("/reply/replyToPost")
    public Result replyToPost(@RequestBody PostReplyWrapper prw){
        boolean isAnonymous=prw.getIsAnonymous()==1?true:false;
        Integer resultI=null;
        if(prw!=null){
            resultI=replyService.replyToPost(prw.getPostId(),prw.getUid(),prw.getContent(),isAnonymous,prw.getAnonymousName());
        }
        Result result=new Result();
        result.setCode(resultI);
        result.setInfo(null);
        return result;
    }

    @RequestMapping("/reply/innerReplyToPost")
    public Result innerReplyToPost(@RequestBody InnerPostReplyWrapper innerPostReplyWrapper){
        boolean isAnonymous=innerPostReplyWrapper.getIsAnonymous()==1?true:false;
        Integer resultI=null;
        if(innerPostReplyWrapper!=null){
            resultI=replyService.createInnerReply(innerPostReplyWrapper.getPostId(),innerPostReplyWrapper.getUid(),innerPostReplyWrapper.getContent(),innerPostReplyWrapper.getFloor()
                          ,isAnonymous,innerPostReplyWrapper.getAnonymousName());
        }
        Result result=new Result();
        result.setCode(resultI);
        result.setInfo(null);
        return result;
    }

    @RequestMapping("/reply/deleteReply/{postId}/{replyId}")
    public Result deleteReply(@PathVariable Integer postId,@PathVariable Integer replyId){
        Result result=new Result();
        if(postId==null||replyId==null){
            result.setCode(0);
            result.setInfo(null);
            return result;
        }
        Integer resultI=replyService.deleteReply(postId,replyId);
        result.setCode(resultI);
        result.setInfo(null);
        return result;
    }

    @RequestMapping("/reply/getRepliesByPostId")
    public List<ReplyWrapper> getRepliesByPostId(@PathVariable Integer postId){
        return replyService.getRepliesByPostId(postId);
    }
}
