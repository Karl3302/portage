package com.zeprofile.zeprofile.utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.zeprofile.zeprofile.fragment.FragmentDiscount;
import com.zeprofile.zeprofile.fragment.FragmentProfile;
import com.zeprofile.zeprofile.MainPage;
//import com.zeprofile.zeprofile.test.Discount;
import com.zeprofile.zeprofile.Login;
import com.zeprofile.zeprofile.R;


public class ZeProfileUtils {
    /**
     * Les trois toast sont distinquer par leur position (Gravity.BOTTOM/TOP/CENTER)
     * En cas de duplicate (ex: en ajouter un autre toast en top), le cas "else" va créer des problèmes
     * Car les deux utilisent "toast.setText()", l'autre utilise "text.setText()"
     */
    private static Toast toast;


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
        } else {
            toast.setText(content);
        }
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    /**
     * Loading fragment for the FrameLayout of the main page
     *
     * @param activity The Activity "Main Page"
     * @param fragment The fragment to load
     *                 Could be:
     *                 - Fragment
     *                 - PreferenceFragment
     */
    public static void loadMainFrame(Activity activity, Fragment fragment) {
        ActionBar mActionBar = (ActionBar) ((AppCompatActivity) activity).getSupportActionBar();
        TextView mTextView = activity.findViewById(R.id.mainPageToolbar).findViewById(R.id.mainPageTitleToolbar);
        FragmentTransaction fragmentTransaction = activity.getFragmentManager().beginTransaction();

        // Set title
        String mTitle;
        switch (fragment.getClass().getSimpleName()) {
            case "FragmentProfile": mTitle=activity.getBaseContext().getResources().getString(R.string.title_fragment_profile);break;
            case "FragmentDiscount": mTitle=activity.getBaseContext().getResources().getString(R.string.title_fragment_discount);break;
            case "FragmentPublicProfile": mTitle=activity.getBaseContext().getResources().getString(R.string.title_fragment_public_profile);break;
            case "PreferenceFragmentVisibility": mTitle=activity.getBaseContext().getResources().getString(R.string.title_fragment_visibility);break;
            case "FragmentBankAccount": mTitle=activity.getBaseContext().getResources().getString(R.string.title_fragment_bank_account);break;
            case "PreferenceFragmentUserSettings": mTitle=activity.getBaseContext().getResources().getString(R.string.title_fragment_user_settings);break;
            case "FragmentAbout": mTitle=activity.getBaseContext().getResources().getString(R.string.title_fragment_about);break;
            default:mTitle="[E]ZeProfileUtils: Title_not_found";break;
        }
        mTextView.setText(mTitle);

        // Set back button
        if (fragment instanceof FragmentDiscount || fragment instanceof FragmentProfile) {
            if (mActionBar != null) mActionBar.setDisplayHomeAsUpEnabled(false);
        } else {
            if (mActionBar != null) mActionBar.setDisplayHomeAsUpEnabled(true);
        }

        /*
         * Set animation
         * Plan A - New fragment slides in the screen from the left side, current fragment slides out by the right side
         * Plan B - New fragment slides in the screen from the right side, current fragment slides out by the left side
         */
        if (fragment instanceof FragmentProfile) { // New fragment is "Fragment Profile" - Plan A
            fragmentTransaction.setCustomAnimations(R.animator.animator_slide_in_left, R.animator.animator_slide_out_right);
        } else { // New fragment is not "Fragment Profile" - Plan B
            fragmentTransaction.setCustomAnimations(R.animator.animator_slide_in_right, R.animator.animator_slide_out_left);
        }

        //TODO 每次加载fragment都new一个会不会产生问题
        fragmentTransaction.replace(R.id.mainPageFrameLayout, fragment).commit();

        /*
        switch (viewContainer) {
            case R.id.mainMenuViewPager:
                // Hide the home button of the toolbar
                if (mActionBar != null) mActionBar.setDisplayHomeAsUpEnabled(false);
                // Load the page
                mMainMenuVP.setCurrentItem(page);
                // Load the sous-page & Set the title
                switch (page) {
                    case 0:
                        if (mProfileRootVP != null) mProfileRootVP.setCurrentItem(0, false);
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
                if (mActionBar != null) mActionBar.setDisplayHomeAsUpEnabled(true);
                // Load the page
                mProfileRootVP.setCurrentItem(page, false);
                // Set the title
                switch (page) {
                    case 1:
                        mTextView.setText(R.string.title_manage_public_profile);
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
                        mTextView.setText(R.string.title_manage_about);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }*/
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
        // If next activity is Login / MainPage -> clear others tasks and retake the original instance (same behavior as single task)
        if (activityNext.getName().equals(Login.class.getName()) | activityNext.getName().equals(MainPage.class.getName()))
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
