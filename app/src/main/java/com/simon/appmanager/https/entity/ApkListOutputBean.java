package com.simon.appmanager.https.entity;

import java.util.List;

/**
 * apk列表
 */
public class ApkListOutputBean {

    /**
     * code : 200
     * msg : 上传成功
     * data : [{"id":3,"file":"https://open.faw-hongqiacademy.com/Uploads/Website/157632818769204.apk","type":"hq_publish","date":"2019-12-14 20:56:45","introduce":"版本更新","vresion":"0.0.1"}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 3
         * file : https://open.faw-hongqiacademy.com/Uploads/Website/157632818769204.apk
         * type : hq_publish
         * date : 2019-12-14 20:56:45
         * introduce : 版本更新
         * vresion : 0.0.1
         */

        private int id;
        private String file;
        private String type;
        private String date;
        private String introduce;
        private String vresion;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public String getVresion() {
            return vresion;
        }

        public void setVresion(String vresion) {
            this.vresion = vresion;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "id=" + id +
                    ", file='" + file + '\'' +
                    ", type='" + type + '\'' +
                    ", date='" + date + '\'' +
                    ", introduce='" + introduce + '\'' +
                    ", vresion='" + vresion + '\'' +
                    '}';
        }
    }

}
