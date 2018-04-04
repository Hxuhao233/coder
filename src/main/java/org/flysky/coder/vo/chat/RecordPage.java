package org.flysky.coder.vo.chat;

/**
 * Created by hxuhao233 on 2018/4/2.
 */
public class RecordPage {
    private int roomId;

    private int pageNum;

    private int pageSize;

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
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
}
