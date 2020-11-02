package com.example.nhantruong.foodfood.activity;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nhantruong.foodfood.R;
import com.example.nhantruong.foodfood.adapter.InfoAccountAdapter;
import com.example.nhantruong.foodfood.model.AccountInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountActivity extends AppCompatActivity {

    ListView listView_Main;
    Button btLogOut,btReSetPassWord,btUpdateInFo;
    Toolbar toolbarMain;
    //Tài khoản người udngf
    public static AccountInfo accountInfoMain;

    //Để reset adapter
    public  InfoAccountAdapter adapter;
    //
    private FirebaseAuth mAuth;
    public static FirebaseUser mFirebaseUser;
    //
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Mapping();
        ActionBar();
        Init();
        LoadListView();

    }

    private void Init() {
        btLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogOut();
            }
        });

        btUpdateInFo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btReSetPassWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AccountActivity.this, "Check email để đổi mật khảu", Toast.LENGTH_SHORT).show();
                ResetPassWord();
            }
        });
    }

    void LoadListView(){

        InfoAccountAdapter adapter = new InfoAccountAdapter(accountInfoMain,getApplicationContext());

        listView_Main.setAdapter(adapter);
    }
    private void Mapping() {
        listView_Main = findViewById(R.id.listView_AccountActivity);
        btLogOut = findViewById(R.id.button_LogOutAccountActivity);
        btReSetPassWord = findViewById(R.id.button_ReSetPasswordAccountActivity);
        btUpdateInFo = findViewById(R.id.button_UpdateAccountActivity);
        toolbarMain = findViewById(R.id.toolBar_AccountActivity);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }
    private void LogOut(){
        btLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(AccountActivity.this);
                dialog.setContentView(R.layout.dialog_logout);

                Button btYes = dialog.findViewById(R.id.button_YesDialogLogOut);
                Button btNo = dialog.findViewById(R.id.button_NoDiaLogLogOut);

                btNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       Intent intent = new Intent(AccountActivity.this,LoginActivity.class);
                       finishAffinity();
                       startActivity(intent);


                    //    System.exit(0);
                    }
                });
                dialog.show();
            }
        });

    }
    private void ResetPassWord(){
        mAuth.sendPasswordResetEmail(mFirebaseUser.getEmail());

    }
    private void ActionBar()
    {
        setSupportActionBar(toolbarMain);
        //Hiển thị nút back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
