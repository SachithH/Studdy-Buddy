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
    Call<SignUpResponse> register(
            @Field("email") String email,
            @Field("password") String password,
            @Field("username") String username,
            @Field("contact") String contact
    );

    @FormUrlEncoded
    @POST("/studybuddy/v1/authenticate/")
    Call<SignInResponse> login(
            @Field("email") String email,
            @Field("password") String password
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
