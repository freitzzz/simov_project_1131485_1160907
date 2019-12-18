package com.ippementa.ipem.view.preferences;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ippementa.ipem.R;

public class UserPreferencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.preferences_container, new UserPreferencesFragment())
                .commit();

        setContentView(R.layout.activity_user_preferences);
    }

}
