package com.ippementa.ipem.view.dish;

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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ippementa.ipem.R;
import com.ippementa.ipem.presenter.dish.MenuDishesModel;
import com.ippementa.ipem.presenter.dish.MenuDishesPresenter;
import com.ippementa.ipem.presenter.menu.AvailableCanteenMenusModel;
import com.ippementa.ipem.util.Provider;
import com.ippementa.ipem.view.settings.SettingsActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MenuDishesActivity extends AppCompatActivity implements MenuDishesView{

    public static final int REQUEST_CODE_FOR_SETTINGS_ACTIVITY = 854;

    private MenuDishesPresenter presenter;

    private MenuDishesListAdapter adapter;

    private AvailableCanteenMenusModel.Item menu;

    private boolean isInDarkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_dishes);

        this.presenter = new MenuDishesPresenter(this);

        this.menu = getIntent().getParcelableExtra("menu");

        // If menu is null then the activity was launched from FCM service

        if(this.menu == null){

            Bundle data = getIntent().getExtras();

            this.menu = new AvailableCanteenMenusModel.Item();

            long schoolId = Long.parseLong(data.getString("schoolId"));

            long canteenId = Long.parseLong(data.getString("canteenId"));

            long menuId = Long.parseLong(data.getString("menuId"));

            menu.schoolId = schoolId;

            menu.canteenId = canteenId;

            menu.id = menuId;

        }

        ImageView headerBackImageView = findViewById(R.id.menu_dishes_header_back_image_view);

        this.isInDarkMode = Provider.instance(this).settings().isInDarkMode();

        if(this.isInDarkMode){

            headerBackImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_arrow_left_white));

            LinearLayout layoutBackAndHeader = findViewById(R.id.menu_dishes_header_linear_layout);

            layoutBackAndHeader.setBackgroundColor(ContextCompat.getColor(this, R.color.colorDark));

        }

        headerBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navigateBackToAvailableCanteenMenusPage();

            }
        });

        ListView menusListView = findViewById(R.id.menu_dishes_list_view);

        registerForContextMenu(menusListView);

        this.adapter = new MenuDishesActivity.MenuDishesListAdapter(this, new ArrayList<MenuDishesModel.Item>());

        menusListView.setAdapter(adapter);

        this.presenter.requestDishes(menu);
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

            final AvailableCanteenMenusModel.Item menu = getIntent().getParcelableExtra("menu");

            presenter.requestDishes(menu);

        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_dishes_context_menu, menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()) {
            case R.id.purchase_dish_bluetooth:
                 intent = new Intent(this, PurchaseDishBluetoothActivity.class);

                startActivity(intent);

                return true;

            case R.id.purchase_dish_nfc:
                intent = new Intent(this,PurchaseDishNFCActivity.class);

                startActivity(intent);

                return true;
        }
        return true;
    }

    @Override
    public void showMenus(MenuDishesModel dishes) {

        adapter.clear();

        adapter.addAll(dishes);

        adapter.notifyDataSetChanged();

    }

    @Override
    public void navigateBackToAvailableCanteenMenusPage() {
        finish();
    }

    @Override
    public void showUnavailableDishesError() {

        Toast.makeText(this, R.string.dishes_not_found_menu_dishes_activity, Toast.LENGTH_LONG).show();

    }

    @Override
    public void showUnavailableDishError() {

        Toast.makeText(this, R.string.dish_not_found_menu_dishes_activity, Toast.LENGTH_LONG).show();

    }

    @Override
    public void showDishWasMarkedFavoriteWithSuccessToast() {

        Toast.makeText(this, R.string.dish_marked_as_favorite_with_success_menu_dishes_activity, Toast.LENGTH_LONG).show();

    }

    @Override
    public void showDishWasUnmarkedFavoriteWithSuccessToast() {

        Toast.makeText(this, R.string.dish_unmarked_as_favorite_with_success_menu_dishes_activity, Toast.LENGTH_LONG).show();

    }

    @Override
    public void showErrorOccurredMarkingDishAsFavoriteToast() {

        Toast.makeText(this, R.string.error_occurred_marking_dish_as_favorite, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showErrorOccurredUnmarkingDishAsFavoriteToast() {

        Toast.makeText(this, R.string.error_occurred_unmarking_dish_as_favorite, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void markDishAsFavorite(MenuDishesModel.Item dish) {

        dish.isFavorite = true;

        adapter.notifyDataSetChanged();

    }

    @Override
    public void unmarkDishAsFavorite(MenuDishesModel.Item dish) {

        dish.isFavorite = false;

        adapter.notifyDataSetChanged();

    }

    @Override
    public void showMarkingDishAsFavoriteRequiresInternetConnectionToast() {

        Toast.makeText(this, R.string.marking_dish_as_favorite_requires_internet_connection, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showUnmarkingDishAsFavoriteRequiresInternetConnectionToast() {

        Toast.makeText(this, R.string.unmarking_dish_as_favorite_requires_internet_connection, Toast.LENGTH_SHORT).show();

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

    private class MenuDishesListAdapter extends ArrayAdapter<MenuDishesModel.Item> {

        public MenuDishesListAdapter(Context context, List<MenuDishesModel.Item> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            final MenuDishesModel.Item dish = getItem(position);

            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.menu_dishes_list_view_item, parent, false);
            }

            TextView dishTypeTextView = convertView.findViewById(R.id.menu_dishes_list_view_item_type_text_view);

            TextView dishDescriptionTextView = convertView.findViewById(R.id.menu_dishes_list_view_item_description_text_view);

            ImageView dishTypeImageView = convertView.findViewById(R.id.menu_dishes_list_view_item_type_image_view);

            final ImageView dishFavoriteMarkImageView = convertView.findViewById(R.id.menu_dishes_list_view_item_favorite_mark_image_view);

            dishTypeTextView.setText(dish.typeAsString);

            dishDescriptionTextView.setText(dish.description);

            switch (dish.type){

                case MEAT:

                    if(isInDarkMode){
                        dishTypeImageView.setImageDrawable(ContextCompat.getDrawable(MenuDishesActivity.this, R.drawable.icon_sausage_white));
                    }else{
                        dishTypeImageView.setImageDrawable(ContextCompat.getDrawable(MenuDishesActivity.this, R.drawable.icon_sausage_black));
                    }

                    break;
                case FISH:

                    if(isInDarkMode){
                        dishTypeImageView.setImageDrawable(ContextCompat.getDrawable(MenuDishesActivity.this, R.drawable.icon_fish_white));
                    }else{
                        dishTypeImageView.setImageDrawable(ContextCompat.getDrawable(MenuDishesActivity.this, R.drawable.icon_fish_black));
                    }

                    break;
                case VEGETARIAN:

                    if(isInDarkMode){
                        dishTypeImageView.setImageDrawable(ContextCompat.getDrawable(MenuDishesActivity.this, R.drawable.icon_leaf_white));
                    }else{
                        dishTypeImageView.setImageDrawable(ContextCompat.getDrawable(MenuDishesActivity.this, R.drawable.icon_leaf_black));
                    }

                    break;
                default:

                    if(isInDarkMode){
                        dishTypeImageView.setImageDrawable(ContextCompat.getDrawable(MenuDishesActivity.this, R.drawable.icon_noodles_white));
                    }else{
                        dishTypeImageView.setImageDrawable(ContextCompat.getDrawable(MenuDishesActivity.this, R.drawable.icon_noodles_black));
                    }

                    break;
            }

            if(dish.isFavorite) {

                if(isInDarkMode){
                    dishFavoriteMarkImageView.setImageDrawable(ContextCompat.getDrawable(MenuDishesActivity.this, R.drawable.icon_star_white));
                }else{
                    dishFavoriteMarkImageView.setImageDrawable(ContextCompat.getDrawable(MenuDishesActivity.this, R.drawable.icon_star_black));
                }

            }else{

                if(isInDarkMode){
                    dishFavoriteMarkImageView.setImageDrawable(ContextCompat.getDrawable(MenuDishesActivity.this, R.drawable.icon_star_outline_white));
                }else{
                    dishFavoriteMarkImageView.setImageDrawable(ContextCompat.getDrawable(MenuDishesActivity.this, R.drawable.icon_star_outline_black));
                }

            }

            dishFavoriteMarkImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!dish.isFavorite) {

                        dish.isFavorite = true;

                        if(isInDarkMode){
                            dishFavoriteMarkImageView.setImageDrawable(ContextCompat.getDrawable(MenuDishesActivity.this, R.drawable.icon_star_white));
                        }else{
                            dishFavoriteMarkImageView.setImageDrawable(ContextCompat.getDrawable(MenuDishesActivity.this, R.drawable.icon_star_black));
                        }

                        presenter.markAsFavorite(MenuDishesActivity.this.menu, dish);

                    }else{

                        dish.isFavorite = false;

                        if(isInDarkMode){
                            dishFavoriteMarkImageView.setImageDrawable(ContextCompat.getDrawable(MenuDishesActivity.this, R.drawable.icon_star_outline_white));
                        }else{
                            dishFavoriteMarkImageView.setImageDrawable(ContextCompat.getDrawable(MenuDishesActivity.this, R.drawable.icon_star_outline_black));
                        }

                        presenter.unmarkAsFavorite(MenuDishesActivity.this.menu, dish);

                    }
                }
            });

            return convertView;
        }
    }
}
