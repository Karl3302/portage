package com.zeprofile.zeprofile.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zeprofile.zeprofile.Test.Discount;
import com.zeprofile.zeprofile.Login;
import com.zeprofile.zeprofile.MainMenu;
import com.zeprofile.zeprofile.R;


public class ZeProfileUtils {
    /**
     * Les trois toast sont distinquer par leur position (Gravity.BOTTOM/TOP/CENTER)
     * En cas de duplicate (ex: en ajouter un autre toast en top), le cas "else" va créer des problèmes
     * Car les deux utilisent "toast.setText()", l'autre utilise "text.setText()"
     */
    private static Toast toast;
    private static TextView text;

    /**
     * The bottom toast is used for showing the error of the application
     * example: "[Error]: Failed reset password. DatabaseHelper.updatePassword return false"
     */
    public static void shortBottomToast(Context context, String content) {
        if (toast == null || toast.getGravity() != Gravity.BOTTOM) {
            toast = Toast.makeText(context,
                    content,
                    Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    /**
     * The center toast is used for showing the successful attempt
     * example: "Se connecter avec succès"
     */
    public static void shortCenterToast(Context context, String content) {
        if (toast == null || toast.getGravity() != Gravity.CENTER) {
            toast = Toast.makeText(context,
                    content,
                    Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * The top toast is used for showing the failed attempt
     * example: "Email n'est pas valide"
     */
    public static void shortTopToastBar(Context context, String content) {

        if (toast == null || toast.getGravity() != Gravity.TOP) {
            toast = Toast.makeText(context,
                    content,
                    Toast.LENGTH_SHORT);
            View layout = LayoutInflater.from(context).inflate(R.layout.toast_custome, null);
            text = (TextView) layout.findViewById(R.id.text);
            toast.setView(layout);
            text.setText(content);
            text.setTextColor(0xFFFFFFFF);
            text.setTextSize(16);
        } else {
            text.setText(content);
        }
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    /**
     * Loading fragment for MainMenuViewPager

     public static void loadMainMenuViewPager(Activity activity, int page) {
     ActionBar mActionBar = (ActionBar) ((AppCompatActivity) activity).getSupportActionBar();
     TextView mTextView = activity.findViewById(R.id.toolbarMainMenu).findViewById(R.id.titleToolbarMainMenu);
     ViewPager mMainMenuVP = activity.findViewById(R.id.mainMenuViewPager);

     // Hide the home button of the toolbar
     mActionBar.setDisplayHomeAsUpEnabled(false);

     switch (page) {
     case 0:
     mMainMenuVP.setCurrentItem(0);
     //mProfileRootVP.setCurrentItem(0,false);
     mTextView.setText(R.string.title_profile);
     break;
     case 1:
     mMainMenuVP.setCurrentItem(1);
     mTextView.setText(R.string.title_discount);
     break;
     default:
     break;
     }
     }
     */


    /**
     * Loading fragment for ProfileRootViewPager
     *
     * @param activity      activity actual
     * @param viewContainer "R.id.mainMenuViewPager"
     *                      "R.id.profileRootViewPager"
     * @param page          the fragment to show
     */
    public static void loadViewPager(Activity activity, int viewContainer, int page) {
        ActionBar mActionBar = (ActionBar) ((AppCompatActivity) activity).getSupportActionBar();
        TextView mTextView = activity.findViewById(R.id.toolbarMainMenu).findViewById(R.id.titleToolbarMainMenu);
        ViewPager mProfileRootVP = activity.findViewById(R.id.profileRootViewPager);
        ViewPager mMainMenuVP = activity.findViewById(R.id.mainMenuViewPager);
        switch (viewContainer) {
            case R.id.mainMenuViewPager:
                // Hide the home button of the toolbar
                mActionBar.setDisplayHomeAsUpEnabled(false);
                // Load the page
                mMainMenuVP.setCurrentItem(page);
                // Load the sous-page & Set the title
                switch (page) {
                    case 0:
                        if(mProfileRootVP!=null) mProfileRootVP.setCurrentItem(0, false);
                        mTextView.setText(R.string.title_profile);
                        break;
                    case 1:
                        mTextView.setText(R.string.title_discount);
                        break;
                    default:
                        break;
                }
                break;
            case R.id.profileRootViewPager:
                // Show the home button of the toolbar
                mActionBar.setDisplayHomeAsUpEnabled(true);
                // Load the page
                mProfileRootVP.setCurrentItem(page, false);
                // Set the title
                switch (page) {
                    case 1:
                        mTextView.setText(R.string.title_manage_profile_public);
                        break;
                    case 2:
                        mTextView.setText(R.string.title_manage_visibility);
                        break;
                    case 3:
                        mTextView.setText(R.string.title_manage_bank_account);
                        break;
                    case 4:
                        mTextView.setText(R.string.title_manage_settings);
                        break;
                    case 5:
                        mTextView.setText(R.string.title_manage_feedback);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    /**
     * Move to next activity and transfer STRING data (single)
     *
     * @param activityActual the actual activity
     * @param activityNext   the next activity
     * @param dataArray      dataArray[0] - indicator (name) of the data
     *                       dataArray[1] - content of the data
     *                       If dataArray.length>0 ==> there will be a data transferring
     */
    public static void moveToNextActivity(Context activityActual, Class activityNext, String... dataArray) {
        Intent i = new Intent(activityActual, activityNext);
        // If next activity is Login / Discount / MainMenu -> clear others tasks and retake the original instance (same behavior as single task)
        if (activityNext.getName().equals(Login.class.getName()) | activityNext.getName().equals(Discount.class.getName()) | activityNext.getName().equals(MainMenu.class.getName()))
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (dataArray.length > 0)
            i.putExtra(dataArray[0], dataArray[1]); // If there is a data transferring
        activityActual.startActivity(i);
    }

    /**
     * Get the STRING data transferred from the last activity
     */
    public static String getStringFromLastActivity(Activity activityActual, String dataName) {
        return activityActual.getIntent().getStringExtra(dataName);
    }
}
