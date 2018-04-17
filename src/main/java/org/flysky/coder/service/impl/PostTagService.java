package org.flysky.coder.service.impl;

import org.flysky.coder.entity.PostTag;
import org.flysky.coder.mapper.PostTagMapper;
import org.flysky.coder.service.IPostTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostTagService implements IPostTagService{
    @Autowired
    private PostTagMapper postTagMapper;

    @Override
    public List<PostTag> getPostTagByPostId(int postId) {
        return postTagMapper.getPostTagByPostId(postId);
    }
}
