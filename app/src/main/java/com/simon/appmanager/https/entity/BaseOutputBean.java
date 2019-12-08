package com.simon.appmanager.https.entity;

/**
 * 公用bean
 */
public class BaseOutputBean {

    /**
     * status : 200
     * code : 200
     * state : null
     * msg : 请求成功
     */

    private int status;
    private int code;
    private String msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

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
