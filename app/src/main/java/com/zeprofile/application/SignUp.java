/*
 * Created by Hang on 18-4-12 下午3:19
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 18-4-12 下午1:51
 */

package com.zeprofile.application;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zeprofile.application.utils.ApiZeprofile;
import com.zeprofile.application.utils.DatabaseHelper;
import com.zeprofile.application.utils.RetrofitBuilder;
import com.zeprofile.application.utils.ZeProfileUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignUp extends AppCompatActivity {

    private DatabaseHelper db;
    private String email;
    private EditText lastNameTextField, firstNameTextField, emailTextField, passwordTextField;
    private Button signupBtn, loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initView();
        initData();
        configView();
    }

    public void initView() {
        db = new DatabaseHelper(this);
        lastNameTextField = (EditText) findViewById(R.id.lastNameTextField);
        firstNameTextField = (EditText) findViewById(R.id.firstNameTextField);
        emailTextField = (EditText) findViewById(R.id.emailTextField);
        passwordTextField = (EditText) findViewById(R.id.passwordTextField);
        signupBtn = (Button) findViewById(R.id.signUpBtn);
        loginBtn = (Button) findViewById(R.id.loginBtn);
    }

    public void initData() {
        // Get the userEmail transferred by the last activity
        email = ZeProfileUtils.getStringFromLastActivity(this, "emailAddress");
    }

    public void configView() {
        if (DatabaseHelper.isValidEmail(email) && (!db.isUsedEmail(email))){
            emailTextField.setText(email);
        }
        // Set up click event for sign up button
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lastName = lastNameTextField.getText().toString();
                String firstName = firstNameTextField.getText().toString();
                final String email = emailTextField.getText().toString();
                String password = passwordTextField.getText().toString();
                // Make sure the text fields are filled
                if (TextUtils.isEmpty(lastName) || TextUtils.isEmpty(firstName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    ZeProfileUtils.shortTopToastBar(getApplicationContext(), "Veuillez compléter tous les champs");
                } else {
                    if(DatabaseHelper.isValidEmail(email)){
                        SignUpRequest signUpRequest = new SignUpRequest(firstName,lastName,email,password);
                        Retrofit retrofit = RetrofitBuilder.getRetrofit();
                        ApiZeprofile netService = retrofit.create(ApiZeprofile.class);
                        Call<SignUpResponse> call = netService.signUp(signUpRequest);
                        call.enqueue(new Callback<SignUpResponse>() {
                            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                                if (response.isSuccessful()) {
                                    //Log.d("--- Network ---", "[API return] status code = " + response.code() + "\n message = " + response.message() + "\n raw = " + response.raw() + "\n MessageFromBody=" + response.body().getMsg());
                                    ZeProfileUtils.shortCenterToast(getApplicationContext(), "Inscription avec succès");
                                    // Start next activity - Login and transfer the email
                                    ZeProfileUtils.moveToNextActivity(SignUp.this, Login.class, "emailAddress", email);
                                } else {
                                    //Log.d("--- Network ---", "[API return] status code = " + response.code() + "\n message = " + response.message() + "\n raw = " + response.raw());
                                    ZeProfileUtils.shortCenterToast(getApplicationContext(), "Cette adresse e-mail est déjà inscrit sur Ze Profile");
                                }
                            }
                            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                                ZeProfileUtils.shortCenterToast(getApplicationContext(), getString(R.string.error_network));
                            }
                        });
                    }else {
                        ZeProfileUtils.shortTopToastBar(getApplicationContext(), "Veuillez saisir une adresse e-mail valide");
                    }
                    /*
                    早期版本, 使用本地数据库
                    Boolean isValidEmail = DatabaseHelper.isValidEmail(email);
                    Boolean isUsedEmail = db.isUsedEmail(email);
                    if (!isUsedEmail && isValidEmail) {
                        if (db.insertNewUser(email, lastName, firstName, password)) {
                            ZeProfileUtils.shortCenterToast(getApplicationContext(), "Inscription avec succès");
                           /* // clear the text field
                            lastNameTextField.setText(null);
                            firstNameTextField.setText(null);
                            emailTextField.setText(null);
                            passwordTextField.setText(null);*//*
                            // Start next activity - Login and transfer the email
                            ZeProfileUtils.moveToNextActivity(SignUp.this, Login.class, "emailAddress", email);
                        }
                    } else if (!isValidEmail) {
                        ZeProfileUtils.shortTopToastBar(getApplicationContext(), "Email n'est pas valide");
                    } else if (isUsedEmail) {
                        ZeProfileUtils.shortTopToastBar(getApplicationContext(), "Email déjà utilisé");
                    }*/
                }
            }
        });
        // Set up click event for login button
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZeProfileUtils.moveToNextActivity(SignUp.this, Login.class);
            }
        });
    }

    public class SignUpResponse {
        private String email;
        public String getMsg() {
            return email;
        }
    }

    public class SignUpRequest {
        private String firstname;
        private String lastname;
        private String email;
        private String password;
        public SignUpRequest(String firstname, String lastname,String email, String password){
            this.firstname=firstname;
            this.lastname=lastname;
            this.email=email;
            this.password=password;
        }
    }
}
