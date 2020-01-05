package com.ippementa.ipem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.ippementa.ipem.util.Provider;
import com.ippementa.ipem.view.school.AvailableSchoolsActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("fcm", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        Log.d("fcm", token);
                        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();

                        Provider.instance(getApplicationContext()).settings().changeFcmRegistrationToken(getApplicationContext(), token);
                    }
                });

        Intent intent = new Intent(this, AvailableSchoolsActivity.class);

        startActivity(intent);
    }
}
