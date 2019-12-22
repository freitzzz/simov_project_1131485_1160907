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

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        this.presenter = (SettingsPresenter)getArguments().getSerializable("presenter");

        Context context = getPreferenceManager().getContext();

        PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);

        this.offlineModeSwitch = new SwitchPreferenceCompat(context);

        this.offlineModeSwitch.setKey("offline-mode");
        this.offlineModeSwitch.setTitle(R.string.settings_offline_mode);
        this.offlineModeSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                boolean checked = (boolean)newValue;

                if(checked){

                    presenter.startOfflineDataDownload();

                }else{

                    presenter.setNotUseOfflineData();

                }

                return true;
            }
        });

        screen.addPreference(this.offlineModeSwitch);

        setPreferenceScreen(screen);
    }

    public void enableOfflineModeSwitchInteraction(){

        this.offlineModeSwitch.setSelectable(true);

    }

    public void disableOfflineModeSwitchInteraction(){

        this.offlineModeSwitch.setSelectable(false);

    }
}
