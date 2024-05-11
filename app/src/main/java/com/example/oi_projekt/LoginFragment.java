package com.example.oi_projekt;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginFragment extends Fragment {
    private Button login_button;
    private EditText login_email;
    private EditText login_password;

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
                if(name == "" || password == "") {
                    Toast.makeText(getActivity(),"name and password required", Toast.LENGTH_LONG).show();
                    Log.d("LOG", "1");
                }
                else {
                    Log.d("LOG", "2");
                    Intent gameActivity = new Intent(getActivity(), GameChooserActivity.class);
                    startActivity(gameActivity);
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