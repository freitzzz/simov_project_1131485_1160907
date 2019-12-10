package com.ippementa.ipem.view.school;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ippementa.ipem.R;
import com.ippementa.ipem.presenter.school.AvailableSchoolsModel;
import com.ippementa.ipem.presenter.school.AvailableSchoolsPresenter;
import com.ippementa.ipem.view.canteen.AvailableCanteensActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AvailableSchoolsActivity extends AppCompatActivity implements AvailableSchoolsView {

    private AvailableSchoolsPresenter presenter;

    private AvailableSchoolsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.presenter = new AvailableSchoolsPresenter(this);

        setContentView(R.layout.activity_available_schools);

        final ListView schoolsListView = findViewById(R.id.available_schools_list_view);

        schoolsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AvailableSchoolsModel.Item school = adapter.getItem(position);

                navigateToSchoolCanteensPage(school);
            }
        });

        schoolsListView.setAdapter(new AvailableSchoolsListAdapter(this, new ArrayList<AvailableSchoolsModel.Item>()));

        presenter.requestSchools();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        presenter.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        presenter.onPause();
    }

    @Override
    public void showSchools(AvailableSchoolsModel schools) {

        adapter.clear();

        adapter.addAll(schools);

        adapter.notifyDataSetChanged();

    }

    @Override
    public void navigateToSchoolCanteensPage(AvailableSchoolsModel.Item school) {

        Intent availableCanteensActivity = new Intent(AvailableSchoolsActivity.this, AvailableCanteensActivity.class);

        availableCanteensActivity.putExtra("school", school);

        startActivity(availableCanteensActivity);

    }

    @Override
    public void showUnavailableSchoolsError() {

        Toast.makeText(this, "No Schools Available", Toast.LENGTH_LONG).show();

    }

    @Override
    public void showNoInternetConnectionError() {

        Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();

    }

    @Override
    public void showServerNotAvailableError() {

        Toast.makeText(this, "Server Not Available", Toast.LENGTH_LONG).show();

    }

    @Override
    public void showUnexepectedServerFailureError() {

        Toast.makeText(this, "Unexpected Server Failure", Toast.LENGTH_LONG).show();

    }

    private class AvailableSchoolsListAdapter extends ArrayAdapter<AvailableSchoolsModel.Item> {

        public AvailableSchoolsListAdapter(Context context, List<AvailableSchoolsModel.Item> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            AvailableSchoolsModel.Item school = getItem(position);

            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.available_schools_list_view_item, parent, false);
            }

            TextView schoolAcronymTextView = convertView.findViewById(R.id.available_schools_list_view_item_school_acronym_text_view);

            TextView schoolNameTextView = convertView.findViewById(R.id.available_schools_list_view_item_school_name_text_view);

            schoolAcronymTextView.setText(school.acronym);

            schoolNameTextView.setText(school.name);

            return convertView;
        }
    }

}
