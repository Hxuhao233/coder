package org.flysky.coder;

import com.github.pagehelper.PageInfo;
import org.flysky.coder.entity.Post;
import org.flysky.coder.service.impl.PostService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;


public class PostServiceTest {
    /*@Autowired
    private PostService postService;

    //@Test
    public void createTest(){
        int uid=1;
        String title="nnn";
        String content="ccc";
        Integer sectorId=1;
        List<String> tagNameList=new ArrayList<>();
        tagNameList.add("ddd");
        tagNameList.add("eee");
        boolean isAnonymous=false;
        String anonymousName=null;
        Integer type=0;
        postService.createPost(uid,title,content,sectorId,tagNameList,isAnonymous,anonymousName,type);
    }

    //@Test
    public void upvote(){
        postService.upvotePost(4);
    }

    //@Test
    public void downvote(){
        postService.downvotePost(4);
    }

    //@Test
    public void deletePost(){
        postService.deletePost(4);
    }

    //@Test
    public void recoverPost(){
        postService.recoverPost(4);
    }

    @Test
    public void collectPost(){
        postService.collectPost(7,1);
        List<Post> postList=postService.showUserCollectionList(1);
        System.out.print(postList.toString());
    }

    @Test
    public void removeCollectedPost(){
        postService.removeCollectedPost(4,1);
        List<Post> postList=postService.showUserCollectionList(1);
    }

    @Test
    public void recommendPost(){
       // postService.recommendPost(4);
//        postService.recommendPost(5);
//        postService.recommendPost(6);
//        postService.recommendPost(7);
//        postService.recommendPost(8);
        postService.recommendPost(9);
    }

    @Test
    public void removeRecommendedPost(){
        postService.removeRecommendedPost(5);
    }

    @Test
    public void addStickyPost(){
        postService.addStickyPost(4,1);
    }

    @Test
    public void removeStickyPost(){
        postService.removeStickyPost(4,1);
    }

    @Test
    public void searchPostByTitleAndContentAndType() {
        PageInfo<Post> plist=postService.searchPostByTitleAndContentAndType("1",null,1,1);
        plist.getSize();
    }

    @Test
    public void searchPostByUsername(){
        PageInfo<Post> plist=postService.searchPostByUsername("hxhh",0,1);
        plist.getSize();
    }

    @Test
    public void isPostCollected(){
        Integer xx=postService.isPostCollected(1,5);
        xx.getClass();
    }*/


}
