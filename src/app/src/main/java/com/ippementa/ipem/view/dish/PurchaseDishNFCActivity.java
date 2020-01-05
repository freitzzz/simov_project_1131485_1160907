package com.ippementa.ipem.view.dish;

import android.content.Intent;
import android.content.res.Resources;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ippementa.ipem.R;
import com.ippementa.ipem.presenter.dish.PurchaseDishPresenter;
import com.ippementa.ipem.util.Provider;

import java.io.IOException;

public class PurchaseDishNFCActivity extends AppCompatActivity implements PurchaseDishView, NfcAdapter.ReaderCallback {

    private PurchaseDishPresenter presenter;

    private TextView isNfcReady;

    private TextView nfcResult;

    private NfcAdapter adapter;

    private int timesTagRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_dish_nfc);

        this.presenter = new PurchaseDishPresenter(this);
        this.isNfcReady = findViewById(R.id.is_nfc_ready);
        this.nfcResult = findViewById(R.id.nfc_result_text_view);
        this.timesTagRead = 0;

        if(this.presenter.checkIfDeviceHasNFCAvaliable() == true)
        {
            this.adapter = NfcAdapter.getDefaultAdapter(this);
            // check if nfc is enabled
            if(this.presenter.checkIfDeviceHasNFCOn() == false) {
                Toast.makeText(getApplicationContext(), R.string.activate_nfc, Toast.LENGTH_LONG).show();
                startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
            }
        }
    }

    @Override
    public Resources.Theme getTheme() {

        Resources.Theme theme = super.getTheme();

        boolean isInDarkMode = Provider.instance(this).settings().isInDarkMode();

        if (isInDarkMode) {
            theme.applyStyle(R.style.DarkMode, true);
        } else {
            theme.applyStyle(R.style.LightMode, true);
        }

        return theme;

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(this.presenter.checkIfDeviceHasNFCOn()) changeTextNFCReady();

        // nfc
        if (this.adapter != null) {
            this.adapter.enableReaderMode(this, this,
                    NfcAdapter.FLAG_READER_NFC_A,
                    null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (adapter != null) adapter.disableReaderMode(this); // nfc
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
                    if (timesTagRead == 0) nfcResult.setText(R.string.purchase_done);
                    else nfcResult.setText(R.string.purchase_already_done);
                    timesTagRead++;
                }
            });

            nfca.close();

        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void changeTextNFCReady() {
        this.isNfcReady.setText(R.string.read_nfc);
    }
}
