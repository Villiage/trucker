package com.fxlc.trucker.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cyd on 2017/6/23.
 */

public class Truck implements Serializable {
    private int status;
    private int cartype;


    public int getCartype() {
        return cartype;
    }

    public void setCartype(int cartype) {
        this.cartype = cartype;
    }

    private String brand;
    private String style;
    private String soup;
    private String drive;

    public String getDrive() {
        return drive;
    }

    public void setDrive(String drive) {
        this.drive = drive;
    }

    private String carNo;
    private String driveImg1;
    private String driveImg2;
    private String driveImg3;
    private List<String> driveImg;
    private String manageImg;

    private String handcarNo;
    private String handdriveImg1;
    private String handdriveImg2;
    private String handdriveImg3;
    private List<String> handdriveImg;

    private String handmanageImg;
    private String type;
    private String length;
    private String height;
    private String mortgageMoney;



    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
    public String getSoup() {
        return soup;
    }

    public void setSoup(String soup) {
        this.soup = soup;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getDriveImg1() {
        return driveImg1;
    }

    public void setDriveImg1(String driveImg1) {
        this.driveImg1 = driveImg1;
    }

    public String getDriveImg2() {
        return driveImg2;
    }

    public void setDriveImg2(String driveImg2) {
        this.driveImg2 = driveImg2;
    }

    public String getDriveImg3() {
        return driveImg3;
    }

    public void setDriveImg3(String driveImg3) {
        this.driveImg3 = driveImg3;
    }

    public String getManageImg() {
        return manageImg;
    }

    public void setManageImg(String manageImg) {
        this.manageImg = manageImg;
    }

    public String getHandcarNo() {
        return handcarNo;
    }

    public void setHandcarNo(String handcarNo) {
        this.handcarNo = handcarNo;
    }

    public String getHanddriveImg1() {
        return handdriveImg1;
    }

    public void setHanddriveImg1(String handdriveImg1) {
        this.handdriveImg1 = handdriveImg1;
    }

    public String getHanddriveImg2() {
        return handdriveImg2;
    }

    public void setHanddriveImg2(String handdriveImg2) {
        this.handdriveImg2 = handdriveImg2;
    }

    public String getHanddriveImg3() {
        return handdriveImg3;
    }

    public void setHanddriveImg3(String handdriveImg3) {
        this.handdriveImg3 = handdriveImg3;
    }

    public String getHandmanageImg() {
        return handmanageImg;
    }

    public void setHandmanageImg(String handmanageImg) {
        this.handmanageImg = handmanageImg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public List<String> getDriveImg() {
        return driveImg;
    }

    public void setDriveImg(List<String> driveImg) {
        this.driveImg = driveImg;
    }

    public List<String> getHanddriveImg() {
        return handdriveImg;
    }

    public void setHanddriveImg(List<String> handdriveImg) {
        this.handdriveImg = handdriveImg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMortgageMoney() {
        return mortgageMoney;
    }

    public void setMortgageMoney(String mortgageMoney) {
        this.mortgageMoney = mortgageMoney;
    }
}
