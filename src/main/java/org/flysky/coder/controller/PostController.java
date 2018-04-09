package org.flysky.coder.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.flysky.coder.controller.wrapper.PostWrapper;
import org.flysky.coder.controller.wrapper.ResultWrapper;
import org.flysky.coder.entity.Post;
import org.flysky.coder.service.impl.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostController {
    @Autowired
    private PostService postService;

    @RequestMapping("/forum/upvote/{postId}")
    public ResultWrapper upvote(@PathVariable int postId){
        Integer result= postService.upvotePost(postId);
        return new ResultWrapper(result);
    }

    @RequestMapping("/forum/downvote/{postId}")
    public ResultWrapper downvote(@PathVariable int postId){
        Integer result=postService.downvotePost(postId);
        return new ResultWrapper(result);
    }

    @RequestMapping("/forum/createPost")
    public ResultWrapper createPost(@RequestBody PostWrapper postWrapper){
        Integer uid=postWrapper.getUid();
        String title=postWrapper.getTitle();
        String content=postWrapper.getContent();
        Integer sectorId=postWrapper.getSectorId();
        List<String> tagNameList=postWrapper.getTagNameList();
        boolean isAnonymous=postWrapper.isAnonymous();
        String anonymousName=postWrapper.getAnonymousName();
        Integer type=postWrapper.getType();

        Integer result=postService.createPost(uid,title,content,sectorId,tagNameList,isAnonymous,anonymousName,type);
        return new ResultWrapper(result);
    }

    @RequestMapping("/forum/deletePost/{postId}")
    public ResultWrapper deletePost(@PathVariable int postId){
        Integer result=postService.deletePost(postId);
        return new ResultWrapper(result);
    }

    @RequestMapping("/forum/collectPost/{uid}/{postId}")
    public ResultWrapper collectPost(@PathVariable int uid,@PathVariable int postId){
        Integer result=postService.collectPost(postId,uid);
        return new ResultWrapper(result);
    }

    @RequestMapping("/forum/removeCollectedPost/{uid}/{postId}")
    public ResultWrapper removeCollectedPost(@PathVariable int uid,@PathVariable int postId){
        Integer result=postService.removeCollectedPost(postId,uid);
        return new ResultWrapper(result);
    }

    @RequestMapping("/forum/showPostBySectorAndType/{page}/{sectorId}/{type}")
    public PageInfo<Post> showPostBySectorAndType(@PathVariable int sectorId,@PathVariable int type,@PathVariable int page){
        PageInfo<Post> postList=postService.viewPostBySectorAndType(sectorId,type,page);
        return postList;
    }

    @RequestMapping("/forum/addStickyPost/{sectorId}/{postId}")
    public ResultWrapper addStickyPost(@PathVariable int sectorId,@PathVariable int postId){
       Integer result=postService.addStickyPost(postId,sectorId);
       return new ResultWrapper(result);
    }

    @RequestMapping("/forum/removeStickyPost/{sectorId}/{postId}")
    public ResultWrapper removeStickyPost(@PathVariable int sectorId,@PathVariable int postId){
        Integer result=postService.removeStickyPost(postId,sectorId);
        return new ResultWrapper(result);
    }

    @RequestMapping("/forum/recommendPost/{postId}")
    public ResultWrapper recommendPost(@PathVariable int postId){
        Integer result=postService.recommendPost(postId);
        return new ResultWrapper(result);
    }

    @RequestMapping("/forum/removeRecommendedPost/{postId}")
    public ResultWrapper removeRecommendedPost(@PathVariable int postId){
        Integer result=postService.removeRecommendedPost(postId);
        return new ResultWrapper(result);
    }

    @RequestMapping("/forum/searchPostByTitleAndContent/{page}/{title}/{content}")
    public PageInfo<Post> searchPostByTitleAndContentAndType(@PathVariable int postId,@PathVariable int page){
        PageInfo<Post> result=postService.searchPostByTitleAndContentAndType()
    }

}
