package com.ippementa.ipem.view.canteen;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ippementa.ipem.R;
import com.ippementa.ipem.presenter.canteen.AvailableCanteensModel;
import com.ippementa.ipem.presenter.canteen.AvailableCanteensPresenter;
import com.ippementa.ipem.presenter.canteen.CanteenWithMapLocationModel;
import com.ippementa.ipem.presenter.school.AvailableSchoolsModel;
import com.ippementa.ipem.util.Provider;
import com.ippementa.ipem.view.menu.AvailableCanteenMenusActivity;
import com.ippementa.ipem.view.settings.SettingsActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AvailableCanteensActivity extends AppCompatActivity implements AvailableCanteensView{

    public static final int REQUEST_CODE_FOR_SETTINGS_ACTIVITY = 854;

    private AvailableCanteensPresenter presenter;

    private AvailableCanteensListAdapter adapter;

    private AvailableSchoolsModel.Item school;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_available_canteens);

        this.presenter = new AvailableCanteensPresenter(this);

        this.school = getIntent().getParcelableExtra("school");

        TextView headerTextView = findViewById(R.id.available_canteens_header_text_view);

        headerTextView.setText(headerTextView.getText().toString() + " " + school.acronym);

        Button headerBackButton = findViewById(R.id.available_canteens_header_back_button);

        headerBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navigateBackToAvailableSchoolsPage();

            }
        });

        boolean isInDarkMode = Provider.instance(this).settings().isInDarkMode();

        if(isInDarkMode){
            headerBackButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_arrow_left_white, 0, 0,0 );
        }

        ListView canteensListView = findViewById(R.id.available_canteens_list_view);

        canteensListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AvailableCanteensModel.Item canteen = adapter.getItem(position);

                navigateToCanteenMenusPage(canteen);
            }
        });

        registerForContextMenu(canteensListView);

        this.adapter = new AvailableCanteensActivity.AvailableCanteensListAdapter(this, new ArrayList<AvailableCanteensModel.Item>());

        canteensListView.setAdapter(adapter);

        this.presenter.requestCanteens(school.id);
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.canteen_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.canteen_context_menu_map_location:

                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

                int canteenItemPosition = info.position;

                AvailableCanteensModel.Item canteenItem = adapter.getItem(canteenItemPosition);

                presenter.requestCanteenToDisplayOnMap(canteenItem.schoolId, canteenItem.id);

                break;
            case R.id.canteen_context_menu_map_routine:

                AdapterView.AdapterContextMenuInfo _info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

                int _canteenItemPosition = _info.position;

                AvailableCanteensModel.Item _canteenItem = adapter.getItem(_canteenItemPosition);

                presenter.requestRouteToCanteen(_canteenItem.schoolId, _canteenItem.id);

                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_FOR_SETTINGS_ACTIVITY){

            final AvailableSchoolsModel.Item school = getIntent().getParcelableExtra("school");

            presenter.requestCanteens(school.id);

        }

    }

    @Override
    public void showCanteens(AvailableCanteensModel canteens) {

        adapter.clear();

        adapter.addAll(canteens);

        adapter.notifyDataSetChanged();

    }

    @Override
    public void navigateToCanteenMenusPage(AvailableCanteensModel.Item canteen) {

        Intent intent = new Intent(this, AvailableCanteenMenusActivity.class);

        intent.putExtra("canteen", canteen);

        startActivity(intent);

    }

    @Override
    public void navigateToCanteenOnMapLocation(CanteenWithMapLocationModel canteen) {

        Intent intent = new Intent(AvailableCanteensActivity.this, CanteensLocationOnMapActivity.class);

        intent.putExtra("canteen", canteen);

        startActivity(intent);

    }

    @Override
    public void navigateBackToAvailableSchoolsPage() {

        finish();

    }

    @Override
    public void showUnavailableCanteenError() {

        Toast.makeText(this, "Canteen was not found", Toast.LENGTH_LONG).show();

    }

    @Override
    public void showUnavailableCanteensError() {

        Toast.makeText(this, "No Available Canteens", Toast.LENGTH_LONG).show();

    }

    @Override
    public void navigateToRouteToCanteen(CanteenWithMapLocationModel model) {

        Intent intent = new Intent(AvailableCanteensActivity.this, RouteToCanteenActivity.class);

        intent.putExtra("canteen-location", model);

        startActivity(intent);

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

    private class AvailableCanteensListAdapter extends ArrayAdapter<AvailableCanteensModel.Item> {

        public AvailableCanteensListAdapter(Context context, List<AvailableCanteensModel.Item> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            AvailableCanteensModel.Item canteen = getItem(position);

            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.available_canteens_list_view_item, parent, false);
            }

            canteen.schoolId = school.id;

            TextView canteenNameTextView = convertView.findViewById(R.id.available_canteens_list_view_item_canteen_name_text_view);

            canteenNameTextView.setText(canteen.name);

            return convertView;
        }
    }
}
