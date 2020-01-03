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

}
