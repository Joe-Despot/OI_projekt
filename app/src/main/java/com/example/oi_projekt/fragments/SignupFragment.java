package com.example.oi_projekt.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.oi_projekt.R;
import com.example.oi_projekt.interfaces.IFragmentToActivity;

public class SignupFragment extends Fragment {
    private Button signup_button;
    private EditText signup_email;
    private EditText signup_password;
    private EditText signup_password_confirm;
    private IFragmentToActivity dataPasser;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            dataPasser = (IFragmentToActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnDataPass");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        signup_button = (Button)view.findViewById(R.id.signup_button);
        signup_email = view.findViewById(R.id.signup_email);
        signup_password = view.findViewById(R.id.signup_password);
        signup_password_confirm = view.findViewById(R.id.signup_confirm);

        signup_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String name = signup_email.getText().toString();
                String password = signup_password.getText().toString();
                String confirm_password = signup_password_confirm.getText().toString();
                if(name == "" || password == "" || confirm_password=="") {
                    Toast.makeText(getActivity(),"Email, password and confirmed password are required!", Toast.LENGTH_LONG).show();
                    Log.d("LOG", "1");
                }
                else {
                    dataPasser.onDataPassSignup(name, password, confirm_password);
                }
            }
        });
        return view;
    }
}