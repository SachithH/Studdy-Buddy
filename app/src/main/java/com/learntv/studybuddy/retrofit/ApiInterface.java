package com.learntv.studybuddy.retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {
//    For post request
    @FormUrlEncoded
    @POST("/studybuddy/v1/authenticate/")
    Call<SignInResponse> login(
            @Field("email") String email,
            @Field("password") String password
    );
}
