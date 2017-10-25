package com.fxlc.trucker.bean;

import java.io.Serializable;

/**
 * Created by cyd on 2017/8/22.
 */

public class City implements Serializable{

    private  String id;
    private String name;
    private  String parentId;
    private  int type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
