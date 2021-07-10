package com.jiuhua.jiuhuacontrol;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface TDinterface {

    @POST("rest/sql/")
    Call<TDReception> getCall(@Header("Authorization") String authorization,
                              @Body RequestBody sql);
}
