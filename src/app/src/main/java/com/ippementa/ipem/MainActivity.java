package com.ippementa.ipem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.ippementa.ipem.model.school.AvailableSchoolsModel;
import com.ippementa.ipem.model.school.SchoolsRepository;
import com.ippementa.ipem.view.school.AvailableSchoolsActivity;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new RequestAvailableSchoolsTask().execute();
    }

    private class RequestAvailableSchoolsTask extends AsyncTask<Void, Integer, AvailableSchoolsModel>{

        @Override
        protected AvailableSchoolsModel doInBackground(Void... voids) {

            SchoolsRepository repository = new SchoolsRepository();

            try {
                return repository.availableSchools();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(AvailableSchoolsModel items) {
            super.onPostExecute(items);

            Intent availableSchoolsActivity = new Intent(MainActivity.this, AvailableSchoolsActivity.class);

            ArrayList<String[]> schools = new ArrayList<>();

            for(AvailableSchoolsModel.Item item : items){
                schools.add(new String[]{Long.toString(item.id),item.acronym, item.name});
            }

            availableSchoolsActivity.putExtra("schools", schools);

            startActivity(availableSchoolsActivity);
        }
    }
}
