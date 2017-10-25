package com.fxlc.trucker.api;

import com.fxlc.trucker.bean.AllBill;
import com.fxlc.trucker.bean.BillInfo;
import com.fxlc.trucker.bean.CarList;
import com.fxlc.trucker.bean.CurrentOrder;
import com.fxlc.trucker.bean.MyOrder;
import com.fxlc.trucker.bean.OrderList;
import com.fxlc.trucker.bean.ReceiveOrder;
import com.fxlc.trucker.net.HttpResult;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by cyd on 2017/8/28.
 */

public interface OrderService {


    @POST("sourceList")
    Call<HttpResult<OrderList>> listOrder();

    @GET("currentOrder")
    Call<HttpResult<CurrentOrder>> currentOrder();

    @POST("confirmOrder")
    Call<HttpResult<CurrentOrder>> receiveOrder(@Query("sourceId") String orderId, @Query("carId") String carId);

    @Multipart
    @POST("loadOrder")
    Call<HttpResult<CurrentOrder>> loadOrder(@QueryMap Map<String, String> map, @Part("loadImg\"; filename=\"" + "loadImg") RequestBody body);

    @Multipart
    @POST("unloadOrder")
    Call<HttpResult<CurrentOrder>> unloadOrder(@QueryMap Map<String, String> map, @Part("unloadImg\"; filename=\"" + "unloadImg") RequestBody body);

    @POST("goCount")
    Call<HttpResult> goCount(@Query("orderId") String orderId);

    @GET("allOrderList")
    Call<HttpResult<MyOrder>> myOrder();

    @GET("feeInfo")
    Call<HttpResult<CurrentOrder>> feeInfo(@Query("orderId") String orderId);

    @GET("orderInfo")
    Call<HttpResult<OrderList.OrderBean>> orderInfo(@Query("orderId") String orderId);

    @GET("carList")
    Call<HttpResult<CarList>> listCar();

    @GET("billManage")
    Call<HttpResult<AllBill>> listAllbill();

    @GET("billInfo")
    Call<HttpResult<BillInfo>> billInfo(@Query("orderId") String orderId);

    @POST("delReportNo")
    Call<HttpResult> delReportNo(@Query("reportNoId") String reportNoId);

}
