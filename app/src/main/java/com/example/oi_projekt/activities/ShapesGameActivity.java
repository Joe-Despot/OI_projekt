package com.example.oi_projekt.activities;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.oi_projekt.R;
import com.example.oi_projekt.animation.MyBounce;
import com.example.oi_projekt.listeners.DragTouchListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ShapesGameActivity extends AppCompatActivity {
    ImageView shape1;
    ImageView shape2;
    ImageView shape3;
    ImageView shape4;
    ImageView shape5;
    ImageView shape6;
    private ImageButton back_button;
    TextView dropOfText1;
    TextView dropOfText2;
    TextView dropOfText3;
    TextView dropOfText4;
    private Button button_start;
    private TextView timer_text;
    private TextView rounds_text;
    private boolean game_started_flag = false;
    public static Integer rounds = 0;
    private TextView game_finished_text;
    private MediaPlayer win_sound;
    private MediaPlayer try_again_sound;
    private MediaPlayer wrong_sound;
    private MediaPlayer good_sound;
    String lang;
    private LinearLayout one_lolipop;
    private Map<TextView, View> shapeAndTextMap = new HashMap<>();
    float[] shape1Pos;
    float[] shape2Pos;
    float[] shape3Pos;
    float[] shape4Pos;
    float[] shape5Pos;
    float[] shape6Pos;
    ConstraintLayout parentLayout;
    CountDownTimer timer_start;
    private String you_can_better, well_done, congrats;


    Map<String,Integer> shapeDrawableMap  = new HashMap<String, Integer>() {{
        put("Circle", R.drawable.shape_circle);
        put("Star", R.drawable.shape_star);
        put("Square", R.drawable.shape_square);
        put("Oval", R.drawable.shape_oval);
        put("Triangle", R.drawable.shape_triangle);
        put("Plus", R.drawable.shape_plus);
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shapes_game);

        SharedPreferences sharedPreferences_audio =  getSharedPreferences("LanguageSettingsStore", MODE_PRIVATE);
        lang = sharedPreferences_audio.getString(LoginSignupPageActivity.current_email, "en");
        switch (lang) {
            case "en":
                you_can_better = "You can do better!";
                congrats = "Congrats!";
                well_done = "Well done!";
                break;
            case "hr":
                you_can_better = "Možeš bolje!";
                congrats = "Odlično!";
                well_done = "Vrlo dobro!";
                break;
            default:
                you_can_better = "You can do better!";
                congrats = "Congrats!";
                well_done = "Well done!";
                break;
        }

        game_finished_text = findViewById(R.id.game_finished_text);
        game_finished_text.setVisibility(View.GONE);
        back_button = findViewById(R.id.back_button);

        win_sound = MediaPlayer.create(this,R.raw.win_sound);
        wrong_sound = MediaPlayer.create(this,R.raw.wrong);
        try_again_sound = MediaPlayer.create(this,R.raw.try_again);
        good_sound = MediaPlayer.create(this,R.raw.good_sound);

        dropOfText1  = findViewById(R.id.dropOfText1);
        dropOfText2  = findViewById(R.id.dropOfText2);
        dropOfText3  = findViewById(R.id.dropOfText3);
        dropOfText4 = findViewById(R.id.dropOfText4);

        shape1 = findViewById(R.id.draggableShape1);
        shape2 = findViewById(R.id.draggableShape2);
        shape3 = findViewById(R.id.draggableShape3);
        shape4 = findViewById(R.id.draggableShape4);
        shape5 = findViewById(R.id.draggableShape5);
        shape6 = findViewById(R.id.draggableShape6);

        button_start = findViewById(R.id.start_button);
        timer_text = findViewById(R.id.timer_text);
        rounds_text = findViewById(R.id.rounds_text);
        one_lolipop = findViewById(R.id.one_lolipop);
        one_lolipop.setVisibility(View.GONE);
        shape2=(ImageView)findViewById(R.id.draggableShape2);
        shape1=(ImageView)findViewById(R.id.draggableShape1);

        parentLayout = findViewById(R.id.parentLayout);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation myAnim = AnimationUtils.loadAnimation(ShapesGameActivity.this, R.anim.bounce);
                MyBounce interpolator = new MyBounce(0.01, 20);
                myAnim.setInterpolator(interpolator);
                back_button.startAnimation(myAnim);
                removeItems();
                button_start.setEnabled(false);

                new CountDownTimer(1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }
                    @Override
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
                button_start.setEnabled(false);
                game_started_flag= true;
                shape6Pos = new float[]{ shape6.getX(),shape6.getY()};
                shape5Pos = new float[]{ shape5.getX(),shape5.getY()};
                shape4Pos = new float[]{ shape4.getX(),shape4.getY()};
                shape3Pos = new float[]{ shape3.getX(),shape3.getY()};
                shape2Pos = new float[]{ shape2.getX(),shape2.getY()};
                shape1Pos = new float[]{ shape1.getX(),shape1.getY()};

                randomizeField();
                timer_start = new CountDownTimer(30000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        NumberFormat f = new DecimalFormat("00");
                        long sec = (millisUntilFinished / 1000) % 60;
                        timer_text.setText(f.format(sec) + "s");
                        rounds_text.setText(rounds.toString());

                    }
                    public void onFinish() {
                        timer_text.setText("0s");
                        Done();
                    }
                }.start();
            }
        });
    }

    public void randomizeField(){
        int randomIndex;
        String randomValue;
        List<String> shapeTextList = new ArrayList<>(Arrays.asList("Circle", "Square", "Star", "Triangle", "Oval", "Plus"));
        List<String> tmpShapes = new ArrayList<>();

        randomIndex = new Random().nextInt(shapeTextList.size());
        randomValue = shapeTextList.get(randomIndex);
        dropOfText1.setText(randomValue);
        shapeTextList.remove(randomValue);
        tmpShapes.add(randomValue);

        randomIndex = new Random().nextInt(shapeTextList.size());
        randomValue = shapeTextList.get(randomIndex);
        dropOfText2.setText(randomValue);
        shapeTextList.remove(randomValue);
        tmpShapes.add(randomValue);

        randomIndex = new Random().nextInt(shapeTextList.size());
        randomValue = shapeTextList.get(randomIndex);
        dropOfText3.setText(randomValue);
        shapeTextList.remove(randomValue);
        tmpShapes.add(randomValue);

        randomIndex = new Random().nextInt(shapeTextList.size());
        randomValue = shapeTextList.get(randomIndex);
        dropOfText4.setText(randomValue);
        shapeTextList.remove(randomValue);
        tmpShapes.add(randomValue);

        randomIndex = new Random().nextInt(tmpShapes.size());
        randomValue = tmpShapes.get(randomIndex);
        tmpShapes.remove(randomValue);
        shape1.setBackground(getResources().getDrawable(shapeDrawableMap.get(randomValue)));
        shape1.setX(shape1Pos[0]);
        shape1.setY(shape1Pos[1]);
        DragTouchListener dragListener1 = new DragTouchListener(parentLayout,shape1,randomValue,dropOfText1, dropOfText2, dropOfText3,dropOfText4, this);

        randomIndex = new Random().nextInt(tmpShapes.size());
        randomValue = tmpShapes.get(randomIndex);
        tmpShapes.remove(randomValue);
        shape2.setBackground(getResources().getDrawable(shapeDrawableMap.get(randomValue)));
        shape2.setX(shape2Pos[0]);
        shape2.setY(shape2Pos[1]);
        DragTouchListener dragListener2 = new DragTouchListener(parentLayout,shape2,randomValue,dropOfText1, dropOfText2, dropOfText3,dropOfText4, this);

        randomIndex = new Random().nextInt(tmpShapes.size());
        randomValue = tmpShapes.get(randomIndex);
        tmpShapes.remove(randomValue);
        shape3.setBackground(getResources().getDrawable(shapeDrawableMap.get(randomValue)));
        shape3.setX(shape3Pos[0]);
        shape3.setY(shape3Pos[1]);
        DragTouchListener dragListener3 = new DragTouchListener(parentLayout,shape3,randomValue,dropOfText1, dropOfText2, dropOfText3,dropOfText4,this);

        randomIndex = new Random().nextInt(tmpShapes.size());
        randomValue = tmpShapes.get(randomIndex);
        tmpShapes.remove(randomValue);
        shape4.setBackground(getResources().getDrawable(shapeDrawableMap.get(randomValue)));
        shape4.setX(shape4Pos[0]);
        shape4.setY(shape4Pos[1]);
        DragTouchListener dragListener4 = new DragTouchListener(parentLayout,shape4,randomValue,dropOfText1, dropOfText2, dropOfText3,dropOfText4,this);

        randomIndex = new Random().nextInt(shapeTextList.size());
        randomValue = shapeTextList.get(randomIndex);
        shapeTextList.remove(randomValue);
        shape5.setBackground(getResources().getDrawable(shapeDrawableMap.get(randomValue)));
        shape5.setX(shape5Pos[0]);
        shape5.setY(shape5Pos[1]);
        DragTouchListener dragListener5 = new DragTouchListener(parentLayout,shape5,randomValue,dropOfText1, dropOfText2, dropOfText3,dropOfText4,this);

        randomIndex = new Random().nextInt(shapeTextList.size());
        randomValue = shapeTextList.get(randomIndex);
        shapeTextList.remove(randomValue);
        shape6.setBackground(getResources().getDrawable(shapeDrawableMap.get(randomValue)));
        shape6.setX(shape6Pos[0]);
        shape6.setY(shape6Pos[1]);
        DragTouchListener dragListener6 = new DragTouchListener(parentLayout,shape6,randomValue,dropOfText1, dropOfText2, dropOfText3,dropOfText4,this);

        shape1.setOnTouchListener(dragListener1);
        shape2.setOnTouchListener(dragListener2);
        shape3.setOnTouchListener(dragListener3);
        shape4.setOnTouchListener(dragListener4);
        shape5.setOnTouchListener(dragListener5);
        shape6.setOnTouchListener(dragListener6);
    }

    private void Done(){
        game_started_flag = false;
        // lokalno čuvanje u obliku ključ:string(lista)
        SharedPreferences sharedPreferences =  getSharedPreferences("ShapesGameStore", MODE_PRIVATE);
        String scores = sharedPreferences.getString(LoginSignupPageActivity.current_email, "");
        // Svaka igra je sačuvana kao string liste na koji konkatiram sljedeći rezultat
        scores = scores == "" ? rounds.toString() : scores + "," + rounds.toString();
        getSharedPreferences("ShapesGameStore", MODE_PRIVATE)
                .edit()
                .putString(LoginSignupPageActivity.current_email, scores)
                .apply();
        removeItems();
        game_finished_text.setVisibility(View.VISIBLE);

        if(rounds>=7){
            game_finished_text.setText(rounds + " " + congrats);
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
        rounds = 0;
    }

    private void removeItems() {
        button_start.setVisibility(View.GONE);
        timer_text.setVisibility(View.GONE);
        rounds_text.setVisibility(View.GONE);
        shape1.setVisibility(View.GONE);
        shape2.setVisibility(View.GONE);
        shape3.setVisibility(View.GONE);
        shape4.setVisibility(View.GONE);
        shape5.setVisibility(View.GONE);
        shape6.setVisibility(View.GONE);
        dropOfText1.setVisibility(View.GONE);
        dropOfText2.setVisibility(View.GONE);
        dropOfText3.setVisibility(View.GONE);
        dropOfText4.setVisibility(View.GONE);
    }

    public void playGood_sound(){
        good_sound.start();
    }
    public void playWrong_sound(){
        wrong_sound.start();
    }

}