package com.example.oi_projekt.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.oi_projekt.R;
import com.example.oi_projekt.adapter.ItemAdapter;

public class NumbersGameActivity extends AppCompatActivity {

    private Button button_done;
    private ImageButton back_button;
    HashMap itemsMap = new HashMap<Integer, Integer>();
    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private TextView timer_text;
    private TextView rounds_text;
    private TextView game_finished_text;

    private int rounds = 0;

    List<String> items;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numbers_game);

        back_button = findViewById(R.id.back_button);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        button_done = (Button)findViewById(R.id.done_button);
        timer_text = findViewById(R.id.timer_text);
        rounds_text = findViewById(R.id.rounds_text);
        game_finished_text = findViewById(R.id.game_finished_text);
        game_finished_text.setVisibility(View.GONE);


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
                    if(rounds == 10){
                        Done();
                    }
                    Toast.makeText(NumbersGameActivity.this, "Well Done!", Toast.LENGTH_SHORT).show();
                    RandomizeItems();
                    rounds_text.setText(++rounds + " / 10");
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
                finish();
            }
        });

        button_done.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new CountDownTimer(60000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        // Used for formatting digit to be in 2 digits only
                        NumberFormat f = new DecimalFormat("00");
                        long sec = (millisUntilFinished / 1000) % 60;
                        timer_text.setText(f.format(sec) + "s");
                    }
                    // When the task is over it will print 00:00:00 there
                    public void onFinish() {
                        timer_text.setText("0s");
                        Done();
                    }
                }.start();            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void RandomizeItems(){
        items = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            int random1 = new Random().nextInt(51);
            int random2 = new Random().nextInt(51);
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
    }

    private void Done(){
        SharedPreferences sharedPreferences =  getSharedPreferences("NumbersGameOrderStore", MODE_PRIVATE);
        Integer score = sharedPreferences.getInt(LoginSignupPageActivity.current_email, 0);
        getSharedPreferences("NumbersGameOrderStore", MODE_PRIVATE)
                .edit()
                .putInt(LoginSignupPageActivity.current_email, rounds)
                .apply();
        RemoveItems();
        if(rounds==10){
            game_finished_text.setText("10/10");
        }else if(rounds<10 && rounds>5){
            game_finished_text.setText("<10/10");
        }else{
            game_finished_text.setText("<<10/10");
        }
        button_done.setVisibility(View.GONE);
        timer_text.setVisibility(View.GONE);
        rounds_text.setVisibility(View.GONE);
        game_finished_text.setVisibility(View.VISIBLE) ;

    }
}