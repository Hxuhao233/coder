package org.flysky.coder.controller.deprecated;

import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.flysky.coder.entity.Post;
import org.flysky.coder.entity.User;
import org.flysky.coder.service.IPostService;
import org.flysky.coder.service.IUserService;
import org.flysky.coder.token.RedisTokenService;
import org.flysky.coder.vo.PostSearchWrapper;
import org.flysky.coder.vo.PostWrapper;
import org.flysky.coder.vo.Result;
import org.flysky.coder.vo.ResultWrapper;
import org.flysky.coder.vo.user.AnonymousNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.List;

//@RestController
public class PostControler {
    /*@Autowired
    private IPostService postService;

    @Autowired
    private RedisTokenService redisTokenService;



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

    //@RequiresRoles("user")
    @RequestMapping(value="/forum/createPost/{token}",method = RequestMethod.POST)
    public ResultWrapper createPost(@RequestBody PostWrapper postWrapper,@PathVariable String token){
        Integer uid=redisTokenService.getIdByToken(token);
        String title=postWrapper.getTitle();
        String content=postWrapper.getContent();
        Integer sectorId=postWrapper.getSectorId();
        List<String> tagNameList=postWrapper.getTagNameList();
        Integer isAnonymousInt=postWrapper.getIsAnonymous();
        String anonymousName=postWrapper.getAnonymousName();
        Integer type=postWrapper.getType();
        boolean isAnonymous;
        Integer result=null;
        if(isAnonymousInt==1){
            isAnonymous=true;
            result=postService.createPost(uid,title,content,sectorId,tagNameList,isAnonymous, AnonymousNameGenerator.autoGenerate(),type);
        }else{
            isAnonymous=false;
            result=postService.createPost(uid,title,content,sectorId,tagNameList,isAnonymous,anonymousName,type);
        }
        return new ResultWrapper(result);
    }

    //@RequiresRoles("manager")
    @RequestMapping("/forum/deletePost/{postId}")
    public ResultWrapper deletePost(@PathVariable int postId){
        Integer result=postService.deletePost(postId);
        return new ResultWrapper(result);
    }

    //@RequiresRoles("user")
    @RequestMapping("/forum/collectPost/{postId}/{token}")
    public ResultWrapper collectPost(@PathVariable int postId,@PathVariable String token){
        Integer uid=redisTokenService.getIdByToken(token);
        Integer result=postService.collectPost(postId,uid);
        return new ResultWrapper(result);
    }

    //@RequiresRoles("user")
    @RequestMapping("/forum/removeCollectedPost/{postId}")
    public ResultWrapper removeCollectedPost(@PathVariable int postId,@PathVariable String token){
        Integer uid=redisTokenService.getIdByToken(token);
        Integer result=postService.removeCollectedPost(postId,uid);
        return new ResultWrapper(result);
    }

   // @RequiresRoles("user")
    @RequestMapping("/forum/showPostBySectorAndType/{page}/{sectorId}/{type}")
    public PageInfo<Post> showPostBySectorAndType(@PathVariable int sectorId, @PathVariable int type, @PathVariable int page){
        PageInfo<Post> postList=postService.viewPostBySectorAndType(sectorId,type,page);
        return postList;
    }

   // @RequiresRoles("manager")
    @RequestMapping("/forum/addStickyPost/{sectorId}/{postId}")
    public ResultWrapper addStickyPost(@PathVariable int sectorId,@PathVariable int postId){
        Integer result=postService.addStickyPost(postId,sectorId);
        return new ResultWrapper(result);
    }

   // @RequiresRoles("manager")
    @RequestMapping("/forum/removeStickyPost/{sectorId}/{postId}")
    public ResultWrapper removeStickyPost(@PathVariable int sectorId,@PathVariable int postId){
        Integer result=postService.removeStickyPost(postId,sectorId);
        return new ResultWrapper(result);
    }

    //@RequiresRoles("manager")
    @RequestMapping("/forum/recommendPost/{postId}")
    public ResultWrapper recommendPost(@PathVariable int postId){
        Integer result=postService.recommendPost(postId);
        return new ResultWrapper(result);
    }

    //@RequiresRoles("manager")
    @RequestMapping("/forum/removeRecommendedPost/{postId}")
    public ResultWrapper removeRecommendedPost(@PathVariable int postId){
        Integer result=postService.removeRecommendedPost(postId);
        return new ResultWrapper(result);
    }

    @RequestMapping("/forum/searchPostByTitleAndContent/{type}/{page}/{title}/{content}")
    public PageInfo<Post> searchPostByTitleAndContentAndType(@PathVariable String title,@PathVariable String content,@PathVariable int page,@PathVariable int type){
        PageInfo<Post> result=postService.searchPostByTitleAndContentAndType(title,content,type,page);
        return result;
    }

    @RequestMapping("/forum/searchPostByUserAndType/{type}/{page}/{username}")
    public PageInfo<Post> searchPostByUserAndType(@PathVariable String username,@PathVariable Integer type,@PathVariable Integer page){
        PageInfo<Post> result=postService.searchPostByUsername(username, type, page);
        return result;
    }

    //@RequiresRoles("user")
    @RequestMapping("/usercenter/getCollectedPost/{token}")
    public List<Post> getCollectedPost(HttpSession session,@PathVariable String token){
        Integer uid=redisTokenService.getIdByToken(token);
        List<Post> result=postService.showUserCollectionList(uid);
        return result;
    }

    @RequestMapping("/forum/isCollectedPost/{postid}/{token}")
    public Result isPostCollected(@PathVariable Integer postid,HttpSession session,@PathVariable String token){
        Integer uid=redisTokenService.getIdByToken(token);
        Result result=new Result();
        result.setCode(postService.isPostCollected(uid,postid));
        return result;
    }

    @RequestMapping("/forum/showStickyPostBySectorId/{sectorId}")
    public ResultWrapper showStickyPostBySectorId(@PathVariable Integer sectorId){
        ResultWrapper rw=new ResultWrapper();
        rw.setPayload(postService.showStickyPostBySectorId(sectorId));
        return rw;
    }

    @RequestMapping("/forum/showAllRecommendedPosts")
    public ResultWrapper showAllRecommendedPosts(){
        ResultWrapper rw=new ResultWrapper();
        rw.setPayload(postService.showAllRecommendedPosts());
        return rw;
    }

    @RequestMapping("/forum/searchByTitleAndTimeAndType")
    public ResultWrapper searchByTitleAndTimeAndType(@RequestBody PostSearchWrapper postSearchWrapper){
        List<Post> postList=postService.getPostByTitleAndTimeAndType(postSearchWrapper.getTitle(),postSearchWrapper.getTime1(),postSearchWrapper.getTime2(),postSearchWrapper.getType());
        ResultWrapper rw=new ResultWrapper();
        rw.setPayload(postList);
        return rw;
    }

    @RequestMapping("/post/getPostById/{postId}")
    public Post getPostById(@PathVariable Integer postId){
        return postService.getPostByPostId(postId);
    }

    @RequestMapping("/homexxxx/1")
    @ResponseBody
    public String home() {
        return "Hello sb";
    }
*/
}