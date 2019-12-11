package com.ippementa.ipem.view.canteen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.ippementa.ipem.presenter.school.AvailableSchoolsModel;
import com.ippementa.ipem.view.menu.AvailableCanteenMenusActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AvailableCanteensActivity extends AppCompatActivity implements AvailableCanteensView{

    private AvailableCanteensPresenter presenter;

    private AvailableCanteensListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_available_canteens);

        this.presenter = new AvailableCanteensPresenter(this);

        final AvailableSchoolsModel.Item school = getIntent().getParcelableExtra("school");

        TextView headerTextView = findViewById(R.id.available_canteens_header_text_view);

        headerTextView.setText(headerTextView.getText().toString() + " " + school.acronym);

        Button headerBackButton = findViewById(R.id.available_canteens_header_back_button);

        headerBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navigateBackToAvailableSchoolsPage();

            }
        });

        ListView canteensListView = findViewById(R.id.available_canteens_list_view);

        canteensListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AvailableCanteensModel.Item canteen = adapter.getItem(position);

                canteen.schoolId = school.id;

                navigateToCanteenMenusPage(canteen);
            }
        });

        this.adapter = new AvailableCanteensActivity.AvailableCanteensListAdapter(this, new ArrayList<AvailableCanteensModel.Item>());

        canteensListView.setAdapter(adapter);

        this.presenter.requestCanteens(school.id);
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
    public void navigateBackToAvailableSchoolsPage() {

        finish();

    }

    @Override
    public void showUnavailableCanteensError() {

        Toast.makeText(this, "No Available Canteens", Toast.LENGTH_LONG).show();

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

            TextView canteenNameTextView = convertView.findViewById(R.id.available_canteens_list_view_item_canteen_name_text_view);

            canteenNameTextView.setText(canteen.name);

            return convertView;
        }
    }
}
