package com.simon.appmanager.https.entity;

/**
 * 公用传入bean
 */
public class CommonOutputBean {

    /**
     * code : 301
     * msg : 关注失败
     */

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
