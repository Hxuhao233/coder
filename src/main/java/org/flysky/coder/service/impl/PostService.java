package org.flysky.coder.service.impl;


import org.flysky.coder.entity.Post;
import org.flysky.coder.entity.User;
import org.flysky.coder.mapper.PostMapper;
import org.flysky.coder.mapper.UserMapper;
import org.flysky.coder.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostService implements IPostService {
    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Integer createPost(Integer uid, String title, String content, Integer sectorId, Integer tagId, boolean isAnonymous, String anonymousName, Integer type) {
        Post post = new Post();
        post.setUserId(uid);
        post.setTitle(title);
        post.setContent(content);
        post.setSectorId(sectorId);
        post.setCreatedAt(LocalDateTime.now());
        if (type == 1) {  //如果是匿名帖
            post.setType(1);
            if (isAnonymous) { //如果用户选择在匿名帖中匿名
                post.setAnonymous(1);
                post.setAnonymousName(anonymousName);
            } else {           //如果用户选择在匿名帖中使用真实名字
                post.setAnonymous(0);
                User user=userMapper.selectByPrimaryKey(uid);
                if(user!=null){
                    String username=user.getUsername();
                    if(username!=null){
                        post.setAnonymousName(username);
                    }
                    else{
                        return 0;
                    }
                }else{
                    return 0;
                }
                post.setAnonymousName(null);
            }

        } else { //如果不是匿名帖
            post.setType(0);
            post.setAnonymous(0);
            post.setAnonymousName(null);
        }
        postMapper.insert(post);

        return 1;
    }

    public Integer deletePost(Post post) {
        Integer isDeleted=post.getIsDeleted();
        if(isDeleted==0) {
            post.setIsDeleted(1);
            postMapper.updateByPrimaryKey(post);
        }
        else{
            return 0;
        }
        return 1;
    }

    public Integer recoverPost(Post post){
        Integer isDeleted=post.getIsDeleted();
        if(isDeleted==1) {
            post.setIsDeleted(0);
            postMapper.updateByPrimaryKey(post);
        }
        else{
            return 0;
        }
        return 1;
    }

}
