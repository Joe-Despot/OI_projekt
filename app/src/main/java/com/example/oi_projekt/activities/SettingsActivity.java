package com.example.oi_projekt.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import com.example.oi_projekt.animation.MyBounce;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    public static boolean audioFlag = true;
    public String language;
    private ImageButton back_button;
    private Switch audioSwitch;
    private TextView textview;
    private TextView settingsText;
    private TextView nameText;
    private TextView languageText;
    private TextView audioText;
    private TextView saveText;
    private EditText name;
    private Button button_save;
    private Button log_out;
    private String settings_string;
    private String name_string;
    private String language_string;
    private String choose_language_string;
    private String audio_string;
    private String audioon_string;
    private String audiooff_string;
    private String save_string;
    private String log_out_string,user_string;

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
        SharedPreferences sharedPreferences_audio =  getSharedPreferences("LanguageSettingsStore", MODE_PRIVATE);
        String lang = sharedPreferences_audio.getString(LoginSignupPageActivity.current_email, "en");
        switch (lang) {
            case "en":
                settings_string = "Settings";
                name_string = "Name";
                language_string = "Language";
                audio_string = "Audio";
                audioon_string = "On";
                audiooff_string = "Off";
                save_string = "Save";
                log_out_string = "Logout";
                user_string = "Name";
                choose_language_string = "Choose Language";
                break;
            case "hr":
                settings_string = "Postavke";
                name_string = "Ime";
                language_string = "Jezik";
                audio_string = "Zvuk";
                audioon_string = "Da";
                audiooff_string = "Ne";
                save_string = "Spremi";
                log_out_string = "Odjava";
                choose_language_string = "Odaberi Jezik";
                user_string = "Ime";
                break;
            default:
                choose_language_string = "Choose Language";
                log_out_string = "Logout";
                settings_string = "Settings";
                name_string = "Name";
                language_string = "Language";
                audio_string = "Audio";
                audioon_string = "On";
                audiooff_string = "Off";
                save_string = "Save";
                user_string = "Name";
                break;
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.dropdown_item, languages);
        AutoCompleteTextView autocompleteTV = findViewById(R.id.autoCompleteTextView);
        autocompleteTV.setText(choose_language_string);
        autocompleteTV.setAdapter(arrayAdapter);

        audioSwitch = (Switch)findViewById(R.id.toggleButton);
        textview = (TextView)findViewById(R.id.audioTextView);
        back_button = findViewById(R.id.back_button_settings);
        name = findViewById(R.id.name);
        button_save = findViewById(R.id.button_save);
        button_save.setText(save_string);
        log_out = findViewById(R.id.log_out);
        log_out.setText(log_out_string);
        nameText = findViewById(R.id.nameText);
        audioText = findViewById(R.id.audioText);
        languageText = findViewById(R.id.languageText);
        settingsText = findViewById(R.id.settingsText);
        settingsText.setText(settings_string);
        nameText.setText(name_string+ ":");
        languageText.setText(language_string+ ":");
        audioText.setText(audio_string + ":");

        SharedPreferences sharedPreferences1 =  getSharedPreferences("AudioSettingsStore", MODE_PRIVATE);
        String audioString = sharedPreferences1.getString(LoginSignupPageActivity.current_email, "On");
        if(audioString.equals("On")){
            audioFlag = true;
            audioSwitch.setChecked(true);
            textview.setText(audioon_string);
        }else {
            audioFlag = false;
            audioSwitch.setChecked(false);
            textview.setText(audiooff_string);
        }
        SharedPreferences sharedPreferences_name =  getSharedPreferences("NameSettingsStore", MODE_PRIVATE);
        String username = sharedPreferences_name.getString(LoginSignupPageActivity.current_email, user_string);
        name.setHint(username);
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent(SettingsActivity.this, LoginSignupPageActivity.class);
                finish();
                startActivity(resultIntent);
            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation myAnim = AnimationUtils.loadAnimation(SettingsActivity.this, R.anim.bounce);
                MyBounce interpolator = new MyBounce(0.01, 20);
                myAnim.setInterpolator(interpolator);
                back_button.startAnimation(myAnim);
                Intent resultIntent = new Intent(SettingsActivity.this, GameChooserActivity.class);
                finish();
                startActivity(resultIntent);
            }
        });

        autocompleteTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = (String) parent.getItemAtPosition(position);
                Toast.makeText(SettingsActivity.this, "Selected: " + selectedLanguage, Toast.LENGTH_SHORT).show();
                if(selectedLanguage.equals("English"))
                    language = "en";
                if(selectedLanguage.equals("Hrvatski"))
                    language = "hr";
            }
        });

        button_save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String audioString = audioFlag == true ? "On" : "Off";
                getSharedPreferences("AudioSettingsStore", MODE_PRIVATE)
                        .edit()
                        .putString(LoginSignupPageActivity.current_email,audioString)
                        .apply();
                if(!name.getText().toString().equals(null) && !name.getText().toString().equals("")) {
                    getSharedPreferences("NameSettingsStore", MODE_PRIVATE)
                            .edit()
                            .putString(LoginSignupPageActivity.current_email, name.getText().toString())
                            .apply();
                }
                getSharedPreferences("LanguageSettingsStore", MODE_PRIVATE)
                        .edit()
                        .putString(LoginSignupPageActivity.current_email,language)
                        .apply();
                new CountDownTimer(60000, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }
                    public void onFinish() {
                    }
                }.start();
            }
        });
    }


    public void onToggleClick(View view)
    {
        if (audioSwitch.isChecked()) {
            textview.setText(audioon_string);
            audioFlag = true;
        }
        else {
            textview.setText(audiooff_string);
            audioFlag = false;

        }
    }
}