package com.example.oi_projekt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.oi_projekt.databinding.ActivityMain2Binding;

public class GameChooserActivity extends AppCompatActivity {

private ActivityMain2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ImageView settings_image =(ImageView)findViewById(R.id.settings_button);
        ImageView menu_image =(ImageView)findViewById(R.id.results_button);
        ImageView numbers_game_button =(ImageView)findViewById(R.id.numbers_game_button);

        settings_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(GameChooserActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
            }
        });
        menu_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent(GameChooserActivity.this, ResultsActivity.class);
                startActivity(resultIntent);
            }
        });
        numbers_game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numbers_game_button.setImageResource(R.drawable.door_opened_numbers);
            }
        });
    }

}