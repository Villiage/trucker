package com.fxlc.trucker.bean;

import java.util.List;

/**
 * Created by cyd on 2017/9/6.
 */

public class MyOrder {

    private List<MyOrderBean> finishList;
    private List<MyOrderBean> unfinishList;

    public List<MyOrder.MyOrderBean> getFinishList() {
        return finishList;
    }

    public void setFinishList(List<MyOrderBean> finishList) {
        this.finishList = finishList;
    }

    public List<MyOrderBean> getUnfinishList() {
        return unfinishList;
    }

    public void setUnfinishList(List<MyOrderBean> unfinishList) {
        this.unfinishList = unfinishList;
    }

    public static class MyOrderBean {
      
        private String orderId;
        private String orderNo;
        private String from;
        private String to;
        private String money;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }
    }
}
