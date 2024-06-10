package com.example.oi_projekt.activities;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.oi_projekt.R;
import com.example.oi_projekt.adapter.ItemAdapter;
import com.example.oi_projekt.animation.ColorsBallView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ColorsGameActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private TextView textView;
    private ImageView ballImageView;
    private View topLeft;
    private View topRight;
    private View bottomLeft;
    private View bottomRight;
    private View middle;

    private TextView game_finished_text;

    private float[] gravity;
    private float[] geomagnetic;
    private float posX = 100;
    private float posY = 100;
    private float maxX;
    private float maxY;
    private Button button_start;
    private TextView timer_text;
    private TextView rounds_text;
    private TextView color_text;
    private String choosingColor;
    Map<String, String> colorSectionsMap  = new HashMap<String, String>();
    private Integer rounds = 0;
    Map<String, String> colorsHexMap  = new HashMap<String, String>() {{
        put("Red", "#cc3300");
        put("Green", "#009933");
        put("Yellow", "#ffff66");
        put("Orange", "#ff9900");
    }};
private boolean game_started_flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colors_game);

        int width= Resources.getSystem().getDisplayMetrics().widthPixels;
        int height=Resources.getSystem().getDisplayMetrics().heightPixels;
        color_text= findViewById(R.id.color_text);
        textView = findViewById(R.id.textView3);
        ballImageView = findViewById(R.id.ballImageView);
        button_start = findViewById(R.id.start_button);
        timer_text = findViewById(R.id.timer_text);
        rounds_text = findViewById(R.id.rounds_text);

        bottomLeft = findViewById(R.id.bottomLeft);
        bottomRight = findViewById(R.id.bottomRight);
        topLeft = findViewById(R.id.topLeft);
        topRight = findViewById(R.id.topRight);
        middle = findViewById(R.id.middle);

        bottomLeft.setBackgroundResource(R.color.purple);
        bottomRight.setBackgroundResource(R.color.purple);
        topLeft.setBackgroundResource(R.color.purple);
        topRight.setBackgroundResource(R.color.purple);

        game_finished_text = findViewById(R.id.game_finished_text);
        game_finished_text.setVisibility(View.GONE);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        }

        // Check if the sensors are available
        if (accelerometer == null) {
            Toast.makeText(this, "Accelerometer not available", Toast.LENGTH_SHORT).show();
        }
        if (magnetometer == null) {
            Toast.makeText(this, "Magnetometer not available", Toast.LENGTH_SHORT).show();
        }

        button_start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                game_started_flag= true;
                randomizeColors();
                new CountDownTimer(30000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        NumberFormat f = new DecimalFormat("00");
                        long sec = (millisUntilFinished / 1000) % 20;
                        timer_text.setText(f.format(sec) + "s");
                    }
                    public void onFinish() {
                        timer_text.setText("0s");
                        Done();
                    }
                }.start();            }
        });
    }

    private void randomizeColors() {
        if(rounds == 7){
            Done();
        }
        int randomIndex;
        String randomValue;
        List<String> keyList = new ArrayList<String>(colorsHexMap.keySet());
        List<String> tmpKeySet = new ArrayList<>();

        randomIndex = new Random().nextInt(keyList.size());
        randomValue = keyList.get(randomIndex);
        tmpKeySet.add(randomValue);
        keyList.remove(randomIndex);
        colorSectionsMap.put("bottomLeft", randomValue);
        bottomLeft.setBackgroundColor(Color.parseColor(colorsHexMap.get(randomValue)));

        randomIndex = new Random().nextInt(keyList.size());
        randomValue = keyList.get(randomIndex);
        tmpKeySet.add(randomValue);
        keyList.remove(randomValue);
        colorSectionsMap.put("bottomRight", randomValue);
        bottomRight.setBackgroundColor(Color.parseColor(colorsHexMap.get(randomValue)));

        randomIndex = new Random().nextInt(keyList.size());
        randomValue = keyList.get(randomIndex);
        tmpKeySet.add(randomValue);
        keyList.remove(randomValue);
        colorSectionsMap.put("topLeft", randomValue);
        topLeft.setBackgroundColor(Color.parseColor(colorsHexMap.get(randomValue)));

        randomIndex = new Random().nextInt(keyList.size());
        randomValue = keyList.get(randomIndex);
        tmpKeySet.add(randomValue);
        keyList.remove(randomValue);
        colorSectionsMap.put("topRight", randomValue);
        topRight.setBackgroundColor(Color.parseColor(colorsHexMap.get(randomValue)));

        randomIndex = new Random().nextInt(tmpKeySet.size());
        choosingColor = tmpKeySet.get(randomIndex);
        color_text.setText(choosingColor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null && accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
        if (sensorManager != null && magnetometer != null) {
            sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
        }

        // Get the screen dimensions
        ballImageView.post(() -> {
            maxX = ((ConstraintLayout) ballImageView.getParent()).getWidth() - ballImageView.getWidth();
            maxY = ((ConstraintLayout) ballImageView.getParent()).getHeight() - ballImageView.getHeight();
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this, accelerometer);
            sensorManager.unregisterListener(this, magnetometer);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        posX = ballImageView.getX();
        posY = ballImageView.getY();
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            gravity = event.values;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            geomagnetic = event.values;
        }
        if (gravity != null && geomagnetic != null) {
            float[] R = new float[9];
            float[] I = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, gravity, geomagnetic);
            if (success) {
                float[] orientation = new float[3];
                SensorManager.getOrientation(R, orientation);
                float pitch = orientation[1];
                float roll = orientation[2];

                if(Math.abs(Math.toDegrees(pitch))>5 && game_started_flag){
                    float deltaY = -pitch * 15;

                    posY += deltaY;
                    if (posY < 0) {
                        posY = 0;
                    } else if (posY > maxY) {
                        posY = maxY;
                    }
                    ballImageView.setY(posY);
                }

                if(Math.abs(Math.toDegrees(roll))>5 && game_started_flag){
                    float deltaX = roll * 15;
                    posX += deltaX;
                    if (posX < 0) {
                        posX = 0;
                    } else if (posX > maxX) {
                        posX = maxX;
                    }
                    ballImageView.setX(posX);
                }

                // Check for overlap
                if (isViewOverlapping(ballImageView, middle) && game_started_flag) {
                    // Do something when the ImageView overlaps with the LinearLayout
                    textView.setText("mid");
                }
                else {
                    if (isViewOverlapping(ballImageView, bottomLeft) && game_started_flag) {
                        if(choosingColor.equalsIgnoreCase(colorSectionsMap.get("bottomLeft"))){
                            textView.setText("win!");
                            rounds++;
                            randomizeColors();
                        }
                        else
                            textView.setText("bottomLeft");
                    }
                    if (isViewOverlapping(ballImageView, bottomRight) && game_started_flag) {
                        if(choosingColor.equalsIgnoreCase(colorSectionsMap.get("bottomRight"))){
                            textView.setText("win!");
                            rounds++;
                            randomizeColors();
                        }
                        else
                            textView.setText("bottomRight");
                    }
                    if (isViewOverlapping(ballImageView, topLeft) && game_started_flag) {
                        if(choosingColor.equalsIgnoreCase(colorSectionsMap.get("topLeft"))){
                            rounds++;
                            textView.setText("win!");
                            randomizeColors();
                        }
                        else
                            textView.setText("topLeft");
                    }
                    if (isViewOverlapping(ballImageView, topRight) && game_started_flag) {
                        if(choosingColor.equalsIgnoreCase(colorSectionsMap.get("topRight"))){
                            textView.setText("win!");
                            rounds++;
                            randomizeColors();
                        }
                        else
                            textView.setText("topRight");
                    }
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    private boolean isViewOverlapping(View view1, View view2) {
        Rect rect1 = new Rect();
        Rect rect2 = new Rect();

        view1.getHitRect(rect1);
        view2.getHitRect(rect2);

        return Rect.intersects(rect1, rect2);
    }
    private void RemoveItems(){
        bottomLeft.setBackgroundResource(R.color.purple);
        bottomRight.setBackgroundResource(R.color.purple);
        topLeft.setBackgroundResource(R.color.purple);
        topRight.setBackgroundResource(R.color.purple);
    }

    private void Done(){
        // lokalno čuvanje u obliku ključ:string(lista)
        SharedPreferences sharedPreferences =  getSharedPreferences("ColorsGameStore", MODE_PRIVATE);
        String scores = sharedPreferences.getString(LoginSignupPageActivity.current_email, "");
        // Svaka igra je sačuvana kao string liste na koji konkatiram sljedeći rezultat
        scores = scores == "" ? rounds.toString() : scores + "," + rounds.toString();
        getSharedPreferences("ColorsGameStore", MODE_PRIVATE)
                .edit()
                .putString(LoginSignupPageActivity.current_email, scores)
                .apply();
        RemoveItems();
        if(rounds==7){
            game_finished_text.setText("7/7 Congrats!");
        }else if(rounds<7 && rounds>3){
            game_finished_text.setText(rounds+"/7 Well Done!");
        }else{
            game_finished_text.setText(rounds+"/7 You Can Do Better!");
        }
        button_start.setVisibility(View.GONE);
        timer_text.setVisibility(View.GONE);
        rounds_text.setVisibility(View.GONE);
        game_finished_text.setVisibility(View.VISIBLE) ;
    }
}