package com.ippementa.ipem.view.dish;


import android.content.Intent;
import android.content.res.Resources;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ippementa.ipem.R;
import com.ippementa.ipem.presenter.dish.PurchaseDishPresenter;
import com.ippementa.ipem.util.Provider;
import com.ippementa.ipem.view.settings.SettingsActivity;

import java.io.IOException;

public class PurchaseDishActivity extends AppCompatActivity implements PurchaseDishView, NfcAdapter.ReaderCallback {

    private PurchaseDishPresenter presenter;

    public static final int REQUEST_CODE_FOR_SETTINGS_ACTIVITY = 854;

    private NfcAdapter adapter;

    private TextView nfcResult;

    private int timesTagRead = 0; //number of times that the tag was read by the device

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_purchase_dish);

        this.nfcResult = (TextView) findViewById(R.id.nfc_result_text_view);

        this.nfcResult.setText("");

        adapter = (NfcAdapter) NfcAdapter.getDefaultAdapter(this);

        this.presenter = new PurchaseDishPresenter(this);

        if(this.presenter.checkIfDeviceHasNFCAvaliable() == true)
        {
            // check if nfc is enabled
            if(this.presenter.checkIfDeviceHasNFCOn() == false) Toast.makeText(this, R.string.nfc_turned_off, Toast.LENGTH_LONG).show();
        }
        else {
            //check if device has bluetooth

            if(this.presenter.checkIfDeviceHasBluetooth() == true) {
                //check if bluetooth is enabled

                if (this.presenter.checkIfDeviceHasBluetoothOn() == false)
                    Toast.makeText(this, R.string.bluetooth_turned_off, Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, R.string.no_bluetooth_nfc, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter.enableReaderMode( this, this,
                NfcAdapter.FLAG_READER_NFC_A,
                null);

    }

    @Override
    protected void onPause() {
        super.onPause();

        adapter.disableReaderMode(this);

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
    public void onTagDiscovered(Tag tag) {

        NfcA nfca = NfcA.get(tag);

        try {
            nfca.connect();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Code to run on UI thread
                    if(timesTagRead == 0) nfcResult.setText(R.string.purchase_done);
                    else nfcResult.setText(R.string.purchase_already_done);
                    timesTagRead++;
                }
            });

            nfca.close();

        }
        catch(IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

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

}
