package com.learntv.studybuddy.retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {
//    For post request
    @FormUrlEncoded
    @POST("/studybuddy/v1/register/")
    Call<SignInResponse> register(
            @Field("email") String email,
            @Field("password") String password,
            @Field("password") String username,
            @Field("contact") int contact
    );

    @FormUrlEncoded
    @POST("/studybuddy/v1/authenticate/")
    Call<SignInResponse> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("/studybuddy/v1/session/")
    Call<SignInResponse> session(
            @Field("token") String token
    );

    @FormUrlEncoded
    @POST("/studybuddy/v1/vod/grade-06/Syllabus-2016/sinhala/")
    Call<VODResponse> grade6_2011_sinhalaList(
            @Field("token") String token);
}
