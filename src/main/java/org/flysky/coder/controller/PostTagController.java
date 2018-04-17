package org.flysky.coder.controller;

import org.flysky.coder.entity.PostTag;
import org.flysky.coder.service.IPostTagService;
import org.flysky.coder.vo.ResultWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PostTagController {
    @Autowired
    private IPostTagService postTagService;

    @RequestMapping("/postTag/getPostTagsByPostId/{postId}")
    public ResultWrapper getPostTagsByPostId(@PathVariable Integer postId){
        ResultWrapper resultWrapper=new ResultWrapper();
        if(postId==null){
            resultWrapper.setPayload(null);
            resultWrapper.setCode(0);

        }
        else{
            resultWrapper.setCode(1);
            resultWrapper.setPayload(postTagService.getPostTagByPostId(postId));
        }

        return resultWrapper;
    }
}
