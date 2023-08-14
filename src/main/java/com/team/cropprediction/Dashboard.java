package com.team.cropprediction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.OkHttpClient;

public class Dashboard extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public NavigationView nav;
    public Toolbar toolbar;
    String user;
    String password;
    TextView t1,t2,t3,t4;
   Button button;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nav = findViewById(R.id.nav);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        SharedPreferences sharedPreferences = getSharedPreferences(Login.PREFS_NAME,0);
        user = sharedPreferences.getString("user", "");
        password = sharedPreferences.getString("password", "");

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.menu_dashboard:

                        break;

                    case R.id.menu_logout:
                        SharedPreferences.Editor editor = getSharedPreferences(Login.PREFS_NAME,0).edit();
                        editor.remove("user");
                        editor.remove("password");
                        editor.apply();
                        overridePendingTransition(0,0);
                        finish();
                        Intent intent1 = new Intent(Dashboard.this, Login.class);
                        startActivity(intent1);
                        Toast.makeText(Dashboard.this, "LogOut Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    default:
                        return true;
                }
                return true;
            }
        });

        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);
        t4 = findViewById(R.id.t4);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String Soil_Moisture= t1.getText().toString().trim();
            String Tempreture= t2.getText().toString().trim();
            String Humidity= t3.getText().toString().trim();
            String rainfall = t4.getText().toString().trim();

            if(Soil_Moisture.isEmpty())
            {
                Toast.makeText(Dashboard.this, "Enter Soil Moisture", Toast.LENGTH_SHORT).show();
            }
            else if (Tempreture.isEmpty())
            {
                Toast.makeText(Dashboard.this, "Enter temperature", Toast.LENGTH_SHORT).show();
            }
            else if (Humidity.isEmpty())
            {
                Toast.makeText(Dashboard.this, "Enter Humidity", Toast.LENGTH_SHORT).show();
            }
            else if(rainfall.isEmpty())
            {
                Toast.makeText(Dashboard.this, "Enter Rainfall", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Intent intent = new Intent(Dashboard.this,Prediction.class);
                startActivity(intent);
            }
            }
        });
        getData();

    }
    private void getData() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("http://tsm.ecssofttech.com/Library/api/Crop_Prediction.php")
                .build();
        try {
            okhttp3.Response response = client.newCall(request).execute();
            String responseString = Objects.requireNonNull(response.body()).string();
            System.out.println(responseString);

            JSONArray contacts = new JSONArray(responseString);


            for (int i = 0; i < contacts.length(); i++) {
                JSONObject c = contacts.getJSONObject(i);
                String Soil_Moisturizer = c.getString("Soil_Moisturizer");
                String Tempreture = c.getString("Tempreture");
                String Humidity = c.getString("Humidity");
                String rainfall = c.getString("rainfall");

                t1.setText(Soil_Moisturizer+" kPa");
                t2.setText(Tempreture+" °C");
                t3.setText(Humidity+" %");
                t4.setText(rainfall+" mm");

            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
}