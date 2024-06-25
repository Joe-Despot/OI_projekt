package com.example.oi_projekt.activities;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.oi_projekt.animation.MyBounce;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import com.example.oi_projekt.R;

public class ColorsGameActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private ImageView ballImageView;
    private View topLeft;
    private View topRight;
    private View bottomLeft;
    private View bottomRight;
    private View middle;
    private TextView game_finished_text;
    private TextView color_text;
    private ImageButton back_button;
    private float[] gravity;
    private float[] geomagnetic;
    private float posX = 100;
    private float posY = 100;
    private float maxX;
    private float maxY;
    private Button button_start;
    private TextView timer_text;
    private TextView rounds_text;
    private String choosingColor;
    private LinearLayout one_lolipop;
    Map<String, String> colorSectionsMap  = new HashMap<String, String>();
    private MediaPlayer win_sound;
    private MediaPlayer try_again_sound;
    private MediaPlayer wrong_sound;
    private MediaPlayer good_sound;
    LinearLayout parentView;
    CountDownTimer timer_back;
    CountDownTimer timer_start;
    private String you_can_better, well_done, congrats;

    private Integer rounds = 0;
    Map<String, String> colorsHexMapEn  = new HashMap<String, String>() {{
        put("Red", "#cc3300");
        put("Green", "#009933");
        put("Yellow", "#ffff66");
        put("Orange", "#ff9900");
        put("Pink", "#FFC0CB");
        put("Purple", "#800080");
        put("Brown", "#A52A2A");
        put("Blue", "#0000FF");
    }};
    Map<String, String> colorsHexMapHr  = new HashMap<String, String>() {{
        put("Crvena", "#cc3300");
        put("Zelena", "#009933");
        put("Žuta", "#ffff66");
        put("Narančasta", "#ff9900");
        put("Roza", "#FFC0CB");
        put("Ljubičasta", "#800080");
        put("Smeđa", "#A52A2A");
        put("Plava", "#0000FF");
    }};
    String lang;
    String color_string;

    private boolean game_started_flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colors_game);

        SharedPreferences sharedPreferences_audio =  getSharedPreferences("LanguageSettingsStore", MODE_PRIVATE);
        lang = sharedPreferences_audio.getString(LoginSignupPageActivity.current_email, "en");
        switch (lang) {
            case "en":
                color_string = "COLOR";
                you_can_better = "You can do better!";
                congrats = "Congrats!";
                well_done = "Well done!";
                break;
            case "hr":
                color_string = "BOJA";
                you_can_better = "Možeš bolje!";
                congrats = "Odlično!";
                well_done = "Vrlo dobro!";
                break;
            default:
                color_string = "COLOR";
                you_can_better = "You can do better!";
                congrats = "Congrats!";
                well_done = "Well done!";
                break;
        }
        win_sound = MediaPlayer.create(this,R.raw.win_sound);
        wrong_sound = MediaPlayer.create(this,R.raw.wrong);
        try_again_sound = MediaPlayer.create(this,R.raw.try_again);
        good_sound = MediaPlayer.create(this,R.raw.good_sound);
        
        color_text= findViewById(R.id.color_text);
        ballImageView = findViewById(R.id.ballImageView);

        button_start = findViewById(R.id.start_button);
        timer_text = findViewById(R.id.timer_text);
        rounds_text = findViewById(R.id.rounds_text);
        one_lolipop = findViewById(R.id.one_lolipop);
        one_lolipop.setVisibility(View.GONE);
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
        back_button = findViewById(R.id.back_button);

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

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation myAnim = AnimationUtils.loadAnimation(ColorsGameActivity.this, R.anim.bounce);
                MyBounce interpolator = new MyBounce(0.01, 20);
                myAnim.setInterpolator(interpolator);
                back_button.startAnimation(myAnim);
                RemoveItems();
                timer_back = new CountDownTimer(1000, 1000) {
                    public void onTick(long millisUntilFinished) {}

                    public void onFinish() {
                        if(timer_start!=null)
                            timer_start.cancel();
                        finish();
                    }

                }.start();
            }
        });

        button_start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                game_started_flag= true;
                randomizeColors();
                button_start.setEnabled(false);

                timer_start = new CountDownTimer(30000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        NumberFormat f = new DecimalFormat("00");
                        long sec = (millisUntilFinished / 1000) % 60;
                        timer_text.setText(f.format(sec) + "s");
                    }
                    public void onFinish() {
                        timer_text.setText("0s");
                        Done();
                    }
                }.start();
            }
        });
        color_text.setText(color_string);
    }

    private void randomizeColors() {
        int randomIndex;
        String randomValue;
        List<String> keyList;
        List<String> tmpKeySet;
        Map<String, String> colorsHexMap;

        switch (lang) {
            case "en":
                keyList = new ArrayList<String>(colorsHexMapEn.keySet());
                tmpKeySet= new ArrayList<>();
                colorsHexMap = colorsHexMapEn;
                break;
            case "hr":
                keyList = new ArrayList<String>(colorsHexMapHr.keySet());
                tmpKeySet = new ArrayList<>();
                colorsHexMap = colorsHexMapHr;
                break;
            default:
                keyList = new ArrayList<String>(colorsHexMapEn.keySet());
                tmpKeySet = new ArrayList<>();
                colorsHexMap = colorsHexMapEn;
                break;
        }

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

                if (isViewOverlapping(ballImageView, middle) && game_started_flag) {
                }
                else {
                    if (isViewOverlapping(ballImageView, bottomLeft) && game_started_flag) {
                        if(choosingColor.equalsIgnoreCase(colorSectionsMap.get("bottomLeft"))){
                            randomizeColors();
                            rounds_text.setText((++rounds).toString());
                            if(SettingsActivity.audioFlag)
                                good_sound.start();

                        }
                        else{
                            if(SettingsActivity.audioFlag)
                                wrong_sound.start();
                        }
                    }
                    if (isViewOverlapping(ballImageView, bottomRight) && game_started_flag) {
                        if(choosingColor.equalsIgnoreCase(colorSectionsMap.get("bottomRight"))){
                            randomizeColors();
                            rounds_text.setText((++rounds).toString());
                            if(SettingsActivity.audioFlag)
                                good_sound.start();}
                        else{
                            if(SettingsActivity.audioFlag)
                                wrong_sound.start();
                        }
                    }
                    if (isViewOverlapping(ballImageView, topLeft) && game_started_flag) {
                        if(choosingColor.equalsIgnoreCase(colorSectionsMap.get("topLeft"))){
                            randomizeColors();
                            rounds_text.setText((++rounds).toString());
                            if(SettingsActivity.audioFlag)
                                good_sound.start();
                        }
                        else{
                            if(SettingsActivity.audioFlag)
                                wrong_sound.start();
                        }
                    }
                    if (isViewOverlapping(ballImageView, topRight) && game_started_flag) {
                        if(choosingColor.equalsIgnoreCase(colorSectionsMap.get("topRight"))){
                            randomizeColors();
                            rounds_text.setText((++rounds).toString());
                            if(SettingsActivity.audioFlag)
                                good_sound.start();
                        }
                        else{
                            if(SettingsActivity.audioFlag)
                                wrong_sound.start();
                        }
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
        color_text.setVisibility(View.GONE);
        button_start.setVisibility(View.GONE);
        timer_text.setVisibility(View.GONE);
        rounds_text.setVisibility(View.GONE);
        ballImageView.setVisibility(View.GONE);
    }

    private void Done(){
        game_started_flag = false;
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
        game_finished_text.setVisibility(View.VISIBLE);

        if(rounds>=7){
            game_finished_text.setText(rounds + " " +  congrats);
            if(SettingsActivity.audioFlag)
                win_sound.start();
        }else if(rounds<7 && rounds>3){
            game_finished_text.setText(rounds + " " + well_done);
            if(SettingsActivity.audioFlag)
                win_sound.start();
        }else{
            game_finished_text.setText(rounds + " " + you_can_better);
            if(SettingsActivity.audioFlag)
                try_again_sound.start();
        }
    }

    private void goodJobBorder(){
        parentView.setBackground(ContextCompat.getDrawable(this, R.drawable.win_border));
    }
}