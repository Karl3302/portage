/*
 * Created by Hang on 18-4-12 下午3:19
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 18-4-12 下午1:51
 */

package com.zeprofile.zeprofile;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zeprofile.zeprofile.Utils.DatabaseHelper;
import com.zeprofile.zeprofile.Utils.ZeProfileUtils;

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
                String email = emailTextField.getText().toString();
                String password = passwordTextField.getText().toString();
                // Make sure the text fields are filled
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    ZeProfileUtils.shortTopToastBar(getApplicationContext(), "Veuillez compléter tous les champs");
                } else {
                    if (DatabaseHelper.isValidEmail(email)) {
                        if (db.checkPasswordForLogin(email, password)) {
                            ZeProfileUtils.shortCenterToast(getApplicationContext(), "Se connecter avec succès");

                            // Clear password
                            passwordTextField.setText(null);

                            // Move to next activity: mon profile and transfer the email
                            ZeProfileUtils.moveToNextActivity(Login.this, MainMenu.class, "emailAddress", email);
                        } else {
                            ZeProfileUtils.shortTopToastBar(getApplicationContext(), "Email ou mot de passe n'est pas reconnu par le système");
                        }
                    } else {
                        ZeProfileUtils.shortTopToastBar(getApplicationContext(), "Email n'est pas valide");
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
}
