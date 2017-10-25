package com.fxlc.trucker.bean;

/**
 * Created by cyd on 2017/8/21.
 */

public class WithdrawInfo {

    /**
     * money : 1
     * status : 审核中
     * createDate : 2017-08-22 09:25:57
     * successDate :
     * bankName : 农业银行
     * bankNo : 648726942669
     */

    private String money;
    private String status;
    private String createDate;
    private String successDate;
    private String bankName;
    private String bankNo;

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getSuccessDate() {
        return successDate;
    }

    public void setSuccessDate(String successDate) {
        this.successDate = successDate;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }
}
