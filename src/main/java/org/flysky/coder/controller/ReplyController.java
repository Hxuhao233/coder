package org.flysky.coder.controller;



import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.flysky.coder.entity.Reply;
import org.flysky.coder.entity.User;
import org.flysky.coder.service.INotificationService;
import org.flysky.coder.service.IPostService;
import org.flysky.coder.service.IReplyService;
import org.flysky.coder.service.IUserService;
import org.flysky.coder.vo.Result;
import org.flysky.coder.vo.ResultWrapper;

import org.flysky.coder.vo.user.AnonymousNameGenerator;
import org.flysky.coder.vo2.Request.Reply.Anonymous.AnonymousReplyWrapper;
import org.flysky.coder.vo2.Request.Reply.Anonymous.SearchAnonymousReplyWrapper;

import org.flysky.coder.vo2.Request.Reply.Forum.InnerReplyWrapper;
import org.flysky.coder.vo2.Request.Reply.Forum.PostReplyWrapper;
import org.flysky.coder.vo2.Request.Reply.Forum.SearchReplyWrapper;
import org.flysky.coder.vo2.Response.ReplyWrapper;
import org.flysky.coder.vo2.Response.SearchReplyResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ReplyController {
    @Autowired
    private IReplyService replyService;

    @Autowired
    private IPostService postService;


    @Autowired
    private INotificationService notificationService;

    @Autowired
    private IUserService userService;

    /*
    需求104 用户在论坛回复帖子
     */
    @RequestMapping("/reply/replyToForumPost")
    public Result replyToForumPost(@RequestBody PostReplyWrapper replyWrapper,HttpSession session){
        User u=(User)session.getAttribute("user");
        Integer uid=u.getId();
        //Integer uid=2;
        Integer resultI=replyService.replyToPost(replyWrapper.getPostId(),uid,replyWrapper.getContent(),false,null,0);
        Result result=new Result();
        result.setCode(resultI);
        result.setInfo(null);
        return result;
    }

    /*
    需求303 用户在匿名区回复帖子
     */
    @RequestMapping("/reply/replyToAnonymousPost")
    public Result replyToAnonymousPost(@RequestBody AnonymousReplyWrapper anonymousReplyWrapper,HttpSession session){
        User u=(User)session.getAttribute("user");
       Integer uid=u.getId();
        //Integer uid=17;
        boolean isAnonymous=anonymousReplyWrapper.getIsAnonymous()==1?true:false;

        Integer resultI=null;
        if(isAnonymous) {
            resultI=replyService.replyToPost(anonymousReplyWrapper.getPostId(), uid, anonymousReplyWrapper.getContent(), true, AnonymousNameGenerator.autoGenerate(),1);
        }else{
            resultI=replyService.replyToPost(anonymousReplyWrapper.getPostId(), uid, anonymousReplyWrapper.getContent(), false, userService.getUserNameById(uid),1);
        }

        Result result=new Result();
        result.setCode(resultI);
        result.setInfo(null);
        return result;
    }

    /*
    需求105 用户在论坛中回复其他用户的回复
     */
    @RequestMapping("/reply/innerReplyToPost")
    public Result innerReplyToPost(@RequestBody InnerReplyWrapper innerReplyWrapper,HttpSession session){
        User u=(User)session.getAttribute("user");
        //Integer uid=u.getId();
        Integer uid=17;
        Integer resultI=replyService.createInnerReply(innerReplyWrapper.getPostId(), uid, innerReplyWrapper.getContent(), innerReplyWrapper.getFloorCnt()
                , false, null,0);
        Result result=new Result();
        result.setCode(resultI);
        result.setInfo(null);
        return result;
    }

    /*
    需求111 305 管理员删除回复
     */
    @RequiresRoles("manager")
    @RequestMapping("/reply/deleteReply/{replyId}")
    public Result deleteReply(@PathVariable Integer replyId){
        Result result=new Result();
        if(replyId==null){
            result.setCode(0);
            result.setInfo(null);
            return result;
        }
        Reply reply=replyService.getReplyById(replyId);
        Integer resultI=replyService.deleteReply(reply.getPostid(),replyId);
        result.setCode(resultI);
        result.setInfo(null);
        return result;
    }

    /*
    需求A 搜索论坛帖子回复
     */
    @RequiresRoles("manager")
    @RequestMapping("/reply/searchForumPostReply")
    public ResultWrapper searchForumPostReply(@RequestBody SearchReplyWrapper searchReplyWrapper,
                                              @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        ResultWrapper resultWrapper=new ResultWrapper();
        PageInfo replyPageInfo=replyService.searchForumPostReply(searchReplyWrapper.getContent(),searchReplyWrapper.getUsername(),searchReplyWrapper.getTitle(),pageNum,pageSize);
        List<Reply> replyList=replyPageInfo.getList();
        List<SearchReplyResult> searchReplyResultList=new ArrayList<>();
        for(Reply r:replyList){
            SearchReplyResult searchReplyResult=new SearchReplyResult();
            searchReplyResult.setContent(r.getContent());
            searchReplyResult.setTime(r.getCreatedAt());
            searchReplyResult.setTitle(postService.getPostByPostId(r.getPostid()).getTitle());
            searchReplyResult.setUsername(userService.getUserById(r.getUserId()).getUsername());
            searchReplyResult.setId(r.getId());
            searchReplyResultList.add(searchReplyResult);
        }
        replyPageInfo.setList(searchReplyResultList);
        resultWrapper.setPayload(replyPageInfo);
        return resultWrapper;
    }

    /*
    需求B 搜索匿名区帖子回复
     */
    @RequiresRoles("manager")
    @RequestMapping("/reply/searchAnonymousPostReply")
    public ResultWrapper searchAnonymousPostReply(@RequestBody SearchAnonymousReplyWrapper searchAnonymousReplyWrapper,
                                                  @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        ResultWrapper resultWrapper=new ResultWrapper();
        PageInfo replyPageInfo=replyService.searchAnonymousPostReply(searchAnonymousReplyWrapper.getContent(),searchAnonymousReplyWrapper.getTitle(),pageNum,pageSize);
        List<Reply> replyList=replyPageInfo.getList();
        List<SearchReplyResult> searchReplyResultList=new ArrayList<>();
        for(Reply r:replyList){
            SearchReplyResult searchReplyResult=new SearchReplyResult();
            searchReplyResult.setContent(r.getContent());
            searchReplyResult.setTime(r.getCreatedAt());
            searchReplyResult.setTitle(postService.getPostByPostId(r.getPostid()).getTitle());
            searchReplyResult.setUsername("x");
            searchReplyResult.setId(r.getId());
            searchReplyResultList.add(searchReplyResult);
        }
        replyPageInfo.setList(searchReplyResultList);
        resultWrapper.setPayload(replyPageInfo);
        return resultWrapper;
    }


    /*
    需求C 根据postId获取所有回复
     */
    @RequestMapping("/reply/getRepliesByPostId/{postId}")
    public List<ReplyWrapper> getRepliesByPostId(@PathVariable Integer postId){
        return replyService.getRepliesByPostId(postId);
    }

    /*
    需求D 根据ReplyId获取回复
     */
    @RequestMapping("/reply/getReplyById/{id}")
    public Reply getReplyById(@PathVariable Integer id){
        return replyService.getReplyById(id);
    }


}
