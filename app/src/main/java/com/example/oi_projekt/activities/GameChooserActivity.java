package com.example.oi_projekt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oi_projekt.R;
import com.example.oi_projekt.animation.MyBounce;

public class GameChooserActivity extends AppCompatActivity {
    //TODO username prikazati u main screenu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamechooser_activity);

        ImageView settings_image =(ImageView)findViewById(R.id.settings_button);
        ImageView menu_image =(ImageView)findViewById(R.id.results_button);
        ImageView numbers_game_button =(ImageView)findViewById(R.id.numbers_game_button);
        ImageView colors_game_button =(ImageView)findViewById(R.id.colors_game_button);
        ImageView letters_game_button =(ImageView)findViewById(R.id.letters_game_button);

        settings_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation myAnim = AnimationUtils.loadAnimation(GameChooserActivity.this, R.anim.bounce);
                MyBounce interpolator = new MyBounce(0.01, 20);
                myAnim.setInterpolator(interpolator);
                settings_image.startAnimation(myAnim);
                Intent settingsIntent = new Intent(GameChooserActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
            }
        });
        menu_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation myAnim = AnimationUtils.loadAnimation(GameChooserActivity.this, R.anim.bounce);
                MyBounce interpolator = new MyBounce(0.01, 20);
                myAnim.setInterpolator(interpolator);
                menu_image.startAnimation(myAnim);
                Intent resultIntent = new Intent(GameChooserActivity.this, ResultsActivity.class);
                startActivity(resultIntent);
            }
        });
        numbers_game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numbers_game_button.setImageResource(R.drawable.door_opened_numbers);
                Intent intent = new Intent(GameChooserActivity.this, NumbersGameActivity.class);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        numbers_game_button.setImageResource(R.drawable.doors_closed_numbers);
                    }
                }, 1500);
                startActivity(intent);
            }
        });

        colors_game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colors_game_button.setImageResource(R.drawable.door_castle_opened_colors);
                Intent intent = new Intent(GameChooserActivity.this, ColorsGameActivity.class);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        colors_game_button.setImageResource(R.drawable.door_castle_closed_colors);
                    }
                }, 1500);
                startActivity(intent);
            }
        });
    }

}