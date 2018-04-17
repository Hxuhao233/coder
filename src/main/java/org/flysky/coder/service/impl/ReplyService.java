package org.flysky.coder.service.impl;

import org.flysky.coder.entity.Post;
import org.flysky.coder.entity.Reply;
import org.flysky.coder.entity.User;
import org.flysky.coder.mapper.PostMapper;
import org.flysky.coder.mapper.ReplyMapper;
import org.flysky.coder.mapper.UserMapper;
import org.flysky.coder.service.IReplyService;
import org.flysky.coder.vo.ReplyWrapper;
import org.flysky.coder.vo.ResultWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ReplyService implements IReplyService {
    @Autowired
    private ReplyMapper replyMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Integer replyToPost(Integer postId, Integer uid, String content,boolean isAnonymous,String anonymousName) {
        Reply reply=new Reply();
        Post post=postMapper.selectByPrimaryKey(postId);
        User u=userMapper.selectByPrimaryKey(uid);

        if(post==null||u==null){
            return 0;
        }
        int postType=post.getType();
        if(isAnonymous){
            reply.setAnonymous(1);
            reply.setAnonymousName(anonymousName);
            reply.setType(1);
        }else{
            reply.setAnonymous(0);
            reply.setAnonymousName(u.getUsername());
            reply.setType(0);
        }

        reply.setContent(content);
        reply.setCreatedAt(LocalDateTime.now());
        reply.setFloorCnt(post.getFloorCnt()+1);
        reply.setInnerReplyFloor(0);
        post.setFloorCnt(post.getFloorCnt()+1);
        replyMapper.insert(reply);
        postMapper.updateByPrimaryKey(post);

        return null;
    }

    @Override
    public Integer createInnerReply(Integer postId, Integer uid, String content, Integer floorCount,boolean isAnonymous,String anonymousName) {
        Reply reply=new Reply();
        Post post=postMapper.selectByPrimaryKey(postId);
        User u=userMapper.selectByPrimaryKey(uid);

        if(post==null||u==null){
            return 0;
        }
        int postType=post.getType();
        if(isAnonymous){
            reply.setAnonymous(1);
            reply.setAnonymousName(anonymousName);
            reply.setType(1);
        }else{
            reply.setAnonymous(0);
            reply.setAnonymousName(u.getUsername());
            reply.setType(0);
        }

        reply.setContent(content);
        reply.setCreatedAt(LocalDateTime.now());
        reply.setFloorCnt(post.getFloorCnt());
        reply.setInnerReplyFloor(replyMapper.getInnerReplyCountByPostIdAndFloor(postId,floorCount)+1);
        return null;
    }

    @Override
    public Integer deleteReply(Integer postId,Integer replyId) {
        Reply reply=replyMapper.selectByPrimaryKey(replyId);
        if(reply==null){
            return 0;
        }
        Integer floor=reply.getFloorCnt();
        Integer innerFloor=reply.getInnerReplyFloor();
        Integer innerFloorCnt=replyMapper.getInnerReplyCountByPostIdAndFloor(postId,floor);
        /*分几种情况
          1.innerFloor=0,innerFloorCnt=0  可以直接删
          2.innerFloor=0,innerFloorCnt>0  暴力删楼，楼中楼也全删
          3.innerFloor>0                  直接删
         */
        if(innerFloor==0){
            if(innerFloorCnt==0) {
                replyMapper.deleteByPrimaryKey(replyId);
            }else{
                List<Reply> replyList=replyMapper.getAllReplyByPostIdAndFloor(postId,floor);
                for(Reply r:replyList){
                    replyMapper.deleteByPrimaryKey(r.getId());
                }
            }
        }else{
            replyMapper.deleteByPrimaryKey(replyId);
        }


        return 1;
    }

    @Override
    public List<ReplyWrapper> getRepliesByPostId(int postId){
        List<Reply> replyList=replyMapper.getRepliesByPostId(postId);
        List<Integer> floorList=new ArrayList<Integer>();
        for(Reply r:replyList){
            int floor=r.getFloorCnt();
            if(!floorList.contains(floor)){
                floorList.add(floor);
            }
        }

        Collections.sort(floorList);

        List<ReplyWrapper> replyWrapperList=new ArrayList<ReplyWrapper>();
        List<Reply> removeList=new ArrayList<Reply>();

        for(Integer floor:floorList){
            for(Reply r:replyList){
                if(r.getFloorCnt()==floor&&r.getInnerReplyFloor()==0){
                    ReplyWrapper replyWrapper=new ReplyWrapper();
                    replyWrapper.setContent(r.getContent());
                    replyWrapper.setReplyId(r.getId());
                    replyWrapper.setTime(r.getCreatedAt());
                    replyWrapper.setInnerReplyList(null);
                    replyWrapper.setFloor(floor);
                    replyWrapperList.add(replyWrapper);
                    removeList.add(r);
                }
            }
        }

        replyList.removeAll(removeList);

        for(ReplyWrapper rw:replyWrapperList){
            int floor=rw.getFloor();
            List<ReplyWrapper> innerReplyWrapperList=new ArrayList<ReplyWrapper>();
            for(Reply r:replyList){
                if(floor==r.getFloorCnt()){
                    ReplyWrapper replyWrapper=new ReplyWrapper();
                    replyWrapper.setContent(r.getContent());
                    replyWrapper.setReplyId(r.getId());
                    replyWrapper.setTime(r.getCreatedAt());
                    replyWrapper.setInnerReplyList(null);
                    replyWrapper.setFloor(floor);
                    innerReplyWrapperList.add(replyWrapper);
                }
            }
            rw.setInnerReplyList(innerReplyWrapperList);
        }

        return replyWrapperList;
    }

}
