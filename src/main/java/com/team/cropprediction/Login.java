package com.team.cropprediction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Login extends AppCompatActivity {

    EditText user,password;
    Button Signin;
    TextView Signup,Fpass;
    CheckBox checkBox;

    public  static  String PREFS_NAME="MyPrefsFile";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = findViewById(R.id.user);
        password = findViewById(R.id.password);
        Signup = findViewById(R.id.signup);
        Fpass = findViewById(R.id.fpass);
        Signin = findViewById(R.id.signin);
        checkBox = findViewById(R.id.checkBox);

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,Registration.class);
                startActivity(intent);
            }
        });


        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String User= user.getText().toString().trim();
                String Password= password.getText().toString().trim();

                if (User.isEmpty())
                {
                    Toast.makeText(Login.this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                }
                else if (Password.isEmpty())
                {
                    Toast.makeText(Login.this, "Enter Password", Toast.LENGTH_SHORT).show();
                }
                else {

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://tsm.ecssofttech.com/Library/api/Crop_Prediction_Login.php?User=" + User + "&Password=" + Password + "")
                            .build();

                    try {
                        Response response = client.newCall(request).execute();
                        String responseString = Objects.requireNonNull(response.body()).string();
                        System.out.println(responseString);
                        String str = "\tSuccess";
                        if (responseString.equals(str)){

                            Toast.makeText(Login.this,"Login Success",Toast.LENGTH_SHORT).show();

                            if (checkBox.isChecked()) {
                                SharedPreferences sharedPreferences = getSharedPreferences(Login.PREFS_NAME,0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("user",user.getText().toString());
                                editor.putString("password",password.getText().toString());
                                editor.apply();
                                editor.commit();

                                Intent in = new Intent(Login.this,Dashboard.class);
                                startActivity(in);
                                finish();
                            }
                            else {
                                Intent in = new Intent(Login.this,Dashboard.class);
                                startActivity(in);
                                finish();
                            }



                        }else {
                            Toast.makeText(Login.this,"Please Enter Correct Username and password",Toast.LENGTH_SHORT).show();
                        }



                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        Fpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,ForgotPassword.class);
                startActivity(intent);
            }
        });
    }
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}