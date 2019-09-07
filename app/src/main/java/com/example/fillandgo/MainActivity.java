package com.example.fillandgo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;

import android.widget.AdapterView.OnItemSelectedListener;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, OnItemSelectedListener {
    FloatingActionButton fabAll, fabNear, fabFiveKM, fabCusCar, p92, p95, LK, LAD, LSD;
    private static final String LOG_TAG = "ExampleApp";
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private static final String SERVICE_URL = "https://raw.githubusercontent.com/vidura/CPC_mobile/json/results.json";
    protected GoogleMap map;
    private MarkerOptions currentMarker;
    private MarkerOptions mMarker;
    private float mindist;
    private DatabaseManager db;

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        db = new DatabaseManager(this);
        loadSpinnerData();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabNear = findViewById(R.id.fabNearest);
        fabAll = findViewById(R.id.fabAll);
        fabFiveKM = findViewById(R.id.fabFiveKM);
        fabCusCar = findViewById(R.id.fabCusCar);
        p92 = findViewById(R.id.p92);
        p95 = findViewById(R.id.p95);
        LK = findViewById(R.id.LK);
        LSD = findViewById(R.id.LSD);
        LAD = findViewById(R.id.LAD);

        fabNear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpMap("nearest");
            }
        });
        fabAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpMap("all");
            }
        });
        fabFiveKM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpMap("fiveKm");
            }
        });
        fabCusCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent deleteIntent = new Intent(MainActivity.this, YourcarsActivity.class);
                MainActivity.this.startActivity(deleteIntent);
            }
        });
        p92.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpMap("petrol");
            }
        });
        p95.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpMap("superpetrol");
            }
        });
        LK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpMap("LK");
            }
        });
        LAD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpMap("Diesel");
            }
        });
        LSD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpMap("superdiesel");
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        String label = parent.getItemAtPosition(position).toString();
        String fuel[] = label.split(":");
        if (fuel[1].equals("95Petrol")) {
            setUpMap("superpetrol");
        }
        if (fuel[1].equals("92Petrol")) {
            setUpMap("petrol");
        }
        if (fuel[1].equals("Diesel")) {
            setUpMap("diesel");
        }
        if (fuel[1].equals("Super Diesel")) {
            setUpMap("superdiesel");
        }
        Toast.makeText(parent.getContext(), "You Selected " + label, Toast.LENGTH_LONG).show();

    }

    private void loadSpinnerData() {

        ArrayList<Car_get_set> cars = db.selectAll();
        if (cars.size() > 0) {
            ArrayList<String> allCars = new ArrayList<>();
            for (Car_get_set car : cars) {


                String lol = car.getCarName() + ":" + car.getFuelType();

                allCars.add(lol);
            }

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, allCars);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fillup_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {


            case R.id.addcar:

                return true;

            default:
                return super.onOptionsItemSelected(item);


        }
    }

    public void AddCar(View v){
        Intent addIntent = new Intent(this, Add_car.class);
        this.startActivity(addIntent);
    }

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude()
                            + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment)
                            getSupportFragmentManager().findFragmentById(R.id.google_map);
                    supportMapFragment.getMapAsync(MainActivity.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        BitmapDrawable bitmapdrawx = (BitmapDrawable) getResources().getDrawable(R.drawable.usericon);
        Bitmap x = bitmapdrawx.getBitmap();
        Bitmap smallMarkerx = Bitmap.createScaledBitmap(x, 80, 80, false);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng).title("I Am Here!")
                .snippet("Current User Location")
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarkerx));
        map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        map.addMarker(markerOptions);

        setUpMap("fiveKm");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                }
                break;
        }
    }

    protected void setUpMap(final String fueltype) {
        // Retrieve the city data from the web service
        // In a worker thread since it's a network operation.
        new Thread(new Runnable() {
            public void run() {
                try {
                    retrieveAndAddCities(fueltype);
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Cannot retrive cities", e);
                    return;
                }
            }
        }).start();
    }

    protected void retrieveAndAddCities(final String fueltype) throws IOException {
        HttpURLConnection conn = null;
        final StringBuilder json = new StringBuilder();
        try {
            // Connect to the web service
            URL url = new URL(SERVICE_URL);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Read the JSON data into the StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                json.append(buff, 0, read);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to service", e);
            throw new IOException("Error connecting to service", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        // Create markers for the city data.
        // Must run this on the UI thread since it's a UI operation.
        runOnUiThread(new Runnable() {
            public void run() {
                try {

                    createMarkersFromJson(json.toString(), fueltype);
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Error processing JSON", e);
                }
            }
        });
    }

    void createMarkersFromJson(String json, String fueltype) throws JSONException {

        int heightx = 100;
        int widthx = 100;
        BitmapDrawable bitmapdrawx = (BitmapDrawable) getResources().getDrawable(R.drawable.usericon);
        Bitmap x = bitmapdrawx.getBitmap();
        Bitmap smallMarkerx = Bitmap.createScaledBitmap(x, widthx, heightx, false);
        // De-serialize the JSON string into an array of city objects
        JSONArray jsonArray = new JSONArray(json);
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

        if (fueltype.equals("petrol")) {
            map.clear();
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng).title("I Am Here!")
                    .snippet("Current User Location")
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarkerx));
            map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            map.addMarker(markerOptions);
            int height = 80;
            int width = 80;
            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.gas);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

            for (int i = 0; i < jsonArray.length(); i++) {
                // Create a marker for each city in the JSON data.

                JSONObject jsonObj = jsonArray.getJSONObject(i);
                String fueltypes = jsonObj.getString("sproducts");
                String[] fueltypes1 = fueltypes.split(",");
                for (int j = 0; j < fueltypes1.length; j++) {
                    if (fueltypes1[j].equals("92Petrol")) {
                        LatLng PERTH = new LatLng(jsonObj.getDouble("latitude"), jsonObj.getDouble("longitude"));
                        String title = jsonObj.getString("name");
                        String address = jsonObj.getString("saddress");
                        String city = jsonObj.getString("scity");
                        String air = jsonObj.getString("air");
                        String bathroom = jsonObj.getString("bathroom");
                        String atm = jsonObj.getString("atm");
                        String supermarket = jsonObj.getString("market");
                        String all = "City: " + city + "\n" + "Address: " + address + "\n" + "Air: " + air + "\n" + "Bathroom: " + bathroom + "\n" + "ATM: " + atm + "\n" + "Supermarket: " + supermarket;
                        map.addMarker(new MarkerOptions()
                                .position(PERTH)
                                .title(title)
                                .snippet(all)
                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                        );
                    }
                }


            }
        }
        if (fueltype.equals("diesel")) {

            map.clear();
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng).title("I Am Here!")
                    .snippet("Current User Location")
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarkerx));
            map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            map.addMarker(markerOptions);
            int height = 80;
            int width = 80;
            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.gas);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
            for (int i = 0; i < jsonArray.length(); i++) {
                // Create a marker for each city in the JSON data.

                JSONObject jsonObj = jsonArray.getJSONObject(i);
                String fueltypes = jsonObj.getString("sproducts");
                String[] fueltypes1 = fueltypes.split(",");
                for (int j = 0; j < fueltypes1.length; j++) {
                    if (fueltypes1[j].equals("LAD")) {
                        LatLng PERTH = new LatLng(jsonObj.getDouble("latitude"), jsonObj.getDouble("longitude"));
                        String title = jsonObj.getString("name");
                        String address = jsonObj.getString("saddress");
                        String city = jsonObj.getString("scity");
                        String air = jsonObj.getString("air");
                        String bathroom = jsonObj.getString("bathroom");
                        String atm = jsonObj.getString("atm");
                        String supermarket = jsonObj.getString("market");
                        String all = "City: " + city + "\n" + "Address: " + address + "\n" + "Air: " + air + "\n" + "Bathroom: " + bathroom + "\n" + "ATM: " + atm + "\n" + "Supermarket: " + supermarket;
                        map.addMarker(new MarkerOptions()
                                .position(PERTH)
                                .title(title)
                                .snippet(all)
                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                        );
                    }
                }


            }
        }
        if (fueltype.equals("superdiesel")) {
            map.clear();
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng).title("I Am Here!")
                    .snippet("Current User Location")
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarkerx));
            map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            map.addMarker(markerOptions);
            int height = 80;
            int width = 80;
            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.gas);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
            for (int i = 0; i < jsonArray.length(); i++) {
                // Create a marker for each city in the JSON data.

                JSONObject jsonObj = jsonArray.getJSONObject(i);
                String fueltypes = jsonObj.getString("sproducts");
                String[] fueltypes1 = fueltypes.split(",");
                for (int j = 0; j < fueltypes1.length; j++) {
                    if (fueltypes1[j].equals("LSD")) {
                        LatLng PERTH = new LatLng(jsonObj.getDouble("latitude"), jsonObj.getDouble("longitude"));
                        String title = jsonObj.getString("name");
                        String address = jsonObj.getString("saddress");
                        String city = jsonObj.getString("scity");
                        String air = jsonObj.getString("air");
                        String bathroom = jsonObj.getString("bathroom");
                        String atm = jsonObj.getString("atm");
                        String supermarket = jsonObj.getString("market");
                        String all = "City: " + city + "\n" + "Address: " + address + "\n" + "Air: " + air + "\n" + "Bathroom: " + bathroom + "\n" + "ATM: " + atm + "\n" + "Supermarket: " + supermarket;
                        map.addMarker(new MarkerOptions()
                                .position(PERTH)
                                .title(title)
                                .snippet(all)
                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                        );
                    }
                }


            }
        }
        if (fueltype.equals("superpetrol")) {
            map.clear();
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng).title("I Am Here!")
                    .snippet("Current User Location")
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarkerx));
            map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            map.addMarker(markerOptions);
            int height = 80;
            int width = 80;
            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.gas);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
            for (int i = 0; i < jsonArray.length(); i++) {
                // Create a marker for each city in the JSON data.

                JSONObject jsonObj = jsonArray.getJSONObject(i);
                String fueltypes = jsonObj.getString("sproducts");
                String[] fueltypes1 = fueltypes.split(",");
                for (int j = 0; j < fueltypes1.length; j++) {
                    if (fueltypes1[j].equals("95Petrol")) {
                        LatLng PERTH = new LatLng(jsonObj.getDouble("latitude"), jsonObj.getDouble("longitude"));
                        String title = jsonObj.getString("name");
                        String address = jsonObj.getString("saddress");
                        String city = jsonObj.getString("scity");
                        String air = jsonObj.getString("air");
                        String bathroom = jsonObj.getString("bathroom");
                        String atm = jsonObj.getString("atm");
                        String supermarket = jsonObj.getString("market");
                        String all = "City: " + city + "\n" + "Address: " + address + "\n" + "Air: " + air + "\n" + "Bathroom: " + bathroom + "\n" + "ATM: " + atm + "\n" + "Supermarket: " + supermarket;
                        map.addMarker(new MarkerOptions()
                                .position(PERTH)
                                .title(title)
                                .snippet(all)
                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                        );
                    }
                }


            }
        }
        if (fueltype.equals("LK")) {
            map.clear();
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng).title("I Am Here!")
                    .snippet("Current User Location")
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarkerx));
            map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            map.addMarker(markerOptions);
            int height = 80;
            int width = 80;
            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.gas);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
            for (int i = 0; i < jsonArray.length(); i++) {
                // Create a marker for each city in the JSON data.

                JSONObject jsonObj = jsonArray.getJSONObject(i);
                String fueltypes = jsonObj.getString("sproducts");
                String[] fueltypes1 = fueltypes.split(",");
                for (int j = 0; j < fueltypes1.length; j++) {
                    if (fueltypes1[j].equals("LK")) {
                        LatLng PERTH = new LatLng(jsonObj.getDouble("latitude"), jsonObj.getDouble("longitude"));
                        String title = jsonObj.getString("name");
                        String address = jsonObj.getString("saddress");
                        String city = jsonObj.getString("scity");
                        String air = jsonObj.getString("air");
                        String bathroom = jsonObj.getString("bathroom");
                        String atm = jsonObj.getString("atm");
                        String supermarket = jsonObj.getString("market");
                        String all = "City: " + city + "\n" + "Address: " + address + "\n" + "Air: " + air + "\n" + "Bathroom: " + bathroom + "\n" + "ATM: " + atm + "\n" + "Supermarket: " + supermarket;
                        map.addMarker(new MarkerOptions()
                                .position(PERTH)
                                .title(title)
                                .snippet(all)
                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                        );
                    }
                }


            }
        }
        if (fueltype.equals("all")) {
            map.clear();
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng).title("I Am Here!")
                    .snippet("Current User Location")
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarkerx));
            map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            map.addMarker(markerOptions);
            int height = 80;
            int width = 80;
            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.gas);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
            for (int i = 0; i < jsonArray.length(); i++) {
                // Create a marker for each city in the JSON data.


                JSONObject jsonObj = jsonArray.getJSONObject(i);
                String fueltypes = jsonObj.getString("sproducts");

                LatLng PERTH = new LatLng(jsonObj.getDouble("latitude"), jsonObj.getDouble("longitude"));
                String title = jsonObj.getString("name");
                String address = jsonObj.getString("saddress");
                String city = jsonObj.getString("scity");
                String air = jsonObj.getString("air");
                String bathroom = jsonObj.getString("bathroom");
                String atm = jsonObj.getString("atm");
                String supermarket = jsonObj.getString("market");
                String all = "City: " + city + "\n" + "Address: " + address + "\n" + "Air: " + air + "\n" + "Bathroom: " + bathroom + "\n" + "ATM: " + atm + "\n" + "Supermarket: " + supermarket;
                map.addMarker(new MarkerOptions()
                        .position(PERTH)
                        .title(title)
                        .snippet(all)
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                );
            }
        }
        if (fueltype.equals("fiveKm")) {
            map.clear();
            Circle circle = map.addCircle(new CircleOptions().center(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())).radius(5000).strokeColor(Color.GRAY).fillColor(0x22000000));
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng).title("I Am Here!")
                    .snippet("Current User Location")
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarkerx));
            map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            map.addMarker(markerOptions);
            // De-serialize the JSON string into an array of city objects
            int height = 80;
            int width = 80;
            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.gas);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
            for (int i = 0; i < jsonArray.length(); i++) {
                // Create a marker for each city in the JSON data.
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                double lat = jsonObj.getDouble("latitude");
                double lon = jsonObj.getDouble("longitude");
                LatLng PERTH = new LatLng(jsonObj.getDouble("latitude"), jsonObj.getDouble("longitude"));
                String title = jsonObj.getString("name");
                String address = jsonObj.getString("saddress");
                String city = jsonObj.getString("scity");
                String air = jsonObj.getString("air");
                String bathroom = jsonObj.getString("bathroom");
                String atm = jsonObj.getString("atm");
                String supermarket = jsonObj.getString("market");
                String all = "City: " + city + "\n" + "Address: " + address + "\n" + "Air: " + air + "\n" + "Bathroom: " + bathroom + "\n" + "ATM: " + atm + "\n" + "Supermarket: " + supermarket;
                currentMarker = new MarkerOptions()
                        .title(title)
                        .snippet(all)
                        .position(PERTH)
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));


                float[] distance = new float[1];
                Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(), lat, lon, distance);
                if (i == 0) {
                    mindist = distance[0];
                } else if (distance[0] < 5000) {
                    mindist = distance[0];

                    map.addMarker(currentMarker);
                }

            }


        }
        if (fueltype.equals("nearest")) {
            double longi = 0;
            double lati = 0;
            map.clear();
            int height = 80;
            int width = 80;
            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.gas);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
            for (int i = 0; i < jsonArray.length(); i++) {
                // Create a marker for each city in the JSON data.
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                double lat = jsonObj.getDouble("latitude");
                double lon = jsonObj.getDouble("longitude");
                LatLng PERTH = new LatLng(jsonObj.getDouble("latitude"), jsonObj.getDouble("longitude"));
                String title = jsonObj.getString("name");
                String address = jsonObj.getString("saddress");
                String city = jsonObj.getString("scity");
                String air = jsonObj.getString("air");
                String bathroom = jsonObj.getString("bathroom");
                String atm = jsonObj.getString("atm");
                String supermarket = jsonObj.getString("market");
                String all = "City: " + city + "\n" + "Address: " + address + "\n" + "Air: " + air + "\n" + "Bathroom: " + bathroom + "\n" + "ATM: " + atm + "\n" + "Supermarket: " + supermarket;
                currentMarker = new MarkerOptions()
                        .title(title)
                        .snippet(all)
                        .position(PERTH)
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))

                ;


                float[] distance = new float[1];
                Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(), lat, lon, distance);
                if (i == 0) {
                    mindist = distance[0];
                } else if (mindist > distance[0]) {
                    mindist = distance[0];
                    mMarker = currentMarker;
                    lati = jsonObj.getDouble("latitude");
                    longi = jsonObj.getDouble("longitude");

                }

            }
            LatLng nearestLocation = new LatLng(lati, longi);

            map.addMarker(mMarker);
            map.animateCamera(CameraUpdateFactory.newLatLng(nearestLocation));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(nearestLocation, 15));
        }
        map.setInfoWindowAdapter(new ViewInfoAdapter(MainActivity.this));
    }
}




