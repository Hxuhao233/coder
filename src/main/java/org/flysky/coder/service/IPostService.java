package org.flysky.coder.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.tomcat.jni.Local;
import org.flysky.coder.entity.Post;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface IPostService {
    public Integer createPost(Integer uid, String title, String content, Integer sectorId, List<String> tagNameList, boolean isAnonymous, String anonymousName, Integer type);

    public Integer upvotePost(Integer postId);

    public Integer downvotePost(Integer postId);

    public Integer collectPost(Integer postId, Integer uid);

    public Integer removeCollectedPost(Integer postId, Integer uid);

    public List<Post> showUserCollectionList(Integer uid);

    public Integer addStickyPost(Integer postId, Integer sectorId);

    public Integer removeStickyPost(Integer postId, Integer sectorId);

    public Integer recommendPost(Integer postId);

    public Integer removeRecommendedPost(Integer postId);

    public PageInfo<Post> searchPostByTitleAndContentAndType(String title, String content, Integer type, Integer page);

    public Integer deletePost(Integer postId);

    public Integer recoverPost(Integer postId);

    public PageInfo<Post> viewPostBySectorAndType(Integer sectorId, Integer type, Integer page);

    public PageInfo<Post> searchPostByUsername(String username, Integer type, Integer page);

    public Integer isPostCollected(Integer uid, Integer postId);

    public List<Integer> showStickyPostBySectorId(Integer sectorId);

    public List<Integer> showAllRecommendedPosts();

    public Post getPostByPostId(Integer postId);

    public List<Post> getPostByTitleAndTimeAndType(String title, LocalDateTime time1,LocalDateTime time2,Integer type);

}
