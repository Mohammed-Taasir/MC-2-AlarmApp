package com.example.alarmapplication;

import static android.content.Intent.getIntent;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyService extends Service {

    private static final int NOTIFICATION_ID = 1;

    private static final int INTERVAL_SECONDS = 10;
    private static final String TAG = "MyService";

    private Handler handler;
    private Runnable runnable;
    private MediaPlayer mediaPlayer;
    private boolean isRinging;
    private int s1, s2, destroy;



    public MyService() {
    }

    @Override
    public void onCreate(){
        super.onCreate();

        // Create a notification to display while the service is running
        final String CHANNEL_ID = "Foreground Service";
        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ID,CHANNEL_ID, NotificationManager.IMPORTANCE_LOW);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getSystemService(NotificationManager.class).createNotificationChannel(notificationChannel);
        }
        Notification.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new Notification.Builder(this, CHANNEL_ID).
                    setContentText("Using Foreground Service for long running ...")
                    .setContentTitle("Alarm Application running")
                    .setSmallIcon(R.drawable.ic_baseline_alarm_24);
        }
        startForeground(101, builder.build());

        handler = new Handler();
        s1 = 0;
        s2 = 0;
        destroy = 0;
//        mediaPlayer = MediaPlayer.create(this, R.raw.my_music);
//        isRinging = false;
    }

    MyBroadcastReceiver dynamicBroadcastReceiver = new MyBroadcastReceiver();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        intentFilter.addAction(Intent.ACTION_BATTERY_OKAY);
        intentFilter.addAction(Intent.ACTION_BATTERY_LOW);
        intentFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        intentFilter.addAction(TelephonyManager.EXTRA_STATE_RINGING);
        registerReceiver(dynamicBroadcastReceiver, intentFilter);

        runnable = new Runnable() {
            @Override
            public void run() {                                      // this code runs every 10 seconds to checkCurrentTime.
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.my_music);
                isRinging = false;
                checkCurrentTime(intent);
                handler.postDelayed(this, INTERVAL_SECONDS * 1000);
            }
        };
//        checkCurrentTime(intent);
        Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Service started");
//        mediaPlayer.start();
        handler.post(runnable);
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        handler.removeCallbacks(runnable);
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.release();
        destroy = 1;
        isRinging = false;
        Toast.makeText(this, "Service stopped", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Service stopped");
        unregisterReceiver(dynamicBroadcastReceiver);
    }


    private void checkCurrentTime(Intent intent) {
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        int second = now.get(Calendar.SECOND);
        int inputHour1 = intent.getIntExtra("inputHour1", -1);
        int inputMinute1 = intent.getIntExtra("inputMinute1", -1);
        int inputHour2 = intent.getIntExtra("inputHour2", -1);
        int inputMinute2 = intent.getIntExtra("inputMinute2", -1);
//        Log.d(TAG, "input hour1 "+inputHour1);
//        Log.d(TAG, "input minute1 "+inputMinute1);
//        Log.d(TAG, "hour "+hour);
//        Log.d(TAG, "minute "+minute);
//        Log.d(TAG, "input hour2 "+inputHour2);
//        Log.d(TAG, "input minute2 "+inputMinute2);
//        Log.d(TAG, "Time matched, ringing started");
        if ((hour == inputHour1 && minute == inputMinute1 && !isRinging && s1 != 10) || (hour == inputHour2 && minute == inputMinute2 && !isRinging && s2 != 10)) {
            if(hour == inputHour1 && minute == inputMinute1){       // only once in a 60 second duration
                s1 = 10;
                Toast.makeText(this, "Alarm 1 is ringing", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Alarm 1 is ringing");
            }
            if(hour == inputHour2 && minute == inputMinute2){       // only once in a 60 second duration
                s2 = 10;
                Toast.makeText(this, "Alarm 2 is ringing", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Alarm 2 is ringing");
            }

//            Log.d(TAG, "it comes here");
            mediaPlayer.start();
            isRinging = true;

//            Toast.makeText(this, "Time matched, ringing started", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Time matched, ringing started");
            handler.postDelayed(new Runnable() {            // this code is running music for 10 seconds
                @Override
                public void run() {
//                    if (mediaPlayer.isPlaying()) {
//                        mediaPlayer.stop();
//                    }
//                    isRinging = false;
                    if(destroy != 1){                   // handling situations with flag so destroyed object is not accessed
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                        }
                        isRinging = false;
                    }
                }
            }, 10000);
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
}