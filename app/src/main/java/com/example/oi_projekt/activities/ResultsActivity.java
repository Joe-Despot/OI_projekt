package com.example.oi_projekt.activities;

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

    //TODO niš uz calculator game nije implementirano, dodati game
    private TextView settings_title_textview;
    private ImageButton back_button;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private CardAdapter cardAdapterCalculator;
    private CardAdapter cardAdapterOrderEquations;
    private ArrayList<String> cardListCalculator;
    private ArrayList<String> cardListOrderEquations;

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
        settings_title_textview = findViewById(R.id.settings_title);
        settings_title_textview.setText(R.string.settings);

        SharedPreferences sharedPreferences =  getSharedPreferences("NumbersGameOrderStore", MODE_PRIVATE);
        String numbers_game_order_score_saved = sharedPreferences.getString(LoginSignupPageActivity.current_email, "");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        cardListCalculator = new ArrayList<>();
        cardListOrderEquations = new ArrayList<>();

        cardAdapterCalculator = new CardAdapter(cardListCalculator);
        cardAdapterOrderEquations = new CardAdapter(cardListOrderEquations);

        recyclerView.setAdapter(cardAdapterCalculator);
        recyclerView2.setAdapter(cardAdapterOrderEquations);

        for(String s: numbers_game_order_score_saved.split(",")){
            cardListOrderEquations.add(s + " / 7");
            cardAdapterOrderEquations.notifyItemInserted(cardListOrderEquations.size() - 1);
        }

        for(String s: numbers_game_order_score_saved.split(",")){
            cardListCalculator.add(s + " / 7");
            cardAdapterCalculator.notifyItemInserted(cardListCalculator.size() - 1);
        }


        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation myAnim = AnimationUtils.loadAnimation(ResultsActivity.this, R.anim.bounce);
                MyBounce interpolator = new MyBounce(0.2, 20);
                myAnim.setInterpolator(interpolator);
                back_button.startAnimation(myAnim);
                finish();
            }
        });
    }

}