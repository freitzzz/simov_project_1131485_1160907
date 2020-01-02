package com.ippementa.ipem.view.dish;


import android.content.Context;
import android.content.Intent;
import android.nfc.NfcManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.ippementa.ipem.R;
import com.ippementa.ipem.presenter.dish.PurchaseDishPresenter;
import com.ippementa.ipem.view.settings.SettingsActivity;

public class PurchaseDishActivity extends AppCompatActivity implements PurchaseDishView {

    private PurchaseDishPresenter presenter;

    public static final int REQUEST_CODE_FOR_SETTINGS_ACTIVITY = 854;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.presenter = new PurchaseDishPresenter(this);

        if(this.checkIfDeviceHasNFCAvaliable())
        {
            // ask for permission to use nfc
        }
        else {
            // ask for permission to use Bluetooth
        }
    }

    @Override
    public boolean checkIfDeviceHasNFCAvaliable() {
        boolean hasNFC = false;

        NfcManager manager = (NfcManager) this.getSystemService(Context.NFC_SERVICE);
        if(manager != null) hasNFC = true;
        return hasNFC;
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
}
