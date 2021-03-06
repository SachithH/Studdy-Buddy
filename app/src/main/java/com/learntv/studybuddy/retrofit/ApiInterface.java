package com.learntv.studybuddy.retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {
//    For post request
    @FormUrlEncoded
    @POST("/studybuddy/v1/register/")
    Call<SignUpResponse> registerMobile(
            @Field("mobileNo") String contact,
            @Field("password") String password,
            @Field("referral_code") String referral_code,
            @Field("device_id") String device_id,
            @Field("api_key") String apiKey,
            @Field("api_secret") String apiSecret
    );

    @FormUrlEncoded
    @POST("/studybuddy/v1/mobile/getOtp")
    Call<CommonResponse> otpRequest(
            @Field("api_key") String apiKey,
            @Field("api_secret") String apiSecret,
            @Field("mobileNo") String mobile
    );

    @FormUrlEncoded
    @POST("/studybuddy/v1/mobile/setOtp")
    Call<CommonResponse> otpVerify(
            @Field("api_key") String apiKey,
            @Field("api_secret") String apiSecret,
            @Field("mobileNo") String mobile,
            @Field("otp") String otp
    );

    @FormUrlEncoded
    @POST("/studybuddy/v1/authenticate/mobile")
    Call<SignIn> login(
            @Field("mobileNo") String mobileNo,
            @Field("password") String password,
            @Field("api_key") String api_key,
            @Field("api_secret") String api_secret
    );



    @FormUrlEncoded
    @POST("/studybuddy/v1/session")
    Call<CommonResponse> auhtenticate(
            @Field("token") String token
    );

    @FormUrlEncoded
    @POST("/studybuddy/v1/session")
    Call<GradesData> grades(
            @Field("api_key") String apiKey,
            @Field("api_secret") String apiSecret,
            @Field("token") String token
    );


    @FormUrlEncoded
    @POST("/studybuddy/v1/vod/getLessonList")
    Call<VODResponse> video_list(
            @Field("api_key") String apiKey,
            @Field("api_secret") String apiSecret,
            @Field("token") String token,
            @Field("grade_id") int gradeId,
            @Field("subject_id") int subjectId,
            @Field("syllabus_id") int syllabusId

    );

    @FormUrlEncoded
    @POST("/studybuddy/v1/student/learning/setFavorite")
    Call<CommonResponse> set_favourite(
            @Field("api_key") String apiKey,
            @Field("api_secret") String apiSecret,
            @Field("token") String token,
            @Field("video_id") int video_id
    );

    @FormUrlEncoded
    @POST("/studybuddy/v1/vod/getLesson/")
    Call<LessonResponse> lesson_data(
            @Field("api_key") String apiKey,
            @Field("api_secret") String apiSecret,
            @Field("token") String token,
            @Field("video_id") int videoId

    );

    @FormUrlEncoded
    @POST("/studybuddy/v1/student/learning/setAnswer/")
    Call<McqResponse> check_mcq(
            @Field("token") String token,
            @Field("api_key") String apiKey,
            @Field("api_secret") String apiSecret,
            @Field("video_id") int video_id,
            @Field("question_id") int question_id,
            @Field("option_id") int option_id,
            @Field("started_at") String started_at,
            @Field("ended_at") String ended_at

    );

    @FormUrlEncoded
    @POST("/studybuddy/v1/mobile/getRecoveryCode")
    Call<CommonResponse> request_recovery(
            @Field("mobileNo") String mobile,
            @Field("api_key") String apiKey,
            @Field("api_secret") String apiSecret
    );

    @FormUrlEncoded
    @POST("/studybuddy/v1/mobile/setRecoveryCode")
    Call<CommonResponse> send_recovery_otp(
            @Field("mobileNo") String mobile,
            @Field("recovery_code") String recoveryCode,
            @Field("api_key") String apiKey,
            @Field("api_secret") String apiSecret
    );

    @FormUrlEncoded
    @POST("/studybuddy/v1/register/setPassword")
    Call<CommonResponse> request_new_pw(
            @Field("mobileNo") String mobile,
            @Field("password") String password,
            @Field("api_key") String apiKey,
            @Field("api_secret") String apiSecret
    );
}
