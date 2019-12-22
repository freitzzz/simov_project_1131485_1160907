package com.ippementa.ipem.view.settings;

import com.ippementa.ipem.view.error.ErrorView;

public interface SettingsView extends ErrorView {

    void enableOfflineModeSwitchInteraction();

    void disableOfflineModeSwitchInteraction();

    void showOfflineDataDownloadStartSnackbar();

    void showOfflineDataDownloadFinishSnackbar();

}
