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
import android.view.View;
import android.widget.AdapterView;

import com.fvf.ivoohcliente.R;
import com.fvf.ivoohcliente.Service.ProdutoService;
import com.fvf.ivoohcliente.adapter.AdapterProduto;
import com.fvf.ivoohcliente.helper.FirebaseConfig;
import com.fvf.ivoohcliente.listener.RecyclerItemClickListener;
import com.fvf.ivoohcliente.model.Produto;

import java.util.ArrayList;
import java.util.List;

public class VendedorActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerProdutos;
    private AdapterProduto adapterProduto;
    private final List<Produto> produtos = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendedor);

        inicializarComponentes();
        recuperarProdutos();

        recyclerProdutos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerProdutos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Produto produtoSelecionado = produtos.get(position);
                                Intent intent = new Intent(VendedorActivity.this, NovoProdutoVendedorActivity.class);
                                intent.putExtra("modoEdicao", true);
                                intent.putExtra("produto", produtoSelecionado);
                                startActivity(intent);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );
    }

    private void inicializarComponentes() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Ivô - vendedor");
        setSupportActionBar(toolbar);

        // Configurar recyclerView
        recyclerProdutos = findViewById(R.id.recyclerProdutos);
        recyclerProdutos.setLayoutManager(new LinearLayoutManager(this));
        recyclerProdutos.setHasFixedSize(true);
        adapterProduto = new AdapterProduto(produtos, this);
        recyclerProdutos.setAdapter(adapterProduto);
    }

    private void recuperarProdutos() {
        ProdutoService.Callback callback = (produtosList) -> {
            produtos.clear();
            produtos.addAll(produtosList);
            adapterProduto.notifyDataSetChanged();
        };
        new ProdutoService().getProdutosByEmpresa(callback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_vendedor, menu);
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
            case R.id.menuNovoProduto:
                abrirNovoProduto();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        produtos.clear();
        adapterProduto.notifyDataSetChanged();
        recuperarProdutos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        produtos.clear();
        adapterProduto.notifyDataSetChanged();
        recuperarProdutos();
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
        startActivity(new Intent(this, ConfiguracoesEmpresaActivity.class));
    }

    private void abrirNovoProduto() {
        startActivity(new Intent(this, NovoProdutoVendedorActivity.class));
    }

}
