package com.kkb.imugraph;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    GraphView GV1;
    GraphView GV2;

    Handler tmHandler;
    Runnable tmRunnable;

    SensorManager sManager;
    Sensor accSensor;
    Sensor gyroSensor;

    TextView tvAccX;
    TextView tvAccY;
    TextView tvAccZ;

    TextView tvGyroX;
    TextView tvGyroY;
    TextView tvGyroZ;


    int num = 0;

    float accX;
    float accY;
    float accZ;

    float accXDisp;
    float accYDisp;
    float accZDisp;

    float gyroX;
    float gyroY;
    float gyroZ;

    float gyroXDisp;
    float gyroYDisp;
    float gyroZDisp;

    float ratioY;

    //********************************************************************************************//
    //                                                                                            //
    //                                   Overriden Methods                                        //
    //                                                                                            //
    //********************************************************************************************//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tmRunnable = new Runnable() {
            @Override
            public void run() {
                int h = GV1.getHeight();
                ratioY = h / 25;

                accXDisp = (h / 2) - ratioY * accX;
                accYDisp = (h / 2) - ratioY * accY;
                accZDisp = (h / 2) - ratioY * accZ;

                gyroXDisp = (h / 2) - ratioY * gyroX;
                gyroYDisp = (h / 2) - ratioY * gyroY;
                gyroZDisp = (h / 2) - ratioY * gyroZ;

                GV1.changeValue(accXDisp, accYDisp, accZDisp);
                GV2.changeValue(gyroXDisp, gyroYDisp, gyroZDisp);

                tmHandler.postDelayed(tmRunnable, 1);
            }
        };
        tmHandler = new Handler();

        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accSensor = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroSensor = sManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        InitializeComponent();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    protected void onPause() {
        SensorAndGraphStop();

        super.onPause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        SensorAndGraphStop();

//		tm.cancel();
        tmHandler.removeCallbacks(tmRunnable);

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//		 Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);

//        SubMenu group1 = menu.addSubMenu("Sensor");
//        group1.add(0, 1, 0, "Start");
//        group1.add(0, 2, 0, "Stop");

//        SubMenu group2 = menu.addSubMenu("Graph");
//        group2.add(0, 3, 0, "Start");
//        group2.add(0, 4, 0, "Stop");

        menu.add(0, 1, 0, "Start");
        menu.add(0, 2, 0, "Stop");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == 1)			// Sensor Start
        {
            SensorAndGraphStart();
        }
        else if(id == 2)	// Sensor Stop
        {
            SensorAndGraphStop();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {
            float var0 = event.values[0];
            float var1 = event.values[1];
            float var2 = event.values[2];

            if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            {
                accX = var0;
                accY = var1;
                accZ = var2;

                tvAccX.setText("X: " + String.format("%.3f", accX));
                tvAccY.setText("Y: " + String.format("%.3f", accY));
                tvAccZ.setText("Z: " + String.format("%.3f", accZ));
            }
            else if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE)
            {
                gyroX = var0;
                gyroY = var1;
                gyroZ = var2;

                tvGyroX.setText("X: " + String.format("%.3f", gyroX));
                tvGyroY.setText("Y: " + String.format("%.3f", gyroY));
                tvGyroZ.setText("Z: " + String.format("%.3f", gyroZ));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }

    //********************************************************************************************//
    //                                                                                            //
    //                               User Defined Sub-routines                                    //
    //                                                                                            //
    //********************************************************************************************//
    public void InitializeComponent()
    {
        GV1 = findViewById(R.id.graphView1);
        GV2 = findViewById(R.id.graphView2);

        tvAccX = findViewById(R.id.tvAX);
        tvAccY = findViewById(R.id.tvAY);
        tvAccZ = findViewById(R.id.tvAZ);

        tvGyroX = findViewById(R.id.tvGX);
        tvGyroY = findViewById(R.id.tvGY);
        tvGyroZ = findViewById(R.id.tvGZ);
    }

    private void SensorAndGraphStart() {
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        sManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_GAME);
        sManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_GAME);

        GV1.startGraph();
        GV2.startGraph();

        tmHandler.postDelayed(tmRunnable, 10);
    }

    private void SensorAndGraphStop() {
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        tmHandler.removeCallbacks(tmRunnable);
        GV1.resetGraph();
        GV2.resetGraph();

        sManager.unregisterListener(this);

        tvAccX.setText("X: Off");
        tvAccY.setText("Y: Off");
        tvAccZ.setText("Z: Off");

        tvGyroX.setText("X: Off");
        tvGyroY.setText("Y: Off");
        tvGyroZ.setText("Z: Off");
    }
}
