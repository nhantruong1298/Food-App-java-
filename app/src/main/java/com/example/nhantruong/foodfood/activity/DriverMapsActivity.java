package com.example.nhantruong.foodfood.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.nhantruong.foodfood.R;
import com.example.nhantruong.foodfood.model.OrderInfo;
import com.example.nhantruong.foodfood.utils.Constant;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DriverMapsActivity extends FragmentActivity implements OnMapReadyCallback,Constant {

    private static final int LOCATION_REQUEST_CODE =1 ;
    private GoogleMap mMap;
    private ArrayList<Marker> listCustomerMarker = new ArrayList<>();
    private LatLng customerLocation;
    private Marker customerMarker;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private String driveEmail;
    //
    Toolbar toolbarMain;
    Button btCustomerLocation;
    //
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Mapping();
        getDataFromItent();
        Init();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpCustomerLocation();
        checkLocationPermission();
        setCustomerLocation(customerLocation);
    }


    private void Init() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        btCustomerLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(customerLocation,17));
            }
        });
    }

    private void Mapping()
    {
        toolbarMain = findViewById(R.id.toolBar_DriverMapsActivity);
        btCustomerLocation = findViewById(R.id.bt_LocationCustomer);
    }

    private void getDataFromItent(){
        Intent intent = getIntent();
        double latitude = Double.parseDouble(intent.getStringExtra("latitude"));
        double longitude = Double.parseDouble(intent.getStringExtra("longitude"));
        customerLocation = new LatLng(latitude, longitude);

    }

    private void setCustomerLocation(LatLng customerLocation) {
        customerMarker = mMap.addMarker(new MarkerOptions()
                .position(customerLocation)
                .title("Khách Hàng")
        );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(customerLocation,17));
    }


    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            new AlertDialog.Builder(this)
                    .setTitle("Quyền truy cập")
                    .setMessage("Cho phép định vị địa chỉ ?")
                    .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(DriverMapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("Không", new DialogInterface.OnClickListener() {
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
                        // day la ham cap nhat location
                        mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Hãy bật định vị khi sử dụng ứng dụng", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

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
                for (final Location location : locationResult.getLocations()) {
                    //lấy tọa độ nhận được, để cập nhật lên firebase
                    lastLocation = locationResult.getLastLocation();
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                    mDatabaseReference.child(USER).child(ORDERINFOS).child(TRANSACTIONS).child(WAITING).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            if(dataSnapshot != null)
                            {
                                OrderInfo orderInfo = dataSnapshot.getValue(OrderInfo.class);
                                if(orderInfo.getDriverInfo().getEmail().equals(AccountActivity.accountInfoMain.getEmail()))
                                {
                                    mDatabaseReference.child(USER)
                                            .child(ORDERINFOS)
                                            .child(TRANSACTIONS)
                                            .child(WAITING)
                                            .child(dataSnapshot.getKey())
                                            .child(DRIVER_INFO)
                                            .child(LATITUDE).setValue(String.valueOf(location.getLatitude()));
                                    mDatabaseReference.child(USER)
                                            .child(ORDERINFOS)
                                            .child(TRANSACTIONS)
                                            .child(WAITING)
                                            .child(dataSnapshot.getKey())
                                            .child(DRIVER_INFO)
                                            .child(LONGITUDE).setValue(String.valueOf(location.getLongitude()));

                                }
                            }
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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
            }
            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {

            }
        };
    }
}
