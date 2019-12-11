package com.ippementa.ipem.view.menu;

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
import com.ippementa.ipem.presenter.canteen.AvailableCanteensModel;
import com.ippementa.ipem.presenter.menu.AvailableCanteenMenusModel;
import com.ippementa.ipem.presenter.menu.AvailableCanteenMenusPresenter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class AvailableCanteenMenusActivity extends AppCompatActivity implements AvailableCanteenMenusView{

    private AvailableCanteenMenusPresenter presenter;

    private AvailableCanteenMenusListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_canteen_menus);

        this.presenter = new AvailableCanteenMenusPresenter(this);

        AvailableCanteensModel.Item canteen = getIntent().getParcelableExtra("canteen");

        TextView headerTextView = findViewById(R.id.available_canteen_menus_header_text_view);

        headerTextView.setText(headerTextView.getText().toString() + " " + canteen.name);

        Button headerBackButton = findViewById(R.id.available_canteen_menus_header_back_button);

        headerBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navigateBackToAvailableCanteensPage();

            }
        });

        ListView menusListView = findViewById(R.id.available_canteen_menus_list_view);

        this.adapter = new AvailableCanteenMenusActivity.AvailableCanteenMenusListAdapter(this, new ArrayList<AvailableCanteenMenusModel.Item>());

        menusListView.setAdapter(adapter);

        this.presenter.requestMenus(canteen);
    }

    @Override
    public void showMenus(AvailableCanteenMenusModel menus) {

        adapter.clear();

        adapter.addAll(menus);

        adapter.notifyDataSetChanged();

    }

    @Override
    public void navigateToMenuDishesPage(AvailableCanteenMenusModel.Item menu) {

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

            menuTypeTextView.setText(menu.type);

            switch (menu.type.toLowerCase()){

                case "lunch":
                    menuTypeImageView.setImageDrawable(ContextCompat.getDrawable(AvailableCanteenMenusActivity.this, android.R.drawable.star_on));
                    break;
                default:
                    menuTypeImageView.setImageDrawable(ContextCompat.getDrawable(AvailableCanteenMenusActivity.this, android.R.drawable.star_off));
                    break;
            }

            return convertView;
        }
    }
}