package com.zeprofile.zeprofile.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.SwitchPreference;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
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

public class PreferenceFragmentVisibility extends PreferenceFragment {
    private SwitchPreference mLocalizationContinuousSwitchPreference, mLocalizationAddressSwitchPreference;
    private ListPreference mDurationListPreference, mDestinationListPreference;
    private static DatabaseHelper mDataBaseHelper;
    private static String email;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_user_visibility);
        setHasOptionsMenu(true);

        initViews();
        initData();
        configViews();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        CustomRelativeLayout customRelativeLayout = new CustomRelativeLayout(getActivity());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        customRelativeLayout.setLayoutParams(layoutParams);
        customRelativeLayout.addView(view);
        return customRelativeLayout;
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
                // Log.d("====stringV====","index="+index+"  email="+email);
                boolean res;
                switch (preference.getKey()) {
                    case "user_visibility_limited_duration":
                        res = mDataBaseHelper.updateUserInfo(email, "duration", stringValue);
                        break;
                    case "user_visibility_destination":
                        res = mDataBaseHelper.updateUserInfo(email, "recipient", stringValue);
                        break;
                    default:
                        res = false;
                }
            } else if (preference instanceof SwitchPreference) {
                //Log.d("====SwitchPref====", "stringV=" + stringValue);
                boolean res;
                switch (preference.getKey()) {
                    case "user_visibility_localization_continuous":
                        res = mDataBaseHelper.updateUserInfo(email, "locationContinous", stringValue);
                        break;
                    case "user_visibility_localization_address":
                        res = mDataBaseHelper.updateUserInfo(email, "locationAddress", stringValue);
                        break;
                    default:
                        res = false;
                }
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
            }
            return true;
        }
    };

    public void initViews() {
        mDataBaseHelper = new DatabaseHelper(getActivity());
        mLocalizationContinuousSwitchPreference = (SwitchPreference) findPreference(getString(R.string.key_preference_localization_continuous));
        mLocalizationAddressSwitchPreference = (SwitchPreference) findPreference(getString(R.string.key_preference_localization_address));
        mDurationListPreference = (ListPreference) findPreference(getString(R.string.key_preference_duration));
        mDestinationListPreference = (ListPreference) findPreference(getString(R.string.key_preference_destination));
    }

    public void initData() {
        email = ZeProfileUtils.getStringFromLastActivity(getActivity(), "emailAddress");
    }

    public void configViews() {
        if ((mDataBaseHelper.getUserInfo(email, "locationContinous") != null) && (mDataBaseHelper.getUserInfo(email, "locationContinous") == "true"))
            mLocalizationContinuousSwitchPreference.setChecked(true);
        else mLocalizationContinuousSwitchPreference.setChecked(false);

        if ((mDataBaseHelper.getUserInfo(email, "locationAddress") != null) && (mDataBaseHelper.getUserInfo(email, "locationAddress") == "true"))
            mLocalizationAddressSwitchPreference.setChecked(true);
        else mLocalizationAddressSwitchPreference.setChecked(false);

        mDurationListPreference.setValue(mDataBaseHelper.getUserInfo(email, "duration"));
        mDestinationListPreference.setValue(mDataBaseHelper.getUserInfo(email, "recipient"));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_preference_localization_continuous)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_preference_localization_address)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_preference_duration)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_preference_destination)));
    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        if (preference instanceof CheckBoxPreference || preference instanceof SwitchPreference) {
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getBoolean(preference.getKey(), false));
        } else {
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), ""));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            ZeProfileUtils.loadMainFrame(getActivity(), new FragmentProfile());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}