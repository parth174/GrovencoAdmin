package com.grovenco.grovencoadmin;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Random;

public class NotificationService extends Service {

    int flag = 0 ;
    MediaPlayer mp ;

    @Override
    public void onCreate() {
        super.onCreate();

        Toast.makeText(NotificationService.this, "Service created", Toast.LENGTH_SHORT).show();

        Intent notificationIntent = new Intent(this, HomeActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Grovenco Channel";
            String description = "Notificaation Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("0", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Do something. For example, fetch fresh data from backend to create a rich notification?

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(NotificationService.this, "0");
        builder.setContentTitle("Running in background")
                .setAutoCancel(false)
                .setContentIntent(pendingIntent)
                .setColor(getResources().getColor(R.color.colorAccent))
                .setContentText("GrovencoAdmin is running in background to notify about new orders")
                .setSmallIcon(R.mipmap.grovenco_logo_small)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        startForeground(11, builder.build());

        FirebaseUtil.orders_ref.whereEqualTo("order_status", "placed").whereEqualTo("seen_admin",false).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if(queryDocumentSnapshots != null){

                    mp= MediaPlayer.create(NotificationService.this, R.raw.alarm );

                    for(DocumentChange document : queryDocumentSnapshots.getDocumentChanges()){

                        if (document.getType() == DocumentChange.Type.ADDED) {

                            if(!mp.isPlaying()){
                                mp.start();

                                Intent notificationIntent = new Intent(NotificationService.this, OrdersActivity.class);
                                PendingIntent pendingIntent =
                                        PendingIntent.getActivity(NotificationService.this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

                                final NotificationCompat.Builder builder = new NotificationCompat.Builder(NotificationService.this, "0");
                                builder.setContentTitle("New Unseen Orders")
                                        .setAutoCancel(true)
                                        .setContentIntent(pendingIntent)
                                        .setColor(getResources().getColor(R.color.colorAccent))
                                        .setContentText("You have new unseen orders")
                                        .setSmallIcon(R.mipmap.grovenco_logo_small)
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                                NotificationManager notificationManager = null;

                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

                                    notificationManager = getSystemService(NotificationManager.class);

                                }

                                if(notificationManager != null){

                                    notificationManager.notify(201, builder.build());

                                }


                            }

                        }

                    }

                }

            }

        }) ;

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, final int flags, int startId) {
        return START_STICKY;
    }


}
