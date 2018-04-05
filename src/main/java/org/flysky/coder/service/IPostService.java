package org.flysky.coder.service;

import org.flysky.coder.entity.Post;

import java.time.LocalDateTime;
import java.util.List;

public interface IPostService {
    Integer createPost(Integer uid, String title, String content, Integer sectorId, Integer tagId, boolean isAnonymous, String anonymousName, Integer type);

    Integer upvotePost(Integer postId);

    Integer downvotePost(Integer postId);

    Integer collectPost(Integer postId, Integer uid);

    Integer removeCollectedPost(Integer postId, Integer uid);

    Integer addStickyPost(Integer postId);

    Integer removeStickyPost(Integer postId);

    Integer recommendPost(Integer postId);

    Integer removeRecommendedPost(Integer postId);

    List<Post> searchPostByTitleAndContentAndUid(String title, String content, Integer uid);

    Integer deletePost(Post post);

    Integer recoverPost(Post post);

    List<Post> viewPostBySector(Integer sectorId);
}
