/*
 * Created by Hang on 18-4-12 下午3:19
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 18-4-12 下午3:14
 */

package com.zeprofile.application;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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


public class SendResetEmail extends AppCompatActivity {

    private DatabaseHelper db;
    private String email;
    private EditText emailTextField;
    private Button sendCodeBtn;
    private String code;

    // Don't do anything when the orientation of the device is changed
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
        setContentView(R.layout.activity_send_reset_email);

        initView();
        initData();
        configView();
    }

    public void initView() {
        db = new DatabaseHelper(this);
        emailTextField = (EditText) findViewById(R.id.emailTextField);
        sendCodeBtn = (Button) findViewById(R.id.sendCodeBtn);

        // Adjust the dialog
        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.7); // 高度设置为屏幕的70%
        p.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度设置为屏幕的100%
        dialogWindow.setAttributes(p);
    }

    public void initData() {
        // Get the userEmail transferred by the last activity
        email = ZeProfileUtils.getStringFromLastActivity(this, "emailAddress");
    }

    public void configView() {
        if(DatabaseHelper.isValidEmail(email)) emailTextField.setText(email);
        // Set up click event for sendCode button
        sendCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailTextField.getText().toString();
                // Make sure the text fields are filled
                if (TextUtils.isEmpty(email)) {
                    ZeProfileUtils.shortTopToastBar(getApplicationContext(), "Veuillez saisir votre email");
                } else {
                    if(DatabaseHelper.isValidEmail(email)) {
                        SendEmailRequest sendEmailRequest = new SendEmailRequest(email);
                        Retrofit retrofit = RetrofitBuilder.getRetrofit();
                        ApiZeprofile netService = retrofit.create(ApiZeprofile.class);
                        Call<SendEmailResponse> call = netService.sendResetEmail(sendEmailRequest);
                        call.enqueue(new Callback<SendEmailResponse>() {
                            public void onResponse(Call<SendEmailResponse> call, Response<SendEmailResponse> response) {
                                if (response.isSuccessful()) {
                                    //Log.d("--- Network ---", "[API return] status code = " + response.code() + "\n message = " + response.message() + "\n raw = " + response.raw() + "\n hydramember=" + response.body().getMember()+"\n resp="+response.toString());
                                    ZeProfileUtils.shortCenterToast(getApplicationContext(), "Un e-mail a été envoyé avec un code de récupération");
                                    // Destroy this activity
                                    SendResetEmail.this.finish();
                                    // Start next activity: ResetPassword and transfer the email address
                                    ZeProfileUtils.moveToNextActivity(SendResetEmail.this, ResetPassword.class, "emailAddress", email);
                                } else {
                                    //Log.d("--- Network ---", "[API return] status code = " + response.code() + "\n message = " + response.message() + "\n raw = " + response.raw());
                                    ZeProfileUtils.shortCenterToast(getApplicationContext(), "Email n'est pas reconnu par le système");
                                }
                            }
                            public void onFailure(Call<SendEmailResponse> call, Throwable t) {
                                ZeProfileUtils.shortCenterToast(getApplicationContext(), getString(R.string.error_network));
                            }
                        });
                    }else {
                        ZeProfileUtils.shortCenterToast(getApplicationContext(), "Veuillez saisir une adresse e-mail valide");
                    }

                    /*早期版本,使用本地数据库
                    Boolean isValidEmail = DatabaseHelper.isValidEmail(email);
                    Boolean isUsedEmail = db.isUsedEmail(email);
                    if (isUsedEmail && isValidEmail) {

                        // Send email of reset from the app (demo version)
                        // Generate a random code (entre 100000 - 999999)
                        code = (new Random().nextInt(899999) + 100000) + "";
                        // Send email * just for test/demo
                        //  Creating SendMail object
                        SendEmail sEmail = new SendEmail(SendResetEmail.this, email, "Ze Profile - Code de réinitialisation", "Votre code de réinitialisation est " + code);
                        //  Executing sendmail to send email
                        sEmail.execute();
                        // Save the code in database * just for test/demo
                        if (!db.saveResetCode(email, code)) {
                            ZeProfileUtils.shortBottomToast(getApplicationContext(), "[Error]: insertResetCode return false");
                        } else {
                            ZeProfileUtils.shortCenterToast(getApplicationContext(), "Le code de réinitialisation est envoyé par email");

                            // Destroy this activity
                            SendResetEmail.this.finish();

                            // Start next activity: ResetPassword and transfer the email address
                            ZeProfileUtils.moveToNextActivity(SendResetEmail.this, ResetPassword.class, "emailAddress", email);
                        }
                    } else if (!isValidEmail) {
                        ZeProfileUtils.shortTopToastBar(getApplicationContext(), "Email non valide");
                    } else if (!isUsedEmail) {
                        ZeProfileUtils.shortTopToastBar(getApplicationContext(), "Email n'est pas reconnu par le système");
                    }*/
                }
            }
        });
    }
    //TODO: 无法取得后端返回的信息"Un e-mail a été envoyé avec un code de récupération"
    public class SendEmailResponse {
        private String hydramember;
        public String getMember() {
            return hydramember;
        }
    }

    public class SendEmailRequest {
        private String email;
        public SendEmailRequest(String email){
            this.email=email;
        }
    }
/*
    @Override
    public boolean onTouchEvent(MotionEvent event){

        int action = MotionEventCompat.getActionMasked(event);

        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
                //Log.d(DEBUG_TAG,"Action was DOWN");
                return true;
            case (MotionEvent.ACTION_MOVE) :
                //Log.d(DEBUG_TAG,"Action was MOVE");
                return true;
            case (MotionEvent.ACTION_UP) :
                //Log.d(DEBUG_TAG,"Action was UP");
                return true;
            case (MotionEvent.ACTION_CANCEL) :
                //Log.d(DEBUG_TAG,"Action was CANCEL");
                return true;
            case (MotionEvent.ACTION_OUTSIDE) :
                //Log.d(DEBUG_TAG,"Movement occurred outside bounds " + "of current screen element");
                return true;
            default :
                return super.onTouchEvent(event);
        }
    }*/
}
