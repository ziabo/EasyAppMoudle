package com.example.testing.testrxandre.bean;

import java.util.List;

/**
 * Created by ziabo on 2017/5/9.
 * 这个是实体类,里面只有我们关注的数据,其他的都统一处理
 */

public class DataBean {

    /**
     * nextPage : 1
     * count : 6
     * pageSize : 20
     * prevPage : 1
     * currentPage : 1
     * pageNum : 1
     * healthInfo : [{"img":"http://test2.mb.zkrj.com/wlstatic/image/2017-03-30/0394155040297634.png","formatCreateDate":"03-30","formatUpdateDate":"04-01","index":1,"updateTime":"2017-04-01 16:01:44","title":"茶的物极必反","accountId":"4028817d549332dd015494a0edd80000","subTitle":"知识","createTime":"2017-03-30 16:19:34","publish":true,"adminAccountId":"4028817d549332dd015494a0edd80000","orders":1,"id":"8a9a35085b180e49015b1e4c604800ad","account":"zkrj"},{"img":"http://test2.mb.zkrj.com/wlstatic/image/2017-03-30/1143420851997416.jpeg","formatCreateDate":"03-30","formatUpdateDate":"04-01","index":2,"updateTime":"2017-04-01 15:59:24","title":"少食多餐在说什么？","accountId":"4028817d549332dd015494a0edd80000","subTitle":"糖尿病","createTime":"2017-03-30 16:12:03","publish":true,"adminAccountId":"4028817d549332dd015494a0edd80000","orders":1,"id":"8a9a35085b180e49015b1e4581a100aa","account":"zkrj"},{"img":"http://test2.mb.zkrj.com/wlstatic/image/2017-03-30/7490881972133794.jpeg","formatCreateDate":"03-30","formatUpdateDate":"04-01","index":3,"updateTime":"2017-04-01 15:56:22","title":"健康运动踢毽子","accountId":"4028817d549332dd015494a0edd80000","subTitle":"高血压","createTime":"2017-03-30 16:10:58","publish":true,"adminAccountId":"4028817d549332dd015494a0edd80000","orders":1,"id":"8a9a35085b180e49015b1e4483c400a8","account":"zkrj"},{"img":"http://test2.mb.zkrj.com/wlstatic/image/2017-03-30/8457604241319624.jpeg","formatCreateDate":"03-30","formatUpdateDate":"04-01","index":4,"updateTime":"2017-04-01 15:54:19","title":"糖尿病病足的定义与预防","accountId":"4028817d549332dd015494a0edd80000","subTitle":"糖尿病","createTime":"2017-03-30 11:59:50","publish":true,"adminAccountId":"4028817d549332dd015494a0edd80000","orders":1,"id":"8a9a35085b180e49015b1d5e9897009a","account":"zkrj"},{"img":"http://test2.mb.zkrj.com/wlstatic/image/2017-03-30/4638045747699966.jpeg","formatCreateDate":"03-27","formatUpdateDate":"04-01","index":5,"updateTime":"2017-04-01 15:50:28","title":"日常小事才不是小事","accountId":"4028817d549332dd015494a0edd80000","subTitle":"高血压","createTime":"2017-03-27 11:13:05","publish":true,"adminAccountId":"4028817d549332dd015494a0edd80000","orders":1,"id":"8a9a35085b09d7e0015b0dc0b4750005","account":"zkrj"},{"img":"http://test2.mb.zkrj.com/wlstatic/image/2017-04-01/9660687163661661.jpeg","formatCreateDate":"03-27","formatUpdateDate":"04-01","index":6,"updateTime":"2017-04-01 14:57:04","title":"洗澡谨记五个不","accountId":"4028817d549332dd015494a0edd80000","subTitle":"糖尿病","createTime":"2017-03-27 11:10:10","publish":true,"adminAccountId":"4028817d549332dd015494a0edd80000","orders":1,"id":"8a9a35085b09d7e0015b0dbe08e60003","account":"zkrj"}]
     */

    public int nextPage;
    public int count;
    public int pageSize;
    public int prevPage;
    public int currentPage;
    public int pageNum;
    public List<HealthInfoBean> healthInfo;

    public static class HealthInfoBean {
        /**
         * img : http://test2.mb.zkrj.com/wlstatic/image/2017-03-30/0394155040297634.png
         * formatCreateDate : 03-30
         * formatUpdateDate : 04-01
         * index : 1
         * updateTime : 2017-04-01 16:01:44
         * title : 茶的物极必反
         * accountId : 4028817d549332dd015494a0edd80000
         * subTitle : 知识
         * createTime : 2017-03-30 16:19:34
         * publish : true
         * adminAccountId : 4028817d549332dd015494a0edd80000
         * orders : 1
         * id : 8a9a35085b180e49015b1e4c604800ad
         * account : zkrj
         */

        public String img;
        public String formatCreateDate;
        public String formatUpdateDate;
        public int index;
        public String updateTime;
        public String title;
        public String accountId;
        public String subTitle;
        public String createTime;
        public boolean publish;
        public String adminAccountId;
        public int orders;
        public String id;
        public String account;

        @Override
        public String toString() {
            return "HealthInfoBean{" +
                    "img='" + img + '\'' +
                    ", formatCreateDate='" + formatCreateDate + '\'' +
                    ", formatUpdateDate='" + formatUpdateDate + '\'' +
                    ", index=" + index +
                    ", updateTime='" + updateTime + '\'' +
                    ", title='" + title + '\'' +
                    ", accountId='" + accountId + '\'' +
                    ", subTitle='" + subTitle + '\'' +
                    ", createTime='" + createTime + '\'' +
                    ", publish=" + publish +
                    ", adminAccountId='" + adminAccountId + '\'' +
                    ", orders=" + orders +
                    ", id='" + id + '\'' +
                    ", account='" + account + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "DataBean{" +
                "nextPage=" + nextPage +
                ", count=" + count +
                ", pageSize=" + pageSize +
                ", prevPage=" + prevPage +
                ", currentPage=" + currentPage +
                ", pageNum=" + pageNum +
                ", healthInfo=" + healthInfo +
                '}';
    }
}
