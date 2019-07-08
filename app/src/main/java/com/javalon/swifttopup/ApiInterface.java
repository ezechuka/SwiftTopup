package com.javalon.swifttopup;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiInterface {

    @GET
    Call<PurchaseTransact> purchase(@Url String purchaseUrl);

    @GET
    Call<QueryTransact> queryPurchase(@Url String queryUrl);

    @GET
    Call<VerifyPhoneNumber> verifyNumber(@Url String phoneUrl);
}