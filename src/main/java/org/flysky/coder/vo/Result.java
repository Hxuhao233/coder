package org.flysky.coder.vo;

/**
 * Created by hxuhao233 on 2018/4/10.
 */
public class Result {

    private int code;

    private String info;

    public Result(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
