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
import com.fvf.ivoohcliente.Service.UsuarioService;
import com.fvf.ivoohcliente.model.Usuario;

public class ConfiguracoesUsuarioActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Usuario usuario;
    private EditText edtNome;
    private EditText edtEmail;
    private EditText edtEnderecoEntrega;
    private EditText edtSenha;
    private EditText edtConfirmarSenha;
    private Button btnSalvar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes_usuario);

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        edtNome = findViewById(R.id.edtNomeUsuario);
        edtEmail = findViewById(R.id.edtEmailUsuario);
        edtEnderecoEntrega = findViewById(R.id.edtEnderecoUsuario);
        edtSenha = findViewById(R.id.edtSenhaUsuario);
        edtConfirmarSenha = findViewById(R.id.edtConfirmarSenhaUsuario);
        btnSalvar = findViewById(R.id.btnSalvarConfiguracoesUsuario);
        btnSalvar.setOnClickListener(salvar());

       new UsuarioService().getUsuarioLogado(user -> {
           usuario = user;
           edtNome.setText(usuario.getNome());
           edtEmail.setText(usuario.getEmail());
           edtEnderecoEntrega.setText(usuario.getEndereco().getDescricao());
           edtConfirmarSenha.setText(usuario.getSenha());
           edtSenha.setText(usuario.getSenha());
       });

        // Configuração action bar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações");
        setSupportActionBar(toolbar);

        // abilitando botão de voltar na action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public Button.OnClickListener salvar() {
        return (View v) -> {
            if (usuarioValido()) {
                if(new UsuarioService().save(usuario)) {
                    Toast("Informações salvas");
                } else {
                    Toast("Falha ao salvar informações");
                }
            }
        };
    }

    public boolean usuarioValido() {
        if (usuario.getNome() == null || "".equals(usuario.getNome())) {
            Toast("Preencha o campo nome");
            return false;
        }
        if (usuario.getCpf() == null || "".equals(usuario.getCpf())) {
            Toast("Preencha o campo cpf");
            return false;
        }
        if (usuario.getEmail() == null || "".equals(usuario.getEmail())) {
            Toast("Preencha o campo email");
            return false;
        }
        if (usuario.getTelefone() == null || "".equals(usuario.getTelefone())) {
            Toast("Preencha o campo telefone");
            return false;
        }
        if (usuario.getEndereco().getDescricao() == null || "".equals(usuario.getEndereco().getDescricao())) {
            Toast("Preencha o campo endereço");
            return false;
        }
        if (usuario.getEndereco().getCidade() == null || "".equals(usuario.getEndereco().getCidade())) {
            Toast("Preencha o campo cidade");
            return false;
        }
        if (usuario.getSenha() == null || "".equals(usuario.getSenha())) {
            Toast("Preencha o campo senha");
            return false;
        }
        if (!usuario.getSenha().equals(edtConfirmarSenha.getText().toString())) {
            Toast("As senhas são diferentes");
            return false;
        }
        return true;
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
