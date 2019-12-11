package com.ippementa.ipem.view.dish;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
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

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MenuDishesActivity extends AppCompatActivity implements MenuDishesView{

    private MenuDishesPresenter presenter;

    private MenuDishesListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_dishes);

        this.presenter = new MenuDishesPresenter(this);

        AvailableCanteenMenusModel.Item menu = getIntent().getParcelableExtra("menu");

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

            TextView menuTypeTextView = convertView.findViewById(R.id.menu_dishes_list_view_item_type_text_view);

            TextView menuDescriptionTextView = convertView.findViewById(R.id.menu_dishes_list_view_item_description_text_view);

            ImageView menuTypeImageView = convertView.findViewById(R.id.menu_dishes_list_view_item_type_image_view);

            menuTypeTextView.setText(dish.type);

            menuDescriptionTextView.setText(dish.description);

            switch (dish.type.toLowerCase()){

                case "meat":
                    menuTypeImageView.setImageDrawable(ContextCompat.getDrawable(MenuDishesActivity.this, android.R.drawable.star_on));
                    break;
                case "fish":
                    menuTypeImageView.setImageDrawable(ContextCompat.getDrawable(MenuDishesActivity.this, android.R.drawable.arrow_up_float));
                    break;
                case "diet":
                    menuTypeImageView.setImageDrawable(ContextCompat.getDrawable(MenuDishesActivity.this, android.R.drawable.ic_dialog_email));
                    break;
                default:
                    menuTypeImageView.setImageDrawable(ContextCompat.getDrawable(MenuDishesActivity.this, android.R.drawable.star_off));
                    break;
            }

            return convertView;
        }
    }
}