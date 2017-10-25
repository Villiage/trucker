package com.fxlc.trucker.bean;

import java.util.List;

/**
 * Created by cyd on 2017/10/12.
 */

public class MsgList {

    private List<MsgListBean> msgList;

    public List<MsgListBean> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<MsgListBean> msgList) {
        this.msgList = msgList;
    }

    public static class MsgListBean {
        /**
         * content : 您于2017-10-11 15:29:25申请提现:1元
         * type : 1
         * createDate : 2017-10-11 15:29:25
         * messageId : 5b35e29e00f743f3bcaaf0cbea0d5d7c
         */

        private String content;
        private String type;
        private String createDate;
        private String messageId;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }
    }
}
