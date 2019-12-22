package com.ippementa.ipem.view.settings;

import android.os.Bundle;
import android.widget.Toast;

import com.ippementa.ipem.R;
import com.ippementa.ipem.presenter.settings.SettingsPresenter;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity implements SettingsView{

    private SettingsFragment contentFragment;

    private SettingsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.presenter = new SettingsPresenter(this);

        this.contentFragment = new SettingsFragment();

        Bundle fragmentArguments = new Bundle();

        fragmentArguments.putSerializable("presenter", presenter);

        this.contentFragment.setArguments(fragmentArguments);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_settings_root_layout, this.contentFragment)
                .commit();

        // TODO: Network errors in fragment
        // Only insert in database the necessary files after download
        // Redirect user after download
        // Block user change of offline mode while data is being downloaded

    }

    @Override
    public void enableOfflineModeSwitchInteraction() {

        this.contentFragment.enableOfflineModeSwitchInteraction();

    }

    @Override
    public void disableOfflineModeSwitchInteraction() {

        this.contentFragment.disableOfflineModeSwitchInteraction();

    }

    @Override
    public void showOfflineDataDownloadStartSnackbar() {

        Toast.makeText(this,
                R.string.settings_offline_data_download_start,
                Toast.LENGTH_LONG)
                .show();

    }

    @Override
    public void showOfflineDataDownloadFinishSnackbar() {

        Toast.makeText(this,
                R.string.settings_offline_data_download_finish,
                Toast.LENGTH_LONG)
                .show();

    }

    @Override
    public void showNoInternetConnectionError() {

        Toast.makeText(this,
                R.string.no_internet_connection_error,
                Toast.LENGTH_LONG)
                .show();

    }

    @Override
    public void showServerNotAvailableError() {
        Toast.makeText(this,
                R.string.server_not_available_error,
                Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void showUnexepectedServerFailureError() {
        Toast.makeText(this,
                R.string.unexpected_server_failure_error,
                Toast.LENGTH_LONG)
                .show();
    }
}
