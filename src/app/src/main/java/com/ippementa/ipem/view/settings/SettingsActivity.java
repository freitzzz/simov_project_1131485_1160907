package com.ippementa.ipem.view.settings;

import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Toast;

import com.ippementa.ipem.R;
import com.ippementa.ipem.presenter.settings.SettingsPresenter;
import com.ippementa.ipem.util.Provider;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity implements SettingsView{

    private SettingsFragment contentFragment;

    private SettingsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.presenter = new SettingsPresenter(this);

        this.contentFragment = new SettingsFragment();

        Bundle fragmentArguments = new Bundle();

        fragmentArguments.putSerializable("presenter", presenter);

        this.contentFragment.setArguments(fragmentArguments);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_settings_root_layout, this.contentFragment)
                .commit();

    }

    @Override
    public void enableOfflineModeSwitchInteraction() {

        this.contentFragment.enableOfflineModeSwitchInteraction();

    }

    @Override
    public void disableOfflineModeSwitchInteraction() {
        this.contentFragment.disableOfflineModeSwitchInteraction();

    }

    @Override
    public void deactivateOfflineModeSwitch() {

        this.contentFragment.setOfflineModeSwitchToFalse();

    }

    @Override
    public void showOfflineDataDownloadStartSnackbar() {

        Toast.makeText(this,
                R.string.settings_offline_data_download_start,
                Toast.LENGTH_LONG)
                .show();

    }

    @Override
    public void showOfflineDataDownloadFinishSnackbar() {

        Toast.makeText(this,
                R.string.settings_offline_data_download_finish,
                Toast.LENGTH_LONG)
                .show();

    }

    @Override
    public void activateDarkModeSwitch() {
        this.contentFragment.setDarkModeSwitchToTrue();
    }

    @Override
    public void deactivateDarkModeSwitch() {

        this.contentFragment.setDarkModeSwitchToFalse();

    }

    @Override
    public void showDarkModeEnabledSnackbar() {

        Toast.makeText(this,
                R.string.settings_dark_mode_enabled,
                Toast.LENGTH_SHORT)
                .show();

    }

    @Override
    public void showDarkModeDisabledSnackbar() {

        Toast.makeText(this,
                R.string.settings_dark_mode_disabled,
                Toast.LENGTH_SHORT)
                .show();

    }

    @Override
    public void showErrorEnablingDarkModeSnackBar() {

        Toast.makeText(this,
                R.string.settings_error_enabling_dark_mode,
                Toast.LENGTH_LONG)
                .show();

    }

    @Override
    public void showErrorDisablingDarkModeSnackBar() {

        Toast.makeText(this,
                R.string.settings_error_disabling_dark_mode,
                Toast.LENGTH_LONG)
                .show();

    }

    @Override
    public void enableFavoriteDishPushNotificationsSwitchInteraction() {

        this.contentFragment.enableFavoriteDishPushNotificationsSwitchInteraction();

    }

    @Override
    public void disableFavoriteDishPushNotificationsSwitchInteraction() {

        this.contentFragment.disableFavoriteDishPushNotificationsSwitchInteraction();

    }

    @Override
    public void deactivateFavoriteDishPushNotificationsSwitch() {

        this.contentFragment.setFavoriteDishPushNotificationsSwitchToFalse();

    }

    @Override
    public void showRegisteringFavoriteDishesPushNotificationsReceiveStartToast() {

        Toast.makeText(this,
                R.string.registering_favorite_dishes_push_notifications_receive_start,
                Toast.LENGTH_LONG)
                .show();

    }

    @Override
    public void showRegisteringFavoriteDishesPushNotificationsReceiveFinishToast() {

        Toast.makeText(this,
                R.string.registering_favorite_dishes_push_notifications_receive_finish,
                Toast.LENGTH_LONG)
                .show();

    }

    @Override
    public void showUnregisteringFavoriteDishesPushNotificationsReceiveStartToast() {

        Toast.makeText(this,
                R.string.unregistering_favorite_dishes_push_notifications_receive_start,
                Toast.LENGTH_LONG)
                .show();

    }

    @Override
    public void activateFavoriteDishPushNotificationsSwitch() {

        this.contentFragment.setFavoriteDishPushNotificationsSwitchToTrue();

    }

    @Override
    public void showUnregisteringFavoriteDishesPushNotificationsReceiveFinishToast() {

        Toast.makeText(this,
                R.string.unregistering_favorite_dishes_push_notifications_receive_finish,
                Toast.LENGTH_LONG)
                .show();

    }

    @Override
    public void enableNearbyCanteensPushNotificationsSwitchInteraction() {

        this.contentFragment.enableNearbyCanteensPushNotificationsSwitchInteraction();

    }

    @Override
    public void disableNearbyCanteensPushNotificationsSwitchInteraction() {

        this.contentFragment.disableNearbyCanteensPushNotificationsSwitchInteraction();

    }

    @Override
    public void deactivateNearbyCanteensDishPushNotificationsSwitch() {

        this.contentFragment.setNearbyCanteensPushNotificationsSwitchToFalse();

    }

    @Override
    public void showRegisteringNearbyCanteensPushNotificationsReceiveStartToast() {

        Toast.makeText(this,
                R.string.registering_nearby_canteens_push_notifications_receive_start,
                Toast.LENGTH_LONG)
                .show();

    }

    @Override
    public void showRegisteringNearbyCanteensPushNotificationsReceiveFinishToast() {

        Toast.makeText(this,
                R.string.registering_nearby_canteens_push_notifications_receive_finish,
                Toast.LENGTH_LONG)
                .show();

    }

    @Override
    public void showUnregisteringNearbyCanteensPushNotificationsReceiveStartToast() {

        Toast.makeText(this,
                R.string.unregistering_nearby_canteens_push_notifications_receive_start,
                Toast.LENGTH_LONG)
                .show();

    }

    @Override
    public void activateNearbyCanteensPushNotificationsSwitch() {

        this.contentFragment.setNearbyCanteensPushNotificationsSwitchToTrue();

    }

    @Override
    public void showUnregisteringNearbyCanteensPushNotificationsReceiveFinishToast() {

        Toast.makeText(this,
                R.string.unregistering_nearby_canteens_push_notifications_receive_finish,
                Toast.LENGTH_LONG)
                .show();

    }

    @Override
    public void showApplicationRequireFineLocationAccessPermissionToast() {

        Toast.makeText(this,
                R.string.application_requires_fine_location_access_permission,
                Toast.LENGTH_LONG)
                .show();

    }

    @Override
    public void showRegisteringNearbyCanteensPushNotificationsReceiveFailedToast() {

        Toast.makeText(this,
                R.string.registering_nearby_canteens_push_notifications_receive_failed,
                Toast.LENGTH_LONG)
                .show();

    }

    @Override
    public void showUnregisteringNearbyCanteensPushNotificationsReceiveFailedToast() {

        Toast.makeText(this,
                R.string.unregistering_nearby_canteens_push_notifications_receive_failed,
                Toast.LENGTH_LONG)
                .show();

    }

    @Override
    public void showNoInternetConnectionError() {

        Toast.makeText(this,
                R.string.no_internet_connection_error,
                Toast.LENGTH_LONG)
                .show();

    }

    @Override
    public void showServerNotAvailableError() {
        Toast.makeText(this,
                R.string.server_not_available_error,
                Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void showUnexepectedServerFailureError() {
        Toast.makeText(this,
                R.string.unexpected_server_failure_error,
                Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public Resources.Theme getTheme() {

        Resources.Theme theme = super.getTheme();

        boolean isInDarkMode = Provider.instance(this).settings().isInDarkMode();

        if(isInDarkMode){
            theme.applyStyle(R.style.DarkMode, true);
        }else{
            theme.applyStyle(R.style.LightMode, true);
        }

        return theme;

    }
}
