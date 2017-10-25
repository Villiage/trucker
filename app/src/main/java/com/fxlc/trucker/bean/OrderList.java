package com.fxlc.trucker.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cyd on 2017/8/28.
 */

public class OrderList {


    private List<OrderBean> source;

    public List<OrderBean> getSource() {
        return source;
    }

    public void setSource(List<OrderBean> source) {
        this.source = source;
    }

    public static class OrderBean implements Serializable{


        private String orderId;
        private String orderNo;
        private String sourceId;
        private String goodsType;
        private int freight;
        private int msgFee;
        private int perPrice;
        private int insurance;
        private String from;
        private String fromPlace;
        private double loadLon;
        private double loadLat;
        private int loadFee;
        private String to;
        private String toPlace;
        private double unloadLon;
        private double unloadLat;
        private int unlaodFee;
        private int needNum;
        private int agreeTrucks;
        private int waiLoadTrucks;
        private int waiUnloadTrucks;
        private float allowLoss;
        private String comPhone;
        private String comName;
        private List<String> remark;
        private int isInsurance;
        private String loadPhone;
        private String unloadPhone;

        public String getSourceId() {
            return sourceId;
        }

        public void setSourceId(String sourceId) {
            this.sourceId = sourceId;
        }

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

        public String getGoodsType() {
            return goodsType;
        }

        public void setGoodsType(String goodsType) {
            this.goodsType = goodsType;
        }

        public int getFreight() {
            return freight;
        }

        public void setFreight(int freight) {
            this.freight = freight;
        }

        public int getMsgFee() {
            return msgFee;
        }

        public void setMsgFee(int msgFee) {
            this.msgFee = msgFee;
        }

        public int getPerPrice() {
            return perPrice;
        }

        public void setPerPrice(int perPrice) {
            this.perPrice = perPrice;
        }

        public int getInsurance() {
            return insurance;
        }

        public void setInsurance(int insurance) {
            this.insurance = insurance;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getFromPlace() {
            return fromPlace;
        }

        public void setFromPlace(String fromPlace) {
            this.fromPlace = fromPlace;
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

        public int getLoadFee() {
            return loadFee;
        }

        public void setLoadFee(int loadFee) {
            this.loadFee = loadFee;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getToPlace() {
            return toPlace;
        }

        public void setToPlace(String toPlace) {
            this.toPlace = toPlace;
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

        public int getUnlaodFee() {
            return unlaodFee;
        }

        public void setUnlaodFee(int unlaodFee) {
            this.unlaodFee = unlaodFee;
        }

        public int getNeedNum() {
            return needNum;
        }

        public void setNeedNum(int needNum) {
            this.needNum = needNum;
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

        public int getWaiUnloadTrucks() {
            return waiUnloadTrucks;
        }

        public void setWaiUnloadTrucks(int waiUnloadTrucks) {
            this.waiUnloadTrucks = waiUnloadTrucks;
        }

        public String getComName() {
            return comName;
        }

        public void setComName(String comName) {
            this.comName = comName;
        }

        public String getComPhone() {
            return comPhone;
        }

        public void setComPhone(String comPhone) {
            this.comPhone = comPhone;
        }

        public List<String> getRemark() {
            return remark;
        }

        public void setRemark(List<String> remark) {
            this.remark = remark;
        }

        public float getAllowLoss() {
            return allowLoss;
        }

        public void setAllowLoss(float allowLoss) {
            this.allowLoss = allowLoss;
        }


        public int getIsInsurance() {
            return isInsurance;
        }

        public void setIsInsurance(int isInsurance) {
            this.isInsurance = isInsurance;
        }

        public String getLoadPhone() {
            return loadPhone;
        }

        public void setLoadPhone(String loadPhone) {
            this.loadPhone = loadPhone;
        }

        public String getUnloadPhone() {
            return unloadPhone;
        }

        public void setUnloadPhone(String unloadPhone) {
            this.unloadPhone = unloadPhone;
        }
    }
}
