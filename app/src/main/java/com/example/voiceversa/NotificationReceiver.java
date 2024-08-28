package com.example.voiceversa;

import static com.example.voiceversa.Right.NOTIFICATION_ID;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 123;
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if ("ACTION_SHARE".equals(action)) {
            // Handle Share button click
            Toast.makeText(context, "Pause button clicked", Toast.LENGTH_SHORT).show();
        } else if ("ACTION_SEND".equals(action)) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(NOTIFICATION_ID);
            Toast.makeText(context, "End button clicked", Toast.LENGTH_SHORT).show();
        }
    }
}