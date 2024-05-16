package com.example.oi_projekt.interfaces;

public interface IFragmentToActivity {
    void onDataPassLogin(String email, String password);
    void onDataPassSignup(String email, String password, String confirm_password);

}
