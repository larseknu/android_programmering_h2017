package no.hiof.larseknu.playingwithgooglemaps;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Property;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;

import java.util.ArrayList;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NONE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, AdapterView.OnItemSelectedListener {
    private GoogleMap gMap;
    private LatLng HIOF = new LatLng(59.12797849, 11.35272861);
    private LatLng FREDRIKSTAD = new LatLng(59.21047628, 10.93994737);

    private int kittyCounter = 1;
    private ArrayList<Marker> kittyMarkers;

    private GMapV2Direction mapDirection;

    private static final int LOCATION_PERMISSION = 1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.animateKittens):
                for (Marker kittyMarker : kittyMarkers) {
                    animateMarker(kittyMarker, FREDRIKSTAD);
                }
                break;
            case (R.id.drawRoute):
                new drawRoute().execute();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setDefaultUiSettings() {
        UiSettings uiSettings = gMap.getUiSettings();
        uiSettings.setCompassEnabled(true);
        uiSettings.setTiltGesturesEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMapToolbarEnabled(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        kittyMarkers = new ArrayList<Marker>();

        mapDirection = new GMapV2Direction();

        Spinner spinner = (Spinner) findViewById(R.id.layers_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.layers_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @AfterPermissionGranted(LOCATION_PERMISSION)
    private void setLocationEnabled() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            gMap.setMyLocationEnabled(true);
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.no_location_permission),
                    LOCATION_PERMISSION, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String layerType = (String) parent.getItemAtPosition(position);

        if (gMap != null) {
            if (layerType.equals(getString(R.string.hybrid))) {
                gMap.setMapType(MAP_TYPE_HYBRID);

            } else if (layerType.equals(getString(R.string.satellite))) {
                gMap.setMapType(MAP_TYPE_SATELLITE);

            } else if (layerType.equals(getString(R.string.terrain))) {
                gMap.setMapType(MAP_TYPE_TERRAIN);

            } else if (layerType.equals(getString(R.string.none))) {
                gMap.setMapType(MAP_TYPE_NONE);

            } else {
                gMap.setMapType(MAP_TYPE_NORMAL);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private class drawRoute extends AsyncTask<Void, Void, PolylineOptions> {
        @Override
        protected PolylineOptions doInBackground(Void... params) {
            Document doc = mapDirection.getDocument(HIOF, FREDRIKSTAD, GMapV2Direction.MODE_DRIVING);

            ArrayList<LatLng> directionPoint = mapDirection.getDirection(doc);
            PolylineOptions rectLine = new PolylineOptions().width(3).color(Color.BLUE);

            for (int i = 0; i < directionPoint.size(); i++) {
                rectLine.add(directionPoint.get(i));
            }

            return rectLine ;
        }

        @Override
        protected void onPostExecute(PolylineOptions result) {
            gMap.addPolyline(result);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        gMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Middle of the world"));
        gMap.addMarker(new MarkerOptions().position(HIOF).title("Østfold University College"));
        gMap.addMarker(new MarkerOptions().position(FREDRIKSTAD).title("Fredrikstad Kino"));

        gMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(HIOF, 15, 0, 0)));

        gMap.animateCamera(CameraUpdateFactory.newLatLng(FREDRIKSTAD), 2000, null);

        gMap.setOnMapLongClickListener(this);

        setDefaultUiSettings();

        setLocationEnabled();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        addKittenMarker(latLng, "Kitten Attack");
    }

    private void addKittenMarker(LatLng kittenlocation, String snippet) {
        // Get a kittenIcon from the drawable resources, Must be named "kitten_0X", where X is a number
        BitmapDescriptor kittenIcon = BitmapDescriptorFactory.fromResource(getResources().getIdentifier("kitten_0" + (kittyCounter%3+1), "drawable", getPackageName()));
        // One more kitten is to be added
        kittyCounter++;
        // Create all the marker options for the kitten marker
        MarkerOptions markerOptions = new MarkerOptions().position(kittenlocation)
                .title("Mittens the " + kittyCounter + ".")
                .snippet(snippet)
                .icon(kittenIcon);

        Marker kittyMarker = gMap.addMarker(markerOptions);

        kittyMarkers.add(kittyMarker);
    }

    static void animateMarker(Marker marker, LatLng finalPosition) {
        // The typeevaluator does the calculations for the animation
        TypeEvaluator<LatLng> typeEvaluator = new TypeEvaluator<LatLng>() {
            @Override
            public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
                double lat = (endValue.latitude - startValue.latitude) * fraction + startValue.latitude;
                double lng = (endValue.longitude - startValue.longitude) * fraction + startValue.longitude;
                Log.v("Animator", "Fraction: " + fraction + " Lat: " + lat + " Lon: " + lng);
                return new LatLng(lat, lng);
            }
        };

        // Define the property of the object we want to animate (i.e. get the LatLng from the "position" in ther Marker object)
        Property<Marker, LatLng> property = Property.of(Marker.class, LatLng.class, "position");
        // The animator, we want to animate the marker, based on the property (LatLng), calculated by the typeEvaluator, to the finalPosition
        ObjectAnimator animator = ObjectAnimator.ofObject(marker, property, typeEvaluator, finalPosition);
        // Duration of animation
        animator.setDuration(1000);
        animator.start();
    }

}
