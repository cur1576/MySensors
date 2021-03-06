package com.example.mysensors;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView tv;
    private SensorManager manager;
    private Sensor sensor;
    private SensorEventListener listener;
    private SensorEventListener2 listener2;
    private ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
        scrollView = findViewById(R.id.scroll);

    }

    @Override
    protected void onResume() {
        super.onResume();
        tv.setText("");
        manager = (SensorManager)getSystemService(SENSOR_SERVICE);
        List<Sensor> sensors = manager.getSensorList(Sensor.TYPE_ALL);
        for(Sensor sensor : sensors){
            boolean isDynamic = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                isDynamic=sensor.isDynamicSensor();
            }
            int i = -1;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                i = sensor.getReportingMode();
            }
            tv.append(getString(R.string.template,
                    sensor.getName(),
                    sensor.getVendor(),
                    i,
                    Boolean.toString(isDynamic)));
        }
        sensor = manager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if(sensor == null){
            Toast.makeText(this, "Kein Lichtsensor vorhanden", Toast.LENGTH_SHORT).show();
        }else {
            listener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    if(event.values.length>0){
//                        int i = 1;
//                        for(float f : event.values) {
//                            Log.d(TAG, "onSensorChanged: " + i + ": " + f);
//                            i++;
//                        }
//                        Log.d(TAG, "onSensorChanged: " + event);
//                        float light = event.values[0];
//                        tv.append(Float.toString(light)+"\n");
//                        scrollView.fullScroll(View.FOCUS_DOWN);
                        if(event.values[0]<sensor.getMaximumRange()){
                            tv.setBackgroundColor(Color.RED);
                            Log.d(TAG, "onSensorChanged: " + sensor.getMaximumRange());
                        }else {
                            tv.setBackgroundColor(Color.WHITE);
                        }
                    }

                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }
            };
            manager.registerListener(listener,sensor,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.unregisterListener(listener);
    }
}
