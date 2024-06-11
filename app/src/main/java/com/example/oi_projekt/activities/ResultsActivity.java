package com.example.oi_projekt.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oi_projekt.R;
import com.example.oi_projekt.adapter.CardAdapter;
import com.example.oi_projekt.animation.MyBounce;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {

    private TextView settings_title_textview;
    private ImageButton back_button;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private RecyclerView recyclerView3;
    private CardAdapter cardAdapterColorGame;
    private CardAdapter cardAdapterOrderEquations;
    private CardAdapter cardAdapterShapeGame;
    private ArrayList<String> cardListColorGame;
    private ArrayList<String> cardListOrderEquations;
    private ArrayList<String> cardListShapeGame;
    private TextView orderEquationsText;
    private TextView colorGameText;
    private TextView shapeGameText;
    private String order_equations_string;
    private String color_game_string;
    private String shape_game_string;
    String results_string;

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
        SharedPreferences sharedPreferences_audio =  getSharedPreferences("LanguageSettingsStore", MODE_PRIVATE);
        String lang = sharedPreferences_audio.getString(LoginSignupPageActivity.current_email, "en");
        switch (lang) {
            case "en":
                shape_game_string = "Shapes Game Scores";
                results_string = "Results";
                color_game_string = "Color Game Scores";
                order_equations_string = "Order Equations Game Scores";
                break;
            case "hr":
                shape_game_string = "Rezultati Igre Oblika";
                results_string = "Rezultati";
                color_game_string = "Rezultati Igre Boja";
                order_equations_string = "Rezultati Igre Poredavanje Jednadžbi";
                break;
            default:

                break;
        }
        back_button = findViewById(R.id.back_button);
        settings_title_textview = findViewById(R.id.settings_title);
        colorGameText = findViewById(R.id.colorGame);
        orderEquationsText = findViewById(R.id.orderEquationsGame);
        shapeGameText = findViewById(R.id.shapesGame);

        settings_title_textview.setText(results_string);
        colorGameText.setText(color_game_string);
        orderEquationsText.setText(order_equations_string);
        shapeGameText.setText(shape_game_string);

        SharedPreferences sharedPreferencesColorGame =  getSharedPreferences("ColorsGameStore", MODE_PRIVATE);
        SharedPreferences sharedPreferences =  getSharedPreferences("NumbersGameOrderStore", MODE_PRIVATE);
        SharedPreferences sharedPreferencesShapeGame =  getSharedPreferences("ShapesGameStore", MODE_PRIVATE);
        String numbers_game_order_score_saved = sharedPreferences.getString(LoginSignupPageActivity.current_email, "");
        String colors_game_score_saved = sharedPreferencesColorGame.getString(LoginSignupPageActivity.current_email, "");
        String shapes_game_score_saved = sharedPreferencesShapeGame.getString(LoginSignupPageActivity.current_email, "");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView3 = findViewById(R.id.recyclerView3);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView3.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        cardListColorGame = new ArrayList<>();
        cardListOrderEquations = new ArrayList<>();
        cardListShapeGame = new ArrayList<>();

        cardAdapterColorGame = new CardAdapter(cardListColorGame,lang);
        cardAdapterOrderEquations = new CardAdapter(cardListOrderEquations,lang);
        cardAdapterShapeGame = new CardAdapter(cardListShapeGame,lang);
        recyclerView.setAdapter(cardAdapterColorGame);
        recyclerView2.setAdapter(cardAdapterOrderEquations);
        recyclerView3.setAdapter(cardAdapterShapeGame);

        for(String s: numbers_game_order_score_saved.split(",")){
            cardListOrderEquations.add(s);
            cardAdapterOrderEquations.notifyItemInserted(cardListOrderEquations.size() - 1);
        }

        for(String s: colors_game_score_saved.split(",")){
            cardListColorGame.add(s);
            cardAdapterColorGame.notifyItemInserted(cardListColorGame.size() - 1);
        }

        for(String s: shapes_game_score_saved.split(",")){
            cardListShapeGame.add(s);
            cardAdapterShapeGame.notifyItemInserted(cardListShapeGame.size() - 1);
        }


        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation myAnim = AnimationUtils.loadAnimation(ResultsActivity.this, R.anim.bounce);
                MyBounce interpolator = new MyBounce(0.2, 20);
                myAnim.setInterpolator(interpolator);
                back_button.startAnimation(myAnim);
                Intent resultIntent = new Intent(ResultsActivity.this, GameChooserActivity.class);
                finish();
                startActivity(resultIntent);
            }
        });
    }

}