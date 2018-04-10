package org.flysky.coder.vo;

/**
 * Created by hxuhao233 on 2018/4/10.
 */
public class ResultWrapper extends Result{

    private Object payload;

    public ResultWrapper(int code) {
        super(code);
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }
}
