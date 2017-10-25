package com.fxlc.trucker.bean;

import java.io.Serializable;

/**
 * Created by cyd on 2017/8/21.
 */

public class Withdraw implements Serializable{

    /**
     * cashId : dc1ba5d8b31f40348183d56a600544fd
     * money : 2
     */

    private String cashId;
    private String money;

    public String getCashId() {
        return cashId;
    }

    public void setCashId(String cashId) {
        this.cashId = cashId;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
