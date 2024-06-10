package com.example.oi_projekt.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.oi_projekt.R;
import com.example.oi_projekt.adapter.ViewPageAdapter;
import com.example.oi_projekt.interfaces.IFragmentToActivity;
import com.google.android.material.tabs.TabLayout;

public class LoginSignupPageActivity extends AppCompatActivity implements IFragmentToActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ViewPageAdapter adapter;
    public static String current_email = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup_tab);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager);

        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Signup"));

        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new ViewPageAdapter(fragmentManager, getLifecycle());
        viewPager2.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

    // Handle the data passed from Login fragment
    @Override
    public void onDataPassLogin(String email, String password) {
        SharedPreferences sharedPreferences =  getSharedPreferences("EmailPasswordStore", MODE_PRIVATE);
        if (sharedPreferences.contains(email)) {
            String saved_password = sharedPreferences.getString(email, "Value not found");
            if(saved_password.equals((password))){
                current_email = email;
                Intent gameActivity = new Intent(this, GameChooserActivity.class);
                startActivity(gameActivity);
            }
        } else {
            Toast.makeText(this, "User does not exist!", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle the data passed from Signup fragment
    @Override
    public void onDataPassSignup(String email, String password, String confirm_password) {
        SharedPreferences sharedPreferences =  getSharedPreferences("EmailPasswordStore", MODE_PRIVATE);
        if (sharedPreferences.contains(email)) {
            Toast.makeText(this, "User already exists!", Toast.LENGTH_SHORT).show();
        } else {
            if(confirm_password.equals((password))){
                getSharedPreferences("EmailPasswordStore", MODE_PRIVATE)
                        .edit()
                        .putString(email, password)
                        .apply();
                current_email = email;
                Intent gameActivity = new Intent(this, GameChooserActivity.class);
                startActivity(gameActivity);
            }else {
                Toast.makeText(this, "Confirmed password is different!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}