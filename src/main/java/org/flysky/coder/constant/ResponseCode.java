package org.flysky.coder.constant;


public class ResponseCode {
    /*
    0:名字重复
    1:成功
    2:查询结果为空
    3:没有权限操作
    4:依赖的上一级不存在
    5:重复操作(比如点两次赞、收藏)
    6:重复邮箱
    7:未激活
    8:错误的邮箱或者密码
    500:未知错误
    */
    public static final int DUPLICATE_NAME = 0;

    public static final int SUCCEED = 1;

    public static final int NOT_FOUND = 2;

    public static final int FORBIDDEN = 3;

    public static final int PREV_OBJECT_NOT_FOUND = 4;

    public static final int DUPLICATE_ACTION = 5;

    public static final int DUPLICATE_EMAIL = 6;

    public static final int NOT_ACTIVATED = 7;

    public static final int WRONG_EMAIL_OR_PASSWORD = 8;

    public static final int UNKNOWN_ERROR = 500;
}
