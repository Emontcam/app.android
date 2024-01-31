package com.medac.eventosjaen;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.checkerframework.checker.nullness.qual.NonNull;

public class MyApp extends Application {

    public static final String NOTIFICATION_CHANNEL_ID = "notification_fcm";

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {

                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            System.out.println("El token no fue generado");
                            return;
                        }
                        String token = task.getResult();
                        System.out.println("El token es> " + token);
                    }
                });
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "Notificaciones de FCM",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Estas notificaciones van a ser recibidas desde FCM");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
