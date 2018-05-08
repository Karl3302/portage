/*
 * Created by Hang on 18-4-12 下午3:19
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 18-4-12 下午1:51
 */

package com.zeprofile.zeprofile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zeprofile.zeprofile.utils.DatabaseHelper;
import com.zeprofile.zeprofile.utils.ZeProfileUtils;

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
        if (DatabaseHelper.isValidEmail(email) && (!db.isUsedEmail(email)))
            emailTextField.setText(email);
        // Set up click event for sign up button
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lastName = lastNameTextField.getText().toString();
                String firstName = firstNameTextField.getText().toString();
                String email = emailTextField.getText().toString();
                String password = passwordTextField.getText().toString();
                // Make sure the text fields are filled
                if (TextUtils.isEmpty(lastName) || TextUtils.isEmpty(firstName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    ZeProfileUtils.shortTopToastBar(getApplicationContext(), "Veuillez compléter tous les champs");
                } else {
                    Boolean isValidEmail = DatabaseHelper.isValidEmail(email);
                    Boolean isUsedEmail = db.isUsedEmail(email);
                    if (!isUsedEmail && isValidEmail) {
                        if (db.insertNewUser(email, lastName, firstName, password)) {
                            ZeProfileUtils.shortCenterToast(getApplicationContext(), "Inscription avec succès");
                           /* // clear the text field
                            lastNameTextField.setText(null);
                            firstNameTextField.setText(null);
                            emailTextField.setText(null);
                            passwordTextField.setText(null);*/
                            // Start next activity - Login and transfer the email
                            ZeProfileUtils.moveToNextActivity(SignUp.this, Login.class, "emailAddress", email);
                        }
                    } else if (!isValidEmail) {
                        ZeProfileUtils.shortTopToastBar(getApplicationContext(), "Email n'est pas valide");
                    } else if (isUsedEmail) {
                        ZeProfileUtils.shortTopToastBar(getApplicationContext(), "Email déjà utilisé");
                    }
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
}
