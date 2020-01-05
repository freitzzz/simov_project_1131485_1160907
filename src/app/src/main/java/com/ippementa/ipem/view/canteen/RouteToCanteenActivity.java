package com.ippementa.ipem.view.canteen;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.PathWrapper;
import com.graphhopper.routing.Path;
import com.graphhopper.util.Parameters;
import com.graphhopper.util.PointList;
import com.graphhopper.util.StopWatch;
import com.ippementa.ipem.R;
import com.ippementa.ipem.presenter.canteen.CanteenWithMapLocationModel;

import org.oscim.android.MapView;
import org.oscim.android.canvas.AndroidGraphics;
import org.oscim.backend.canvas.Bitmap;
import org.oscim.core.GeoPoint;
import org.oscim.event.Gesture;
import org.oscim.event.GestureListener;
import org.oscim.event.MotionEvent;
import org.oscim.layers.Layer;
import org.oscim.layers.marker.ItemizedLayer;
import org.oscim.layers.marker.MarkerItem;
import org.oscim.layers.marker.MarkerSymbol;
import org.oscim.layers.tile.buildings.BuildingLayer;
import org.oscim.layers.tile.vector.VectorTileLayer;
import org.oscim.layers.tile.vector.labeling.LabelLayer;
import org.oscim.layers.vector.PathLayer;
import org.oscim.layers.vector.geometries.Style;
import org.oscim.theme.VtmThemes;
import org.oscim.tiling.source.mapfile.MapFileTileSource;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class RouteToCanteenActivity extends AppCompatActivity{

    // Name of the mapsforge map file in device storage
    private static final String MAPSFORGE_MAP_FILE = "porto.map";

    private static final double DEFAULT_SCALE = 1 << 14;

    private static final int ROUTE_TO_CANTEEN_ACTIVITY_REQUEST_CODE_FOR_ACCESSING_DEVICE_LOCATION = 738;

    private MapView mapView;

    private GraphHopper hopper;

    private GeoPoint start;

    private GeoPoint end;

    private volatile boolean prepareInProgress = false;

    private volatile boolean shortestPathRunning = false;

    private ItemizedLayer<MarkerItem> itemizedLayer;

    private PathLayer pathLayer;

    private File mapsFolder;

    private LocationManager locationManager;

    private LocationListener locationListener;

    private FloatingActionButton centerDeviceLocationFloatingActionButton;

    private MarkerItem userMarker;

    private MarkerItem canteenMarker;

    private Location lastKnownUserLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);

        CanteenWithMapLocationModel canteenLocation = getIntent().getParcelableExtra("canteen-location");

        if(canteenLocation == null) {

            canteenLocation = new CanteenWithMapLocationModel();

            canteenLocation.name = getIntent().getStringExtra("name");

            canteenLocation.latitude = Double.parseDouble(getIntent().getStringExtra("latitude"));

            canteenLocation.longitude = Double.parseDouble(getIntent().getStringExtra("longitude"));

        }

        this.canteenMarker = createMarkerItem(new GeoPoint(canteenLocation.latitude, canteenLocation.longitude), R.drawable.icon_fork_knife_ipp_ementa);

        setContentView(R.layout.activity_route_to_canteen);

        mapView = findViewById(R.id.route_to_canteen_map_view);

        mapsFolder = getExternalFilesDir(null);

        this.centerDeviceLocationFloatingActionButton = findViewById(R.id.route_to_canteen_center_position_floating_action_button);

        initFiles();

        boolean hasPermissionToAccessDeviceLocation
                = ContextCompat.checkSelfPermission(
                RouteToCanteenActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if(!hasPermissionToAccessDeviceLocation) {

            ActivityCompat.requestPermissions(
                    RouteToCanteenActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    ROUTE_TO_CANTEEN_ACTIVITY_REQUEST_CODE_FOR_ACCESSING_DEVICE_LOCATION
            );

        }else{

            registerGetLastKnowLocationHandler();

        }

        centerDeviceLocationFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // if last know user location is null than wait for the map to be centered by
                // location listener callback
                if(lastKnownUserLocation != null){

                    mapView.map().setMapPosition(
                            lastKnownUserLocation.getLatitude(),
                            lastKnownUserLocation.getLongitude(),
                            mapView.map().getMapPosition().scale
                    );


                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (hopper != null)
            hopper.close();

        hopper = null;

        if (locationManager != null) {

            locationManager.removeUpdates(locationListener);

        }

        // necessary?
        System.gc();

        // Cleanup VTM
        mapView.map().destroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case ROUTE_TO_CANTEEN_ACTIVITY_REQUEST_CODE_FOR_ACCESSING_DEVICE_LOCATION:

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

        Toast.makeText(
                this,
                R.string.start_fetch_user_device_location_route_to_canteen,
                Toast.LENGTH_LONG
        ).show();

        this.locationManager
                = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        this.locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                GeoPoint point = new GeoPoint(location.getLatitude(), location.getLongitude());

                if(userMarker == null){

                    userMarker = createMarkerItem(point, R.drawable.icon_walk_black);

                }

                if(lastKnownUserLocation == null){

                    Toast.makeText(
                            RouteToCanteenActivity.this,
                            R.string.finish_fetch_user_device_location_route_to_canteen,
                            Toast.LENGTH_LONG
                    ).show();

                    mapView.map().setMapPosition(
                            location.getLatitude(),
                            location.getLongitude(),
                            mapView.map().getMapPosition().scale
                    );

                    if(hopper != null) {

                        calcPath(
                                userMarker.geoPoint.getLatitude(),
                                userMarker.geoPoint.getLongitude(),
                                canteenMarker.geoPoint.getLatitude(),
                                canteenMarker.geoPoint.getLongitude()
                        );

                    }

                    System.out.println("CENTERING POSITION");

                }

                System.out.println(location);

                itemizedLayer.removeItem(userMarker);

                userMarker = createMarkerItem(point, R.drawable.icon_walk_black);

                lastKnownUserLocation = location;

                itemizedLayer.addItem(userMarker);

                mapView.map().updateMap(true);
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
                this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if(hasPermissionToAccessDeviceLocation) {

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        }


    }

    private boolean onLongPress(GeoPoint point) {
        if (!isReady()) {
            return false;
        }

        if (shortestPathRunning) {
            Log.d("gh","Calculation still in progress");
            return false;
        }

        if (start != null && end == null) {
            end = point;
            shortestPathRunning = true;
            itemizedLayer.addItem(createMarkerItem(point, R.drawable.icon_map_marker_black));
            mapView.map().updateMap(true);

            calcPath(start.getLatitude(), start.getLongitude(), end.getLatitude(),
                    end.getLongitude());
        } else {
            start = point;
            end = null;
            // remove routing layers
            mapView.map().layers().remove(pathLayer);
            itemizedLayer.removeAllItems();

            itemizedLayer.addItem(createMarkerItem(start, R.drawable.icon_map_marker_white));
            mapView.map().updateMap(true);
        }
        return true;
    }

    private boolean isReady() {
        // only return true if already loaded
        if (hopper != null)
            return true;

        if (prepareInProgress) {
            Log.d("gh","Preparation still in progress");
            return false;
        }
        Log.d("gh","Prepare finished but GraphHopper not ready. This happens when there was an error while loading the files");
        return true;
    }

    @SuppressWarnings("deprecation")
    private MarkerItem createMarkerItem(GeoPoint p, int resource) {
        Drawable drawable = getResources().getDrawable(resource);

        Bitmap bitmap = AndroidGraphics.drawableToBitmap(drawable);

        MarkerSymbol markerSymbol = new MarkerSymbol(bitmap, 0.5f, 1);

        MarkerItem markerItem = new MarkerItem("", "", p);

        markerItem.setMarker(markerSymbol);

        return markerItem;
    }

    private void calcPath(final double fromLat, final double fromLon,
                         final double toLat, final double toLon) {

        Log.d("gh", "calculating path ...");

        CalculatePathAsyncTask calculatePathAsyncTask = new CalculatePathAsyncTask();

        CalculatePathAsyncTask.Request request = calculatePathAsyncTask.new Request();

        request.fromLatitude = fromLat;

        request.fromLongitude = fromLon;

        request.toLatitude = toLat;

        request.toLongitude = toLon;

        calculatePathAsyncTask.execute(request);

    }

    private PathLayer createPathLayer(PathWrapper response) {
        Style style = Style.builder()
                .fixed(true)
                .generalization(Style.GENERALIZATION_SMALL)
                .strokeColor(0x9900cc33)
                .strokeWidth(4 * getResources().getDisplayMetrics().density)
                .build();

        PathLayer pathLayer = new PathLayer(mapView.map(), style);

        List<GeoPoint> geoPoints = new ArrayList<>();

        PointList pointList = response.getPoints();

        for (int i = 0; i < pointList.getSize(); i++)
            geoPoints.add(new GeoPoint(pointList.getLatitude(i), pointList.getLongitude(i)));

        pathLayer.setPoints(geoPoints);

        return pathLayer;
    }

    private void loadMap() {
        Toast.makeText(this,R.string.loading_map_route_to_canteen, Toast.LENGTH_LONG).show();

        // Map events receiver
        mapView.map().layers().add(new MapEventsReceiver(mapView.map()));

        // Map file source
        MapFileTileSource tileSource = new MapFileTileSource();

        tileSource.setMapFile(new File(getExternalFilesDir(null), MAPSFORGE_MAP_FILE).getAbsolutePath());

        VectorTileLayer mapLayer = mapView.map().setBaseMap(tileSource);

        mapView.map().setTheme(VtmThemes.DEFAULT);

        mapView.map().layers().add(new BuildingLayer(mapView.map(), mapLayer));

        mapView.map().layers().add(new LabelLayer(mapView.map(), mapLayer));

        // Markers layer
        itemizedLayer = new ItemizedLayer<>(mapView.map(), (MarkerSymbol) null);

        mapView.map().layers().add(itemizedLayer);

        // Map position
        //TODO: GeoPoint mapCenter = tileSource.getMapInfo().boundingBox.getCenterPoint();

        itemizedLayer.addItem(canteenMarker);

        mapView.map().setMapPosition(canteenMarker.geoPoint.latitudeE6, canteenMarker.geoPoint.longitudeE6, DEFAULT_SCALE);

        loadGraphStorage();
    }

    private void loadGraphStorage() {
        LoadGraphHopperFilesAsyncTask loadGraphHopperFiles = new LoadGraphHopperFilesAsyncTask();

        loadGraphHopperFiles.execute();
    }

    private void finishPrepare() {
        prepareInProgress = false;
    }

    private void initFiles() {
        prepareInProgress = true;

        loadMap();

    }

    private class MapEventsReceiver extends Layer implements GestureListener {

        MapEventsReceiver(org.oscim.map.Map map) {
            super(map);
        }

        @Override
        public boolean onGesture(Gesture g, MotionEvent e) {

            System.out.println("Gesture: " + g);

            System.out.println("Motion Event: " + e);

            if (g instanceof Gesture.LongPress) {
                GeoPoint p = mMap.viewport().fromScreenPoint(e.getX(), e.getY());
                return onLongPress(p);
            }
            return false;
        }
    }

    /**
     * Loads GraphHopper files and creates a new instance of GraphHopper assigning it as a property of the activity
     */
    private class LoadGraphHopperFilesAsyncTask extends AsyncTask<Void, Void, LoadGraphHopperFilesAsyncTask.Result> {

        @Override
        protected Result doInBackground(Void... v) {

            Result result = new Result();

            try {

                GraphHopper graphHopper = new GraphHopper().forMobile();

                graphHopper.load(mapsFolder.getAbsolutePath());

                Log.d("gh", "found graph " + graphHopper.getGraphHopperStorage().toString() + ", nodes:" + graphHopper.getGraphHopperStorage().getNodes());

                result.hopper = graphHopper;

            }catch (Throwable error) {
                result.error = error;
            }

            return result;
        }

        @Override
        protected void onPostExecute(Result result) {

            boolean hasError = result.error != null;

            if (hasError) {
                Toast.makeText(RouteToCanteenActivity.this,R.string.error_loading_graph_hopper_route_to_canteen + result.error.getMessage(), Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(RouteToCanteenActivity.this,R.string.finished_loading_graph_hopper_route_to_canteen, Toast.LENGTH_LONG).show();
            }

            hopper = result.hopper;

            finishPrepare();

            if(userMarker != null) {

                calcPath(
                        userMarker.geoPoint.getLatitude(),
                        userMarker.geoPoint.getLongitude(),
                        canteenMarker.geoPoint.getLatitude(),
                        canteenMarker.geoPoint.getLongitude()
                );

            }
        }

        public class Result {

            public GraphHopper hopper;

            public Throwable error;

            public Path path;

        }

    }

    /**
     * Calculates the shortest path of two points
     */
    private class CalculatePathAsyncTask extends AsyncTask<CalculatePathAsyncTask.Request, Void, PathWrapper> {

        private float time;

        @Override
        protected PathWrapper doInBackground(Request... requests) {

            Request request = requests[0];

            StopWatch stopWatch = new StopWatch().start();

            GHRequest graphhoperRequest = new GHRequest(
                    request.fromLatitude,
                    request.fromLongitude,
                    request.toLatitude,
                    request.toLongitude
            ).setAlgorithm(Parameters.Algorithms.DIJKSTRA_BI);

            System.out.println(request.fromLatitude);

            System.out.println(request.fromLongitude);

            System.out.println(request.toLatitude);

            System.out.println(request.toLongitude);

            graphhoperRequest.getHints().put(Parameters.Routing.INSTRUCTIONS, "false");

            GHResponse graphhopperResponse = hopper.route(graphhoperRequest);

            time = stopWatch.stop().getSeconds();

            return graphhopperResponse.getBest();
        }

        @Override
        protected void onPostExecute(PathWrapper resp) {
            if (!resp.hasErrors()) {

                double distanceToTravelInKM = (int) (resp.getDistance() / 100) / 10f;

                Log.d("gh", "the route is " + distanceToTravelInKM
                        + "km long, time:" + resp.getTime() / 60000f + "min, debug:" + time);

                pathLayer = createPathLayer(resp);

                mapView.map().layers().add(pathLayer);

                mapView.map().updateMap(true);

                String finishedCalculatingMessage = getString(R.string.finished_calculating_path_route_to_canteen);

                float roundedDistance = BigDecimal.valueOf(distanceToTravelInKM).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

                Toast.makeText(RouteToCanteenActivity.this, finishedCalculatingMessage + roundedDistance + "KM", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(RouteToCanteenActivity.this, R.string.error_calculating_path_route_to_canteen, Toast.LENGTH_LONG).show();
            }
            shortestPathRunning = false;
        }

        public class Request {

            public double fromLatitude;

            public double fromLongitude;

            public double toLatitude;

            public double toLongitude;

        }
    }
}
