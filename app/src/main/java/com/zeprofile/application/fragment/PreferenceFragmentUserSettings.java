package com.zeprofile.application.fragment;

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

import com.zeprofile.application.R;
import com.zeprofile.application.base.CustomRelativeLayout;
import com.zeprofile.application.base.UserInfoBean;
import com.zeprofile.application.utils.ApiZeprofile;
import com.zeprofile.application.utils.DatabaseHelper;
import com.zeprofile.application.utils.RetrofitBuilder;
import com.zeprofile.application.utils.ZeProfileUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PreferenceFragmentUserSettings extends PreferenceFragment {
    private EditTextPreference mLastNameEditTextPreference, mFirstNameEditTextPreference, mEmailEditTextPreference, mHomeAddressEditTextPreference, mDeliveryAddressEditTextPreference;
    private String lastName, firstName, homeAddress, deliveryAddress;
    private static DatabaseHelper mDataBaseHelper;
    private static String token;
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
        getUserInfo();
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
                        res = mDataBaseHelper.updateUserInfo(token, "lastName", stringValue);
                        break;
                    case "user_setting_first_name":
                        res = mDataBaseHelper.updateUserInfo(token, "firstName", stringValue);
                        break;
                    case "user_setting_home_address":
                        res = mDataBaseHelper.updateUserInfo(token, "homeAddress", stringValue);
                        break;
                    case "user_setting_delivery_address":
                        res = mDataBaseHelper.updateUserInfo(token, "deliveryAddress", stringValue);
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
        token = ZeProfileUtils.getStringFromLastActivity(getActivity(), "token");
    }

    public void getUserInfo() {
//        // Show user profile saved in database
//        mEmailEditTextPreference.setText(token);
//        mEmailEditTextPreference.setEnabled(false); // Email cant be changed
//        mLastNameEditTextPreference.setText(mDataBaseHelper.getUserInfo(token, "lastName"));
//        mFirstNameEditTextPreference.setText(mDataBaseHelper.getUserInfo(token, "firstName"));
//        mHomeAddressEditTextPreference.setText(mDataBaseHelper.getUserInfo(token, "homeAddress"));
//        mDeliveryAddressEditTextPreference.setText(mDataBaseHelper.getUserInfo(token, "deliveryAddress"));
//
//        // Set listener for the preferences
//        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_preference_last_name)));
//        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_preference_first_name)));
//        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_preference_email)));
//        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_preference_home_address)));
//        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_preference_delivery_address)));
        Retrofit retrofit = RetrofitBuilder.getRetrofit();
        ApiZeprofile netService = retrofit.create(ApiZeprofile.class);
        Call<UserInfoBean> call = netService.getUserInfo("Bearer "+token);
        call.enqueue(new Callback<UserInfoBean>() {
            public void onResponse(Call<UserInfoBean> call, Response<UserInfoBean> response) {
                if (response.isSuccessful()) {
                    //Log.d("--- LicenseSetting ---", "[Network_updateLicenseSetting] status code = " + response.code()+ "\n message = " + response.message());//
                    // Get user's firstname
                    mFirstNameEditTextPreference.setText(response.body().getFirstname());
                    bindPreferenceSummaryToValue(findPreference(getString(R.string.key_preference_first_name)));
                    // Get user's lastname
                    mLastNameEditTextPreference.setText(response.body().getLastname());
                    bindPreferenceSummaryToValue(findPreference(getString(R.string.key_preference_last_name)));
                    // Get user's email
                    mEmailEditTextPreference.setText(response.body().getEmail());
                    bindPreferenceSummaryToValue(findPreference(getString(R.string.key_preference_email)));
                    // Get user's billing address
                    mHomeAddressEditTextPreference.setText(response.body().getBillingAddress());
                    bindPreferenceSummaryToValue(findPreference(getString(R.string.key_preference_home_address)));
                    // Get user's posting address
                    mDeliveryAddressEditTextPreference.setText(response.body().getPostingAddress());
                    bindPreferenceSummaryToValue(findPreference(getString(R.string.key_preference_delivery_address)));
                } else {
                    //Log.d("--- LicenseSetting ---", "[Network_updateLicenseSetting] status code = " + response.code() + "\n message = " + response.message());// + "\n raw = " + response.raw());
                    ZeProfileUtils.shortCenterToast(getActivity().getBaseContext(), "Error: server return "+response.code());
                }
            }
            public void onFailure(Call<UserInfoBean> call, Throwable t) {
                ZeProfileUtils.shortCenterToast(getActivity().getBaseContext(), getString(R.string.error_network));
            }
        });
    }

    public void updateUserInfo(){
        PreferenceFragmentUserSettingsRequest preferenceFragmentUserSettingRequest = new PreferenceFragmentUserSettingsRequest(
                mFirstNameEditTextPreference.getText(),
                mLastNameEditTextPreference.getText(),
                mEmailEditTextPreference.getText(),
                mHomeAddressEditTextPreference.getText(),"75000","Paris",
                mDeliveryAddressEditTextPreference.getText(),"75000","Paris");
        Retrofit retrofit = RetrofitBuilder.getRetrofit();
        ApiZeprofile netService = retrofit.create(ApiZeprofile.class);
        Call<UserInfoBean> call = netService.putUserInfo("Bearer "+token,preferenceFragmentUserSettingRequest);
        call.enqueue(new Callback<UserInfoBean>() {
            public void onResponse(Call<UserInfoBean> call, Response<UserInfoBean> response) {
                if (response.isSuccessful()) {
                    //Log.d("--- LicenseSetting ---", "[Network_updateLicenseSetting] status code = " + response.code()+ "\n message = " + response.message());//+ "\n raw = " + response.raw() +"\n email="+response.body().getEmail());
                } else {
                    //Log.d("--- LicenseSetting ---", "[Network_updateLicenseSetting] status code = " + response.code() + "\n message = " + response.message());// + "\n raw = " + response.raw());
                    ZeProfileUtils.shortCenterToast(getActivity().getBaseContext(), "Error: server return "+response.code());
                }
            }
            public void onFailure(Call<UserInfoBean> call, Throwable t) {
                ZeProfileUtils.shortCenterToast(getActivity().getBaseContext(), getString(R.string.error_network));
            }
        });
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
            ZeProfileUtils.loadMainFrame(getActivity(),FragmentProfile.class.getSimpleName());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class PreferenceFragmentUserSettingsRequest {
        private String firstname;
        private String lastname;
        private String email;
        private String billingAddress;
        private String billingAddressPostalCode;
        private String billingAddressCity;
        private String postingAddress;
        private String postingAddressPostalCode;
        private String postingAddressCity;
        public PreferenceFragmentUserSettingsRequest(String firstname,
                                                     String lastname,
                                                     String email,
                                                     String billingAddress,
                                                     String billingAddressPostalCode,
                                                     String billingAddressCity,
                                                     String postingAddress,
                                                     String postingAddressPostalCode,
                                                     String postingAddressCity) {
            this.firstname=firstname;
            this.lastname=lastname;
            this.email=email;
            this.billingAddress=billingAddress;
            this.billingAddressPostalCode=billingAddressPostalCode;
            this.billingAddressCity=billingAddressCity;
            this.postingAddress=postingAddress;
            this.postingAddressPostalCode=postingAddressPostalCode;
            this.postingAddressCity=postingAddressCity;
        }
    }

    // This function is called when the fragment is switching between show and hide
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {// When switching to show
            //Log.d("--- LicenseSetting ---", "[Status Info] change to show");
            getUserInfo();
        }else {// When switching to hide
            //Log.d("--- LicenseSetting ---", "[Status Info] change to hide");
            updateUserInfo();
        }
    }
}