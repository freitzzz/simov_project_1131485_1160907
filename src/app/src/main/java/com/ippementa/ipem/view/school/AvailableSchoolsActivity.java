package com.ippementa.ipem.view.school;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ippementa.ipem.R;
import com.ippementa.ipem.presenter.school.AvailableSchoolsModel;
import com.ippementa.ipem.presenter.school.AvailableSchoolsPresenter;
import com.ippementa.ipem.util.Provider;
import com.ippementa.ipem.view.canteen.AvailableCanteensActivity;
import com.ippementa.ipem.view.settings.SettingsActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class AvailableSchoolsActivity extends AppCompatActivity implements AvailableSchoolsView {

    public static final int REQUEST_CODE_FOR_SETTINGS_ACTIVITY = 854;

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

        boolean isInDarkMode = Provider.instance(this).settings().isInDarkMode();

        if (isInDarkMode) {

            ImageView headerLogo = findViewById(R.id.available_schools_header_image_view);

            headerLogo.setBackgroundColor(ContextCompat.getColor(this, R.color.colorDark));

        }


        adapter = new AvailableSchoolsListAdapter(this, new ArrayList<AvailableSchoolsModel.Item>());

        schoolsListView.setAdapter(adapter);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.option_menu_settings_item:

                Intent intent = new Intent(this, SettingsActivity.class);

                startActivityForResult(intent, REQUEST_CODE_FOR_SETTINGS_ACTIVITY);

                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_FOR_SETTINGS_ACTIVITY){

            presenter.requestSchools();

        }

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

        Toast.makeText(this, R.string.no_schools_found_available_schools_activity, Toast.LENGTH_LONG).show();

    }

    @Override
    public void showNoInternetConnectionError() {

        Toast.makeText(this, R.string.no_internet_connection_error, Toast.LENGTH_LONG).show();

    }

    @Override
    public void showServerNotAvailableError() {

        Toast.makeText(this, R.string.server_not_available_error, Toast.LENGTH_LONG).show();

    }

    @Override
    public void showUnexepectedServerFailureError() {

        Toast.makeText(this, R.string.unexpected_server_failure_error, Toast.LENGTH_LONG).show();

    }

    @Override
    public Resources.Theme getTheme() {

        Resources.Theme theme = super.getTheme();

        boolean isInDarkMode = Provider.instance(this).settings().isInDarkMode();

        if(isInDarkMode){
            theme.applyStyle(R.style.DarkMode, true);
        }else{
            theme.applyStyle(R.style.LightMode, true);
        }

        return theme;

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
