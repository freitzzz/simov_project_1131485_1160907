package com.ippementa.ipem.presenter.dish;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;

import com.ippementa.ipem.presenter.IPresenter;
import com.ippementa.ipem.view.dish.PurchaseDishActivity;
import com.ippementa.ipem.view.dish.PurchaseDishView;

public class PurchaseDishPresenter implements IPresenter{

    private PurchaseDishView view;

    private boolean isViewAvaliableToUse;

    public PurchaseDishPresenter(PurchaseDishView view) {
        this.view = view;
        this.isViewAvaliableToUse = true;
    }

    @Override
    public void onDestroy() {
        this.view = null;
        this.isViewAvaliableToUse = false;
    }

    @Override
    public void onResume() {
        this.isViewAvaliableToUse = true;
    }

    @Override
    public void onPause() {
        this.isViewAvaliableToUse = false;
    }

    public boolean checkIfDeviceHasNFCAvaliable() {

        boolean hasNFC = false;

        PurchaseDishActivity context = (PurchaseDishActivity) this.view;

        NfcManager manager = (NfcManager)  context.getSystemService(Context.NFC_SERVICE);
        if(manager != null) {
            NfcAdapter adapter = (NfcAdapter) NfcAdapter.getDefaultAdapter(context);
            if (adapter != null) hasNFC = true;
        }
        return hasNFC;
    }

    public boolean checkIfDeviceHasNFCOn() {

        boolean isNFCOn = false;

        PurchaseDishActivity context = (PurchaseDishActivity) this.view;

        NfcManager manager = (NfcManager)  context.getSystemService(Context.NFC_SERVICE);

        NfcAdapter adapter = (NfcAdapter) NfcAdapter.getDefaultAdapter(context);
        if (adapter.isEnabled()) isNFCOn = true;

        return isNFCOn;
    }

    public boolean checkIfDeviceHasBluetooth () {

        boolean hasBluetooth = false;

        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

        if(adapter != null) hasBluetooth = true;

        return hasBluetooth;
    }

    public boolean checkIfDeviceHasBluetoothOn () {

        boolean isBluetoothOn = false;

        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

        if(adapter != null && adapter.isEnabled()) isBluetoothOn = true;

        return isBluetoothOn;
    }
}
