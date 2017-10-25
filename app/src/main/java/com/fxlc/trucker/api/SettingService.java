package com.fxlc.trucker.api;


import com.fxlc.trucker.bean.Carousel;
import com.fxlc.trucker.bean.MsgList;
import com.fxlc.trucker.bean.UsualPlace;
import com.fxlc.trucker.bean.UsualPlaceData;
import com.fxlc.trucker.bean.Version;
import com.fxlc.trucker.net.HttpResult;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by cyd on 2017/7/12.
 */

public interface SettingService {
    @FormUrlEncoded
    @POST("saveFeedback")
    Call<HttpResult> feedback(@Field("feedback") String feedback);

    @POST("saveUsuallyPlace")
    Call<HttpResult<UsualPlace>> saveUsualPlace(@Query("provinceId") String pId, @Query("cityId") String cId, @Query("countyId") String dId);

    @GET("usuallyPlace")
    Call<HttpResult<UsualPlaceData>> listUsuallyPlace();

    @POST("delUsuallyPlace")
    Call<HttpResult> delUsuallyPlace(@Query("usuallyPlaceId") String pid);

    @GET("carousel")
    Call<HttpResult<Carousel>> getCarousel();

    @GET("saveTrack")
    Call<HttpResult> saveLocation(@Query("lat") double lat, @Query("lon") double lon, @Query("position") String position);

    @GET("msgList")
    Call<HttpResult<MsgList>>  getMsg();

    @GET("getVersion")
    Call<HttpResult<Version>> getVersion(@Query("platform") int platform,@Query("apptype") int type);

}
