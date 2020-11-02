package com.example.nhantruong.foodfood.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhantruong.foodfood.R;
import com.example.nhantruong.foodfood.adapter.FoodOfCartAdapter;
import com.example.nhantruong.foodfood.intface.AdapterAndActivity;
import com.example.nhantruong.foodfood.model.CustomerInfo;
import com.example.nhantruong.foodfood.model.DriverInfo;
import com.example.nhantruong.foodfood.model.FoodOfCart;
import com.example.nhantruong.foodfood.model.FoodOfOrder;
import com.example.nhantruong.foodfood.model.OrderInfo;
import com.example.nhantruong.foodfood.utils.Constant;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class CartActivity extends AppCompatActivity implements AdapterAndActivity, Constant {

    Toolbar toolbarMain;
    ListView listViewCart;
    TextView textViewToTalOrder;
    Button buttonPaymentOrder;
    ImageView imageViewEmptyCart;

    //Location
    private static final int LOCATION_REQUEST_CODE = 1;
    private LocationRequest locationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private LatLng customerLocation;


    //Tạo bên ngoài để làm chức năng xóa món
    FoodOfCartAdapter adapter;

    //Dữ liệu giỏ hàng
    public static ArrayList<FoodOfCart> cartArrayList;
    //Dữ liệu để thanh toán
    public static ArrayList<FoodOfOrder> foodOfOrders;

    //tổng giá trị giỏ hàng
    int toTal = 0;
    DecimalFormat decimalFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Mapping();
        ActionBar();
        LoadListViewMain();
        Init();

    }

    private void Init() {
        buttonPaymentOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Payment();
            }
        });
        setUpCustomerLocation();
        checkLocationPermission();
    }

    private void Payment() {
        if(foodOfOrders != null && foodOfOrders.size() != 0)
        {
            final Dialog dialog = new Dialog(CartActivity.this);
            dialog.setContentView(R.layout.dialog_accept_order);
            final EditText edtNameCustomer = dialog.findViewById(R.id.editText_NameCustomer_DialogOrder);
            final EditText edtAddressCustomer = dialog.findViewById(R.id.editText_AddressCustomer_DialogOrder);
            final EditText edtPhoneCustomer = dialog.findViewById(R.id.editText_PhoneCustomer_DialogOrder);
            final Button btSend = dialog.findViewById(R.id.button_Send_DialogOrder);

            edtNameCustomer.setText(AccountActivity.accountInfoMain.getSurName()+" "+AccountActivity.accountInfoMain.getFirstName());
            edtAddressCustomer.setText(AccountActivity.accountInfoMain.getAddress());
            edtPhoneCustomer.setText(AccountActivity.accountInfoMain.getPhone());

            btSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String date = java.time.LocalDate.now().toString();
                    String time = java.time.LocalTime.now().toString();
                    String total = String.valueOf(toTal);



                    CustomerInfo customerInfo = new CustomerInfo(edtNameCustomer.getText().toString()
                            ,edtAddressCustomer.getText().toString()
                            ,edtPhoneCustomer.getText().toString()
                            ,AccountActivity.accountInfoMain.getEmail(),String.valueOf(customerLocation.longitude),String.valueOf(customerLocation.latitude));

                    DriverInfo driverInfo = new DriverInfo("null","null","null","null","null");


                    DatabaseReference customerRef = FirebaseDatabase.getInstance()
                            .getReference()
                            .child(USER)
                            .child(ORDERINFOS)
                            .child(TRANSACTIONS)
                            .child(WAITING);
                    String key = customerRef.push().getKey();
                    HashMap map = new HashMap();
                    map.put(CUSTOMER_INFO,customerInfo);
                    map.put(DRIVER_INFO,driverInfo);
                    map.put(STATUS,STATUS_WAIT_DRIVER);
                    map.put(TIME,time);
                    map.put(DATE,date);
                    map.put(TOTAL,total);
                    map.put(FOOD_OF_ORDERS,foodOfOrders);

                    customerRef.child(key).updateChildren(map);

                    dialog.dismiss();
                    Toast.makeText(CartActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();



                    cartArrayList = null;
                    foodOfOrders = null;
                    listViewCart.setAdapter(null);
                    EmptyCart();
                    ResetToTalCart();
                }



            });


            dialog.show();
        }
        else
        {
            buttonPaymentOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

    }

    private void LoadListViewMain(){

        //TH chưa thêm món vào giỏ hàng
        if(cartArrayList != null)
        {
            adapter = new FoodOfCartAdapter(cartArrayList,this,this);

            listViewCart.setAdapter(adapter);

            imageViewEmptyCart.setVisibility(View.INVISIBLE);
        }



    }
    private void ActionBar()
    {
        setSupportActionBar(toolbarMain);
        //Hiển thị nút back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }
    private void Mapping(){
        toolbarMain = findViewById(R.id.toolBar_CartActivity);
        listViewCart = findViewById(R.id.listView_CartActivity);
        buttonPaymentOrder = findViewById(R.id.button_CartPayment);
        textViewToTalOrder = findViewById(R.id.textView_TotalOrder);
        imageViewEmptyCart = findViewById(R.id.imageView_EmptyCart);

        //
        decimalFormat = new DecimalFormat("###,###,###");


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Thiết lập chức năng trên menu
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void RemoveItemInListView(int position) {
        cartArrayList.remove(position);
        foodOfOrders.remove(position);
        adapter.notifyDataSetChanged();
        Payment();




    }

    @Override
    public void TotalOfCart(int toTalItem) {
        if(toTalItem == 0)
        {
            toTal = 0;
            textViewToTalOrder.setText(decimalFormat.format(toTal)+" đ");
            EmptyCart();
        }
        else
        {
            int amount;
            int price;
            for(int i=0;i<toTalItem;i++)
            {
                amount = cartArrayList.get(i).getCount();
                price = Integer.parseInt(cartArrayList.get(i).getPrice());
                toTal += (amount*price);
            }
            textViewToTalOrder.setText(decimalFormat.format(toTal)+" đ");
        }
    }

    @Override
    public void ResetToTalCart() {
        toTal = 0;

        textViewToTalOrder.setText(decimalFormat.format(toTal)+" đ");
    }


    public void EmptyCart() {
        imageViewEmptyCart.setVisibility(View.VISIBLE);
    }



    //Location

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


                    customerLocation = new LatLng(location.getLatitude(),location.getLongitude());

                }
            }
            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {

            }
        };
    }

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Quyền truy câp")
                    .setMessage("Cho phép truy cập vị trí")
                    .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(CartActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                            , LOCATION_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
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
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions
            , @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case LOCATION_REQUEST_CODE:{
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//                        day la ham cap nhat location
                        mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Cho phép truy cập vị trí để sử dụng", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }
}
