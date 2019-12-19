package com.ippementa.ipem.view.preferences;

import android.os.Bundle;


import androidx.preference.PreferenceFragmentCompat;

import com.ippementa.ipem.R;

public class UserPreferencesFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        //Load the preferences from the xml
        addPreferencesFromResource(R.xml.preferences);
    }
}
