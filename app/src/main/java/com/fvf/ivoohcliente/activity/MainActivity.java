package com.fvf.ivoohcliente.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fvf.ivoohcliente.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        new Handler().postDelayed(() -> {
            abrirAutenticacao();
        }, 3000);
    }

    private void abrirAutenticacao() {
        Intent i = new Intent(this, AutenticacaoActivity.class);
        startActivity(i);
        finish();
    }

}
