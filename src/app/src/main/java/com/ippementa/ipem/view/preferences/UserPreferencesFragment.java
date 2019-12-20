package com.ippementa.ipem.view.preferences;


import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.ippementa.ipem.R;

public class UserPreferencesFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

}
