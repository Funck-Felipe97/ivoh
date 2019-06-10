package com.fvf.ivoohcliente.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fvf.ivoohcliente.R;
import com.fvf.ivoohcliente.Service.ProdutoService;
import com.fvf.ivoohcliente.Service.UsuarioService;
import com.fvf.ivoohcliente.model.Produto;

public class NovoProdutoVendedorActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText edtNomeProduto;
    private EditText edtDescricaoProduto;
    private EditText edtPrecoProduto;
    private boolean modoEdicao = false;
    private String idProduto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_produto_vendedor);

        inicializarComponentes();

        // abilitando botão de voltar na action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null && getIntent().getExtras() != null) {
            modoEdicao = getIntent().getExtras().getBoolean("modoEdicao");
        }

        if (modoEdicao) preencherCampos();
    }

    private void preencherCampos() {
        Button btnSalvar = findViewById(R.id.btnSalvarProduto);
        btnSalvar.setText("Salvar");
        Produto produto = (Produto) getIntent().getExtras().get("produto");
        if (produto != null) {
            edtNomeProduto.setText(produto.getNome());
            edtDescricaoProduto.setText(produto.getDescricao());
            edtPrecoProduto.setText(produto.getPreco().toString());
            idProduto = produto.getIdProduto();
        }
    }

    public void validarDadosProduto(View v) {
        String nome = edtNomeProduto.getText().toString();
        Double preco = 0D;
        String descricao = edtDescricaoProduto.getText().toString();

        try {
            preco = Double.valueOf(edtPrecoProduto.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            Toast("Digite um valor válido para o preço do produto");
            return;
        }
        if (nome.isEmpty()) {
            Toast("Digite um nome para o produto");
            return;
        }
        if (descricao.isEmpty()) {
            Toast("Digite uma descrição para o produto");
            return;
        }

        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setDescricao(descricao);
        produto.setPreco(preco);
        produto.setIdUsuario(UsuarioService.getIdUsuario());
        produto.setIdProduto(idProduto);

        boolean produtoSalvo = false;

        if (!modoEdicao) {
            produtoSalvo = new ProdutoService().save(produto);
        } else {
            produtoSalvo = new ProdutoService().update(produto);
        }

        if (produtoSalvo) {
            Toast("Informações do produto salvo com sucesso");
        } else {
            Toast("Falha ao salvar dados do produto");
        }
        finish();
    }

    /**
     * Método resposavel por inicializar os componentes do layout
     *
     * @author Felipe Carvalho Funck
     */
    private void inicializarComponentes() {
        // Configuração action bar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo produto");
        setSupportActionBar(toolbar);

        edtNomeProduto = findViewById(R.id.edtNomeProduto);
        edtDescricaoProduto = findViewById(R.id.edtDescricaoProduto);
        edtPrecoProduto = findViewById(R.id.edtPrecoProduto);
    }

    /**
     * Método resposavel por mostrar um toast na tela de autenticação
     *
     * @param mensagem Mensagem a ser exibida
     * @author Felipe Carvalho Funck
     */
    public void Toast(String mensagem) {
        Toast.makeText(
                this,
                mensagem,
                Toast.LENGTH_SHORT
        ).show();
    }

}
