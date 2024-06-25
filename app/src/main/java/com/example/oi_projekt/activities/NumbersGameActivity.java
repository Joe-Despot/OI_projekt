package com.example.oi_projekt.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.oi_projekt.R;
import com.example.oi_projekt.adapter.ItemAdapter;
import com.example.oi_projekt.animation.MyBounce;

public class NumbersGameActivity extends AppCompatActivity {
    private String you_can_better, well_done, congrats;
    private Button button_done;
    private ImageButton back_button;
    HashMap itemsMap = new HashMap<Integer, Integer>();
    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private TextView timer_text;
    private TextView rounds_text;
    private TextView game_finished_text;
    private Integer rounds = 0;
    List<String> items;
    private MediaPlayer win_sound;
    private MediaPlayer try_again_sound;
    private MediaPlayer good_sound;
    private boolean game_started = false;
    CountDownTimer timer_start;
    String lang;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numbers_game);

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

        back_button = findViewById(R.id.back_button);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        button_done = (Button)findViewById(R.id.done_button);
        timer_text = findViewById(R.id.timer_text);
        rounds_text = findViewById(R.id.rounds_text);
        game_finished_text = findViewById(R.id.game_finished_text);
        game_finished_text.setVisibility(View.GONE);
        win_sound = MediaPlayer.create(this,R.raw.win_sound);
        good_sound = MediaPlayer.create(this,R.raw.good_sound);
        try_again_sound = MediaPlayer.create(this,R.raw.try_again);

        RandomizeItems();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                return makeMovementFlags(dragFlags, 0);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                List<Integer> items_map = (List<Integer>) itemsMap.values().stream().sorted().collect(Collectors.toList());
                List<Integer> items_done = new ArrayList<>();
                for(String s: items){
                    String[] arrOfStr = s.split("\\+",2);
                    Integer first_num = Integer.parseInt(arrOfStr[0].trim());
                    Integer second_num = Integer.parseInt(arrOfStr[1].trim());
                    items_done.add(first_num+second_num);
                }
                if(items_done.equals(items_map)){
                    good_sound.start();
                    RandomizeItems();
                    rounds_text.setText((++rounds).toString());
                }
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // No swipe action
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation myAnim = AnimationUtils.loadAnimation(NumbersGameActivity.this, R.anim.bounce);
                MyBounce interpolator = new MyBounce(0.01, 20);
                myAnim.setInterpolator(interpolator);
                back_button.startAnimation(myAnim);
                RemoveItems();
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

        button_done.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility((View.VISIBLE));
                button_done.setEnabled(false);
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
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setVisibility(View.GONE);
    }

    private void RandomizeItems(){
        items = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            int random1 = new Random().nextInt(15);
            int random2 = new Random().nextInt(15);
            itemsMap.put(i, random1 + random2);
            items.add(random1 + " + " + random2);
        }
        adapter = new ItemAdapter(items, this);
        recyclerView.setAdapter(adapter);
    }

    private void RemoveItems(){
        items = new ArrayList<>();
        adapter = new ItemAdapter(items, this);
        recyclerView.setAdapter(adapter);
        button_done.setVisibility(View.GONE);
        timer_text.setVisibility(View.GONE);
        rounds_text.setVisibility(View.GONE);
    }

    private void Done(){
        SharedPreferences sharedPreferences =  getSharedPreferences("NumbersGameOrderStore", MODE_PRIVATE);
        String scores = sharedPreferences.getString(LoginSignupPageActivity.current_email, "");
        scores = scores == "" ? rounds.toString() : scores + "," + rounds.toString();
        getSharedPreferences("NumbersGameOrderStore", MODE_PRIVATE)
                .edit()
                .putString(LoginSignupPageActivity.current_email, scores)
                .apply();
        RemoveItems();
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
    }
}