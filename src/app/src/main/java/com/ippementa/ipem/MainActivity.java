package com.ippementa.ipem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.ippementa.ipem.util.Provider;
import com.ippementa.ipem.view.school.AvailableSchoolsActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String[] INTERNAL_STORAGE_FILES = new String[]{
            "porto.map",
            "edges",
            "geometry",
            "location_index",
            "nodes",
            "nodes_ch_fastest_car_node",
            "properties",
            "shortcuts_fastest_car_node",
            "string_index_keys",
            "string_index_vals"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView ippementaLogoImageView = findViewById(R.id.ippementa_logo_main_activity);

        Animation splashAnimation = AnimationUtils.loadAnimation(this, R.anim.splash);

        ippementaLogoImageView.startAnimation(splashAnimation);

        try {
            grantThatAllFilesExistInInternalStorage();
        } catch (IOException e) {
            e.printStackTrace();

            // If an IOException occurs then not all required files exist in the internal storage
            // This means that the application state is not valid to continue usage

            finish();
        }

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

        //startActivity(intent);
    }

    private void grantThatAllFilesExistInInternalStorage() throws IOException{

        for(String file : INTERNAL_STORAGE_FILES) {

            File fileOnInternalStorage = new File(getExternalFilesDir(null), file);

            if(!fileOnInternalStorage.exists()) {

                FileOutputStream out = new FileOutputStream(fileOnInternalStorage);

                System.out.println(file);

                int resourceId = getResources().getIdentifier(file.split("\\.")[0], "raw", getPackageName());

                System.out.println(resourceId);

                copyStream(getResources().openRawResource(resourceId), out);

                out.close();
            }

        }

    }

    private static void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}
