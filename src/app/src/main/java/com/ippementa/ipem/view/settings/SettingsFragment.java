package com.ippementa.ipem.view.settings;

import android.content.Context;
import android.os.Bundle;

import com.ippementa.ipem.R;
import com.ippementa.ipem.presenter.settings.SettingsPresenter;
import com.ippementa.ipem.util.Provider;
import com.ippementa.ipem.util.Settings;

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

    private boolean isInDarkMode;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        this.presenter = (SettingsPresenter)getArguments().getSerializable("presenter");

        Context context = getPreferenceManager().getContext();

        PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);

        this.isInDarkMode = Provider.instance(context).settings().isInDarkMode();

        this.offlineModeSwitch = new SwitchPreferenceCompat(context);

        this.offlineModeSwitch.setKey("offline-mode");
        this.offlineModeSwitch.setTitle(R.string.settings_offline_mode);

        this.offlineModeSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                boolean checked = (boolean)newValue;

                updateOfflineModeSwitchIcon(checked);

                if(checked){

                    presenter.startOfflineDataDownload();

                }else{;

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

                updateFavoriteDishesAvailabilityPushNotificationsSwitchIcon(checked);

                if(checked){

                    presenter.registerFavoriteDishesPushNotificationsReceive();

                }else{

                    presenter.unregisterFavoriteDishesPushNotificationsReceive();

                }

                return true;
            }
        });

        this.nearbyCanteensPushNotificationsSwitch = new SwitchPreferenceCompat(context);

        this.nearbyCanteensPushNotificationsSwitch.setKey("nearby-canteens-push-notifications");
        this.nearbyCanteensPushNotificationsSwitch.setTitle(R.string.nearby_canteens_push_notifications);

        this.nearbyCanteensPushNotificationsSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                boolean checked = (boolean)newValue;

                updateNearbyCanteensPushNotificationsSwitchIcon(checked);

                if(checked){

                }else{

                }

                return true;
            }
        });

        this.darkModeSwitch = new SwitchPreferenceCompat(context);

        this.darkModeSwitch.setKey("dark-mode");
        this.darkModeSwitch.setTitle(R.string.settings_dark_mode);

        this.darkModeSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                boolean checked = (boolean)newValue;

                updateDarkModeSwitchIcon(checked);

                if(checked){

                    presenter.changeToDarkMode();

                }else{

                    presenter.changeToLightMode();

                }

                return true;
            }
        });

        Settings settings = Provider.instance(context).settings();

        this.updateOfflineModeSwitchIcon(settings.isInOfflineMode());

        this.updateFavoriteDishesAvailabilityPushNotificationsSwitchIcon(this.favoriteDishesAvailabilityPushNotificationsSwitch.isChecked());

        this.updateNearbyCanteensPushNotificationsSwitchIcon(this.nearbyCanteensPushNotificationsSwitch.isChecked());

        this.updateDarkModeSwitchIcon(settings.isInDarkMode());

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



    public void updateOfflineModeSwitchIcon(boolean isChecked) {

        SwitchPreferenceCompat switchToUpdate = this.offlineModeSwitch;

        if(isChecked) {

            if(isInDarkMode){
                switchToUpdate.setIcon(R.drawable.icon_wifi_off_white);
            }else{
                switchToUpdate.setIcon(R.drawable.icon_wifi_off_black);
            }

        }else{

            if(isInDarkMode){
                switchToUpdate.setIcon(R.drawable.icon_wifi_white);
            }else{
                switchToUpdate.setIcon(R.drawable.icon_wifi_black);
            }

        }

    }

    public void updateFavoriteDishesAvailabilityPushNotificationsSwitchIcon(boolean isChecked) {

        SwitchPreferenceCompat switchToUpdate = this.favoriteDishesAvailabilityPushNotificationsSwitch;

        if(isChecked) {

            if(isInDarkMode){
                switchToUpdate.setIcon(R.drawable.icon_star_white);
            }else{
                switchToUpdate.setIcon(R.drawable.icon_star_black);
            }

        }else{

            if(isInDarkMode){
                switchToUpdate.setIcon(R.drawable.icon_star_off_white);
            }else{
                switchToUpdate.setIcon(R.drawable.icon_star_off_black);
            }

        }

    }

    public void updateNearbyCanteensPushNotificationsSwitchIcon(boolean isChecked) {

        SwitchPreferenceCompat switchToUpdate = this.nearbyCanteensPushNotificationsSwitch;

        if(isChecked) {

            if(isInDarkMode){
                switchToUpdate.setIcon(R.drawable.icon_map_marker_white);
            }else{
                switchToUpdate.setIcon(R.drawable.icon_map_marker_black);
            }

        }else{

            if(isInDarkMode){
                switchToUpdate.setIcon(R.drawable.icon_map_marker_off_white);
            }else{
                switchToUpdate.setIcon(R.drawable.icon_map_marker_off_black);
            }

        }

    }

    public void updateDarkModeSwitchIcon(boolean isChecked) {

        SwitchPreferenceCompat switchToUpdate = this.darkModeSwitch;

        if(isChecked) {

            if(isInDarkMode){
                switchToUpdate.setIcon(R.drawable.icon_moon_white);
            }else{
                switchToUpdate.setIcon(R.drawable.icon_moon_black);
            }

        }else{

            if(isInDarkMode){
                switchToUpdate.setIcon(R.drawable.icon_sun_white);
            }else{
                switchToUpdate.setIcon(R.drawable.icon_sun_black);
            }

        }

    }

    public void setDarkModeSwitchToFalse() {

        this.darkModeSwitch.setChecked(false);

        this.updateDarkModeSwitchIcon(false);

    }

    public void setDarkModeSwitchToTrue() {

        this.darkModeSwitch.setChecked(true);

        this.updateDarkModeSwitchIcon(true);

    }

    public void enableFavoriteDishPushNotificationsSwitchInteraction() {

        this.favoriteDishesAvailabilityPushNotificationsSwitch.setSelectable(true);

    }

    public void disableFavoriteDishPushNotificationsSwitchInteraction() {

        this.favoriteDishesAvailabilityPushNotificationsSwitch.setSelectable(false);

    }

    public void setFavoriteDishPushNotificationsSwitchToFalse() {

        this.favoriteDishesAvailabilityPushNotificationsSwitch.setChecked(false);

        updateFavoriteDishesAvailabilityPushNotificationsSwitchIcon(true);

    }

    public void setFavoriteDishPushNotificationsSwitchToTrue() {

        this.favoriteDishesAvailabilityPushNotificationsSwitch.setChecked(true);

        updateFavoriteDishesAvailabilityPushNotificationsSwitchIcon(true);

    }
}
