package com.ippementa.ipem.view.school;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ippementa.ipem.R;
import com.ippementa.ipem.model.canteen.AvailableCanteensModel;
import com.ippementa.ipem.model.canteen.CanteensRepository;
import com.ippementa.ipem.view.canteen.AvailableCanteensActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AvailableSchoolsActivity extends AppCompatActivity {

    private List<String[]> availableSchools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_available_schools);

        ListView schoolsListView = findViewById(R.id.available_schools_list_view);

        this.availableSchools = (ArrayList<String[]>)getIntent().getExtras().getSerializable("schools");

        schoolsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] schoolInfo = availableSchools.get(position);

                long schoolId = Long.parseLong(schoolInfo[0]);

                new RequestAvailableSchoolsTask().execute(schoolId);
            }
        });

        schoolsListView.setAdapter(new AvailableSchoolsListAdapter(this, availableSchools));

    }

    private class AvailableSchoolsListAdapter extends ArrayAdapter<String[]> {

        public AvailableSchoolsListAdapter(Context context, List<String[]> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            String[] school = getItem(position);

            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.available_schools_list_view_item, parent, false);
            }

            TextView schoolAcronymTextView = convertView.findViewById(R.id.available_schools_list_view_item_school_acronym_text_view);

            TextView schoolNameTextView = convertView.findViewById(R.id.available_schools_list_view_item_school_name_text_view);

            schoolAcronymTextView.setText(school[1]);

            schoolNameTextView.setText(school[2]);

            return convertView;
        }
    }

    private class RequestAvailableSchoolsTask extends AsyncTask<Long, Integer, AvailableCanteensModel> {

        @Override
        protected AvailableCanteensModel doInBackground(Long... schooldIds) {

            CanteensRepository repository = new CanteensRepository();

            try {
                return repository.availableCanteens(schooldIds[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(AvailableCanteensModel items) {
            super.onPostExecute(items);

            Intent availableCanteensActivity = new Intent(AvailableSchoolsActivity.this, AvailableCanteensActivity.class);

            ArrayList<String[]> canteens = new ArrayList<>();

            for(AvailableCanteensModel.Item item : items){
                canteens.add(new String[]{Long.toString(item.id), item.name});
            }

            availableCanteensActivity.putExtra("canteens", canteens);

            startActivity(availableCanteensActivity);
        }
    }

}
