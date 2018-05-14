package org.flysky.coder.controller;


import com.github.pagehelper.PageInfo;
import org.flysky.coder.entity.Post;
import org.flysky.coder.service.IPostService;
import org.flysky.coder.service.IUserService;
import org.flysky.coder.token.RedisTokenService;
import org.flysky.coder.vo.PostWrapper;
import org.flysky.coder.vo.Result;
import org.flysky.coder.vo.ResultWrapper;
import org.flysky.coder.vo.user.AnonymousNameGenerator;
import org.flysky.coder.vo2.Request.Post.Anonymous.AnonymousPostWrapper;
import org.flysky.coder.vo2.Request.Post.Anonymous.SearchAnonymousPostWrapper;
import org.flysky.coder.vo2.Request.Post.Forum.ForumPostWrapper;
import org.flysky.coder.vo2.Response.SearchPostWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PostController {
    @Autowired
    private IPostService postService;

    @Autowired
    private RedisTokenService redisTokenService;

    @Autowired
    private IUserService userService;

    /*
    需求101.A 论坛分类浏览
     */
    @RequestMapping("/forum/showForumPostBySectorAndType/{page}/{sectorId}")
    public PageInfo<Post> showForumPostBySectorAndType(@PathVariable int sectorId, @PathVariable int page){
        PageInfo<Post> postList=postService.viewPostBySectorAndType(sectorId,0,page);
        return postList;
    }

    /*
    需求101.B 匿名区分类浏览
     */
    @RequestMapping("/forum/showAnonymousPostBySectorAndType/{page}/{sectorId}")
    public PageInfo<Post> showAnonymousPostBySectorAndType(@PathVariable int sectorId, @PathVariable int page){
        PageInfo<Post> postList=postService.viewPostBySectorAndType(sectorId,1,page);
        return postList;
    }

    /*
    需求103 用户在论坛发帖
     */
    @RequestMapping(value="/forum/createForumPost/{token}",method = RequestMethod.POST)
    public ResultWrapper createForumPost(@RequestBody ForumPostWrapper forumPostWrapper, @PathVariable String token){
        Integer uid=redisTokenService.getIdByToken(token);

        String title=forumPostWrapper.getTitle();
        String content=forumPostWrapper.getContent();
        Integer sectorId=forumPostWrapper.getSectorId();
        List<String> tagNameList=forumPostWrapper.getTagNameList();

        Integer result=postService.createPost(uid,title,content,sectorId,tagNameList,false,null,0);

        return new ResultWrapper(result);
    }

    /*
    需求301 用户在匿名区发帖
     */
    @RequestMapping(value="/anonymous/createAnonymousPost/{token}",method = RequestMethod.POST)
    public ResultWrapper createAnonymousPost(@RequestBody AnonymousPostWrapper postWrapper, @PathVariable String token){
        Integer uid=redisTokenService.getIdByToken(token);

        String title=postWrapper.getTitle();
        String content=postWrapper.getContent();
        Integer result=postService.createPost(uid,title,content,1000,null,true, AnonymousNameGenerator.autoGenerate(),1);
        return new ResultWrapper(result);
    }

    /*
    需求106 302 点赞论坛帖子 点赞匿名区帖子
     */
    @RequestMapping("/forum/upvote/{postId}")
    public ResultWrapper upvote(@PathVariable int postId){
        Integer result= postService.upvotePost(postId);
        return new ResultWrapper(result);
    }

    /*
    需求107 点踩论坛帖子
     */
    @RequestMapping("/forum/downvote/{postId}")
    public ResultWrapper downvote(@PathVariable int postId){
        Integer result=postService.downvotePost(postId);
        return new ResultWrapper(result);
    }

    /*
    需求108.A 收藏帖子
     */
    @RequestMapping("/forum/collectPost/{postId}/{token}")
    public ResultWrapper collectPost(@PathVariable int postId,@PathVariable String token){
        Integer uid=redisTokenService.getIdByToken(token);
        Integer result=postService.collectPost(postId,uid);
        return new ResultWrapper(result);
    }

    /*
    需求108.B 取消收藏帖子
     */
    @RequestMapping("/forum/removeCollectedPost/{postId}")
    public ResultWrapper removeCollectedPost(@PathVariable int postId,@PathVariable String token){
        Integer uid=redisTokenService.getIdByToken(token);
        Integer result=postService.removeCollectedPost(postId,uid);
        return new ResultWrapper(result);
    }

    /*
    需求108.C 查看帖子是否被收藏
     */
    @RequestMapping("/forum/isPostCollected/{postid}/{token}")
    public Result isPostCollected(@PathVariable Integer postid, HttpSession session, @PathVariable String token){
        Integer uid=redisTokenService.getIdByToken(token);
        Result result=new Result();
        result.setCode(postService.isPostCollected(uid,postid));
        return result;
    }

    /*
    需求109 用户在论坛关注其他用户
     */

    /*
    需求110 用户在论坛取消关注其他用户
     */

    /*
    需求111 删除帖子
     */
    @RequestMapping("/forum/deletePost/{postId}")
    public ResultWrapper deletePost(@PathVariable int postId){
        Integer result=postService.deletePost(postId);
        return new ResultWrapper(result);
    }

    /*
    需求113 论坛管理员推荐帖子
     */

    @RequestMapping("/forum/recommendPost/{postId}")
    public ResultWrapper recommendPost(@PathVariable int postId){
        Integer result=postService.recommendPost(postId);
        return new ResultWrapper(result);
    }

    /*
    需求114 论坛管理员取消推荐帖子
     */
    @RequestMapping("/forum/removeRecommendedPost/{postId}")
    public ResultWrapper removeRecommendedPost(@PathVariable int postId){
        Integer result=postService.removeRecommendedPost(postId);
        return new ResultWrapper(result);
    }

    /*
    需求113.5 获取推荐帖子列表
     */
    @RequestMapping("/forum/showAllRecommendedPosts")
    public ResultWrapper showAllRecommendedPosts(){
        ResultWrapper rw=new ResultWrapper();
        List<Integer> postIdList=postService.showAllRecommendedPosts();
        List<Post> postList=new ArrayList<>();
        for(Integer id:postIdList){
            postList.add(postService.getPostByPostId(id));
        }
        rw.setPayload(postList);
        return rw;
    }

    /*
    需求115 论坛管理员置顶某版块帖子
     */
    @RequestMapping("/forum/addStickyPost/{postId}")
    public ResultWrapper addStickyPost(@PathVariable int postId){
        Post p=postService.getPostByPostId(postId);
        Integer result=postService.addStickyPost(postId,p.getSectorId());
        return new ResultWrapper(result);
    }

    /*
    需求116 论坛管理员取消置顶某版块帖子
     */
    @RequestMapping("/forum/removeStickyPost/{postId}")
    public ResultWrapper removeStickyPost(@PathVariable int postId){
        Post p=postService.getPostByPostId(postId);
        Integer result=postService.removeStickyPost(postId,p.getSectorId());
        return new ResultWrapper(result);
    }

    /*
    需求115.5 论坛管理员获取某版块置顶帖子列表
     */
    @RequestMapping("/forum/showStickyPostBySectorId/{sectorId}")
    public ResultWrapper showStickyPostBySectorId(@PathVariable Integer sectorId){
        ResultWrapper rw=new ResultWrapper();
        List<Integer> postIdList=postService.showStickyPostBySectorId(sectorId);
        List<Post> postList=new ArrayList<>();
        for(Integer id:postIdList){
            postList.add(postService.getPostByPostId(id));
        }
        rw.setPayload(postList);
        return rw;
    }

    /*
    需求117 用户按照帖子名搜索论坛帖子
     */
    @RequestMapping("/forum/searchPostByTitle/{title}")
    public ResultWrapper searchPostByTitle(@PathVariable String title){
        ResultWrapper rw=new ResultWrapper();
        rw.setPayload(postService.searchPost(title,null,null,0));
        return rw;
    }

    /*
    需求117.A 用户按照帖子名搜索匿名区帖子
     */
    @RequestMapping("/forum/searchAnonymousPostByTitle")
    public ResultWrapper searchAnonymousPostByTitle(@RequestBody SearchAnonymousPostWrapper sapw){
        ResultWrapper rw=new ResultWrapper();
        rw.setPayload(postService.searchPost(sapw.getTitle(),null,null,1));
        return rw;
    }

    /*
    需求117.B 管理员按照发帖人 发贴内容 发贴标题等搜索帖子
     */
    @RequestMapping("/forum/searchPostByUsernameAndContentAndTitle")
    public ResultWrapper searchPostByUsernameAndContentAndTitle(@RequestBody SearchPostWrapper searchPostWrapper){
        ResultWrapper rw=new ResultWrapper();
        rw.setPayload(postService.searchPost(searchPostWrapper.getTitle(),searchPostWrapper.getContent(),searchPostWrapper.getUsername(),0));
        return rw;
    }

    /*
    需求117.C 管理员按照 发贴内容 发贴标题等搜索匿名区帖子
     */
    @RequestMapping("/forum/searchAnonymousPostByUsernameAndContentAndTitle")
    public ResultWrapper searchAnonymousPostByUsernameAndContentAndTitle(@RequestBody SearchPostWrapper searchPostWrapper){
        ResultWrapper rw=new ResultWrapper();
        rw.setPayload(postService.searchPost(searchPostWrapper.getTitle(),searchPostWrapper.getContent(),null,1));
        return rw;
    }

    /*
    需求B 根据帖子ID获取帖子信息
     */
    @RequestMapping("/post/getPostById/{postId}")
    public Post getPostById(@PathVariable Integer postId){
        return postService.getPostByPostId(postId);
    }

}
