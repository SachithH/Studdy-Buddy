package com.learntv.studybuddy.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {
//    For post request
    @FormUrlEncoded
    @POST("/studybuddy/v1/register/")
    Call<SignUpResponse> registerEmail(
            @Field("email") String email,
            @Field("password") String password,
            @Field("username") String username,
            @Field("api_key") String apiKey,
            @Field("api_secret") String apiSecret
    );

    @FormUrlEncoded
    @POST("/studybuddy/v1/register/")
    Call<SignUpResponse> registerMobile(
            @Field("password") String password,
            @Field("username") String username,
            @Field("contact") String contact,
            @Field("api_key") String apiKey,
            @Field("api_secret") String apiSecret
    );

    @FormUrlEncoded
    @POST("/studybuddy/v1/register/")
    Call<SignUpResponse> registerEmailAndMobile(
            @Field("email") String email,
            @Field("password") String password,
            @Field("username") String username,
            @Field("contact") String contact,
            @Field("api_key") String apiKey,
            @Field("api_secret") String apiSecret
    );

    @FormUrlEncoded
    @POST("/studybuddy/v1/mobile/otp/send/")
    Call<CommonResponse> otpRequest(
            @Field("api_key") String apiKey,
            @Field("api_secret") String apiSecret,
            @Field("mobile") String mobile
    );

    @FormUrlEncoded
    @POST("/studybuddy/v1/mobile/otp/verify/")
    Call<CommonResponse> otpVerify(
            @Field("api_key") String apiKey,
            @Field("api_secret") String apiSecret,
            @Field("mobile") String mobile,
            @Field("otp") String otp
    );


    @FormUrlEncoded
    @POST("/studybuddy/v1/googleoauth/tokensignin")
    Call<SignIn> googleApi(
        @Field("client_id") String clientId,
        @Field("id_token") String googleToken
    );

    @FormUrlEncoded
    @POST("/studybuddy/v1/authenticate/email/")
    Call<SignIn> login(
            @Field("email") String email,
            @Field("password") String password,
            @Field("api_key") String api_key,
            @Field("api_secret") String api_secret
    );



    @FormUrlEncoded
    @POST("/studybuddy/v1/session")
    Call<ValidateToken> auhtenticate(
            @Field("token") String token
    );

    @FormUrlEncoded
    @POST("/studybuddy/v1/vod/{grades}/{syllabus}/{subject}/")
    Call<List<VODResponse>> video_list(
            @Field("token") String token,
            @Path("grades") String grades,
            @Path("syllabus") String syllabus,
            @Path("subject") String subject);
}
