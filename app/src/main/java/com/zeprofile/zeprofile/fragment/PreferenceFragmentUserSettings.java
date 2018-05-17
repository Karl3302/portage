package com.zeprofile.zeprofile.fragment;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.zeprofile.zeprofile.R;
import com.zeprofile.zeprofile.base.CustomRelativeLayout;
import com.zeprofile.zeprofile.utils.DatabaseHelper;
import com.zeprofile.zeprofile.utils.ZeProfileUtils;

public class PreferenceFragmentUserSettings extends PreferenceFragment {
    private EditTextPreference mLastNameEditTextPreference, mFirstNameEditTextPreference, mEmailEditTextPreference, mHomeAddressEditTextPreference, mDeliveryAddressEditTextPreference;
    private String lastName, firstName, homeAddress, deliveryAddress;
    private static DatabaseHelper mDataBaseHelper;
    private static String email;
    // Save the created view
    private CustomRelativeLayout savedCustomRelativeLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_user_setting);
        setHasOptionsMenu(true);
        // Maintain the current instance when the screen orientation has changed
        setRetainInstance(true);

        initViews();
        initData();
        configViews();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedCustomRelativeLayout == null) {
            View mView = super.onCreateView(inflater, container, savedInstanceState);
            savedCustomRelativeLayout = new CustomRelativeLayout(getActivity());
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            savedCustomRelativeLayout.setLayoutParams(layoutParams);
            savedCustomRelativeLayout.addView(mView);
        }
        return savedCustomRelativeLayout;
    }

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary("silent");

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's simple string representation.
                preference.setSummary(stringValue);
                // Call to change the database
                boolean res;
                switch (preference.getKey()) {
                    case "user_setting_last_name":
                        res = mDataBaseHelper.updateUserInfo(email, "lastName", stringValue);
                        break;
                    case "user_setting_first_name":
                        res = mDataBaseHelper.updateUserInfo(email, "firstName", stringValue);
                        break;
                    case "user_setting_home_address":
                        res = mDataBaseHelper.updateUserInfo(email, "homeAddress", stringValue);
                        break;
                    case "user_setting_delivery_address":
                        res = mDataBaseHelper.updateUserInfo(email, "deliveryAddress", stringValue);
                        break;
                    default:
                        res = false;
                }
            }
            return true;
        }
    };

    public void initViews() {
        mDataBaseHelper = new DatabaseHelper(getActivity());
        mLastNameEditTextPreference = (EditTextPreference) findPreference(getString(R.string.key_preference_last_name));
        mFirstNameEditTextPreference = (EditTextPreference) findPreference(getString(R.string.key_preference_first_name));
        mEmailEditTextPreference = (EditTextPreference) findPreference(getString(R.string.key_preference_email));
        mHomeAddressEditTextPreference = (EditTextPreference) findPreference(getString(R.string.key_preference_home_address));
        mDeliveryAddressEditTextPreference = (EditTextPreference) findPreference(getString(R.string.key_preference_delivery_address));
    }

    public void initData() {
        email = ZeProfileUtils.getStringFromLastActivity(getActivity(), "emailAddress");
    }

    public void configViews() {
        // Show user profile saved in database
        mEmailEditTextPreference.setText(email);
        mEmailEditTextPreference.setEnabled(false); // Email cant be changed
        mLastNameEditTextPreference.setText(mDataBaseHelper.getUserInfo(email, "lastName"));
        mFirstNameEditTextPreference.setText(mDataBaseHelper.getUserInfo(email, "firstName"));
        mHomeAddressEditTextPreference.setText(mDataBaseHelper.getUserInfo(email, "homeAddress"));
        mDeliveryAddressEditTextPreference.setText(mDataBaseHelper.getUserInfo(email, "deliveryAddress"));

        // Set listener for the preferences
        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_preference_last_name)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_preference_first_name)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_preference_email)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_preference_home_address)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_preference_delivery_address)));
    }

    private static void bindPreferenceSummaryToValue(Preference preference) { // default: static
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getActivity().getFragmentManager().popBackStackImmediate();
            ZeProfileUtils.setMainFrameToolBar(getActivity());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}