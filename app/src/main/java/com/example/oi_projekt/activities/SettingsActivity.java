package com.example.oi_projekt.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.oi_projekt.R;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    private ImageButton back_button;
    private Switch audioSwitch;
    private TextView textview;
    private String[] languages = {"English", "Hrvatski"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.dropdown_item, languages);
        AutoCompleteTextView autocompleteTV = findViewById(R.id.autoCompleteTextView);
        autocompleteTV.setAdapter(arrayAdapter);

        audioSwitch = (Switch)findViewById(R.id.toggleButton);
        textview = (TextView)findViewById(R.id.audioTextView);
        back_button = findViewById(R.id.back_button_settings);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        autocompleteTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = (String) parent.getItemAtPosition(position);
                Toast.makeText(SettingsActivity.this, "Selected: " + selectedLanguage, Toast.LENGTH_SHORT).show();
                // TODO promjena jezika
            }
        });
    }


    public void onToggleClick(View view)
    {
        if (audioSwitch.isChecked()) {
            textview.setText("On");
        }
        else {
            textview.setText("Off");
        }
        // TODO dodati zvuk
    }
}