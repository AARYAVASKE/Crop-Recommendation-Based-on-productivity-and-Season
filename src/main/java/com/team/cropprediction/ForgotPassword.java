package com.team.cropprediction;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ForgotPassword extends AppCompatActivity {
EditText mobile,pass,cpass;
CardView n_card,w_card;
Button check,reset;
String Mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mobile = findViewById(R.id.mobile);
        pass = findViewById(R.id.pass);
        cpass = findViewById(R.id.cpass);
        n_card = findViewById(R.id.n_card);
        w_card = findViewById(R.id.w_card);
        check = findViewById(R.id.check);
        reset = findViewById(R.id.reset);

        n_card.setVisibility(View.GONE);
        w_card.setVisibility(View.GONE);

        final Pattern PASSWORD_PATTERN =
                Pattern.compile("^" +
                        "(?=.*[0-9])" +         //at least 1 digit
                        "(?=.*[a-z])" +         //at least 1 lower case letter
                        "(?=.*[A-Z])" +         //at least 1 upper case letter
                        // "(?=.*[a-zA-Z])" +      //any letter
                        "(?=.*[@#$%^&+=])" +    //at least 1 special character
                        "(?=\\S+$)" +           //no white spaces
                        ".{4,}" +               //at least 4 characters
                        "$");

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Mobile = mobile.getText().toString().trim();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://tsm.ecssofttech.com/Library/api/Crop_Prediction_Check_User.php?mobile=" + Mobile + "")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    String responseString = Objects.requireNonNull(response.body()).string();
                    System.out.println(responseString);
                    String s2 = responseString.toString().trim();
                    String s1 = "Registred User";

                    System.out.println(s2);

                    if(responseString.equals(s1)){
                        Toast.makeText(ForgotPassword.this, "UserFound", Toast.LENGTH_SHORT).show();
                        n_card.setVisibility(View.VISIBLE);
                        w_card.setVisibility(View.GONE);

                    }else{
                        Toast.makeText(ForgotPassword.this, "User Not Found", Toast.LENGTH_SHORT).show();

                        w_card.setVisibility(View.VISIBLE);
                        n_card.setVisibility(View.GONE);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Mobile = mobile.getText().toString().trim();
                String Password = pass.getText().toString().trim();
                String CPassword = cpass.getText().toString().trim();

                if (Password.isEmpty())
                {
                    Toast.makeText(ForgotPassword.this, "Enter New Password", Toast.LENGTH_SHORT).show();
                }
                else if (!(Password).equals(CPassword))
                {
                    Toast.makeText(ForgotPassword.this, "Password Does Not Match", Toast.LENGTH_SHORT).show();
                }
                else if (!PASSWORD_PATTERN.matcher(Password).matches())
                {
                    Toast.makeText(ForgotPassword.this, "Weak Password", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://tsm.ecssofttech.com/Library/api/Crop_Prediction_Reset_Password.php?mobile=" + Mobile + "&password=" + CPassword +"")
                            .build();

                    try {
                        Response response = client.newCall(request).execute();
                        String responseString1 = Objects.requireNonNull(response.body()).string();
                        System.out.println(responseString1);
                        String s2 = responseString1.toString().trim();
                        String s1 = "Password Reset Successfully";

                        System.out.println(s2);

                        if(responseString1.equals(s1)){
                            Toast.makeText(ForgotPassword.this, "Reset Password Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ForgotPassword.this,Login.class);
                            startActivity(intent);
                            finish();

                        }else{
                            Toast.makeText(ForgotPassword.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}