package com.example.alarmapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {

    private int receivedBroadcasts = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String action = intent.getAction();

        if (action.equals(Intent.ACTION_BATTERY_LOW) ||
                action.equals(Intent.ACTION_POWER_CONNECTED) ||
                action.equals(Intent.ACTION_BATTERY_OKAY) ||
                action.equals(Intent.ACTION_POWER_DISCONNECTED) ||
                action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED) ||
                action.equals(Intent.ACTION_BATTERY_CHANGED)) {
            receivedBroadcasts++;

            if(action.equals(Intent.ACTION_POWER_CONNECTED)){
                Log.d("MyService", "PHONE CONNECTED TO POWER");
                Toast.makeText(context.getApplicationContext(), "PHONE CONNECTED TO POWER", Toast.LENGTH_LONG).show();
            }
            if(action.equals(Intent.ACTION_POWER_DISCONNECTED)){
                Log.d("MyService", "PHONE DISCONNECTED FROM POWER");
                Toast.makeText(context.getApplicationContext(), "PHONE DISCONNECTED FROM POWER", Toast.LENGTH_LONG).show();
            }
            if(action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)){
                Log.d("MyService", "incoming call to user");
                Toast.makeText(context.getApplicationContext(), "incoming call to user", Toast.LENGTH_LONG).show();
            }
            if(action.equals(Intent.ACTION_BATTERY_CHANGED)){
                Log.d("MyService", "battery quality check");
                Toast.makeText(context.getApplicationContext(), "battery quality check", Toast.LENGTH_LONG).show();
            }
            if(action.equals(Intent.ACTION_BATTERY_LOW)){
                Log.d("MyService", "battery low");
                Toast.makeText(context.getApplicationContext(), "BATTERY LOW", Toast.LENGTH_LONG).show();
            }
            if(action.equals(Intent.ACTION_BATTERY_OKAY)){
                Log.d("MyService", "battery okay");
                Toast.makeText(context.getApplicationContext(), "BATTERY OKAY", Toast.LENGTH_LONG).show();
            }

            if (receivedBroadcasts == 2) {
                // Stop the service and any ringing
                context.stopService(new Intent(context, MyService.class));
                // Stop any ongoing ringing, e.g. using AudioManager
            }
        }
    }
}