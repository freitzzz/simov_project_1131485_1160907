package com.ippementa.ipem;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ippementa.ipem.view.preferences.UserPreferencesActivity;
import com.ippementa.ipem.view.school.AvailableSchoolsActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, AvailableSchoolsActivity.class);
        //Intent intent = new Intent(this, UserPreferencesActivity.class);
        startActivity(intent);
    }
}
