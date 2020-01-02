package com.ippementa.ipem.view.payment;


import android.content.Context;
import android.nfc.NfcManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ippementa.ipem.presenter.payment.PurchaseDishPresenter;

public class PurchaseDishActivity extends AppCompatActivity implements PurchaseDishView {

    private PurchaseDishPresenter presenter;

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
}
