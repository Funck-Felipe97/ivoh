package com.fvf.ivoohcliente.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.fvf.ivoohcliente.R;
import com.fvf.ivoohcliente.helper.FirebaseConfig;

public class HomeActivity extends AppCompatActivity {

    private Button btnLogout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        btnLogout = findViewById(R.id.buttonLogout);
        btnLogout.setOnClickListener((View v) -> {
            FirebaseConfig.getFirebaseAuth().signOut();
            startActivity(new Intent(this, AutenticacaoActivity.class));
        });

    }
}
