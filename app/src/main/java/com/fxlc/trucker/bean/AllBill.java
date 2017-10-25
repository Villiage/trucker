package com.fxlc.trucker.bean;

import java.util.List;

/**
 * Created by cyd on 2017/9/26.
 */

public class AllBill {


    private List<ReportBean> reportList;
    private List<BillBean> loadList;
    private List<BillBean> unloadList;
    private List<BillBean> finishList;

    public List<ReportBean> getReportList() {
        return reportList;
    }

    public void setReportList(List<ReportBean> reportNoList) {
        this.reportList = reportNoList;
    }

    public List<BillBean> getLoadList() {
        return loadList;
    }

    public void setLoadList(List<BillBean> loadList) {
        this.loadList = loadList;
    }

    public List<BillBean> getUnloadList() {
        return unloadList;
    }

    public void setUnloadList(List<BillBean> unloadList) {
        this.unloadList = unloadList;
    }

    public List<BillBean> getFinishList() {
        return finishList;
    }

    public void setFinishList(List<BillBean> finishList) {
        this.finishList = finishList;
    }

    public static class ReportBean {
        /**
         * reportNoId : 1
         * reportNoDate : 2017-09-22 17:31:44
         * carNo : 晋8888811
         * sourceName : 德一万
         */

        private String reportNoId;
        private String reportNoDate;
        private String carNo;
        private String sourceName;

        public String getReportNoId() {
            return reportNoId;
        }

        public void setReportNoId(String reportNoId) {
            this.reportNoId = reportNoId;
        }

        public String getReportNoDate() {
            return reportNoDate;
        }

        public void setReportNoDate(String reportNoDate) {
            this.reportNoDate = reportNoDate;
        }

        public String getCarNo() {
            return carNo;
        }

        public void setCarNo(String carNo) {
            this.carNo = carNo;
        }

        public String getSourceName() {
            return sourceName;
        }

        public void setSourceName(String sourceName) {
            this.sourceName = sourceName;
        }
    }

    public static class BillBean {


  

        private String orderId;
        private String carNo;
        private String handcarNo;
        private String loadDate;
        private String unloadDate;
        private String loadWeight;
        private String unloadWeight;
        private String endSum;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getCarNo() {
            return carNo;
        }

        public void setCarNo(String carNo) {
            this.carNo = carNo;
        }

        public String getHandcarNo() {
            return handcarNo;
        }

        public void setHandcarNo(String handcarNo) {
            this.handcarNo = handcarNo;
        }

        public String getLoadDate() {
            return loadDate;
        }

        public void setLoadDate(String loadDate) {
            this.loadDate = loadDate;
        }

        public String getUnloadDate() {
            return unloadDate;
        }

        public void setUnloadDate(String unloadDate) {
            this.unloadDate = unloadDate;
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

        public String getEndSum() {
            return endSum;
        }

        public void setEndSum(String endSum) {
            this.endSum = endSum;
        }
    }

 
}
