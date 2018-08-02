package com.zeprofile.application.utils;

import android.util.Log;

import com.zeprofile.application.Login;
import com.zeprofile.application.ResetPassword;
import com.zeprofile.application.SendResetEmail;
import com.zeprofile.application.SignUp;
import com.zeprofile.application.fragment.FragmentDiscount;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiZeprofile {

    @POST("/api/login")
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Call<Login.LoginResponse> login(@Body Login.LoginRequest loginRequest);

    @POST("/api/users/me/newpasswordrequest")
    @Headers({"Content-Type: application/json"})
    Call<SendResetEmail.SendEmailResponse> sendResetEmail(@Body SendResetEmail.SendEmailRequest sendEmailRequest);

    @POST("/api/users/me/newpasswordreset")
    @Headers({"Content-Type: application/json"})
    Call<ResetPassword.ResetPasswordResponse> resetPassword(@Body ResetPassword.ResetPasswordRequest resetPasswordRequest);

    @POST("/api/users")
    @Headers({"Content-Type: application/json"})
    Call<SignUp.SignUpResponse> signUp(@Body SignUp.SignUpRequest signUpRequest);
}
