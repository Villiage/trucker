package com.fxlc.trucker.api;


import com.fxlc.trucker.bean.CardInfo;
import com.fxlc.trucker.bean.UInfo;
import com.fxlc.trucker.bean.User;
import com.fxlc.trucker.bean.UserInfo;
import com.fxlc.trucker.net.HttpCallback;
import com.fxlc.trucker.net.HttpResult;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by cyd on 2017/6/14.
 */

public interface UserService {
    @POST("register")
    Call<HttpResult> reg(@QueryMap Map<String, String> param);

    @POST("login")
    Call<HttpResult<User>> login(@Query("mobile") String mobile, @Query("yzm") String yzm);

    @POST("loginPass")
    Call<HttpResult> setpwd(@QueryMap Map<String, String> param);

    @GET("sms/getSms")
    Call<ResponseBody> getSms(@Query("mobile") String mobile);
//     @Multipart
//    @POST("saveUserCard")
//    Call<HttpResult>  saveIDcard (@Part("pcardpositive") RequestBody face, @Part("pcardreverse") RequestBody back, @QueryMap Map<String, String> param);

    //    @Multipart
//    @POST("saveUserCard")
//    Call<HttpResult>  saveIDcard2 (@Part MultipartBody.Part face, @Part MultipartBody.Part back, @QueryMap Map<String, String> param);
    @Multipart
    @POST("saveUserCard")
    Call<HttpResult> saveIDcard(@Part("pcardpositive\"; filename=\"" + "pcardpositive") RequestBody face, @Part("pcardreverse\"; filename=\"" + "pcardreverse") RequestBody back, @QueryMap Map<String, String> param);

    @GET("idCardInfo")
    Call<HttpResult<CardInfo>> cardInfo();


    @GET("user/getUserInformation")
    Call<HttpResult<UInfo>> getUInfo(@QueryMap Map<String, String> param);


    @POST("payPass")
    Call<HttpResult> savePayPass(@QueryMap HashMap<String, String> map);

    @GET("getUserInfo")
    Call<HttpResult<UserInfo>> getUserInfo();

    @GET("isPaypass")
    Call<HttpResult> isPaypass();

    @POST("verificationPaypass")
    Call<HttpResult> verifypwd(@Query("payPass") String pwd);
}
