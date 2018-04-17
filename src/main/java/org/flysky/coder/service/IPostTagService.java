package org.flysky.coder.service;

import org.flysky.coder.entity.PostTag;

import java.util.List;

public interface IPostTagService {
    List<PostTag> getPostTagByPostId(int postId);
}
