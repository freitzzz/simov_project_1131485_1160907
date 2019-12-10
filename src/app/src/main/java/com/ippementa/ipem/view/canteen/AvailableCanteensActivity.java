package com.ippementa.ipem.view.canteen;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ippementa.ipem.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AvailableCanteensActivity extends AppCompatActivity {

    private List<String[]> availableCanteens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_available_canteens);

        String schoolName = getIntent().getStringExtra("school.name");

        TextView headerTextView = findViewById(R.id.available_canteens_header_text_view);

        headerTextView.setText(headerTextView.getText().toString() + schoolName);

        ListView canteensListView = findViewById(R.id.available_canteens_list_view);

        this.availableCanteens = (ArrayList<String[]>)getIntent().getExtras().getSerializable("canteens");

        canteensListView.setAdapter(new AvailableCanteensActivity.AvailableCanteensListAdapter(this, availableCanteens));
    }

    private class AvailableCanteensListAdapter extends ArrayAdapter<String[]> {

        public AvailableCanteensListAdapter(Context context, List<String[]> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            String[] canteen = getItem(position);

            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.available_canteens_list_view_item, parent, false);
            }

            TextView canteenNameTextView = convertView.findViewById(R.id.available_canteens_list_view_item_canteen_name_text_view);

            canteenNameTextView.setText(canteen[1]);

            return convertView;
        }
    }
}
