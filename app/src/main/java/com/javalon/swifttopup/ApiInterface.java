package com.javalon.swifttopup;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/APIAirtimeV1.asp")
    Call<PurchaseTransact> purchaseAirtime(
            @Query("UserID") String userId,
            @Query("APIKey") String apikey,
            @Query("MobileNetwork") String network,
            @Query("Amount") String amount,
            @Query("MobileNumber") String number,
            @Query("RequestID") String requestId,
            @Query("CallbackURL") String callbackUrl);

    @GET("/APIDatabundleV1.asp")
    Call<PurchaseTransact> purchaseData(
            @Query("UserID") String userId,
            @Query("APIKey") String apikey,
            @Query("MobileNetwork") String network,
            @Query("DataPlan") String data,
            @Query("MobileNumber") String number,
            @Query("RequestID") String requestId,
            @Query("CallbackURL") String callbackUrl);

    @GET("/APIQueryV1.asp")
    Call<QueryTransact> queryPurchase(@Query("UserID") String userId,
                                      @Query("APIKey") String apikey,
                                      @Query("OrderID") String orderId);

    @GET("/api/validate")
    Call<VerifyPhoneNumber> verifyNumber(@Query("access_key") String accessKey,
                                         @Query("number") String number,
                                         @Query("country_code") String countyCode,
                                         @Query("format") String format);
}