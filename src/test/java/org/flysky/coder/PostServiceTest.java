package org.flysky.coder;

import org.flysky.coder.service.impl.PostService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostServiceTest {
    @Autowired
    private PostService postService;

    @Test
    public void createTest(){
        int uid=1;
        String title="nimabi";
        String content="cnmgb";
        Integer sectorId=1;
        List<String> tagNameList=new ArrayList<>();
        tagNameList.add("约炮");
        tagNameList.add("草比");
        boolean isAnonymous=false;
        String anonymousName=null;
        Integer type=0;
        postService.createPost(uid,title,content,sectorId,tagNameList,isAnonymous,anonymousName,type);
    }

    @Test
    public void upvote(){
        postService.upvotePost(4);
    }

    @Test
    public void downvote(){
        postService.downvotePost(4);
    }

    @Test
    public void deletePost(){
        postService.deletePost(4);
    }

    @Test
    public void recoverPost(){
        postService.recoverPost(4);
    }
}
