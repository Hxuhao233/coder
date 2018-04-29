package org.flysky.coder.vo;

import java.time.LocalDateTime;

public class PostSearchWrapper {
    private String title;
    private LocalDateTime time1;
    private LocalDateTime time2;
    private Integer type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getTime1() {
        return time1;
    }

    public void setTime1(LocalDateTime time1) {
        this.time1 = time1;
    }

    public LocalDateTime getTime2() {
        return time2;
    }

    public void setTime2(LocalDateTime time2) {
        this.time2 = time2;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public PostSearchWrapper(){

    }


}
