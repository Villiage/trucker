package com.fxlc.trucker.bean;

import java.util.List;

/**
 * Created by cyd on 2017/9/25.
 */

public class CarList {


    private List<CarlistBean> carlist;

    public List<CarlistBean> getCarlist() {
        return carlist;
    }

    public void setCarlist(List<CarlistBean> carlist) {
        this.carlist = carlist;
    }

    public static class CarlistBean {


        private String carId;
        private String carNo;
        private String carStatus;

        public String getCarId() {
            return carId;
        }

        public void setCarId(String carId) {
            this.carId = carId;
        }

        public String getCarNo() {
            return carNo;
        }

        public void setCarNo(String carNo) {
            this.carNo = carNo;
        }

        public String getCarStatus() {
            return carStatus;
        }

        public void setCarStatus(String carStatus) {
            this.carStatus = carStatus;
        }
    }
}
