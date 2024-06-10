package com.example.oi_projekt.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oi_projekt.R;
import com.example.oi_projekt.animation.MyBounce;

public class GameChooserActivity extends AppCompatActivity {
    private TextView welcomeText;
    String welcome_string;
    String username_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamechooser_activity);
        Toast.makeText(GameChooserActivity.this, "______", Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences_audio =  getSharedPreferences("LanguageSettingsStore", MODE_PRIVATE);
        String lang = sharedPreferences_audio.getString(LoginSignupPageActivity.current_email, "en");
        switch (lang) {
            case "en":
                welcome_string = "Welcome";
                break;
            case "hr":
                welcome_string = "Dobrodošli";
                break;
            default:
                break;
        }
        ImageView settings_image =(ImageView)findViewById(R.id.settings_button);
        ImageView menu_image =(ImageView)findViewById(R.id.results_button);
        ImageView numbers_game_button =(ImageView)findViewById(R.id.numbers_game_button);
        ImageView colors_game_button =(ImageView)findViewById(R.id.colors_game_button);
        ImageView letters_game_button =(ImageView)findViewById(R.id.letters_game_button);
        welcomeText = findViewById(R.id.welcome_text);

        SharedPreferences sharedPreferences_name = getSharedPreferences("NameSettingsStore", MODE_PRIVATE);
        String username_string = sharedPreferences_name.getString(LoginSignupPageActivity.current_email, "User");
        welcomeText.setText(welcome_string + ", " + username_string);

        settings_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation myAnim = AnimationUtils.loadAnimation(GameChooserActivity.this, R.anim.bounce);
                MyBounce interpolator = new MyBounce(0.01, 20);
                myAnim.setInterpolator(interpolator);
                settings_image.startAnimation(myAnim);
                Intent settingsIntent = new Intent(GameChooserActivity.this, SettingsActivity.class);
                finish();
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
                finish();
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