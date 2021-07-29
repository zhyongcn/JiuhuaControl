package com.jiuhua.jiuhuacontrol.repository;

import com.jiuhua.jiuhuacontrol.CommandFromPhone;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface CloudServer {

    @POST("rest/sqlt/")
    Call<TDReception> respoFormTDengine(@Header("Authorization") String authorization,
                                        @Body RequestBody sql);

    @POST
    Call<String> reposForCommand(@Url String fullurl, @Body CommandFromPhone commandFromPhone);
}
