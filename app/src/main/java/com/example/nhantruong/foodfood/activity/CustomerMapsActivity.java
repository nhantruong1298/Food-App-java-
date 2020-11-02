package com.example.nhantruong.foodfood.activity;



import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.nhantruong.foodfood.R;
import com.example.nhantruong.foodfood.fragment.OrderInfomation_Customer_Fragment;
import com.example.nhantruong.foodfood.model.OrderInfo;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class CustomerMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int LOCATION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private LatLng customerLocation,driverLocation;


    private Marker driverMaker;
    private  String driverEmail;
    //
    Button btDriverLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Mapping();
        Init();
       GetDataFromIntent();
       setUpCustomerLocation();

    }

    private void Init() {
        btDriverLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(driverLocation != null)
                {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(driverLocation,16));
                }

            }
        });
    }

    private void Mapping() {
        btDriverLocation = findViewById(R.id.bt_LocationDriver);
    }

    private void GetDataFromIntent()
    {
        Intent intent = getIntent();
        if(intent != null)
        {
            driverEmail = intent.getStringExtra("DriverEmail");
        }

    }

    // buoc nay la buoc chuan bi cho su kien cap nhat vi tri cua user
    private void setUpCustomerLocation() {
        mFusedLocationClient = new FusedLocationProviderClient(this);
        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //        callback khi gọi requestLocationUpdate
        mLocationCallback = new LocationCallback(){
            @Override
            //gọi event cập nhật vị trí
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    //lấy tọa độ nhận được, để cập nhật lên firebase
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                    customerLocation = new LatLng(location.getLatitude(),location.getLongitude());



                }
            }
            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                Log.e("nhan",""+locationAvailability.toString());
            }
        };
    }

    //  khi map khoi tao xong roi moi bat dau lay location dua len
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // check permission truoc, roi moi goi ham` lay location
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            checkLocationPermission();
        }
        getDriverLocation();
    }

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            new AlertDialog.Builder(this)
                    .setTitle("Grant permission")
                    .setMessage("Please provide permission")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(CustomerMapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create()
                    .show();
        }
        else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
                mMap.setMyLocationEnabled(true);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case LOCATION_REQUEST_CODE:{
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//                        day la ham cap nhat location
                        mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Please grant permission", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

//    get driver locatuon
    private void getDriverLocation(){
        final DatabaseReference driverRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("User")
                .child("OrdersInfo")
                .child("Transactions")
                .child("Waiting");
        driverRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists() ){

                    OrderInfo orderInfo = dataSnapshot.getValue(OrderInfo.class);
                    if(orderInfo.getDriverInfo().getEmail().equals(driverEmail) )
                    {
                        double latitude = Double.parseDouble(orderInfo.getDriverInfo().getLatitude());
                        double longitude = Double.parseDouble(orderInfo.getDriverInfo().getLongitude());
                        driverLocation = new LatLng(latitude,longitude);



                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.driver);
                        driverMaker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(latitude,longitude))
                                .title("Tài xế").icon(icon));

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(driverLocation,16));
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists() ){

                    OrderInfo orderInfo = dataSnapshot.getValue(OrderInfo.class);
                    if(orderInfo.getDriverInfo().getEmail().equals(driverEmail) )
                    {
                        double latitude = Double.parseDouble(orderInfo.getDriverInfo().getLatitude());
                        double longitude = Double.parseDouble(orderInfo.getDriverInfo().getLongitude());
                        driverLocation = new LatLng(latitude,longitude);

                      checkDistance(customerLocation,driverLocation);

                        if (driverMaker != null){
                            driverMaker.remove();
                        }

                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.driver);
                        driverMaker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(latitude,longitude))
                                .title("Tài xế").icon(icon));

                    }
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void checkDistance(LatLng customerLocation,LatLng driverLocation){

        Location location_customer = new Location("");
        location_customer.setLatitude(customerLocation.latitude);
        location_customer.setLongitude(customerLocation.longitude);

        Location location_driver = new Location("");
        location_driver.setLongitude(driverLocation.longitude);
        location_driver.setLatitude(driverLocation.latitude);

        float distance = location_customer.distanceTo(location_driver);
        if(distance < 100)
        {
            Toast.makeText(this, "Tài xế sắp đến", Toast.LENGTH_LONG).show();
        }

    }
}


