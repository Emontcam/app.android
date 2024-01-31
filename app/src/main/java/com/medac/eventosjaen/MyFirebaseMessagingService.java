package com.medac.eventosjaen;


import android.app.NotificationManager;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.medac.eventosjaen.MyApp;
import com.medac.eventosjaen.R;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        showNotification(message);

    }

    private void showNotification(RemoteMessage message) {

        if (message.getNotification() != null) {
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, MyApp.NOTIFICATION_CHANNEL_ID)
                    .setContentTitle(message.getNotification().getTitle())
                    .setContentText(message.getNotification().getBody())
                    .setSmallIcon(R.drawable.logo)
                    .setAutoCancel(true);
            notificationManager.notify(1, notificationBuilder.build());
        }

    }
}