package com.ippementa.ipem.view.canteen;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.PathWrapper;
import com.graphhopper.routing.Path;
import com.graphhopper.util.Constants;
import com.graphhopper.util.Helper;
import com.graphhopper.util.Parameters;
import com.graphhopper.util.PointList;
import com.graphhopper.util.StopWatch;
import com.ippementa.ipem.R;

import org.oscim.android.MapView;
import org.oscim.android.canvas.AndroidGraphics;
import org.oscim.backend.AssetAdapter;
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
import org.oscim.theme.IRenderTheme;
import org.oscim.theme.ThemeFile;
import org.oscim.theme.XmlRenderThemeMenuCallback;
import org.oscim.tiling.source.mapfile.MapFileTileSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import androidx.appcompat.app.AppCompatActivity;

public class RouteToCanteenActivity extends AppCompatActivity{

    public static final int REQUEST_CODE_FOR_SETTINGS_ACTIVITY = 854;

    private MapView mapView;

    private GraphHopper hopper;

    private GeoPoint start;

    private GeoPoint end;

    private volatile boolean prepareInProgress = false;

    private volatile boolean shortestPathRunning = false;

    private ItemizedLayer<MarkerItem> itemizedLayer;

    private PathLayer pathLayer;

    private File mapsFolder;

    private Spinner localSpinner;

    private Button localButton;

    private Spinner remoteSpinner;

    private Button remoteButton;

    protected boolean onLongPress(GeoPoint p) {
        if (!isReady())
            return false;

        if (shortestPathRunning) {
            Log.d("gh","Calculation still in progress");
            return false;
        }

        if (start != null && end == null) {
            end = p;
            shortestPathRunning = true;
            itemizedLayer.addItem(createMarkerItem(p, R.drawable.icon_map_marker_black));
            mapView.map().updateMap(true);

            calcPath(start.getLatitude(), start.getLongitude(), end.getLatitude(),
                    end.getLongitude());
        } else {
            start = p;
            end = null;
            // remove routing layers
            mapView.map().layers().remove(pathLayer);
            itemizedLayer.removeAllItems();

            itemizedLayer.addItem(createMarkerItem(start, R.drawable.icon_map_marker_white));
            mapView.map().updateMap(true);
        }
        return true;
    }

    boolean isReady() {
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

    public void calcPath(final double fromLat, final double fromLon,
                         final double toLat, final double toLon) {

        Log.d("gh", "calculating path ...");
        new AsyncTask<Void, Void, PathWrapper>() {
            float time;

            protected PathWrapper doInBackground(Void... v) {
                StopWatch sw = new StopWatch().start();

                GHRequest req = new GHRequest(fromLat, fromLon, toLat, toLon).
                        setAlgorithm(Parameters.Algorithms.DIJKSTRA_BI);

                req.getHints().
                        put(Parameters.Routing.INSTRUCTIONS, "false");

                GHResponse resp = hopper.route(req);

                time = sw.stop().getSeconds();

                return resp.getBest();
            }

            protected void onPostExecute(PathWrapper resp) {
                if (!resp.hasErrors()) {
                    Log.d("gh", "from:" + fromLat + "," + fromLon + " to:" + toLat + ","
                            + toLon + " found path with distance:" + resp.getDistance()
                            / 1000f + ", nodes:" + resp.getPoints().getSize() + ", time:"
                            + time + " " + resp.getDebugInfo());
                    Log.d("gh", "the route is " + (int) (resp.getDistance() / 100) / 10f
                            + "km long, time:" + resp.getTime() / 60000f + "min, debug:" + time);

                    pathLayer = createPathLayer(resp);
                    mapView.map().layers().add(pathLayer);
                    mapView.map().updateMap(true);
                } else {
                    Toast.makeText(RouteToCanteenActivity.this, "Error:" + resp.getErrors(), Toast.LENGTH_LONG).show();
                }
                shortestPathRunning = false;
            }
        }.execute();
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

    void loadMap(File areaFolder) {
        Toast.makeText(this,"loading map", Toast.LENGTH_LONG).show();

        // Map events receiver
        mapView.map().layers().add(new MapEventsReceiver(mapView.map()));

        // Map file source
        MapFileTileSource tileSource = new MapFileTileSource();
        tileSource.setMapFile(new File(areaFolder, "../../../porto" + ".map").getAbsolutePath());
        VectorTileLayer l = mapView.map().setBaseMap(tileSource);
        mapView.map().setTheme(VtmThemes2.DEFAULT);
        mapView.map().layers().add(new BuildingLayer(mapView.map(), l));
        mapView.map().layers().add(new LabelLayer(mapView.map(), l));

        // Markers layer
        itemizedLayer = new ItemizedLayer<>(mapView.map(), (MarkerSymbol) null);
        mapView.map().layers().add(itemizedLayer);

        // Map position
        GeoPoint mapCenter = tileSource.getMapInfo().boundingBox.getCenterPoint();
        mapView.map().setMapPosition(mapCenter.getLatitude(), mapCenter.getLongitude(), 1 << 15);

        setContentView(mapView);
        loadGraphStorage();
    }

    void loadGraphStorage() {
        Toast.makeText(this,"\"loading graph (\" + Constants.VERSION + \") ... \")", Toast.LENGTH_LONG).show();
        new GHAsyncTask<Void, Void, Path>() {
            protected Path saveDoInBackground(Void... v) throws Exception {
                GraphHopper tmpHopp = new GraphHopper().forMobile();
                tmpHopp.load(new File(mapsFolder, "porto").getAbsolutePath());
                Log.d("gh", "found graph " + tmpHopp.getGraphHopperStorage().toString() + ", nodes:" + tmpHopp.getGraphHopperStorage().getNodes());
                hopper = tmpHopp;
                return null;
            }

            protected void onPostExecute(Path o) {
                if (hasError()) {
                    Toast.makeText(RouteToCanteenActivity.this,"An error happened while creating graph:" + getErrorMessage(), Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(RouteToCanteenActivity.this,"Finished loading graph. Long press to define where to start and end the route.", Toast.LENGTH_LONG).show();
                }

                finishPrepare();
            }
        }.execute();
    }

    private void finishPrepare() {
        prepareInProgress = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_route_to_canteen);

        mapView = new MapView(this);

        final EditText input = new EditText(this);

        input.setText("porto");

        boolean greaterOrEqKitkat = Build.VERSION.SDK_INT >= 19;

        if (greaterOrEqKitkat) {
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                Toast.makeText(this, "GraphHopper is not usable without an external storage!", Toast.LENGTH_LONG).show();
                return;
            }
            mapsFolder = new File(getExternalFilesDir(null), "/graphhopper/maps/");

            System.out.println(mapsFolder.getPath());

        } else {
            mapsFolder = new File(Environment.getExternalStorageDirectory(), "/graphhopper/maps/");
        }

        if (!mapsFolder.exists())
            mapsFolder.mkdirs();

        TextView welcome = (TextView) findViewById(R.id.route_to_canteen_welcome_text_view);
        welcome.setText("Welcome to GraphHopper " + Constants.VERSION + "!");
        welcome.setPadding(6, 3, 3, 3);
        localSpinner = (Spinner) findViewById(R.id.route_to_canteen_locale_area_spinner);
        localButton = (Button) findViewById(R.id.route_to_canteen_button);
        remoteSpinner = (Spinner) findViewById(R.id.route_to_canteen_remote_area_spinner);
        remoteButton = (Button) findViewById(R.id.route_to_canteen_remote_button);
        // TODO get user confirmation to download
        // if (AndroidHelper.isFastDownload(this))
        chooseAreaFromLocal();
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
        // necessary?
        System.gc();

        // Cleanup VTM
        mapView.map().destroy();
    }

    private void chooseAreaFromLocal() {
        List<String> nameList = new ArrayList<>();
        /*String[] files = mapsFolder.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename != null
                        && (filename.endsWith(".ghz") || filename
                        .endsWith("-gh"));
            }
        });*/

        String[] files = mapsFolder.list();

        System.out.println(mapsFolder.listFiles().length);

        System.out.println(files.length);

        if(files == null){
            return;
        }

        Collections.addAll(nameList, files);

        System.out.println(nameList);

        if (nameList.isEmpty())
            return;

        chooseArea(localButton, localSpinner, nameList,
                new MySpinnerListener() {
                    @Override
                    public void onSelect(String selectedArea, String selectedFile) {
                        initFiles();
                    }
                });
    }

    private void chooseArea(Button button, final Spinner spinner,
                            List<String> nameList, final MySpinnerListener myListener) {
        final Map<String, String> nameToFullName = new TreeMap<>();
        for (String fullName : nameList) {
            String tmp = Helper.pruneFileEnd(fullName);
            if (tmp.endsWith("-gh"))
                tmp = tmp.substring(0, tmp.length() - 3);

            tmp = AndroidHelper.getFileName(tmp);
            nameToFullName.put(tmp, fullName);
        }
        nameList.clear();
        nameList.addAll(nameToFullName.keySet());
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, nameList);
        spinner.setAdapter(spinnerArrayAdapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object o = spinner.getSelectedItem();
                if (o != null && o.toString().length() > 0 && !nameToFullName.isEmpty()) {
                    String area = o.toString();
                    myListener.onSelect(area, nameToFullName.get(area));
                } else {
                    myListener.onSelect(null, null);
                }
            }
        });
    }

    private void initFiles() {
        prepareInProgress = true;

        loadMap(new File(mapsFolder, "porto"));

    }

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_available_canteens);

        this.presenter = new AvailableCanteensPresenter(this);

        this.school = getIntent().getParcelableExtra("school");

        TextView headerTextView = findViewById(R.id.available_canteens_header_text_view);

        headerTextView.setText(headerTextView.getText().toString() + " " + school.acronym);

        Button headerBackButton = findViewById(R.id.available_canteens_header_back_button);

        headerBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navigateBackToAvailableSchoolsPage();

            }
        });

        boolean isInDarkMode = Provider.instance(this).settings().isInDarkMode();

        if(isInDarkMode){
            headerBackButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_arrow_left_white, 0, 0,0 );
        }

        ListView canteensListView = findViewById(R.id.available_canteens_list_view);

        canteensListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AvailableCanteensModel.Item canteen = adapter.getItem(position);

                navigateToCanteenMenusPage(canteen);
            }
        });

        registerForContextMenu(canteensListView);

        this.adapter = new RouteToCanteenActivity.AvailableCanteensListAdapter(this, new ArrayList<AvailableCanteensModel.Item>());

        canteensListView.setAdapter(adapter);

        this.presenter.requestCanteens(school.id);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        presenter.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        presenter.onPause();
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.canteen_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.canteen_context_menu_map_location:

                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

                int canteenItemPosition = info.position;

                AvailableCanteensModel.Item canteenItem = adapter.getItem(canteenItemPosition);

                presenter.requestCanteenToDisplayOnMap(canteenItem.schoolId, canteenItem.id);

                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_FOR_SETTINGS_ACTIVITY){

            final AvailableSchoolsModel.Item school = getIntent().getParcelableExtra("school");

            presenter.requestCanteens(school.id);

        }

    }

    @Override
    public void showCanteens(AvailableCanteensModel canteens) {

        adapter.clear();

        adapter.addAll(canteens);

        adapter.notifyDataSetChanged();

    }

    @Override
    public void navigateToCanteenMenusPage(AvailableCanteensModel.Item canteen) {

        Intent intent = new Intent(this, AvailableCanteenMenusActivity.class);

        intent.putExtra("canteen", canteen);

        startActivity(intent);

    }

    @Override
    public void navigateToCanteenOnMapLocation(CanteenWithMapLocationModel canteen) {

        Intent intent = new Intent(RouteToCanteenActivity.this, CanteensLocationOnMapActivity.class);

        intent.putExtra("canteen", canteen);

        startActivity(intent);

    }

    @Override
    public void navigateBackToAvailableSchoolsPage() {

        finish();

    }

    @Override
    public void showUnavailableCanteenError() {

        Toast.makeText(this, "Canteen was not found", Toast.LENGTH_LONG).show();

    }

    @Override
    public void showUnavailableCanteensError() {

        Toast.makeText(this, "No Available Canteens", Toast.LENGTH_LONG).show();

    }

    @Override
    public void showNoInternetConnectionError() {

        Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();

    }

    @Override
    public void showServerNotAvailableError() {

        Toast.makeText(this, "Server Not Available", Toast.LENGTH_LONG).show();

    }

    @Override
    public void showUnexepectedServerFailureError() {

        Toast.makeText(this, "Unexpected Server Failure", Toast.LENGTH_LONG).show();

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

    private class AvailableCanteensListAdapter extends ArrayAdapter<AvailableCanteensModel.Item> {

        public AvailableCanteensListAdapter(Context context, List<AvailableCanteensModel.Item> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            AvailableCanteensModel.Item canteen = getItem(position);

            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.available_canteens_list_view_item, parent, false);
            }

            canteen.schoolId = school.id;

            TextView canteenNameTextView = convertView.findViewById(R.id.available_canteens_list_view_item_canteen_name_text_view);

            canteenNameTextView.setText(canteen.name);

            return convertView;
        }
    }*/

    public interface MySpinnerListener {
        void onSelect(String selectedArea, String selectedFile);
    }

    public static class AndroidHelper {
        public static List<String> readFile(Reader simpleReader) throws IOException {
            BufferedReader reader = new BufferedReader(simpleReader);
            try {
                List<String> res = new ArrayList<String>();
                String line;
                while ((line = reader.readLine()) != null) {
                    res.add(line);
                }
                return res;
            } finally {
                reader.close();
            }
        }

        public static String getFileName(String str) {
            int index = str.lastIndexOf("/");
            if (index > 0) {
                return str.substring(index + 1);
            }
            return str;
        }
    }

    class MapEventsReceiver extends Layer implements GestureListener {

        MapEventsReceiver(org.oscim.map.Map map) {
            super(map);
        }

        @Override
        public boolean onGesture(Gesture g, MotionEvent e) {
            if (g instanceof Gesture.LongPress) {
                GeoPoint p = mMap.viewport().fromScreenPoint(e.getX(), e.getY());
                return onLongPress(p);
            }
            return false;
        }
    }

    enum VtmThemes2 implements ThemeFile {

        DEFAULT("vtm/default.xml"),
        MAPZEN("vtm/mapzen.xml"),
        NEWTRON("vtm/newtron.xml"),
        OPENMAPTILES("vtm/openmaptiles.xml"),
        OSMAGRAY("vtm/osmagray.xml"),
        OSMARENDER("vtm/osmarender.xml"),
        TRONRENDER("vtm/tronrender.xml");

        private final String mPath;

        VtmThemes2(String path) {
            mPath = path;
        }

        @Override
        public XmlRenderThemeMenuCallback getMenuCallback() {
            return null;
        }

        @Override
        public String getRelativePathPrefix() {
            return "";
        }

        @Override
        public InputStream getRenderThemeAsStream() throws IRenderTheme.ThemeException {
            return AssetAdapter.readFileAsStream(mPath);
        }

        @Override
        public boolean isMapsforgeTheme() {
            return false;
        }

        @Override
        public void setMenuCallback(XmlRenderThemeMenuCallback menuCallback) {
        }
    }

    public abstract class GHAsyncTask<A, B, C> extends AsyncTask<A, B, C> {
        private Throwable error;

        protected abstract C saveDoInBackground(A... params) throws Exception;

        protected C doInBackground(A... params) {
            try {
                return saveDoInBackground(params);
            } catch (Throwable t) {
                error = t;
                return null;
            }
        }

        public boolean hasError() {
            return error != null;
        }

        public Throwable getError() {
            return error;
        }

        public String getErrorMessage() {
            if (hasError()) {
                return error.getMessage();
            }
            return "No Error";
        }
    }
}
