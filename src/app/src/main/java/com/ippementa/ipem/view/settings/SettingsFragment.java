package com.ippementa.ipem.view.settings;

import android.content.Context;
import android.os.Bundle;

import com.ippementa.ipem.R;
import com.ippementa.ipem.presenter.settings.SettingsPresenter;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreferenceCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    private SettingsPresenter presenter;

    private SwitchPreferenceCompat offlineModeSwitch;

    private SwitchPreferenceCompat favoriteDishesAvailabilityPushNotificationsSwitch;

    private SwitchPreferenceCompat nearbyCanteensPushNotificationsSwitch;

    private SwitchPreferenceCompat darkModeSwitch;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        this.presenter = (SettingsPresenter)getArguments().getSerializable("presenter");

        Context context = getPreferenceManager().getContext();

        PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);

        this.offlineModeSwitch = new SwitchPreferenceCompat(context);

        this.offlineModeSwitch.setKey("offline-mode");
        this.offlineModeSwitch.setTitle(R.string.settings_offline_mode);

        if(this.offlineModeSwitch.isEnabled()) {
            this.offlineModeSwitch.setIcon(R.drawable.icon_settings_enable_offline_mode);
        }else{
            this.offlineModeSwitch.setIcon(R.drawable.icon_settings_disable_offline_mode);
        }

        this.offlineModeSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                boolean checked = (boolean)newValue;

                if(checked){

                    offlineModeSwitch.setIcon(R.drawable.icon_settings_enable_offline_mode);

                    presenter.startOfflineDataDownload();

                }else{

                    offlineModeSwitch.setIcon(R.drawable.icon_settings_disable_offline_mode);

                    presenter.setNotUseOfflineData();

                }

                return true;
            }
        });

        this.favoriteDishesAvailabilityPushNotificationsSwitch = new SwitchPreferenceCompat(context);

        this.favoriteDishesAvailabilityPushNotificationsSwitch.setKey("favorite-dishes-availability-push-notifications");
        this.favoriteDishesAvailabilityPushNotificationsSwitch.setTitle(R.string.settings_favorite_dishes_availability_push_notifications);

        if(this.favoriteDishesAvailabilityPushNotificationsSwitch.isEnabled()) {
            this.favoriteDishesAvailabilityPushNotificationsSwitch.setIcon(R.drawable.icon_settings_enable_favorite_dishes_availability_push_notifications);
        }else{
            this.favoriteDishesAvailabilityPushNotificationsSwitch.setIcon(R.drawable.icon_settings_disable_favorite_dishes_availability_push_notifications);
        }

        this.favoriteDishesAvailabilityPushNotificationsSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                boolean checked = (boolean)newValue;

                if(checked){

                    favoriteDishesAvailabilityPushNotificationsSwitch.setIcon(R.drawable.icon_settings_enable_favorite_dishes_availability_push_notifications);

                }else{

                    favoriteDishesAvailabilityPushNotificationsSwitch.setIcon(R.drawable.icon_settings_disable_favorite_dishes_availability_push_notifications);

                }

                return true;
            }
        });

        this.nearbyCanteensPushNotificationsSwitch = new SwitchPreferenceCompat(context);

        this.nearbyCanteensPushNotificationsSwitch.setKey("nearby-canteens-push-notifications");
        this.nearbyCanteensPushNotificationsSwitch.setTitle(R.string.nearby_canteens_push_notifications);

        if(this.nearbyCanteensPushNotificationsSwitch.isEnabled()) {
            this.nearbyCanteensPushNotificationsSwitch.setIcon(R.drawable.icon_settings_enable_nearby_canteens_push_notifications);
        }else{
            this.nearbyCanteensPushNotificationsSwitch.setIcon(R.drawable.icon_settings_disable_nearby_canteens_push_notifications);
        }

        this.nearbyCanteensPushNotificationsSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                boolean checked = (boolean)newValue;

                if(checked){

                    nearbyCanteensPushNotificationsSwitch.setIcon(R.drawable.icon_settings_enable_nearby_canteens_push_notifications);

                }else{

                    nearbyCanteensPushNotificationsSwitch.setIcon(R.drawable.icon_settings_disable_nearby_canteens_push_notifications);

                }

                return true;
            }
        });

        this.darkModeSwitch = new SwitchPreferenceCompat(context);

        this.darkModeSwitch.setKey("dark-mode");
        this.darkModeSwitch.setTitle(R.string.settings_dark_mode);

        if(this.darkModeSwitch.isEnabled()) {
            this.darkModeSwitch.setIcon(R.drawable.icon_settings_enable_dark_mode);
        }else{
            this.darkModeSwitch.setIcon(R.drawable.icon_settings_disable_dark_mode);
        }

        this.darkModeSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                boolean checked = (boolean)newValue;
                
                if(checked){

                    presenter.changeToDarkMode();

                    darkModeSwitch.setIcon(R.drawable.icon_settings_enable_dark_mode);

                }else{

                    presenter.changeToLightMode();

                    darkModeSwitch.setIcon(R.drawable.icon_settings_disable_dark_mode);

                }

                return true;
            }
        });

        screen.addPreference(this.offlineModeSwitch);

        screen.addPreference(this.favoriteDishesAvailabilityPushNotificationsSwitch);

        screen.addPreference(this.nearbyCanteensPushNotificationsSwitch);

        screen.addPreference(this.darkModeSwitch);

        setPreferenceScreen(screen);
    }

    public void enableOfflineModeSwitchInteraction(){

        this.offlineModeSwitch.setSelectable(true);

    }

    public void disableOfflineModeSwitchInteraction(){

        this.offlineModeSwitch.setSelectable(false);

    }

    public void setOfflineModeSwitchToFalse(){

        this.offlineModeSwitch.setChecked(false);

        this.offlineModeSwitch.callChangeListener(false);

    }
}
