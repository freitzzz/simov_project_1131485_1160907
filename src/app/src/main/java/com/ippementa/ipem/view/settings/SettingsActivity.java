package com.ippementa.ipem.view.settings;

import android.os.Bundle;

import com.ippementa.ipem.R;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_settings_root_layout,new SettingsFragment())
                .commit();

        // TODO: Network errors in fragment
        // Only insert in database the necessary files after download
        // Redirect user after download
        // Block user change of offline mode while data is being downloaded

    }
}
