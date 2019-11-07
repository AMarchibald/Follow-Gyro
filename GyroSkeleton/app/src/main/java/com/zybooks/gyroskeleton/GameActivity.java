package com.zybooks.gyroskeleton;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Random;
import java.util.Timer;

public class GameActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;
    private SensorEventListener gyroscopeEventListener;
    int CommandNum;
    int GyroNum;
    int score;
    int timer = 7000;

    boolean scoreCheck = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        score = 0;

        final Random rand = new Random();
        CommandNum = rand.nextInt(5-1) +1;
        final TextView timer=findViewById(R.id.Timer);
        final TextView command = findViewById(R.id.Placeholder);
        final TextView Score = findViewById(R.id.Score);
        Score.setText("Score: " + score);

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        if(gyroscopeSensor == null){
            Toast.makeText(this, "This device does not have a Gyroscope.", Toast.LENGTH_SHORT).show();
            finish();
        }

        final CountDownTimer TurnTimer = new CountDownTimer(10000, 1000) {


            public void onFinish() {
                cancel();
                start();

                if(scoreCheck != true){
                    startActivity(new Intent(GameActivity.this, GameLossActivity.class));
                }

                CommandNum = rand.nextInt(5-1) +1;
                if(CommandNum == 1){
                    command.setText("Tilt Left");
                }else if(CommandNum == 2){
                    command.setText("Tilt Right");
                }else if(CommandNum == 3){
                    command.setText("Tilt Forward");
                }else if(CommandNum == 4){
                    command.setText("Tilt Back");
                }

                scoreCheck = false;

            }

            public void onTick(long millisUntilFinished) {
                timer.setText("seconds remaining: " + millisUntilFinished/1000);

                if(GyroNum == CommandNum){
                    score = score + 1;
                    Score.setText("Score: " + (score));
                    scoreCheck = true;
                }

            }

        }.start();


        gyroscopeEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(event.values[0] > 0.5f){
                    getWindow().getDecorView().setBackgroundColor(Color.BLUE);
                    GyroNum = 3;
                }else if(event.values[0] < -0.5f){
                    getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
                    GyroNum = 4;
                }else if(event.values[2] > 0.5f){
                    getWindow().getDecorView().setBackgroundColor(Color.GREEN);
                    GyroNum = 1;
                }else if(event.values[2] < -0.5f){
                    getWindow().getDecorView().setBackgroundColor(Color.RED);
                    GyroNum = 2;
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };


        if(CommandNum == 1){
            command.setText("Tilt Left");
        }else if(CommandNum == 2){
            command.setText("Tilt Right");
        }else if(CommandNum == 3){
            command.setText("Tilt Forward");
        }else if(CommandNum == 4){
            command.setText("Tilt Back");
        }

        Button button = findViewById(R.id.Lose);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(GameActivity.this, GameLossActivity.class));
            }
        });
    }

    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(gyroscopeEventListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(gyroscopeEventListener);
    }

}
