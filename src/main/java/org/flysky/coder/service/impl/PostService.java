package org.flysky.coder.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.flysky.coder.entity.Post;
import org.flysky.coder.entity.PostTag;
import org.flysky.coder.entity.Tag;
import org.flysky.coder.entity.User;
import org.flysky.coder.mapper.PostMapper;
import org.flysky.coder.mapper.PostTagMapper;
import org.flysky.coder.mapper.TagMapper;
import org.flysky.coder.mapper.UserMapper;
import org.flysky.coder.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService implements IPostService {
    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PostTagMapper postTagMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Integer createPost(Integer uid, String title, String content, Integer sectorId, List<String> tagNameList, boolean isAnonymous, String anonymousName, Integer type) {
        Post post = new Post();
        post.setUserId(uid);
        post.setTitle(title);
        post.setContent(content);
        post.setSectorId(sectorId);
        LocalDateTime now=LocalDateTime.now();
        post.setUpdatedAt(now);
        post.setCreatedAt(now);
        post.setUpvote(0);
        post.setDownvote(0);
        post.setIsDeleted(0);
        if (type == 1) {  //如果是匿名帖
            post.setType(1);
            if (isAnonymous) { //如果用户选择在匿名帖中匿名
                post.setAnonymous(1);
                post.setAnonymousName(anonymousName);
            } else {           //如果用户选择在匿名帖中使用真实名字
                post.setAnonymous(0);
                User user = userMapper.selectByPrimaryKey(uid);
                if (user != null) {
                    String username = user.getUsername();
                    if (username != null) {
                        post.setAnonymousName(username);
                    } else {
                        return 0;
                    }
                } else {
                    return 0;
                }
                post.setAnonymousName(null);
            }

        } else { //如果不是匿名帖
            post.setType(0);
            post.setAnonymous(0);
            post.setAnonymousName(null);
        }

        post.setFloorCnt(1);

        postMapper.insert(post);

        List<String> newTagNames=new ArrayList<String>();

        //添加以前没有的Tag
        for(String s:tagNameList){
            Tag tag=tagMapper.getTagByTagNameAndType(s,type);
            if(tag==null){
                newTagNames.add(s);
            }
        }

        for(String s:newTagNames){
            Tag tag=new Tag();
            tag.setName(s);
            tag.setType(type);
            tagMapper.insert(tag);
        }

        //获取添加后所有Tag的ID，并与对应的Post绑定
        for(String s:tagNameList){
            Tag tag=tagMapper.getTagByTagNameAndType(s,type);
            if(tag!=null){
                int tagId=tag.getId();
                PostTag postTag=new PostTag();
                postTag.setPostId(post.getId());
                postTag.setTagId(tagId);
                postTagMapper.insert(postTag);
            }
        }

        return 1;
    }

    @Override
    public Integer upvotePost(Integer postId) {
        Post post=postMapper.selectByPrimaryKey(postId);
        Integer upvote=null;
        if(post!=null){
            upvote=post.getUpvote();
            upvote++;
            post.setUpvote(upvote);
            postMapper.updateByPrimaryKey(post);
            return 1;
        }
        return 0;
    }

    @Override
    public Integer downvotePost(Integer postId) {
        Post post=postMapper.selectByPrimaryKey(postId);
        Integer downvote=null;
        if(post!=null){
            downvote=post.getDownvote();
            downvote++;
            post.setDownvote(downvote);
            postMapper.updateByPrimaryKey(post);
            return 1;
        }
        return 0;
    }

    @Override
    public Integer collectPost(Integer postId, Integer uid) {
        Post post=postMapper.selectByPrimaryKey(postId);
        User user=userMapper.selectByPrimaryKey(uid);
        if(post==null||user==null){
            return 0;
        }
        redisTemplate.opsForList().leftPush(String.valueOf(uid)+"Collection",String.valueOf(postId));
        return 1;
    }

    @Override
    public Integer removeCollectedPost(Integer postId, Integer uid) {
        User user=userMapper.selectByPrimaryKey(uid);
        if(user==null){
            return 0;
        }
        redisTemplate.opsForList().remove(String.valueOf(uid)+"Collection",0,String.valueOf(postId));
        return 1;
    }

    public List<Post> showUserCollectionList(Integer uid){
        User user=userMapper.selectByPrimaryKey(uid);
        if(user==null){
            return null;
        }
        List<String> userCollectionPostIdList=redisTemplate.opsForList().range(String.valueOf(uid)+"Collection",0,-1);
        List<Post> userCollectionList=new ArrayList<Post>();
        for(String s:userCollectionPostIdList){
            Integer postId=Integer.parseInt(s);
            Post post=postMapper.selectByPrimaryKey(postId);
            if(post!=null){
                userCollectionList.add(post);
            }
        }

        return userCollectionList;
    }

    @Override
    public Integer addStickyPost(Integer postId,Integer sectorId) {
        String redisKeyName="StickyPost"+String.valueOf(sectorId);
        Long size=redisTemplate.opsForList().size(redisKeyName);
        if(size>=5){
            return 0;
        }else if(size>0){
            List<String> stickyPostList=redisTemplate.opsForList().range(redisKeyName,0,-1);
            if(stickyPostList.contains(String.valueOf(postId))){//已添加进置顶列表
                return 2;
            }
        }
        redisTemplate.opsForList().leftPush(redisKeyName,String.valueOf(postId));
        return 1;
    }

    @Override
    public Integer removeStickyPost(Integer postId,Integer sectorId) {
        String redisKeyName="StickyPost"+String.valueOf(sectorId);
        redisTemplate.opsForList().remove(redisKeyName,0,String.valueOf(postId));
        return 1;
    }


    @Override
    public Integer recommendPost(Integer postId) {
        Long size=redisTemplate.opsForList().size("RecommendedPost");
        if(size>=5){
            return 0;
        }else if(size>0){
            List<String> recommendedPostList=redisTemplate.opsForList().range("RecommendedPost",0,-1);
            if(recommendedPostList.contains(String.valueOf(postId))){//已添加进推荐列表
                return 2;
            }
        }
        redisTemplate.opsForList().leftPush("RecommendedPost",String.valueOf(postId));
        return 1;
    }

    @Override
    public Integer removeRecommendedPost(Integer postId) {
        redisTemplate.opsForList().remove("RecommendedPost",0,String.valueOf(postId));
        return null;
    }

    @Override
    public PageInfo<Post> searchPostByTitleAndContentAndType(String title, String content,Integer type,Integer page) {
        PageHelper.startPage(page,20);
        List<Post> postList=postMapper.searchPostByTitleAndContent(title,content,type);
        return new PageInfo<Post>(postList);
    }

    @Override
    public Integer deletePost(Integer postId) {
        Post post=postMapper.selectByPrimaryKey(postId);
        Integer isDeleted = post.getIsDeleted();
        if (isDeleted == 0) {
            post.setIsDeleted(1);
            postMapper.updateByPrimaryKey(post);
        } else {
            return 0;
        }
        return 1;
    }

    @Override
    public Integer recoverPost(Integer postId) {
        Post post=postMapper.selectByPrimaryKey(postId);
        Integer isDeleted = post.getIsDeleted();
        if (isDeleted == 1) {
            post.setIsDeleted(0);
            postMapper.updateByPrimaryKey(post);
        } else {
            return 0;
        }
        return 1;
    }

    @Override
    public PageInfo<Post> viewPostBySectorAndType(Integer sectorId,Integer type,Integer page) {
        PageHelper.startPage(page,20);
        List<Post> postList=postMapper.selectBySectorAndType(sectorId,type);
        return new PageInfo<Post>(postList);
    }

    @Override
    public PageInfo<Post> searchPostByUsername(String username,Integer type,Integer page){
        PageHelper.startPage(page,20);
        List<Post> postList=postMapper.searchPostByUsername(username,type);
        return new PageInfo<Post>(postList);
    }
}
