package com.ippementa.ipem.view.menu;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ippementa.ipem.R;
import com.ippementa.ipem.presenter.canteen.AvailableCanteensModel;
import com.ippementa.ipem.presenter.menu.AvailableCanteenMenusModel;
import com.ippementa.ipem.presenter.menu.AvailableCanteenMenusPresenter;
import com.ippementa.ipem.util.Provider;
import com.ippementa.ipem.view.dish.MenuDishesActivity;
import com.ippementa.ipem.view.settings.SettingsActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class AvailableCanteenMenusActivity extends AppCompatActivity implements AvailableCanteenMenusView{

    public static final int REQUEST_CODE_FOR_SETTINGS_ACTIVITY = 854;

    private AvailableCanteenMenusPresenter presenter;

    private AvailableCanteenMenusListAdapter adapter;

    private boolean isInDarkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_canteen_menus);

        this.presenter = new AvailableCanteenMenusPresenter(this);

        final AvailableCanteensModel.Item canteen = getIntent().getParcelableExtra("canteen");

        TextView headerTextView = findViewById(R.id.available_canteen_menus_header_text_view);

        headerTextView.setText(headerTextView.getText().toString() + " " + canteen.name);

        ImageView headerBackImageView = findViewById(R.id.available_canteen_menus_header_back_image_view);

        this.isInDarkMode = Provider.instance(this).settings().isInDarkMode();

        if(this.isInDarkMode){

            headerBackImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_arrow_left_white));

            LinearLayout layoutBackAndHeader = findViewById(R.id.available_canteen_menus_header_linear_layout);

            layoutBackAndHeader.setBackgroundColor(ContextCompat.getColor(this, R.color.colorDark));

        }

        headerBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navigateBackToAvailableCanteensPage();

            }
        });

        ListView menusListView = findViewById(R.id.available_canteen_menus_list_view);

        menusListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AvailableCanteenMenusModel.Item menu = adapter.getItem(position);

                menu.schoolId = canteen.schoolId;

                menu.canteenId = canteen.id;

                navigateToMenuDishesPage(menu);
            }
        });

        this.adapter = new AvailableCanteenMenusActivity.AvailableCanteenMenusListAdapter(this, new ArrayList<AvailableCanteenMenusModel.Item>());

        menusListView.setAdapter(adapter);

        this.presenter.requestMenus(canteen);
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

            final AvailableCanteensModel.Item canteen = getIntent().getParcelableExtra("canteen");

            presenter.requestMenus(canteen);

        }

    }

    @Override
    public void showMenus(AvailableCanteenMenusModel menus) {

        adapter.clear();

        adapter.addAll(menus);

        adapter.notifyDataSetChanged();

    }

    @Override
    public void navigateToMenuDishesPage(AvailableCanteenMenusModel.Item menu) {

        Intent intent = new Intent(this, MenuDishesActivity.class);

        intent.putExtra("menu", menu);

        startActivity(intent);

    }

    @Override
    public void navigateBackToAvailableCanteensPage() {

        finish();

    }

    @Override
    public void showUnavailableMenusError() {

        Toast.makeText(this, "Menus Not Available", Toast.LENGTH_LONG).show();

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

    private class AvailableCanteenMenusListAdapter extends ArrayAdapter<AvailableCanteenMenusModel.Item> {

        public AvailableCanteenMenusListAdapter(Context context, List<AvailableCanteenMenusModel.Item> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            AvailableCanteenMenusModel.Item menu = getItem(position);

            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.available_canteen_menus_list_view_item, parent, false);
            }

            TextView menuTypeTextView = convertView.findViewById(R.id.available_canteen_menus_list_view_item_menu_type_text_view);

            ImageView menuTypeImageView = convertView.findViewById(R.id.available_canteen_menus_list_view_item_menu_type_image_view);

            menuTypeTextView.setText(menu.typeAsString);

            switch (menu.type){



                case LUNCH:
                    
                    if(isInDarkMode){
                        menuTypeImageView.setImageDrawable(ContextCompat.getDrawable(AvailableCanteenMenusActivity.this, R.drawable.icon_sun_white));
                    }else{
                        menuTypeImageView.setImageDrawable(ContextCompat.getDrawable(AvailableCanteenMenusActivity.this, R.drawable.icon_sun_black));
                    }

                    break;
                default:

                    if(isInDarkMode){
                        menuTypeImageView.setImageDrawable(ContextCompat.getDrawable(AvailableCanteenMenusActivity.this, R.drawable.icon_moon_white));
                    }else{
                        menuTypeImageView.setImageDrawable(ContextCompat.getDrawable(AvailableCanteenMenusActivity.this, R.drawable.icon_moon_black));
                    }

                    break;
            }

            return convertView;
        }
    }
}
