package com.zeprofile.application.fragment;

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
import android.util.Log;
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

public class PreferenceFragmentLicenseSetting extends PreferenceFragment {
    //private SwitchPreference mLocalizationContinuousSwitchPreference, mLocalizationAddressSwitchPreference;
    private ListPreference mLocalizationListPreference, mDurationListPreference, mDestinationListPreference, mContentMarketingListPreference, mFrequencyMarketingListPreference;
    private static DatabaseHelper mDataBaseHelper;
    private static String token;
    // Save the created view
    private CustomRelativeLayout savedCustomRelativeLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_license_setting);
        setHasOptionsMenu(true);
        // Maintain the current instance when the screen orientation has changed
        setRetainInstance(true);

        initViews();
        initData();
        getLicenseSetting();
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
//                // Update the value to the database
//                boolean res;
//                switch (preference.getKey()) {
//                    case "license_setting_localization":
//                        Log.d("--- LicenseSetting ---", "[parameter changed] localization changed");
//                        //res = mDataBaseHelper.updateUserInfo(token, "duration", stringValue);
//                        break;
//                    case "license_setting_duration":
//                        Log.d("--- LicenseSetting ---", "[parameter changed] duration changed");
//                        //res = mDataBaseHelper.updateUserInfo(token, "duration", stringValue);
//                        break;
//                    case "license_setting_destination":
//                        //res = mDataBaseHelper.updateUserInfo(token, "recipient", stringValue);
//                        break;
//                    case "license_setting_content_marketing":
//
//                        break;
//                    case "license_setting_frequency_marketing":
//
//                        break;
//                    default:
//                        res = false;
//                }
            } else if (preference instanceof SwitchPreference) {
//                // Set the summary
//                if(mDataBaseHelper.getUserInfo(token,"locationContinous")=="true")
//                ((SwitchPreference) preference).setChecked(res);
//                // Set the value for the database
//                boolean res;
//                switch (preference.getKey()) {
//                    case "user_visibility_localization_continuous":
//                        res = mDataBaseHelper.updateUserInfo(token, "locationContinous", stringValue);
//                        break;
//                    case "user_visibility_localization_address":
//                        res = mDataBaseHelper.updateUserInfo(token, "locationAddress", stringValue);
//                        break;
//                    default:
//                        res = false;
//                }
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
        //mLocalizationContinuousSwitchPreference = (SwitchPreference) findPreference(getString(R.string.key_preference_localization_continuous));
        //mLocalizationAddressSwitchPreference = (SwitchPreference) findPreference(getString(R.string.key_preference_localization_address));
        mLocalizationListPreference = (ListPreference) findPreference(getString(R.string.key_localization_license_setting));
        mDurationListPreference = (ListPreference) findPreference(getString(R.string.key_duration_license_setting));
        mDestinationListPreference = (ListPreference) findPreference(getString(R.string.key_destination_license_setting));
        mContentMarketingListPreference = (ListPreference) findPreference(getString(R.string.key_content_marketing_license_setting));
        mFrequencyMarketingListPreference = (ListPreference) findPreference(getString(R.string.key_frequency_marketing_license_setting));
    }

    public void initData() {
        token = ZeProfileUtils.getStringFromLastActivity(getActivity(), "token");
        //Log.d("--- LicenseSetting ---", "[Data Transfer] token = "+ token);
    }

    public void getLicenseSetting() {
//        if ((mDataBaseHelper.getUserInfo(token, "locationContinous") != null) && (mDataBaseHelper.getUserInfo(token, "locationContinous").equals("true")))
//            mLocalizationContinuousSwitchPreference.setChecked(true);
//        else mLocalizationContinuousSwitchPreference.setChecked(false);
//        if ((mDataBaseHelper.getUserInfo(token, "locationAddress") != null) && (mDataBaseHelper.getUserInfo(token, "locationAddress").equals("true")))
//            mLocalizationAddressSwitchPreference.setChecked(true);
//        else mLocalizationAddressSwitchPreference.setChecked(false);
//        mDurationListPreference.setValue(mDataBaseHelper.getUserInfo(token, "duration"));
//        mDestinationListPreference.setValue(mDataBaseHelper.getUserInfo(token, "recipient"));
//        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_preference_localization_continuous)));
//        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_preference_localization_address)));
//        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_duration_license_setting)));
//        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_destination_license_setting)));
        Retrofit retrofit = RetrofitBuilder.getRetrofit();
        ApiZeprofile netService = retrofit.create(ApiZeprofile.class);
        Call<UserInfoBean> call = netService.getLicenseSetting("Bearer "+token);
        call.enqueue(new Callback<UserInfoBean>() {
            public void onResponse(Call<UserInfoBean> call, Response<UserInfoBean> response) {
                if (response.isSuccessful()) {
                    //Log.d("--- LicenseSetting ---", "[Network_updateLicenseSetting] status code = " + response.code()+ "\n message = " + response.message());//
                    // Get the preference of Localization
                    if(response.body().getContinuousLocation()) mLocalizationListPreference.setValue("0");
                    else mLocalizationListPreference.setValue("1");
                    bindPreferenceSummaryToValue(findPreference(getString(R.string.key_localization_license_setting)));
                    // Get the preference of Duration
                    if(response.body().getDurationYear()) mDurationListPreference.setValue("0");
                    else mDurationListPreference.setValue("1");
                    bindPreferenceSummaryToValue(findPreference(getString(R.string.key_duration_license_setting)));
                    // Get the preference of Destination
                    if(response.body().getReceiverOnly()) mDestinationListPreference.setValue("0");
                    else mDestinationListPreference.setValue("1");
                    bindPreferenceSummaryToValue(findPreference(getString(R.string.key_destination_license_setting)));
                    // Get the preference of Contenu Marketing
                    if(response.body().getMarketingContentAllProducts()) mContentMarketingListPreference.setValue("0");
                    else mContentMarketingListPreference.setValue("1");
                    bindPreferenceSummaryToValue(findPreference(getString(R.string.key_content_marketing_license_setting)));
                    // Get the preference of Frequency Marketing
                    if(response.body().getMarketingFrequencyOncePerMonth()) mFrequencyMarketingListPreference.setValue("0");
                    else mFrequencyMarketingListPreference.setValue("1");
                    bindPreferenceSummaryToValue(findPreference(getString(R.string.key_frequency_marketing_license_setting)));
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

    public void updateLicenseSetting(){
        PreferenceFragmentLicenseSettingRequest preferenceFragmentLicenseSettingRequest = new PreferenceFragmentLicenseSettingRequest(
                mDurationListPreference.getValue().equals("0"),
                mDestinationListPreference.getValue().equals("0"),
                mLocalizationListPreference.getValue().equals("1"),
                mLocalizationListPreference.getValue().equals("0"),mContentMarketingListPreference.getValue().equals("0"),mFrequencyMarketingListPreference.getValue().equals("0"));
        Retrofit retrofit = RetrofitBuilder.getRetrofit();
        ApiZeprofile netService = retrofit.create(ApiZeprofile.class);
        Call<UserInfoBean> call = netService.putLicenseSetting("Bearer "+token,preferenceFragmentLicenseSettingRequest);
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

    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set listeners to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
        // Set listener for CheckBoxPreferences or SwitchPreference
        if (preference instanceof CheckBoxPreference || preference instanceof SwitchPreference) {
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getBoolean(preference.getKey(), false));
        } else {// Set listener for others
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
            ZeProfileUtils.loadMainFrame(getActivity(), FragmentProfile.class.getSimpleName());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class PreferenceFragmentLicenseSettingRequest {
        private Boolean durationYear;                   //Durée: true - 1 an; false - 2 ans
        private Boolean receiverOnly;                   //Destinataire: true - Destinataire uniquement; false - Destinataire et ses partenaires
        private Boolean communicateAddress;             //Localisation: true - Résidence
        private Boolean continuousLocation;             //Localisation: true - Géolocalisation
        private Boolean marketingContentAllProducts;    //Contenu Marketing: true - Tous produits; false - Catégorie achetée
        private Boolean marketingFrequencyOncePerMonth; //Fréquence Marketing: true - 1 fois par mois; false - 1 fois par semaine
        public PreferenceFragmentLicenseSettingRequest(Boolean durationYear,
                                                       Boolean receiverOnly,
                                                       Boolean communicateAddress,
                                                       Boolean continuousLocation,
                                                       Boolean marketingContentAllProducts,
                                                       Boolean marketingFrequencyOncePerMonth){
            this.durationYear=durationYear;
            this.receiverOnly=receiverOnly;
            this.communicateAddress=communicateAddress;
            this.continuousLocation=continuousLocation;
            this.marketingContentAllProducts=marketingContentAllProducts;
            this.marketingFrequencyOncePerMonth=marketingFrequencyOncePerMonth;
        }
    }

    // This function is called when the fragment is switching between show and hide
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {// When switching to show
            //Log.d("--- LicenseSetting ---", "[Status Info] change to show");
            getLicenseSetting();
        }else {// When switching to hide
            //Log.d("--- LicenseSetting ---", "[Status Info] change to hide");
            updateLicenseSetting();
        }
    }
}