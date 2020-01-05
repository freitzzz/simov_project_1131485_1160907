package com.ippementa.ipem.view.settings;

import com.ippementa.ipem.view.error.ErrorView;

public interface SettingsView extends ErrorView {

    void enableOfflineModeSwitchInteraction();

    void disableOfflineModeSwitchInteraction();

    void deactivateOfflineModeSwitch();

    void showOfflineDataDownloadStartSnackbar();

    void showOfflineDataDownloadFinishSnackbar();


    void activateDarkModeSwitch();

    void deactivateDarkModeSwitch();

    void showDarkModeEnabledSnackbar();

    void showDarkModeDisabledSnackbar();

    void showErrorEnablingDarkModeSnackBar();

    void showErrorDisablingDarkModeSnackBar();


    void enableFavoriteDishPushNotificationsSwitchInteraction();

    void disableFavoriteDishPushNotificationsSwitchInteraction();

    void deactivateFavoriteDishPushNotificationsSwitch();

    void showRegisteringFavoriteDishesPushNotificationsReceiveStartToast();

    void showRegisteringFavoriteDishesPushNotificationsReceiveFinishToast();

    void showUnregisteringFavoriteDishesPushNotificationsReceiveStartToast();

    void activateFavoriteDishPushNotificationsSwitch();

    void showUnregisteringFavoriteDishesPushNotificationsReceiveFinishToast();


    void enableNearbyCanteensPushNotificationsSwitchInteraction();

    void disableNearbyCanteensPushNotificationsSwitchInteraction();

    void deactivateNearbyCanteensDishPushNotificationsSwitch();

    void showRegisteringNearbyCanteensPushNotificationsReceiveStartToast();

    void showRegisteringNearbyCanteensPushNotificationsReceiveFinishToast();

    void showUnregisteringNearbyCanteensPushNotificationsReceiveStartToast();

    void activateNearbyCanteensPushNotificationsSwitch();

    void showUnregisteringNearbyCanteensPushNotificationsReceiveFinishToast();

    void showApplicationRequireFineLocationAccessPermissionToast();

    void showRegisteringNearbyCanteensPushNotificationsReceiveFailedToast();

    void showUnregisteringNearbyCanteensPushNotificationsReceiveFailedToast();
}
