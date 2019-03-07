package com.example.mysensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);

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
            tv.append(getString(R.string.template,
                    sensor.getName(),
                    sensor.getVendor(),
                    sensor.getVersion(),
                    Boolean.toString(isDynamic)));
        }
        sensor = manager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(sensor == null){
            Toast.makeText(this, "Kein Lichtsensor vorhanden", Toast.LENGTH_SHORT).show();
        }else {
            listener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    if(event.values.length>0){
                        for(float f : event.values)
                        Log.d(TAG, "onSensorChanged: " + f);
                    }

                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }
            };
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
