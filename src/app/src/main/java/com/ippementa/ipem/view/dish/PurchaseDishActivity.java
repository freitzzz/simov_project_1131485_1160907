package com.ippementa.ipem.view.dish;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import java.lang.reflect.Method;


public class PurchaseDishActivity extends AppCompatActivity implements PurchaseDishView, NfcAdapter.ReaderCallback {

    private PurchaseDishPresenter presenter;

    public static final int REQUEST_CODE_FOR_SETTINGS_ACTIVITY = 854;

    public static final int REQUEST_ENABLE_BT = 550;

    private NfcAdapter adapter;

    private BluetoothAdapter blueAdapter;

    private BluetoothManager bluetoothManager;

    private TextView nfcResult;

    private int timesTagRead = 0; //number of times that the tag was read by the device

    private boolean connected = false; //flag that indicates the bluetooth connection

    // Create a BroadcastReceiver for ACTION_FOUND.

    private final BroadcastReceiver mPairReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                final int prevState = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                    //connected with headset //only if not already paired
                    purchaseDish();
                }

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Register Receiver
        IntentFilter intent = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mPairReceiver, intent);

        setContentView(R.layout.activity_purchase_dish);

        this.nfcResult = (TextView) findViewById(R.id.nfc_result_text_view);

        this.nfcResult.setText("");

        this.presenter = new PurchaseDishPresenter(this);

        /*if(this.presenter.checkIfDeviceHasNFCAvaliable() == true)
        {
            adapter = (NfcAdapter) NfcAdapter.getDefaultAdapter(this);
            // check if nfc is enabled
            if(this.presenter.checkIfDeviceHasNFCOn() == false) Toast.makeText(this, R.string.nfc_turned_off, Toast.LENGTH_LONG).show();
        }
        else {*/
        //check if device has bluetooth

        if (this.presenter.checkIfDeviceHasBluetooth() == true) {
            //check if bluetooth is enabled
            this.bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            this.blueAdapter = BluetoothAdapter.getDefaultAdapter();

        } else {
            Toast.makeText(this, R.string.no_bluetooth_nfc, Toast.LENGTH_SHORT).show();
        }
        //}
    }

    @Override
    protected void onStart() {
        super.onStart();


        if (this.presenter.checkIfDeviceHasBluetoothOn() == false) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            BluetoothDevice harmonyDevice = this.blueAdapter.getRemoteDevice("ED:2F:34:31:B4:BF");
            if (harmonyDevice != null) {
                this.pairDevice(harmonyDevice);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // nfc
        if (adapter != null) {
            adapter.enableReaderMode(this, this,
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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

    private void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void purchaseDish () {

        Toast.makeText(this, R.string.purchase_done, Toast.LENGTH_LONG).show();
    }

}

