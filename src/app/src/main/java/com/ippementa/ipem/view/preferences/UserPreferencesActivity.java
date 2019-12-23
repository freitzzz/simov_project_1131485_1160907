package com.ippementa.ipem.view.preferences;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ippementa.ipem.R;

public class UserPreferencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_preferences);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, new UserPreferencesFragment())
                .commit();

    }
}