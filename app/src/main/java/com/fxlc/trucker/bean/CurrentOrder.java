package com.fxlc.trucker.bean;

import java.io.Serializable;

/**
 * Created by cyd on 2017/8/30.
 */

public class CurrentOrder implements Serializable{




    private String orderId;
    private int orderStatus;
    private String orderNo;
    private String goodsType;
    private String from;
    private double loadLon;
    private double loadLat;
    private String loadPhone;
    private int agreeTrucks;
    private int waiLoadTrucks;
    private String to;
    private double unloadLon;
    private double unloadLat;
    private String unloadPhone;
    private int waiUnloadTrucks;
    private String loadWeight;
    private String unloadWeight;
    private int freight;
    private String freightSum;
    private int perPrice;
    private String realLoss;
    private String allowLoss;
    private String lossSum;
    private int insurance;
    private int isInsurance;
    private String endSum;
    private String loadTime;
    private String unloadTime;



    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public double getLoadLon() {
        return loadLon;
    }

    public void setLoadLon(double loadLon) {
        this.loadLon = loadLon;
    }

    public double getLoadLat() {
        return loadLat;
    }

    public void setLoadLat(double loadLat) {
        this.loadLat = loadLat;
    }

    public String getLoadPhone() {
        return loadPhone;
    }

    public void setLoadPhone(String loadPhone) {
        this.loadPhone = loadPhone;
    }

    public int getAgreeTrucks() {
        return agreeTrucks;
    }

    public void setAgreeTrucks(int agreeTrucks) {
        this.agreeTrucks = agreeTrucks;
    }

    public int getWaiLoadTrucks() {
        return waiLoadTrucks;
    }

    public void setWaiLoadTrucks(int waiLoadTrucks) {
        this.waiLoadTrucks = waiLoadTrucks;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public double getUnloadLon() {
        return unloadLon;
    }

    public void setUnloadLon(double unloadLon) {
        this.unloadLon = unloadLon;
    }

    public double getUnloadLat() {
        return unloadLat;
    }

    public void setUnloadLat(double unloadLat) {
        this.unloadLat = unloadLat;
    }

    public String getUnloadPhone() {
        return unloadPhone;
    }

    public void setUnloadPhone(String unloadPhone) {
        this.unloadPhone = unloadPhone;
    }

    public int getWaiUnloadTrucks() {
        return waiUnloadTrucks;
    }

    public void setWaiUnloadTrucks(int waiUnloadTrucks) {
        this.waiUnloadTrucks = waiUnloadTrucks;
    }

    public String getLoadWeight() {
        return loadWeight;
    }

    public void setLoadWeight(String loadWeight) {
        this.loadWeight = loadWeight;
    }

    public String getUnloadWeight() {
        return unloadWeight;
    }

    public void setUnloadWeight(String unloadWeight) {
        this.unloadWeight = unloadWeight;
    }

    public int getFreight() {
        return freight;
    }

    public void setFreight(int freight) {
        this.freight = freight;
    }

    public String getFreightSum() {
        return freightSum;
    }

    public void setFreightSum(String freightSum) {
        this.freightSum = freightSum;
    }

    public int getPerPrice() {
        return perPrice;
    }

    public void setPerPrice(int perPrice) {
        this.perPrice = perPrice;
    }

    public String getRealLoss() {
        return realLoss;
    }

    public void setRealLoss(String realLoss) {
        this.realLoss = realLoss;
    }

    public String getAllowLoss() {
        return allowLoss;
    }

    public void setAllowLoss(String allowLoss) {
        this.allowLoss = allowLoss;
    }

    public String getLossSum() {
        return lossSum;
    }

    public void setLossSum(String lossSum) {
        this.lossSum = lossSum;
    }

    public int getInsurance() {
        return insurance;
    }

    public void setInsurance(int insurance) {
        this.insurance = insurance;
    }

    public int getIsInsurance() {
        return isInsurance;
    }

    public void setIsInsurance(int isInsurance) {
        this.isInsurance = isInsurance;
    }

    public String getEndSum() {
        return endSum;
    }

    public void setEndSum(String endSum) {
        this.endSum = endSum;
    }


    public String getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(String loadTime) {
        this.loadTime = loadTime;
    }

    public String getUnloadTime() {
        return unloadTime;
    }

    public void setUnloadTime(String unloadTime) {
        this.unloadTime = unloadTime;
    }
}
