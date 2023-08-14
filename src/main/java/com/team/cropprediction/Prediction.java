package com.team.cropprediction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.OkHttpClient;

public class Prediction extends AppCompatActivity {
    TextView t1;
    TextView t2;
    Button button2;
    Button button3;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);
        t1 = findViewById(R.id.t1);
        t2=findViewById(R.id.t2);
       button2=findViewById(R.id.button2);
       button2.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String url = "https://www.google.com/search?q='"+t1.getText().toString()+"'";
               Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
               startActivity(urlIntent);
           }
       });
        button3=findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.google.com/search?q='"+t2.getText().toString()+"'";
                Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(urlIntent);
            }
        });

        getData();

    }




    private void getData()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("http://tsm.ecssofttech.com/Library/api/Crop_Prediction_Max_Value.php")
                .build();
        try {
            okhttp3.Response response = client.newCall(request).execute();
            String responseString = Objects.requireNonNull(response.body()).string();
            System.out.println(responseString);

            JSONArray contacts = new JSONArray(responseString);


            for (int i = 0; i < contacts.length(); i++) {
                JSONObject c = contacts.getJSONObject(i);
                String Prediction = c.getString("Prediction");

                String[] arrOfStr = Prediction.split("-", 2);
                String first=arrOfStr[0];
                String second=arrOfStr[1];
                t1.setText(first);

                t2.setText(second);







            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
    public void onBackPressed() {
        super.onBackPressed();
        Intent in3 = new Intent(Prediction.this,Dashboard.class);
        startActivity(in3);
        finish();
    }
}