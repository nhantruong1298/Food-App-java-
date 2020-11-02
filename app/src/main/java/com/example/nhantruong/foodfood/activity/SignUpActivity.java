package com.example.nhantruong.foodfood.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

public class SignUpActivity extends AppCompatActivity {

    EditText edt_SurName,edt_FirstName,edt_Phone,edt_Email,edt_PassWord,edt_RePassWord;
    Button btn_SignUp;
    Toolbar toolbar_Main;

    //Authentication
    private FirebaseAuth mAuth;
    AccountInfo accoutSignUp;
    //DataBase
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Mapping();
        ActionBar();
        Init();

    }

    private void Init() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btn_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edt_Email.getText().toString().trim();
                String password = edt_PassWord.getText().toString().trim();
                SignUp(email,password);
            }
        });
    }

    private void SignUp(final String email, final String password) {


        if(isEmpty(edt_SurName))
        {
            edt_SurName.setError("Không được để trống");
        }
        else if(isEmpty(edt_FirstName))
        {
            edt_FirstName.setError("Không được để trống");
        }
        else if(isEmpty(edt_Email))
        {
            edt_Email.setError("Không được để trống");
        }
        else if(isEmpty(edt_Phone))
        {
            edt_Phone.setError("Không được để trống");
        }
        else if(isEmpty(edt_PassWord))
        {
            edt_PassWord.setError("Không được để trống");
        }
        else if(isEmpty(edt_RePassWord))
        {
            edt_RePassWord.setError("Không được để trống");
        }
        else if(!edt_Email.getText().toString().contains("@"))
        {
            edt_Email.setError("Email không hợp lệ");
        }
        else if(edt_PassWord.getText().toString().length() < 6 )
        {
            edt_PassWord.setError("Mật khẩu phải dài hơn 6 kí tự");
        }
        else if(!edt_PassWord.getText().toString().equals(edt_RePassWord.getText().toString()))
        {
            edt_RePassWord.setError("Mật khẩu không trùng khớp");
        }
        else
        {


            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Đăng ký thành công

                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(SignUpActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();

                                accoutSignUp = new AccountInfo();
                                accoutSignUp.setSurName(edt_SurName.getText().toString().trim());
                                accoutSignUp.setFirstName(edt_FirstName.getText().toString().trim());
                                accoutSignUp.setAddress("");
                                accoutSignUp.setEmail(edt_Email.getText().toString().trim());
                                accoutSignUp.setPhone(edt_Phone.getText().toString().trim());


                                mDatabase.child("User").child("AccountInfo").child("Customers").push().setValue(accoutSignUp);
                                LoginActivity.edt_Email.setText(email);
                                LoginActivity.edt_PassWord.setText(password);
                                onBackPressed();

                            } else {
                                // Đăng ký thất bại
                               if(isOnline())
                               {
                                   Toast.makeText(SignUpActivity.this, "Chưa kết nối internet", Toast.LENGTH_SHORT).show();
                               }
                               else
                               {
                                   Toast.makeText(SignUpActivity.this, "Thông tin không hợp lệ", Toast.LENGTH_SHORT).show();
                               }


                            }

                            // ...
                        }
                    });
        }




    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }
    private void Mapping() {
        edt_SurName = findViewById(R.id.editText_SurNameSignUpActivity);
        edt_FirstName = findViewById(R.id.editText_FirstNameSignUpActivity);
        edt_Phone = findViewById(R.id.editText_PhoneSignUpActivity);
        edt_Email = findViewById(R.id.editText_EmailSignUpActivity);
        edt_PassWord = findViewById(R.id.editText_PassWordSignUpActivity);
        edt_RePassWord = findViewById(R.id.editText_RePassWordSignUpActivity);
        btn_SignUp = findViewById(R.id.button_SignUpActivity);
        toolbar_Main = findViewById(R.id.toolBar_SignUpActivity);


    }
    private void ActionBar(){
        setSupportActionBar(toolbar_Main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isEmpty(EditText edt)
    {
        if(edt.getText().toString().trim().equals(""))
            return  true;
        return false;




    }
}