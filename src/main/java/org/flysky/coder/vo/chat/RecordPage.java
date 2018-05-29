package org.flysky.coder.vo.chat;

/**
 * Created by hxuhao233 on 2018/4/2.
 */
public class RecordPage {
    private int toId;

    private int type;

    private int pageNum;

    private int pageSize;

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
