package org.flysky.coder.vo2;

import com.github.pagehelper.PageInfo;

import java.util.List;

public class ViewPostWrapper {
    private PageInfo postPageInfo;
    private List<PostResultWrapper> stickyList;

    public ViewPostWrapper() {
    }

    public PageInfo getPostPageInfo() {
        return postPageInfo;
    }

    public void setPostPageInfo(PageInfo postPageInfo) {
        this.postPageInfo = postPageInfo;
    }

    public List<PostResultWrapper> getStickyList() {
        return stickyList;
    }

    public void setStickyList(List<PostResultWrapper> stickyList) {
        this.stickyList = stickyList;
    }
}
