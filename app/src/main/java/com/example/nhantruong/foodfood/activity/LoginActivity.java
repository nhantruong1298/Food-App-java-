package com.example.nhantruong.foodfood.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nhantruong.foodfood.R;
import com.example.nhantruong.foodfood.model.AccountInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    //dùng static truyền dữ liệu từ đăng ký
    public static EditText edt_Email;
    public static TextInputEditText edt_PassWord;

    Button buttonLogin,buttonSignUp,buttonForgotPassWord;

    //FireBase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Mapping();
        Init();

        edt_Email.setText("nhantruong1298@gmail.com");
        edt_PassWord.setText("111111");
    }

    private void Init() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edt_Email.getText().toString().trim();
                String password = edt_PassWord.getText().toString().trim();
                Login(email,password);
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        buttonForgotPassWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReSetPassword();
            }
        });
    }

    void Login(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                             final FirebaseUser user = mAuth.getCurrentUser();
                            AccountActivity.mFirebaseUser = user;

                            SearchCustomer(user);
                            SearchDriver(user);




                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại",
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                });

    }

    private void SearchDriver(final FirebaseUser user) {
        mDatabase.child("User").child("AccountInfo").child("Drivers").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                AccountInfo accountInfo = dataSnapshot.getValue(AccountInfo.class);
                if(accountInfo.getEmail().equals(user.getEmail()))
                {
                    AccountActivity.accountInfoMain = accountInfo;
                    Intent intent = new Intent(LoginActivity.this,DriverActivity.class);
                    finish();
                    startActivity(intent);

                    DriverActivity.mAuth = mAuth;
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
    private void SearchCustomer(final FirebaseUser user){
        mDatabase.child("User").child("AccountInfo").child("Customers").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                AccountInfo accountInfo = dataSnapshot.getValue(AccountInfo.class);
                if(accountInfo.getEmail().equals(user.getEmail()))
                {
                    AccountActivity.accountInfoMain = accountInfo;
                    Intent intent = new Intent(LoginActivity.this,CustomerActivity.class);
                    finish();
                    startActivity(intent);
                    CustomerActivity.mAuth  = mAuth;
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
    private void Mapping() {
        edt_Email = findViewById(R.id.edtiText_EmailLoginActivity);
        edt_PassWord = findViewById(R.id.editText_PassWordLoginActivity);
        buttonLogin = findViewById(R.id.button_LoginActivity);
        buttonSignUp = findViewById(R.id.button_SignUpLoginActivity);
        buttonForgotPassWord = findViewById(R.id.button_ForgotPassWord);




    }

    @Override
    public void onBackPressed() {
     //   Thoát app
        System.exit(0);
    }
    private void ReSetPassword(){
        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.setContentView(R.layout.dia_reset_password);

        final EditText etEmail = dialog.findViewById(R.id.editText_Email_DialogResetPassword);
        Button btAccept = dialog.findViewById(R.id.button_Accept_DialogResetPassword);

        btAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                if(email.contains("@"))
                {
                    mAuth.sendPasswordResetEmail(email);
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Check Email để đổi mật khẩu", Toast.LENGTH_SHORT).show();
                }

            }
        });
        dialog.show();
    }
}
