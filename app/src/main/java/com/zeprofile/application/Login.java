/*
 * Created by Hang on 18-4-12 下午3:19
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 18-4-12 下午1:51
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

public class Login extends AppCompatActivity {

    private DatabaseHelper db;
    private String email;
    // Announce of the elements on this page
    private EditText emailTextField, passwordTextField;
    private Button signupBtn, loginBtn, forgetPasswordBtn;

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
        setContentView(R.layout.activity_login);

        initView();
        initData();
        configView();
    }

    public void initView() {
        db = new DatabaseHelper(this);
        emailTextField = (EditText) findViewById(R.id.emailTextField);
        passwordTextField = (EditText) findViewById(R.id.passwordTextField);
        signupBtn = (Button) findViewById(R.id.signUpBtn);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        forgetPasswordBtn = (Button) findViewById(R.id.forgetPasswordBtn);
    }

    public void initData() {
        // Get the userEmail transferred by the last activity
        email = ZeProfileUtils.getStringFromLastActivity(this, "emailAddress");
    }

    public void configView() {
        if (DatabaseHelper.isValidEmail(email)) emailTextField.setText(email);
        // Set up click event for login button
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailTextField.getText().toString();
                String password = passwordTextField.getText().toString();
                // Make sure the text fields are filled
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    ZeProfileUtils.shortTopToastBar(getApplicationContext(), "Veuillez compléter tous les champs");
                } else {
                    if (DatabaseHelper.isValidEmail(email)) {
                        LoginRequest loginRequest = new LoginRequest(email,password);
                        Retrofit retrofit = RetrofitBuilder.getRetrofit();
                        ApiZeprofile netService = retrofit.create(ApiZeprofile.class);
                        Call<LoginResponse> call = netService.login(loginRequest);
                        call.enqueue(new Callback<LoginResponse>() {
                            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                                if(response.isSuccessful()) {
                                    //Log.d("--- Network ---", "[API return] status code = "+response.code()+"\n message = "+response.message()+"\n token = "+response.body().getToken());
                                    // Clear password
                                    passwordTextField.setText(null);
                                    // Move to next activity: mon profile and transfer the email
                                    ZeProfileUtils.moveToNextActivity(Login.this, MainPage.class,"token", response.body().getToken()); //"emailAddress", email);
                                }
                                else{
                                    //Log.d("--- Network ---", "[API return] status code = "+ response.code()+"\n message = "+response.message()+"\n raw = "+response.raw());
                                    ZeProfileUtils.shortCenterToast(getApplicationContext(), "Email ou mot de passe n'est pas reconnu par le système");
                                }
                            }
                            public void onFailure(Call<LoginResponse> call, Throwable t) {
                                ZeProfileUtils.shortCenterToast(getApplicationContext(), getString(R.string.error_network));
                            }
                        });
                        /*早期的本地数据库版
                        if (db.checkPasswordForLogin(email, password)) {
                            ZeProfileUtils.shortCenterToast(getApplicationContext(), "Se connecter avec succès");
                            // Clear password
                            passwordTextField.setText(null);
                            // Move to next activity: mon profile and transfer the email
                            ZeProfileUtils.moveToNextActivity(Login.this, MainPage.class, "emailAddress", email);
                        }
                        else {
                            ZeProfileUtils.shortTopToastBar(getApplicationContext(), "Email ou mot de passe n'est pas reconnu par le système");
                        }*/
                    } else {
                        ZeProfileUtils.shortTopToastBar(getApplicationContext(), "Veuillez saisir une adresse e-mail valide");
                    }
                }
            }
        });

        // Set up click event for sign up button
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear password
                passwordTextField.setText(null);
                ZeProfileUtils.moveToNextActivity(Login.this, SignUp.class, "emailAddress", emailTextField.getText().toString());
            }
        });

        // Set up click event for reset password button
        forgetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear password
                passwordTextField.setText(null);
                ZeProfileUtils.moveToNextActivity(Login.this, SendResetEmail.class, "emailAddress", emailTextField.getText().toString());
            }
        });
    }

    public void setEmailTextField(String emailText){
        emailTextField.setText(emailText);
    }

    public class LoginResponse {
        private String token;
        public String getToken() {
            return token;
        }
    }

    public class LoginRequest {
        private String username;
        private String password;
        public LoginRequest(String username, String password){
            this.username=username;
            this.password=password;
        }
    }
}
