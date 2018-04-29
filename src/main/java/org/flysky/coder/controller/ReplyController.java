package org.flysky.coder.controller;

import org.apache.ibatis.javassist.runtime.Inner;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.flysky.coder.entity.Post;
import org.flysky.coder.entity.Reply;
import org.flysky.coder.entity.User;
import org.flysky.coder.service.INotificationService;
import org.flysky.coder.service.IPostService;
import org.flysky.coder.service.IReplyService;
import org.flysky.coder.vo.ReplyWrapper;
import org.flysky.coder.vo.Result;
import org.flysky.coder.vo.ResultWrapper;
import org.flysky.coder.vo.reply.InnerPostReplyWrapper;
import org.flysky.coder.vo.reply.PostReplyWrapper;
import org.flysky.coder.vo.reply.SearchReplyWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class ReplyController {
    @Autowired
    private IReplyService replyService;

    @Autowired
    private INotificationService notificationService;

    @Autowired
    private IPostService postService;

    @RequiresRoles("user")
    @RequestMapping("/reply/replyToPost")
    public Result replyToPost(@RequestBody PostReplyWrapper prw,HttpSession session){
        User u=(User)session.getAttribute("user");
        Integer uid=u.getId();
        boolean isAnonymous=prw.getIsAnonymous()==1?true:false;
        Integer resultI=null;
        if(prw!=null){
            resultI=replyService.replyToPost(prw.getPostId(),uid,prw.getContent(),isAnonymous,prw.getAnonymousName());
        }
        Result result=new Result();
        result.setCode(resultI);
        result.setInfo(null);
        return result;
    }

    @RequiresRoles("user")
    @RequestMapping("/reply/innerReplyToPost")
    public Result innerReplyToPost(@RequestBody InnerPostReplyWrapper innerPostReplyWrapper,HttpSession session){
        User u=(User)session.getAttribute("user");
        Integer uid=u.getId();
        boolean isAnonymous=innerPostReplyWrapper.getIsAnonymous()==1?true:false;
        Integer resultI=null;
        if(innerPostReplyWrapper!=null){
            resultI=replyService.createInnerReply(innerPostReplyWrapper.getPostId(),uid,innerPostReplyWrapper.getContent(),innerPostReplyWrapper.getFloor()
                          ,isAnonymous,innerPostReplyWrapper.getAnonymousName());
        }
        Result result=new Result();
        result.setCode(resultI);
        result.setInfo(null);
        return result;
    }

    @RequiresRoles("manager")
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

    @RequestMapping("/reply/getRepliesByPostId/{postId}")
    public List<ReplyWrapper> getRepliesByPostId(@PathVariable Integer postId){
        return replyService.getRepliesByPostId(postId);
    }

    @RequestMapping("/reply/getReplyByContentAndTimeAndType")
    public ResultWrapper getReplyByContentAndTimeAndType(@RequestBody SearchReplyWrapper srw){
        ResultWrapper resultWrapper=new ResultWrapper();
        List<Reply> replyList=replyService.getReplyByContentAndTimeAndType(srw.getContent(),srw.getTime1(),srw.getTime2(),srw.getType());
        resultWrapper.setPayload(replyList);
        return resultWrapper;
    }

}
