package com.example.mr_paul.helperapp;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private boolean isServiceActivated = false;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            // we have got the permission
            // go ahead and start the service in the foreground
            Toast.makeText(this, "Service Started Successfully!", Toast.LENGTH_SHORT).show();
            ContextCompat.startForegroundService(MainActivity.this,new Intent(MainActivity.this,BackgroundService.class));
        }
        else{
            // show an toast message asking for the permission
            Toast.makeText(this, "Do I really need to tell, why you should give me location access??", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // referencing to the views
        final Button startService = findViewById(R.id.start_service);
        final Button stopService = findViewById(R.id.stop_service);
        Button viewProject = findViewById(R.id.github_link);


        // set onClick listener on the startService button and check for location permission there
        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check for location access permission
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){

                    // if permission is not granted, ask for the permission
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                }
                else{
                    // location permission is granted
                    if(!isServiceActivated){
                        // start the service
                        Toast.makeText(MainActivity.this, "Service Started Successfully!", Toast.LENGTH_SHORT).show();
                        ContextCompat.startForegroundService(MainActivity.this,new Intent(MainActivity.this,BackgroundService.class));
                    }

                }
            }
        });


        // if the service is no longed need by the user, turn it off
        stopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call an intent to stop the service
                Toast.makeText(MainActivity.this, "Service Stopped!", Toast.LENGTH_SHORT).show();
                stopService(new Intent(MainActivity.this,BackgroundService.class));
            }
        });


        // calling an intent to browser for viewing the github page for this project
        viewProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://github.com/jyotirmoy-paul/HelperApp_Geofencing";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

    }
}
