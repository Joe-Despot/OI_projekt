package com.example.oi_projekt.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.oi_projekt.R;
import com.example.oi_projekt.animation.MyBounceInterpolator;

public class ResultsActivity extends AppCompatActivity {
    private ImageButton back_button;
    private ProgressBar numbers_order_bar;

    private TextView numbers_game_order_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_results);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        back_button = findViewById(R.id.back_button);
       // numbers_order_bar = findViewById(R.id.CalculatorGameBar);


        //numbers_game_order_score = findViewById(R.id.NumbersGameOrderScore);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation myAnim = AnimationUtils.loadAnimation(ResultsActivity.this, R.anim.bounce);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);
                back_button.startAnimation(myAnim);
                finish();
            }
        });
        //SharedPreferences sharedPreferences =  getSharedPreferences("NumbersGameOrderStore", MODE_PRIVATE);
        //Integer numbers_game_order_score_saved = sharedPreferences.getInt(LoginSignupPageActivity.current_email, 0);
        //numbers_game_order_score.setText(numbers_game_order_score_saved.toString());
    }

}