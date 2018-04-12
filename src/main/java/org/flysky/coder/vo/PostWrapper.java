package org.flysky.coder.vo;

import java.util.List;

public class PostWrapper {
    private Integer uid;
    private String title;
    private String content;
    private Integer sectorId;
    private List<String> tagNameList;
    private boolean isAnonymous;
    private String anonymousName;
    private Integer type;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getSectorId() {
        return sectorId;
    }

    public void setSectorId(Integer sectorId) {
        this.sectorId = sectorId;
    }

    public List<String> getTagNameList() {
        return tagNameList;
    }

    public void setTagNameList(List<String> tagNameList) {
        this.tagNameList = tagNameList;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }

    public String getAnonymousName() {
        return anonymousName;
    }

    public void setAnonymousName(String anonymousName) {
        this.anonymousName = anonymousName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public PostWrapper(Integer uid, String title, String content, Integer sectorId, List<String> tagNameList, boolean isAnonymous, String anonymousName, Integer type) {
        this.uid = uid;
        this.title = title;
        this.content = content;
        this.sectorId = sectorId;
        this.tagNameList = tagNameList;
        this.isAnonymous = isAnonymous;
        this.anonymousName = anonymousName;
        this.type = type;
    }
}
