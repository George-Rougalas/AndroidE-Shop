package com.example.store.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.store.R;


public class IntroActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro);

    }

    public void login(View view){
        startActivity(new Intent(IntroActivity.this, loginActivity.class));
    }

    public void registration(View view){
        startActivity(new Intent(IntroActivity.this, registrationActivity.class));
    }
}