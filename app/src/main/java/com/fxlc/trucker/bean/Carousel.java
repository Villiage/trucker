package com.fxlc.trucker.bean;

import java.util.List;

/**
 * Created by cyd on 2017/9/11.
 */

public class Carousel {

    private List<CarouselListBean> carouselList;

    public List<CarouselListBean> getCarouselList() {
        return carouselList;
    }

    public void setCarouselList(List<CarouselListBean> carouselList) {
        this.carouselList = carouselList;
    }

    public static class CarouselListBean {
        /**
         * photoName : 利息低
         * photoPath : http://192.168.1.102:8080/zklm/userfiles/1/files/zklm/base/zklmCarousel/2017/09/banner2.jpg
         */

        private String photoName;
        private String photoPath;

        public String getPhotoName() {
            return photoName;
        }

        public void setPhotoName(String photoName) {
            this.photoName = photoName;
        }

        public String getPhotoPath() {
            return photoPath;
        }

        public void setPhotoPath(String photoPath) {
            this.photoPath = photoPath;
        }
    }
}
