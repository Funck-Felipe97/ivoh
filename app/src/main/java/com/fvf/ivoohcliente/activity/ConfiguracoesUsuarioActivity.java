package com.fvf.ivoohcliente.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.fvf.ivoohcliente.R;

public class ConfiguracoesUsuarioActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes_usuario);

        // Configuração action bar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações");
        setSupportActionBar(toolbar);

        // abilitando botão de voltar na action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
