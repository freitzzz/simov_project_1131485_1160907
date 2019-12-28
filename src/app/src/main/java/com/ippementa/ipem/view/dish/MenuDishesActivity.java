package com.ippementa.ipem.view.dish;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.ippementa.ipem.R;
import com.ippementa.ipem.presenter.dish.MenuDishesModel;
import com.ippementa.ipem.presenter.dish.MenuDishesPresenter;
import com.ippementa.ipem.presenter.menu.AvailableCanteenMenusModel;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_dishes);

        this.presenter = new MenuDishesPresenter(this);

        final AvailableCanteenMenusModel.Item menu = getIntent().getParcelableExtra("menu");

        Button headerBackButton = findViewById(R.id.menu_dishes_header_back_button);

        headerBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navigateBackToAvailableCanteenMenusPage();

            }
        });

        ListView menusListView = findViewById(R.id.menu_dishes_list_view);

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

    private class MenuDishesListAdapter extends ArrayAdapter<MenuDishesModel.Item> {

        public MenuDishesListAdapter(Context context, List<MenuDishesModel.Item> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            MenuDishesModel.Item dish = getItem(position);

            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.menu_dishes_list_view_item, parent, false);
            }

            TextView dishTypeTextView = convertView.findViewById(R.id.menu_dishes_list_view_item_type_text_view);

            TextView dishDescriptionTextView = convertView.findViewById(R.id.menu_dishes_list_view_item_description_text_view);

            ImageView dishTypeImageView = convertView.findViewById(R.id.menu_dishes_list_view_item_type_image_view);

            ImageView dishFavoriteMarkImageView = convertView.findViewById(R.id.menu_dishes_list_view_item_favorite_mark_image_view);

            dishTypeTextView.setText(dish.typeAsString);

            dishDescriptionTextView.setText(dish.description);

            switch (dish.type){

                case MEAT:
                    dishTypeImageView.setImageDrawable(ContextCompat.getDrawable(MenuDishesActivity.this, R.drawable.icon_dish_meat));
                    break;
                case FISH:
                    dishTypeImageView.setImageDrawable(ContextCompat.getDrawable(MenuDishesActivity.this, R.drawable.icon_dish_fish));
                    break;
                case VEGETARIAN:
                    dishTypeImageView.setImageDrawable(ContextCompat.getDrawable(MenuDishesActivity.this, R.drawable.icon_dish_vegetarian));
                    break;
                default:
                    dishTypeImageView.setImageDrawable(ContextCompat.getDrawable(MenuDishesActivity.this, R.drawable.icon_dish_diet));
                    break;
            }

            if(dish.isFavorite) {
                dishFavoriteMarkImageView.setImageDrawable(ContextCompat.getDrawable(MenuDishesActivity.this, R.drawable.icon_dish_is_favorite));
            }else{
                dishFavoriteMarkImageView.setImageDrawable(ContextCompat.getDrawable(MenuDishesActivity.this, R.drawable.icon_dish_is_not_favorite));
            }
            return convertView;
        }
    }
}
