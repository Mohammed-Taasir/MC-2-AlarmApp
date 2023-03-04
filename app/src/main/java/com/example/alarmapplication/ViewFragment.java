package com.example.alarmapplication;

import static androidx.core.content.ContextCompat.startForegroundService;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Locale;

public class ViewFragment extends Fragment {

    Button timeButton, timeButton2, startButton, stopButton;
    int hour1, minute1, hour2, minute2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setRetainInstance(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view, container, false);
        timeButton = view.findViewById(R.id.timeButton);
        timeButton2 = view.findViewById(R.id.button2);
        startButton = view.findViewById(R.id.button3);
        stopButton = view.findViewById(R.id.button4);

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popTimepicker(view);
            }
        });

//        if(savedInstanceState != null){
//            hour1 = savedInstanceState.getInt("hour1");
//            minute1 = savedInstanceState.getInt("minute1");
//            timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hour1, minute1));
//        }

        timeButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popTimepicker2(view);
            }
        });

//        if(savedInstanceState != null){
//            hour2 = savedInstanceState.getInt("hour2");
//            minute2 = savedInstanceState.getInt("minute2");
//            timeButton2.setText(String.format(Locale.getDefault(), "%02d:%02d", hour2, minute2));
//        }

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent serviceIntent = new Intent(getContext(), MyService.class);
                serviceIntent.putExtra("inputHour1", hour1);
                serviceIntent.putExtra("inputMinute1", minute1);
                serviceIntent.putExtra("inputHour2", hour2);
                serviceIntent.putExtra("inputMinute2", minute2);
                startForegroundService(getContext(), serviceIntent);
//                ContextCompat.startForegroundService(getContext(), serviceIntent);
//                getContext().startService(serviceIntent);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent serviceIntent = new Intent(getContext(), MyService.class);
                getContext().stopService(serviceIntent);
            }
        });

        return view;
    }

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putInt("hour1", hour1);
//        outState.putInt("minute1", minute1);
//        outState.putInt("hour2", hour2);
//        outState.putInt("minute2", minute2);
//    }

    public void popTimepicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if(!(selectedHour >=0 && selectedHour <= 23)){
                    selectedHour = -1;
                }
                if(!(selectedMinute >=0 && selectedMinute <= 59)){
                    selectedMinute = -1;
                }
                hour1 = selectedHour;
                minute1 = selectedMinute;
                timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hour1, minute1));
            }
        };

        int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), style, onTimeSetListener, hour1, minute1, false);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    public void popTimepicker2(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if(!(selectedHour >=0 && selectedHour <= 23)){
                    selectedHour = -1;
                }
                if(!(selectedMinute >=0 && selectedMinute <= 59)){
                    selectedMinute = -1;
                }
                hour2 = selectedHour;
                minute2 = selectedMinute;
                timeButton2.setText(String.format(Locale.getDefault(), "%02d:%02d", hour2, minute2));
            }
        };

        int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), style, onTimeSetListener, hour2, minute2, false);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }
}