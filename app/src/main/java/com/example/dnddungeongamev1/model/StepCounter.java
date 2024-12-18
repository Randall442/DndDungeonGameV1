package com.example.dnddungeongamev1.model;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;


public class StepCounter implements SensorEventListener
{
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    private StepListener listener;

    private int stepsSinceStart;

    private int initialStepCount = -1;


    public interface StepListener
    {
        void onStepCountChanged(int stepCount);
        void onNoStepCounter();
    }
    public StepCounter(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sensorManager != null ? sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) : null;
    }

    public void setStepListener(StepListener listener) {
        this.listener = listener;
    }

    public void start() {
        if (sensorManager != null && stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_UI);
        } else if (listener != null) {
            listener.onNoStepCounter();
        }
    }

    public void stop() {
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
//            Log.d("StepCounter", "Sensor changed: " + event.values[0]);
//            if (listener != null) {
//                listener.onStepCountChanged((int) event.values[0]);
//            }
//        }
//    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER && listener != null) {
            int totalSteps = (int) event.values[0];

            if (initialStepCount == -1) {
                initialStepCount = totalSteps;
            }

            int stepsSinceStart = totalSteps - initialStepCount;

            listener.onStepCountChanged(stepsSinceStart);
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public int getStepsSinceStart()
    {
        return stepsSinceStart;
    }
}
