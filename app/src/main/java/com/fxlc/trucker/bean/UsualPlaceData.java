package com.fxlc.trucker.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cyd on 2017/8/24.
 */

public class UsualPlaceData {


    private List<UsualPlace> usuallyPlace;

    public List<UsualPlace> getUsuallyPlace() {
        return usuallyPlace;
    }

    public void setUsuallyPlace(List<UsualPlace> usuallyPlace) {
        this.usuallyPlace = usuallyPlace;
    }

    public static class UsualPlace implements Serializable{
        /**
         * id : 625100d089d849109276548efae63fe1
         * countyName : 静海县
         */

        private String id;
        private String countyName;

        public UsualPlace(String id, String countyName) {
            this.id = id;
            this.countyName = countyName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCountyName() {
            return countyName;
        }

        public void setCountyName(String countyName) {
            this.countyName = countyName;
        }
    }
}
