package com.operationtrashcollection;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Context context = MainActivity.this.getApplicationContext();
                if (ActivityCompat.checkSelfPermission(MainActivity.this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);



//                    return;
                }

                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();

                Toast.makeText(MainActivity.this,
                        "latitude = " + latitude + "; longitude = " + longitude, Toast.LENGTH_LONG).show();
//                Logger logger = new Logger("appLogger");
//                logger.log(Level.INFO, "asdf");
                Logger.getAnonymousLogger().log(Level.INFO, "latitude = " + latitude);
                Logger.getAnonymousLogger().log(Level.INFO, "longtude = " + longitude);



                JSONObject json = new JSONObject();
                JSONObject manJson = new JSONObject();
                try {
                    manJson.put("latitude", latitude);
                    manJson.put("longitude", longitude);
                    json.put("man", manJson);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }



                HttpURLConnection httpcon;

                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    //your codes here


                    try {
                        //Connect
                        httpcon = (HttpURLConnection) ((new URL("http://192.168.0.15:8081/reportTrash").openConnection()));
                        httpcon.setDoOutput(true);
                        httpcon.setRequestProperty("Content-Type", "application/json");
                        httpcon.setRequestProperty("Accept", "application/json");
                        httpcon.setRequestMethod("POST");
                        httpcon.connect();

                        //Write
                        OutputStream os = httpcon.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                        writer.write(manJson.toString());
                        writer.close();
                        os.close();

                        //Read
                        BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), "UTF-8"));

                        String line = null;
                        StringBuilder sb = new StringBuilder();

                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }

                        br.close();
                        sb.toString();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                //https://maps.googleapis.com/maps/api/place/autocomplete/xml?input=marta&types=establishment&location=33.96266807,-84.36111539&radius=500&strictbounds&key=AIzaSyCsUDTqJPhwahDQ_4sCxlibL-zrNQNj314
                //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=33.96266807,-84.36111539&radius=500&type=transit_station&keyword=marta&key=AIzaSyCsUDTqJPhwahDQ_4sCxlibL-zrNQNj314


//                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);


            }
        });

    }
}
