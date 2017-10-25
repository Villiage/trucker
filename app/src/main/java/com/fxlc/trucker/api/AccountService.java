package com.fxlc.trucker.api;

import com.fxlc.trucker.bean.Balance;
import com.fxlc.trucker.bean.BankCards;
import com.fxlc.trucker.bean.BankList;
import com.fxlc.trucker.bean.WalletDetail;
import com.fxlc.trucker.bean.Withdraw;
import com.fxlc.trucker.bean.WithdrawInfo;
import com.fxlc.trucker.net.HttpResult;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.QueryName;

/**
 * Created by cyd on 2017/8/18.
 */

public interface AccountService {
     @GET("bankList")
    Call<HttpResult<BankList>>   listBank();
    @GET("bankCardList")
    Call<HttpResult<BankCards>>   listBankCard();
    @POST("saveBank")
    Call<HttpResult>   saveBank(@QueryMap Map<String,String> map);

    @POST("delBank")
    Call<HttpResult> delBank(@Query("cardId") String cardId);

   @POST("withdraw")
    Call<HttpResult<Withdraw>>   withdraw(@QueryMap Map<String,String> map);
    @GET("getMoney")
    Call<HttpResult<Balance>>   getMoney(@QueryMap Map<String,String> map);
    @GET("withdrawInfo")
    Call<HttpResult<WithdrawInfo>>   withdrawInfo(@Query("withdrawId") String  withdrawId);

    @GET("walletDetail")
    Call<HttpResult<WalletDetail>>  walletDetail(@Query("year") String year,@Query("month") String month);


}
