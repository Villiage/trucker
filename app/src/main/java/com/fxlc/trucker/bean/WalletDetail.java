package com.fxlc.trucker.bean;

import java.util.List;

/**
 * Created by cyd on 2017/9/7.
 */

public class WalletDetail {


    private List<IncomeBean> income;
    private List<WithdrawBean> withdraw;

    private String totalIncome;
    private String totalWithdraw;

    public String getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(String totalIncome) {
        this.totalIncome = totalIncome;
    }

    public String getTotalWithdraw() {
        return totalWithdraw;
    }

    public void setTotalWithdraw(String totalWithdraw) {
        this.totalWithdraw = totalWithdraw;
    }

    public List<IncomeBean> getIncome() {
        return income;
    }

    public void setIncome(List<IncomeBean> income) {
        this.income = income;
    }

    public List<WithdrawBean> getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(List<WithdrawBean> withdraw) {
        this.withdraw = withdraw;
    }

    public static class IncomeBean {


        private String orderId;
        private String orderNo;
        private String money;
        private String successDate;

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

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getSuccessDate() {
            return successDate;
        }

        public void setSuccessDate(String successDate) {
            this.successDate = successDate;
        }
    }

    public static class WithdrawBean {
        /**
         * withdrawId : 50ed1e8545f54d35946ed01c8d4257a0
         * money : 1
         * successDate : 2017-09-07 14:55:41
         */

        private String withdrawId;
        private String money;
        private String successDate;

        public String getWithdrawId() {
            return withdrawId;
        }

        public void setWithdrawId(String withdrawId) {
            this.withdrawId = withdrawId;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getSuccessDate() {
            return successDate;
        }

        public void setSuccessDate(String successDate) {
            this.successDate = successDate;
        }
    }
}
