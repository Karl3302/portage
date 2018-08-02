package com.zeprofile.application.utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.zeprofile.application.fragment.FragmentAbout;
import com.zeprofile.application.fragment.FragmentBankAccount;
import com.zeprofile.application.fragment.FragmentDiscount;
import com.zeprofile.application.fragment.FragmentProfile;
import com.zeprofile.application.MainPage;
//import com.zeprofile.zeprofile.test.Discount;
import com.zeprofile.application.Login;
import com.zeprofile.application.R;
import com.zeprofile.application.fragment.FragmentPublicProfile;
import com.zeprofile.application.fragment.PreferenceFragmentUserSettings;
import com.zeprofile.application.fragment.PreferenceFragmentVisibility;

public class ZeProfileUtils {
    private static Toast toast;
    private static String currentFragmentName;
    /*
     * Les trois toast sont distinquer par leur position (Gravity.BOTTOM/TOP/CENTER)
     * En cas de duplicate (ex: en ajouter un autre toast en top), le cas "else" va créer des problèmes
     * Car les deux utilisent "toast.setText()", l'autre utilise "text.setText()"
     */

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
     * The top toast is used for showing the user's failed attempt
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
     * Get the STRING data transferred from the last activity
     */
    public static String getStringFromLastActivity(@NonNull Activity activityActual, String dataName) {
        return activityActual.getIntent().getStringExtra(dataName);
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
     * Loading newFragmentClass for the FrameLayout of the main page
     *
     * @param activity        The Activity "Main Page"
     * @param newFragmentName The name of newFragment to load
     *                        Could be:
     *                        - Fragment
     *                        - PreferenceFragment
     */
    public static void loadMainFrame(@NonNull Activity activity, @NonNull String newFragmentName) {
        ActionBar mActionBar = (ActionBar) ((AppCompatActivity) activity).getSupportActionBar();
        TextView mTextView = activity.findViewById(R.id.mainPageToolbar).findViewById(R.id.mainPageTitleToolbar);
        /*Reload the main frame when:
         - the container(mainPageFrameLayout) is empty
         - the newFragment is not in the container*/
        if ((currentFragmentName == null) || !(currentFragmentName.equals(newFragmentName)) || (activity.getFragmentManager().findFragmentById(R.id.mainPageFrameLayout) == null)) {
            Fragment currentFragment = activity.getFragmentManager().findFragmentByTag(currentFragmentName);
            FragmentTransaction fragmentTransaction = activity.getFragmentManager().beginTransaction();
            /*Set animation
            - New Fragment is "Fragment Profile"  → slide in from left
            - New Fragment is others              → slide in from right*/
            if (newFragmentName.equals(FragmentProfile.class.getSimpleName())) {
                fragmentTransaction.setCustomAnimations(R.animator.animator_slide_in_left, R.animator.animator_slide_out_right);
            } else {
                fragmentTransaction.setCustomAnimations(R.animator.animator_slide_in_right, R.animator.animator_slide_out_left);
            }
            // Load mainFrame
            if ((currentFragmentName == null)||((activity.getFragmentManager().findFragmentById(R.id.mainPageFrameLayout) == null))) { // If currentFragment/container is null -> load fragmentProfile
                Log.d("--- LoadFragment ---", "container is empty, creating the profile fragment");
                fragmentTransaction.replace(R.id.mainPageFrameLayout, new FragmentProfile(), newFragmentName);
            } else {
                Log.d("--- LoadFragment ---", "container is not empty, old fragment=" + currentFragmentName);
                if (activity.getFragmentManager().findFragmentByTag(newFragmentName) == null) { // If the new fragment was not added
                    // Create the instance of the new fragment
                    Fragment newFragment;
                    switch (newFragmentName) {
                        case "FragmentProfile":
                            newFragment = new FragmentProfile();
                            break;
                        case "FragmentDiscount":
                            newFragment = new FragmentDiscount();
                            break;
                        case "FragmentPublicProfile":
                            newFragment = new FragmentPublicProfile();
                            break;
                        case "PreferenceFragmentVisibility":
                            newFragment = new PreferenceFragmentVisibility();
                            break;
                        case "FragmentBankAccount":
                            newFragment = new FragmentBankAccount();
                            break;
                        case "PreferenceFragmentUserSettings":
                            newFragment = new PreferenceFragmentUserSettings();
                            break;
                        case "FragmentAbout":
                            newFragment = new FragmentAbout();
                            break;
                        default:
                            newFragment = new FragmentProfile();
                            ZeProfileUtils.shortBottomToast(activity.getBaseContext(), "[Error] ZeProfileUtils.loadMainFrame: " + newFragmentName + " not found");
                            break;
                    }
                    Log.d("--- LoadFragment ---", "fragmentInstance is not exist, new fragment = " + newFragment);
                    fragmentTransaction.hide(currentFragment).add(R.id.mainPageFrameLayout, newFragment, newFragmentName); // hide current fragment, add new fragment
                } else {
                    Log.d("--- LoadFragment ---", "fragmentInstance already exist, new fragment = " + activity.getFragmentManager().findFragmentByTag(newFragmentName));
                    fragmentTransaction.hide(currentFragment).show(activity.getFragmentManager().findFragmentByTag(newFragmentName)); // hide current fragment, show the new fragment
                }
            }
            fragmentTransaction.commit();
            currentFragmentName = newFragmentName;
        } else {
            Log.d("--- LoadFragment ---", "[ZeProfileUtils] currentFragmentName==newFragmentName)");
        }
        // Set activity title
        String mTitle;
        switch (currentFragmentName) {
            case "FragmentProfile":
                mTitle = activity.getBaseContext().getResources().getString(R.string.title_fragment_profile);
                break;
            case "FragmentDiscount":
                mTitle = activity.getBaseContext().getResources().getString(R.string.title_fragment_discount);
                break;
            case "FragmentPublicProfile":
                mTitle = activity.getBaseContext().getResources().getString(R.string.title_fragment_public_profile);
                break;
            case "PreferenceFragmentVisibility":
                mTitle = activity.getBaseContext().getResources().getString(R.string.title_fragment_visibility);
                break;
            case "FragmentBankAccount":
                mTitle = activity.getBaseContext().getResources().getString(R.string.title_fragment_bank_account);
                break;
            case "PreferenceFragmentUserSettings":
                mTitle = activity.getBaseContext().getResources().getString(R.string.title_fragment_user_settings);
                break;
            case "FragmentAbout":
                mTitle = activity.getBaseContext().getResources().getString(R.string.title_fragment_about);
                break;
            default:
                mTitle = "[E]ZeProfileUtils: Title_not_found";
                break;
        }
        mTextView.setText(mTitle);
        // Set back button (depends on the newFragmentClass)
        // Fragments "Discount" and "Profile" don't need a back button.
        if (currentFragmentName.equals(FragmentDiscount.class.getSimpleName()) || currentFragmentName.equals(FragmentProfile.class.getSimpleName())) {
            if (mActionBar != null) mActionBar.setDisplayHomeAsUpEnabled(false);
        } else {
            if (mActionBar != null) mActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    public static String getCurrentFragmentName() {
        return currentFragmentName;
    }


    /**
     * Get the current displayed fragment in mainFrame of MainMenu
     *
     * @param fragmentManager
     * @return currentFragment
     */
//    public static Fragment getActiveFragment(FragmentManager fragmentManager) {
//        if (fragmentManager.getBackStackEntryCount() == 0) {
//            return null;
//        }
//        String tag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount()-1).getName();
//        return fragmentManager.findFragmentByTag(tag);
//    }
}
