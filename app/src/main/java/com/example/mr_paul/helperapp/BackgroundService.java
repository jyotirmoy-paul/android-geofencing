package com.example.mr_paul.helperapp;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import static com.example.mr_paul.helperapp.App.CHANNEL_ID;

public class BackgroundService extends Service implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{


    // important global variables
    GoogleApiClient mGoogleApiClient;
    LocationCallback mLocationCallbacks;
    LocationRequest mLocationRequest;
    AudioManager audioManager;
    private boolean gotOutOfCampusForFirstTime = false;


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
       // do nothing
    }

    @Override
    public void onConnectionSuspended(int i) {
       // do nothing
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        // get my current location
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); // update location every 10 seconds

        // now if we have the authority to look into user's current location, do update get it
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest,mLocationCallbacks,null);
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();

        audioManager = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);

        // the following code needs to be run only once in the entire lifecycle of this service class
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        // setting the locationCallback functionality
        mLocationCallbacks = new LocationCallback(){

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if(locationResult == null){
                    return;
                }

                Location location = locationResult.getLastLocation();
                Double latitude = location.getLatitude();
                Double longitude = location.getLongitude();


                // if the location is inside the defined fence, do following
                if(isInCampus(latitude,longitude)){
                    gotOutOfCampusForFirstTime = true;

                    // phone is in the campus, switch to silence mode, if not already
                    // try to put the phone to vibrate mode
                    try{
                        if(audioManager.getRingerMode() != AudioManager.RINGER_MODE_VIBRATE){
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                        }
                    }
                    catch (Exception e){
                        // do nothing
                    }

                    // try to put the phone in silent mode
                    try{
                        if(audioManager.getRingerMode() != AudioManager.RINGER_MODE_SILENT){
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                        }

                    }
                    catch (Exception e){
                        // do nothing
                    }
                }

                else{
                    // put phone into general mode, if not already (only once)
                    /*
                    * The user can keep either on silent
                    * or general,
                    * but for the first time, the user gets out of campus,
                    * the phone is set to general mode!
                    * */
                    if(gotOutOfCampusForFirstTime && audioManager.getRingerMode() != AudioManager.RINGER_MODE_NORMAL){
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        gotOutOfCampusForFirstTime = false;
                    }
                    // else the user is free to set anything
                }
            }
        };
    }

    // locationInsideZone is defined here
    private boolean isInCampus(Double x,Double y){

        /*
        * Make an circle, with center (X,Y) i.e (latitude, longitude) and with a radius of 500 m
        * now, if the user is present inside the circle, then satisfying (x,y) on the equation
        * of the circle will give a negative value
        * based on this, it is judged, if the user is in the geo-fence or not
        * */

        // the lat and long of : Webel-It Park
        final Double X = 22.9611869;
        final Double Y = 88.4333625;

        // lat and long of a nearby place at dist of 500m (approx)
        final Double X_01 = 22.9609241;
        final Double Y_01 = 88.4330608;

        // finding the radius by finding the distance between the two points
        Double radius = Math.sqrt( Math.pow(X - X_01,2) + Math.pow(Y - Y_01,2));

        // making the circle equation and satisfying the (x,y) point
        Double function = Math.pow(x - X,2) + Math.pow(y - Y,2) - Math.pow(radius,2);

        if(function <= 0){
            // the location is in campus
            return true;
        }
        else{
            // outside campus
            return false;
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        // to keep the activity alive in foreground, show an notification, stating the app status
        Intent notificationIntent = new Intent(this,MainActivity.class);
        Intent[] listOfIntents = new Intent[1];
        listOfIntents[0] = notificationIntent;
        PendingIntent pendingIntent = PendingIntent.getActivities(
                this,0,listOfIntents,0);
        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle("Background Service Active")
                .setContentText("Tap to return")
                .setSmallIcon(R.drawable.android_icon)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1,notification);

        // finally connect the googleApiClient
        if(this.mGoogleApiClient != null){
            mGoogleApiClient.connect();
        }

        return START_STICKY; // this will ensure the auto-start of the service
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
