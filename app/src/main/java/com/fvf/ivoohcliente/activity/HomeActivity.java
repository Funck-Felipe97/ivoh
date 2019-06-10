package com.fvf.ivoohcliente.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.fvf.ivoohcliente.R;
import com.fvf.ivoohcliente.Service.EstabelecimentoService;
import com.fvf.ivoohcliente.adapter.AdapterEmpresa;
import com.fvf.ivoohcliente.helper.FirebaseConfig;
import com.fvf.ivoohcliente.model.Estabelecimento;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private MaterialSearchView searchView;
    private RecyclerView recyclerView;
    private List<Estabelecimento> estabelecimentos = new ArrayList<>();
    private AdapterEmpresa adapterEmpresa;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        inicializarComponentes();
        recuperarEstabelecimetos();

        searchView.setHint("Pesquisar vendedores");
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                pesquisarEstabelecimentos(newText);
                return true;
            }
        });
    }

    private void pesquisarEstabelecimentos(String nomeEstabelecimento) {
        EstabelecimentoService.CallbackList callback = (estabelecimentoList) -> {
            estabelecimentos.clear();
            estabelecimentos.addAll(estabelecimentoList);
            adapterEmpresa.notifyDataSetChanged();
        };
        new EstabelecimentoService().getByNome(callback, nomeEstabelecimento);
    }

    private void inicializarComponentes() {
        searchView = findViewById(R.id.materialSearchView);
        recyclerView = findViewById(R.id.recyclerVendedores);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapterEmpresa = new AdapterEmpresa(estabelecimentos);
        recyclerView.setAdapter(adapterEmpresa);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Ivô");
        setSupportActionBar(toolbar);
    }

    private void recuperarEstabelecimetos() {
        EstabelecimentoService.CallbackList callback = (estabelecimentoList) -> {
            estabelecimentos.clear();
            estabelecimentos.addAll(estabelecimentoList);
            adapterEmpresa.notifyDataSetChanged();
        };
        new EstabelecimentoService().getAll(callback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_usuario, menu);

        // Configurar botão de pesquisa
        MenuItem item = menu.findItem(R.id.menuPesquisa);
        searchView.setMenuItem(item);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSair:
                deslogarUsuario();
                break;
            case R.id.menuConfigurações:
                abrirConfiguracoes();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuario() {
        try {
            FirebaseConfig.getFirebaseAuth().signOut();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void abrirConfiguracoes() {
        startActivity(new Intent(this, ConfiguracoesUsuarioActivity.class));
    }

}
