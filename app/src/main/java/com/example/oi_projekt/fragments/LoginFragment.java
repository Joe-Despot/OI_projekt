package com.example.oi_projekt.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.oi_projekt.R;
import com.example.oi_projekt.activities.GameChooserActivity;
import com.example.oi_projekt.interfaces.IFragmentToActivity;

public class LoginFragment extends Fragment {
    private Button login_button;
    private EditText login_email;
    private EditText login_password;
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        login_button = (Button)view.findViewById(R.id.login_button);
        login_email = view.findViewById(R.id.login_email);
        login_password = view.findViewById(R.id.login_password);

        login_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String name = login_email.getText().toString();
                String password = login_password.getText().toString();
                if(name.equals("") || password.equals("")) {
                    Intent gameActivity = new Intent(getActivity(), GameChooserActivity.class);
                    startActivity(gameActivity);
                    Toast.makeText(getActivity(),"name and password required", Toast.LENGTH_LONG).show();
                }
                else {
                    dataPasser.onDataPassLogin(name, password);
                }
            }
        });

//        edittext.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                Log.d("custom log", s.toString());
//            }
//        });
        return view;
    }
}