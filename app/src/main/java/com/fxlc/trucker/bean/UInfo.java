package com.fxlc.trucker.bean;

import java.io.Serializable;

/**
 * Created by cyd on 2017/6/19.
 */

public class UInfo implements Serializable {




    private String id;
    private String mobile;
    private int pstatus;
    private String token;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getPsstatu() {
        return pstatus;
    }

    public void setPsstatu(int psstatu) {
        this.pstatus = psstatu;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
