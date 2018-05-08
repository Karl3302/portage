/*
 * Created by Hang on 18-4-12 下午3:19
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 18-4-12 下午3:14
 */

package com.zeprofile.zeprofile;

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

import com.zeprofile.zeprofile.utils.DatabaseHelper;
import com.zeprofile.zeprofile.utils.SendEmail;
import com.zeprofile.zeprofile.utils.ZeProfileUtils;

import java.util.Random;


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
                String email = emailTextField.getText().toString();
                // Make sure the text fields are filled
                if (TextUtils.isEmpty(email)) {
                    ZeProfileUtils.shortTopToastBar(getApplicationContext(), "Veuillez saisir votre email");
                } else {
                    Boolean isValidEmail = DatabaseHelper.isValidEmail(email);
                    Boolean isUsedEmail = db.isUsedEmail(email);
                    if (isUsedEmail && isValidEmail) {
                        // Require the server to send email of reset (final version) ******************** à faire

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
                    }
                }
            }
        });
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
