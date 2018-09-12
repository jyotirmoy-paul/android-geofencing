package com.example.mr_paul.helperapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {

    public static final String CHANNEL_ID = "backgroundServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();

        // as soon as the activity is created, a notification channel is created!
        createNotificationChannel();
    }

    private void createNotificationChannel(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID,
                    "Background Service Channel",
                    NotificationManager.IMPORTANCE_MIN);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);

        }
    }

}
