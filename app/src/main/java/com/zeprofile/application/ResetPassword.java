/*
 * Created by Hang on 18-4-12 下午3:19
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 18-4-12 下午1:54
 */

package com.zeprofile.application;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zeprofile.application.utils.RetrofitBuilder;
import com.zeprofile.application.utils.ApiZeprofile;
import com.zeprofile.application.utils.DatabaseHelper;
import com.zeprofile.application.utils.ZeProfileUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ResetPassword extends AppCompatActivity {

    private DatabaseHelper db;
    private String email;

    private EditText codeTextField, newPasswordTextField, confirmPasswordTextField;
    private Button resendCodeBtn, resetPasswordBtn;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // land do nothing is ok
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // port do nothing is ok
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        initView();
        initData();
        configView();
    }

    public void initView() {
        db = new DatabaseHelper(this);
        codeTextField = (EditText) findViewById(R.id.codeTextField);
        newPasswordTextField = (EditText) findViewById(R.id.newPasswordTextField);
        confirmPasswordTextField = (EditText) findViewById(R.id.confirmPasswordTextField);
        resendCodeBtn = (Button) findViewById(R.id.resendCodeBtn);
        resetPasswordBtn = (Button) findViewById(R.id.resetPasswordBtn);
    }

    public void initData() {
        // Get the userEmail transferred by the last activity
        email = ZeProfileUtils.getStringFromLastActivity(ResetPassword.this, "emailAddress");
    }

    public void configView() {
        // Set up click event for resetPassword button
        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = codeTextField.getText().toString();
                String newPassword = newPasswordTextField.getText().toString();
                String confirmPassword = confirmPasswordTextField.getText().toString();

                // Check if the email is transferred by the last activity
                if (TextUtils.isEmpty(email)) {
                    ZeProfileUtils.shortTopToastBar(getApplicationContext(), "Votre dernier code a expiré, veuillez renvoyer le code");
                }

                // Make sure the text fields are filled
                if (TextUtils.isEmpty(code) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
                    ZeProfileUtils.shortTopToastBar(getApplicationContext(), "Veuillez compléter tous les champs");
                } else if (newPassword.equals(confirmPassword)) {
                    // Update the new password
                    ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest(email,code,newPassword);
                    Retrofit retrofit = RetrofitBuilder.getRetrofit();
                    ApiZeprofile netService = retrofit.create(ApiZeprofile.class);
                    Call<ResetPasswordResponse> call = netService.resetPassword(resetPasswordRequest);
                    call.enqueue(new Callback<ResetPasswordResponse>() {
                        public void onResponse(Call<ResetPasswordResponse> call, Response<ResetPasswordResponse> response) {
                            if (response.isSuccessful()) {
                                //Log.d("--- Network ---", "[API return] status code = " + response.code() + "\n message = " + response.message() + "\n raw = " + response.raw() + "\n MessageFromBody=" + response.body().getMsg());
                                ZeProfileUtils.shortCenterToast(getApplicationContext(), "Votre nouveau mot de passe est bien mis en place, veuillez vous reconnecter");
                                // Clear the text field
                                codeTextField.setText(null);
                                newPasswordTextField.setText(null);
                                confirmPasswordTextField.setText(null);
                                // Start the new activity: Login
                                ZeProfileUtils.moveToNextActivity(ResetPassword.this, Login.class, "emailAddress", email);
                            } else {
                                //Log.d("--- Network ---", "[API return] status code = " + response.code() + "\n message = " + response.message() + "\n raw = " + response.raw());
                                ZeProfileUtils.shortCenterToast(getApplicationContext(), "Votre code n'est pas bon, veuillez ressayer");
                            }
                        }
                        public void onFailure(Call<ResetPasswordResponse> call, Throwable t) {
                        }
                    });

                    /*早期版本, 使用本地数据库
                    if (db.checkResetCode(email, code)) {
                        if (!db.updatePassword(email, newPassword)) {
                            ZeProfileUtils.shortBottomToast(getApplicationContext(), "[Error]: Failed reset password. DatabaseHelper.updatePassword return false");
                        } else {
                            ZeProfileUtils.shortCenterToast(getApplicationContext(), "Votre nouveau mot de passe est bien mis en place, veuillez vous reconnecter");
                            // Clear the reset code on the database * test/demo
                            if (!db.clearResetCode(email)) {
                                ZeProfileUtils.shortBottomToast(getApplicationContext(), "[Error]: Failed wipe reset code. DatabaseHelper.wipeResetCode return false");
                            } else {
                                // Clear the text field
                                codeTextField.setText(null);
                                newPasswordTextField.setText(null);
                                confirmPasswordTextField.setText(null);
                                // Start the new activity: Login
                                ZeProfileUtils.moveToNextActivity(ResetPassword.this, Login.class, "emailAddress", email);
                            }
                        }
                    } else {
                        ZeProfileUtils.shortTopToastBar(getApplicationContext(), "Votre code n'est pas bon, veuillez ressayer");
                    }*/
                } else {
                    ZeProfileUtils.shortTopToastBar(getApplicationContext(), "Les deux mot de passe saisis ne sont pas identiques, veuillez resaisir votre mot de passe");
                }
            }
        });
        // Set up click event for resendCode button
        resendCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZeProfileUtils.moveToNextActivity(ResetPassword.this, SendResetEmail.class, "emailAddress", ZeProfileUtils.getStringFromLastActivity(ResetPassword.this, "emailAddress"));
            }
        });
    }

    public class ResetPasswordResponse {
        private String email;
        public String getMsg() {
            return email;
        }
    }

    public class ResetPasswordRequest {
        private String email;
        private String token;
        private String password;
        public ResetPasswordRequest(String email, String token, String password){
            this.email=email;
            this.token=token;
            this.password=password;
        }
    }

}
