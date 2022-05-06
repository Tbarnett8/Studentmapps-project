package com.example.studentmaps;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.slider.Slider;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    boolean isPermissionGranted;
    private GoogleMap mMap;
    ImageView searchbutton;
    EditText locationinput;
    private Slider sBar;
    public LatLng llpoint;
    public float range = 300;
    private DataBaseHelper dbhelper;
    private Button foodbutton;
    private Button unibutton;
    private Button agencybutton;
    private Button clinicsbutton;
    private Button shopsbutton;
    private Button funbutton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        dbhelper = new DataBaseHelper(MapsActivity.this);
        try {
            dbhelper.createDataBase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*--------permissions----------*/

        checkPermission();

        if (isPermissionGranted){
            if (checkGooglePlayServices()){
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);
            } else{
                Toast.makeText(this,"Google Play Services Not Available", Toast.LENGTH_SHORT).show();
            }
        }

        /*--------search button----------*/

        searchbutton=findViewById(R.id.searchButton);
        locationinput=findViewById(R.id.locationInput);

        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMap != null) {
                    mMap.clear();
                }
                String location = locationinput.getText().toString();
                if (location.matches(""))
                {
                    Toast.makeText(MapsActivity.this, "Type a location name", Toast.LENGTH_SHORT).show();
                }else
                {

                    Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                    try {
                        List<Address> listAddress= geocoder.getFromLocationName(location, 1);
                        if (listAddress.size()>0)
                        {
                            LatLng latLng = new LatLng(listAddress.get(0).getLatitude(),listAddress.get(0).getLongitude());
                            llpoint = latLng;
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.title("Search Location");
                            markerOptions.position(llpoint);
                            mMap.addMarker(markerOptions);
                            if (range == 300.0) {
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(llpoint, 16);
                                mMap.animateCamera(cameraUpdate);
                            } else if (range == 600.0) {
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(llpoint, 15);
                                mMap.animateCamera(cameraUpdate);
                            } else if (range == 900.0) {
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(llpoint, 14);
                                mMap.animateCamera(cameraUpdate);
                            } else if (range == 1200.0) {
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(llpoint, 14);
                                mMap.animateCamera(cameraUpdate);
                            } else if (range == 1500.0) {
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(llpoint, 13);
                                mMap.animateCamera(cameraUpdate);
                            } else {
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(llpoint, 13);
                                mMap.animateCamera(cameraUpdate);
                            }
                            drawCircle(llpoint, range);
                            

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        /*--------range bar----------*/

        sBar = findViewById(R.id.sBar);

        sBar.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                String location = locationinput.getText().toString();
                if (location.matches("")) {
                    Toast.makeText(MapsActivity.this, "Type a location name", Toast.LENGTH_SHORT).show();
                } else {
                    mMap.clear();
                    range = slider.getValue();
                    drawCircle(llpoint, range);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.title("Search Location");
                    markerOptions.position(llpoint);
                    mMap.addMarker(markerOptions);
                    if (range == 300.0) {
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(llpoint, 16);
                        mMap.animateCamera(cameraUpdate);
                    } else if (range == 600.0) {
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(llpoint, 15);
                        mMap.animateCamera(cameraUpdate);
                    } else if (range == 900.0) {
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(llpoint, 14);
                        mMap.animateCamera(cameraUpdate);
                    } else if (range == 1200.0) {
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(llpoint, 14);
                        mMap.animateCamera(cameraUpdate);
                    } else if (range == 1500.0) {
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(llpoint, 13);
                        mMap.animateCamera(cameraUpdate);
                    } else {
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(llpoint, 13);
                        mMap.animateCamera(cameraUpdate);
                    }

                }
            }
        });

        /*--------filter buttons----------*/

        foodbutton = findViewById(R.id.food);
        String food = "Restaurant";

        foodbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = locationinput.getText().toString();
                if (location.matches(""))
                {
                    Toast.makeText(MapsActivity.this, "Type a location name", Toast.LENGTH_SHORT).show();
                }else
                {
                mMap.clear();
                drawCircle(llpoint, range);
                ArrayList<LocationModel> test =dbhelper.readTable(food, range);
                for (int i = 0; i <= (test.size() -1); i++) {
                    LatLng foodpoints = new LatLng(test.get(i).getLatitude(), test.get(i).getLongitude());
                    if(isPointOutsideRange(llpoint, foodpoints, range) == false) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.title(test.get(i).getName());
                        markerOptions.position(foodpoints);
                        mMap.addMarker(markerOptions);
                    }
                }
                }

            }
        });

        unibutton = findViewById(R.id.uni);
        String uni = "University";

        unibutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                drawCircle(llpoint, range);
                ArrayList<LocationModel> test =dbhelper.readTable(uni, range);
                for (int i = 0; i <= (test.size() -1); i++) {
                    LatLng unipoints = new LatLng(test.get(i).getLatitude(), test.get(i).getLongitude());
                    if(isPointOutsideRange(llpoint, unipoints, range) == false) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.title(test.get(i).getName());
                        markerOptions.position(unipoints);
                        mMap.addMarker(markerOptions);
                    }
                }

            }
        });

        agencybutton = findViewById(R.id.estateagents);
        String agency = "Agency";

        agencybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                drawCircle(llpoint, range);
                ArrayList<LocationModel> test =dbhelper.readTable(agency, range);
                for (int i = 0; i <= (test.size() -1); i++) {
                    LatLng agencypoints = new LatLng(test.get(i).getLatitude(), test.get(i).getLongitude());
                    if(isPointOutsideRange(llpoint, agencypoints, range) == false) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.title(test.get(i).getName());
                        markerOptions.position(agencypoints);
                        mMap.addMarker(markerOptions);
                    }
                }

            }
        });

        clinicsbutton = findViewById(R.id.clinic);
        String clinic = "Clinic";

        clinicsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                drawCircle(llpoint, range);
                ArrayList<LocationModel> test =dbhelper.readTable(clinic, range);
                for (int i = 0; i <= (test.size() -1); i++) {
                    LatLng clinicpoints = new LatLng(test.get(i).getLatitude(), test.get(i).getLongitude());
                    if(isPointOutsideRange(llpoint, clinicpoints, range) == false) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.title(test.get(i).getName());
                        markerOptions.position(clinicpoints);
                        mMap.addMarker(markerOptions);
                    }
                }

            }
        });

        shopsbutton = findViewById(R.id.supermarkets);
        String shop = "Supermarket";

        shopsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                drawCircle(llpoint, range);
                ArrayList<LocationModel> test =dbhelper.readTable(shop, range);
                for (int i = 0; i <= (test.size() -1); i++) {
                    LatLng shoppoints = new LatLng(test.get(i).getLatitude(), test.get(i).getLongitude());
                    if(isPointOutsideRange(llpoint, shoppoints, range) == false) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.title(test.get(i).getName());
                        markerOptions.position(shoppoints);
                        mMap.addMarker(markerOptions);
                    }
                }

            }
        });

        funbutton = findViewById(R.id.entertainment);
        String fun = "Entertainment";

        funbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                drawCircle(llpoint, range);
                ArrayList<LocationModel> test =dbhelper.readTable(fun, range);
                for (int i = 0; i <= (test.size() -1); i++) {
                    LatLng funpoints = new LatLng(test.get(i).getLatitude(), test.get(i).getLongitude());
                    if(isPointOutsideRange(llpoint, funpoints, range) == false) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.title(test.get(i).getName());
                        markerOptions.position(funpoints);
                        mMap.addMarker(markerOptions);
                    }
                }

            }
        });

    }



    /*--------permission functions----------*/

    private boolean checkGooglePlayServices(){
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int result = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (result == ConnectionResult.SUCCESS){
            return true;
        } else if (googleApiAvailability.isUserResolvableError(result)) {
            Dialog dialog = googleApiAvailability.getErrorDialog(this, result, 201, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    Toast.makeText(MapsActivity.this, "User Cancelled Dialogue", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.show();
        }
        return false;
    }

    private void checkPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                isPermissionGranted = true;
                Toast.makeText(MapsActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), "");
                intent.setData(uri);
                startActivity(intent);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    /*--------map ready----------*/

        @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        /*mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);*/

    }

    /*--------drawing circles----------*/

    private void drawCircle(LatLng point, float radius){
        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();
        // Specifying the center of the circle
        circleOptions.center(point);
        // Radius of the circle
        circleOptions.radius(radius);
        // Border color of the circle
        circleOptions.strokeColor(Color.BLACK);
        // Fill color of the circle
        circleOptions.fillColor(0x30ff0000);
        // Border width of the circle
        circleOptions.strokeWidth(2);
        // Adding the circle to the GoogleMap
        mMap.addCircle(circleOptions);

    }

    /*-----------In range checker-------------*/

    private boolean isPointOutsideRange(LatLng centerPoint, LatLng newPoint, float radius) {
        float[] distances = new float[1];
        Location.distanceBetween(centerPoint.latitude,
                centerPoint.longitude,
                newPoint.latitude,
                newPoint.longitude, distances);
        return radius < distances[0];
    }

}