package org.flysky.coder.service;

import java.time.LocalDateTime;

public interface IPostService {
    public Integer createPost(Integer uid,String title,String content,Integer sectorId,Integer tagId,boolean isAnonymous,String anonymousName,Integer type);
}
