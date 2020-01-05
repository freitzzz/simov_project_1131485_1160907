package com.ippementa.ipem.view.canteen;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ippementa.ipem.R;
import com.ippementa.ipem.presenter.canteen.CanteenWithMapLocationModel;
import com.ippementa.ipem.util.Provider;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidBitmap;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.overlay.Marker;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Activity that displays canteens as POI in a map
 * Initial boilerplate code was retrieved from: https://github.com/mapsforge/mapsforge/blob/master/mapsforge-samples-android/src/main/java/org/mapsforge/samples/android/GettingStarted.java
 */
public class CanteensLocationOnMapActivity extends Activity implements CanteensLocationOnMapView{

    // Name of the map file in device storage
    private static final String MAP_FILE = "porto.map";

    private MapView mapView;

    private static final int CANTEENS_LOCATION_ON_MAP_ACTIVITY_REQUEST_CODE_FOR_ACCESSING_DEVICE_LOCATION = 59823589;

    private LocationManager locationManager;

    private LocationListener locationListener;

    private boolean isToCenterMapOnDeviceLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CanteenWithMapLocationModel canteen = (CanteenWithMapLocationModel)getIntent().getParcelableExtra("canteen");

        /*
         * Before you make any calls on the mapsforge library, you need to initialize the
         * AndroidGraphicFactory. Behind the scenes, this initialization process gathers a bit of
         * information on your device, such as the screen resolution, that allows mapsforge to
         * automatically adapt the rendering for the device.
         * If you forget this step, your app will crash. You can place this code, like in the
         * Samples app, in the Android Application class. This ensures it is created before any
         * specific activity. But it can also be created in the onCreate() method in your activity.
         */
        AndroidGraphicFactory.createInstance(getApplication());

        /*
         * A MapView is an Android View (or ViewGroup) that displays a mapsforge map. You can have
         * multiple MapViews in your app or even a single Activity. Have a look at the mapviewer.xml
         * on how to create a MapView using the Android XML Layout definitions. Here we create a
         * MapView on the fly and make the content view of the activity the MapView. This means
         * that no other elements make up the content of this activity.
         */
        setContentView(R.layout.activity_canteens_location_on_map);
        mapView = findViewById(R.id.canteens_location_on_map_map_view);

        FloatingActionButton getDeviceLocationFloatingActionButton
                = findViewById(R.id.canteens_location_on_map_floating_action_button);

        getDeviceLocationFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean hasPermissionToAccessDeviceLocation
                        = ContextCompat.checkSelfPermission(
                                CanteensLocationOnMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

                if(!hasPermissionToAccessDeviceLocation) {

                    ActivityCompat.requestPermissions(
                            CanteensLocationOnMapActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            CANTEENS_LOCATION_ON_MAP_ACTIVITY_REQUEST_CODE_FOR_ACCESSING_DEVICE_LOCATION
                    );

                    registerGetLastKnowLocationHandler();

                }else{

                    if(locationManager == null){
                        registerGetLastKnowLocationHandler();
                    }else{
                        isToCenterMapOnDeviceLocation = true;
                    }

                }
            }
        });

        try {
            /*
             * We then make some simple adjustments, such as showing a scale bar and zoom controls.
             */
            mapView.getMapScaleBar().setVisible(true);
            mapView.setBuiltInZoomControls(true);

            /*
             * To avoid redrawing all the tiles all the time, we need to set up a tile cache with an
             * utility method.
             */
            TileCache tileCache = AndroidUtil.createTileCache(this, "mapcache",
                    mapView.getModel().displayModel.getTileSize(), 1f,
                    mapView.getModel().frameBufferModel.getOverdrawFactor());

            /*
             * Now we need to set up the process of displaying a map. A map can have several layers,
             * stacked on top of each other. A layer can be a map or some visual elements, such as
             * markers. Here we only show a map based on a mapsforge map file. For this we need a
             * TileRendererLayer. A TileRendererLayer needs a TileCache to hold the generated map
             * tiles, a map file from which the tiles are generated and Rendertheme that defines the
             * appearance of the map.
             */
            File mapFile = new File(getExternalFilesDir(null), MAP_FILE);

            if(!mapFile.exists()) {

                // As map file is packed in application build, it is necessary to read this file
                // and convert it to a file that will be located on the user external storage
                FileOutputStream out = new FileOutputStream(mapFile);
                copyStream(getResources().openRawResource(R.raw.porto), out);
                out.close();
            }

            MapDataStore mapDataStore = new MapFile(mapFile);

            TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, mapDataStore,
                    mapView.getModel().mapViewPosition, AndroidGraphicFactory.INSTANCE);
            tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.DEFAULT);

            /*
             * On its own a tileRendererLayer does not know where to display the map, so we need to
             * associate it with our mapView.
             */
            mapView.getLayerManager().getLayers().add(tileRendererLayer);

            /*
             * The map also needs to know which area to display and at what zoom level.
             * Note: this map position is specific to Berlin area.
             */
            mapView.setCenter(new LatLong(canteen.latitude, canteen.longitude));
            mapView.setZoomLevel((byte) 15);

            Marker canteenMarker = new Marker(
                    new LatLong(canteen.latitude, canteen.longitude),
                    new AndroidBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_fork_knife_ipp_ementa)),
                    0,
                    0
            );

            mapView.addLayer(canteenMarker);

            mapView.setClickable(true);
        } catch (Exception e) {
            /*
             * In case of map file errors avoid crash, but developers should handle these cases!
             */
            e.printStackTrace();

            Toast.makeText(this, R.string.map_rendering_error, Toast.LENGTH_LONG);

            navigateBackToSchoolCanteensPage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CANTEENS_LOCATION_ON_MAP_ACTIVITY_REQUEST_CODE_FOR_ACCESSING_DEVICE_LOCATION:

                if(grantResults.length > 0) {

                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        registerGetLastKnowLocationHandler();

                    }

                }

                break;
            default:
                break;
        }
    }

    private void registerGetLastKnowLocationHandler() {

        this.isToCenterMapOnDeviceLocation = true;

        this.locationManager
                = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        this.locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                System.out.println(location);

                if(isToCenterMapOnDeviceLocation) {

                    LatLong coordinates = new LatLong(location.getLatitude(), location.getLongitude());

                    mapView.setCenter(coordinates);

                    isToCenterMapOnDeviceLocation = false;

                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        boolean hasPermissionToAccessDeviceLocation
                = ContextCompat.checkSelfPermission(
                CanteensLocationOnMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if(hasPermissionToAccessDeviceLocation) {

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        }


    }

    @Override
    protected void onDestroy() {
        /*
         * Whenever your activity exits, some cleanup operations have to be performed lest your app
         * runs out of memory.
         */
        mapView.destroyAll();
        AndroidGraphicFactory.clearResourceMemoryCache();

        if (locationManager != null) {

            locationManager.removeUpdates(locationListener);

        }

        super.onDestroy();
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

    private static void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    @Override
    public void navigateBackToSchoolCanteensPage() {
        finish();
    }
}
