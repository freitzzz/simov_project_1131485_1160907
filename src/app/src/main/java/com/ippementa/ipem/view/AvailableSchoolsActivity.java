package com.ippementa.ipem.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ippementa.ipem.R;

import java.util.ArrayList;
import java.util.List;

public class AvailableSchoolsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_available_schools);

        ListView schoolsListView = findViewById(R.id.available_schools_list_view);

        List<String[]> availableSchools = new ArrayList<>();

        availableSchools.add(new String[]{"ISEP", "Instituto Superior de Engenharia do Porto"});

        availableSchools.add(new String[]{"ESMAD", "Escola Superior de Multimédia Artes e Design"});

        availableSchools.add(new String[]{"ESE", "Escola Superior de Educação"});

        schoolsListView.setAdapter(new AvailableSchoolsListAdapter(this, availableSchools));

    }

    public class AvailableSchoolsListAdapter extends ArrayAdapter<String[]> {

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

            schoolAcronymTextView.setText(school[0]);

            schoolNameTextView.setText(school[1]);

            return convertView;
        }
    }

}
