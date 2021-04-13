package com.app.travelbug;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;

import com.app.travelbug.data.DatabaseHelper;
import com.app.travelbug.data.InfoDialog;
import com.app.travelbug.data.MyClusterManagerRenderer;
import com.app.travelbug.data.model.ClusterMarker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;


import android.location.Location;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.collections.MarkerManager;
import com.google.maps.android.ui.IconGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.Manifest;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        BottomNavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener, InfoDialog.InfoDialogListener {

    //Globals
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final String TAG = "MapsActivity";

    String[] largeCountries = new String[]{"Russia", "Canada", "USA", "United States", "China", "Brazil", "Australia"};
    List<String> largeCountriesList = Arrays.asList(largeCountries);

    //Widgets
    private BottomNavigationView bottomNav;
    private MapsActivity activity = this;
    private GoogleMap mMap;
    private EditText mSearchText;
    private ImageView mGps;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mLocationPermissionGranted = false;
    private DatabaseHelper mDatabaseHelper;


    // Declare a variable for the cluster manager.
    private ClusterManager<ClusterMarker> clusterManager;
    private MyClusterManagerRenderer myClusterManagerRenderer;
    private ArrayList<ClusterMarker> mClusterMarkers = new ArrayList<>();


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mSearchText = findViewById(R.id.input_search);
        mGps = findViewById(R.id.ic_gps);

        mDatabaseHelper = new DatabaseHelper(this);


        if (isServicesOK()) {
            //somethig here
        }

        checkPermissions();
        init();
    }


    private void init(){

        bottomNav = (BottomNavigationView) findViewById(R.id.bottomNav_id);
        bottomNav.setOnNavigationItemSelectedListener(this);
        bottomNav.setSelectedItemId(R.id.map);


        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN ||
                event.getAction() == KeyEvent.KEYCODE_ENTER){

                    geoLocate();
                }

                return false;
            }
        });

        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation();
            }
        });
        hideSoftKeyboard();
    }


    private void geoLocate(){
        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(activity);
        List<Address> list = new ArrayList<>();

        try{
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);
            //Toast.makeText(activity, address.toString(), Toast.LENGTH_SHORT).show();

            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title(searchString));


            moveCamera(address, latLng);
            hideSoftKeyboard();
        }
    }


    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    //Get the current devices location
    private void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            Task location = mFusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Location currentLocation = (Location) task.getResult();
                        new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(),
                                currentLocation.getLongitude()), 12));
                    } else {
                        Toast.makeText(activity, "Unable to get current location", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        } catch (SecurityException e) {
            Log.e(TAG, "getDevicesLocation: SecurityException: " + e.getMessage());
        }
    }


    private void initMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }


    private void moveCamera(Address address, LatLng latLng) {
        if (address.getLocality() != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
        } else if (address.getAdminArea() != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 6));
        } else if (largeCountriesList.contains(address.getCountryName())) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 3));
        } else {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(activity, "Map is ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;

        if (mLocationPermissionGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

        }


        setUpCluster();
    }


    private void setUpCluster() {
        // Position the map.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        clusterManager = new ClusterManager<ClusterMarker>(this, mMap);
        // Point the map's listeners at the listeners implemented by the cluster manager.



        mMap.setOnCameraIdleListener(clusterManager);

        mMap.setInfoWindowAdapter(clusterManager.getMarkerManager());

        //clusterManager.getMarkerManager().onMarkerClick();
        clusterManager.setOnClusterItemClickListener(item -> true);
        mMap.setOnMarkerClickListener(item -> true);

        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<ClusterMarker>() {
            public boolean onClusterItemClick(ClusterMarker item) {
                return true;
            }
        });

        addItems();
    }




    private void addItems() {

        if (clusterManager == null) {
            clusterManager = new ClusterManager<ClusterMarker>(getApplicationContext(), mMap);
        }
        if (myClusterManagerRenderer == null){
            myClusterManagerRenderer = new MyClusterManagerRenderer(activity, mMap, clusterManager);
            clusterManager.setRenderer(myClusterManagerRenderer);
        }


        // Set some lat/lng coordinates to start with.
        double lat = 51.5145160;
        double lng = -0.1270060;

        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < 10; i++) {
            double offset = i / 60d;
            lat = lat + offset;
            lng = lng + offset;

            // Set the title and snippet strings.
            String title = "This is the title " + i;
            String snippet = "and this is the snippet. " + i;

            // Create a cluster item for the marker and set the title and snippet using the constructor.
            ClusterMarker infoWindowItem = new ClusterMarker(lat, lng, title, snippet);

            // Add the cluster item (marker) to the cluster manager.
            clusterManager.addItem(infoWindowItem);
        }

        lat = 39.857277;
        lng = -4.023644;
        String title = "Primate Cathedral of Saint Mary of Toledo";
        String snippet = "Roman Catholic house of worship modelled on Bourges Cathedral, incorporating some Mudejar features.";
        int avatar = R.drawable.cathedral_of_toledo;
        //ClusterMarker newClusterMarker = new ClusterMarker(new LatLng(lat, lng), title, snippet, avatar);

        ClusterMarker newMarker = new ClusterMarker(
                new LatLng(lat, lng), title, snippet, avatar);
        clusterManager.addItem(newMarker);
        mClusterMarkers.add(newMarker);
        clusterManager.cluster();



        clusterManager.setOnClusterItemClickListener(
            new ClusterManager.OnClusterItemClickListener<ClusterMarker>() {
                @Override public boolean onClusterItemClick(ClusterMarker clusterItem) {

                    showInfoWindow(clusterItem);
                    return true;
                }
            });
        mMap.setOnMarkerClickListener(clusterManager);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;
        if (id == R.id.map) {
            return false;
        } else if (id == R.id.planner) {
            intent = new Intent(activity, PlannerActivity.class);
        } else if (id == R.id.favorites) {
            intent = new Intent(activity, FavoritesActivity.class);
        } else if (id == R.id.profile) {
            intent = new Intent(activity, ProfileActivity.class);
        }
        startActivity(intent);
        return true;
    }


    public void AddData(String newEntry){
        boolean insertData = mDatabaseHelper.addData(newEntry);

        if (insertData) {
            Toast.makeText(activity, "Data Successfully Inserted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "Something went dsfsdf wrong", Toast.LENGTH_SHORT).show();
        }
    }


    void checkPermissions() {
        Log.d(TAG, "getLocationPermissions: getting location permission");
        String[] permissions =  {FINE_LOCATION, COARSE_LOCATION};


        //Checks if permission is granted...
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity, "First IF", Toast.LENGTH_SHORT).show();
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(activity, "Second IF", Toast.LENGTH_SHORT).show();

                mLocationPermissionGranted = true;
                initMap();
            }
            //If not, request location permission..
            else {
                ActivityCompat.requestPermissions(activity, permissions, 1234);
            }
        } else {
            ActivityCompat.requestPermissions(activity, permissions, 1234);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionResult: called:");
        mLocationPermissionGranted = false;

        if (requestCode == 1234){
            if(grantResults.length > 0){
                for(int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        mLocationPermissionGranted = false;
                        Toast.makeText(activity, "Denied", Toast.LENGTH_SHORT).show();

                        return;
                    }
                }
                mLocationPermissionGranted = true;
                initMap();
            }
        }
    }


    private void getPlaces() {
        //@TODO for if we want to use places API
        /*if(!Places.isInitialized()){
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        }

        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);
        autocompleteFragment.setLocationBias(RectangularBounds.newInstance(
                new LatLng(-33.880490, 151.184363),
                new LatLng(-33.858754, 151.229596)
        ));
        autocompleteFragment.setCountries("IN");

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NotNull Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            }


            @Override
            public void onError(@NotNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });*/
    }


    public boolean isServicesOK(){
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity);
        if (available == ConnectionResult.SUCCESS){
            //everything is goof
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //error occurred but we can resolve
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(activity, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    public void showInfoWindow(ClusterMarker marker){
        InfoDialog dialog = new InfoDialog(marker);
        dialog.show(getSupportFragmentManager(),"BottomSheet");
    }


    @Override
    public void onFavoriteClicked(ClusterMarker marker) {
        mDatabaseHelper.createPlace(marker);

    }
}



