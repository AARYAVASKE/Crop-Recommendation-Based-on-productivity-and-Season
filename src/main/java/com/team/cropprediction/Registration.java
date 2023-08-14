package com.team.cropprediction;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Registration extends AppCompatActivity {
EditText name,mobile,pass,cpass,email;
Button signup;
TextView signin;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        signin = findViewById(R.id.signin);
        signup = findViewById(R.id.signup);
        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        pass = findViewById(R.id.pass);
        cpass = findViewById(R.id.cpass);
        email = findViewById(R.id.email);

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

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registration.this, Login.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = name.getText().toString().trim();
                String Mobile = mobile.getText().toString().trim();
                String Email = email.getText().toString().trim();
                String Password = pass.getText().toString().trim();
                String ConfirmPassword = cpass.getText().toString().trim();

                ProgressDialog progressDialog = new ProgressDialog(Registration.this);
                progressDialog.setMessage("Please Wait");
                progressDialog.show();

                if (Name.isEmpty())
                {
                    Toast.makeText(Registration.this, "Enter Name", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else if (Mobile.isEmpty())
                {
                    Toast.makeText(Registration.this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else if (Mobile.length()!=10)
                {
                    Toast.makeText(Registration.this, "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else if (Email.isEmpty())
                {
                    Toast.makeText(Registration.this, "Enter Email Address", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else if (Password.isEmpty())
                {
                    Toast.makeText(Registration.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else if (ConfirmPassword.isEmpty())
                {
                    Toast.makeText(Registration.this, "Enter Confirm Password", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else if (!PASSWORD_PATTERN.matcher(Password).matches()) {
                    Toast.makeText(Registration.this, "Weak Password", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else if (!(Password).equals(ConfirmPassword)) {
                    Toast.makeText(Registration.this, "Password Does Not Match", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else {

                    StringRequest request = new StringRequest(Request.Method.POST, "http://tsm.ecssofttech.com/Library/api/Crop_Prediction_Registration.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String s1 = "Record Inserted Successfully";
                            String s2 = "User Already Exist";

                            if (response.equalsIgnoreCase(s2)) {
                                Toast.makeText(Registration.this, "User Already Exist", Toast.LENGTH_SHORT).show();
                                name.setText("");
                                mobile.setText("");
                                pass.setText("");
                                cpass.setText("");
                                progressDialog.dismiss();
                            }
                            else if(response.equalsIgnoreCase(s1)) {
                                Toast.makeText(Registration.this, "Registration Successfully", Toast.LENGTH_SHORT).show();
                                /*SharedPreferences sharedPreferences = getSharedPreferences(Login.PREFS_NAME,0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("userName",userName);
                                editor.putString("password",password);
                                editor.apply();
                                editor.commit();*/

                                Intent intent = new Intent(Registration.this,Dashboard.class);
                                //intent.putExtra("userName",userName);
                                //intent.putExtra("password",password);
                                startActivity(intent);
                                finish();
                                name.setText("");
                                mobile.setText("");
                                pass.setText("");
                                cpass.setText("");
                                progressDialog.dismiss();



                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(Registration.this, "Failed", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }){
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();

                            params.put("Name",Name);
                            params.put("Mobile",Mobile);
                            params.put("Password",Password);
                            params.put("Email",Email);
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(Registration.this);
                    requestQueue.add(request);



                }




            }
        });
    }
    public void onBackPressed() {
        super.onBackPressed();
        Intent in2 = new Intent(Registration.this,Login.class);
        startActivity(in2);
        finish();
    }
}