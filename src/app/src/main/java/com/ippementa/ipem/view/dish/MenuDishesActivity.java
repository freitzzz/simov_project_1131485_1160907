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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.ippementa.ipem.R;
import com.ippementa.ipem.presenter.dish.MenuDishesModel;
import com.ippementa.ipem.presenter.dish.MenuDishesPresenter;
import com.ippementa.ipem.presenter.menu.AvailableCanteenMenusModel;
import com.ippementa.ipem.util.Provider;
import com.ippementa.ipem.view.settings.SettingsActivity;

import java.util.ArrayList;
import java.util.List;

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

        Button headerBackButton = findViewById(R.id.menu_dishes_header_back_button);

        this.isInDarkMode = Provider.instance(this).settings().isInDarkMode();

        if(this.isInDarkMode){
            headerBackButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_arrow_left_white, 0, 0,0 );
        }

        headerBackButton.setOnClickListener(new View.OnClickListener() {
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
        switch(item.getItemId()) {
            case R.id.purchase_dish:
                Intent intent = (Intent) new Intent(this, PurchaseDishActivity.class);

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

        Toast.makeText(this, "No Dishes Available", Toast.LENGTH_LONG).show();

    }

    @Override
    public void showUnavailableDishError() {

        Toast.makeText(this, "Dish is not available", Toast.LENGTH_LONG).show();

    }

    @Override
    public void showDishWasMarkedFavoriteWithSuccessToast() {

        Toast.makeText(this, "Dish was marked as favorite with success!", Toast.LENGTH_LONG).show();

    }

    @Override
    public void showDishWasUnmarkedFavoriteWithSuccessToast() {

        Toast.makeText(this, "Dish was unmarked as favorite with success!", Toast.LENGTH_LONG).show();

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
