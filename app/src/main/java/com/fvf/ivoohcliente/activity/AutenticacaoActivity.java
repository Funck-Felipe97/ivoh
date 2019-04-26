package com.fvf.ivoohcliente.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.fvf.ivoohcliente.R;
import com.fvf.ivoohcliente.Service.UsuarioService;
import com.fvf.ivoohcliente.helper.FirebaseConfig;
import com.fvf.ivoohcliente.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AutenticacaoActivity extends AppCompatActivity {

    private Button botaoAcessar;
    private EditText campoEmail;
    private EditText campoSenha;
    private Switch tipoAcesso;
    private Switch tipoUsuario;
    private LinearLayout linearTipoUsuario;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacao);
        getSupportActionBar().hide();

        autenticacao = FirebaseConfig.getFirebaseAuth();
        inicializarComponentes();
        verificaUsuarioLogado();
        tipoAcesso.setOnCheckedChangeListener(eventoSwitchTipoAcesso());
        botaoAcessar.setOnClickListener(eventoBotaoAcessar());
    }

    /**
     * Método responsável por inicializar os componentes do layout
     *
     * @author Felipe Carvalho Funck
     */
    private void inicializarComponentes() {
        campoEmail = findViewById(R.id.editCadastroEmail);
        campoSenha = findViewById(R.id.editCadastroSenha);
        botaoAcessar = findViewById(R.id.buttonAcesso);
        tipoAcesso = findViewById(R.id.switchAcesso);
        tipoUsuario = findViewById(R.id.switchCompradorVendedor);
        linearTipoUsuario = findViewById(R.id.linerTipoUsuarioId);
    }

    /**
     * Método responsável por retornar o evento de click para o botão acessar
     *
     * @return Evento de click para um Button
     * @author Felipe Carvalho Funck
     */
    private Button.OnClickListener eventoBotaoAcessar() {
        return (View v) -> {
            String email = campoEmail.getText().toString();
            String senha = campoSenha.getText().toString();

            if (tipoAcesso.isChecked()) {
                cadastrarNovoUsuario(email, senha);
            } else {
                if (!email.isEmpty()) {
                    if (!senha.isEmpty()) {
                        logar(email, senha);
                    } else {
                        Toast("Preencha a Senha!");
                    }
                } else {
                    Toast("Preencha o E-mail!");
                }
            }
        };
    }

    /**
     * Método responsável por responder eventos de mudança de estado no switch de tipo de acesso
     *
     * @return Novo evento para a mudança de estados no switch
     * @author Felipe Carvalho Funck
     */
    public CompoundButton.OnCheckedChangeListener eventoSwitchTipoAcesso() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    linearTipoUsuario.setVisibility(View.VISIBLE);
                } else {
                    linearTipoUsuario.setVisibility(View.GONE);
                }
            }
        };
    }

    /**
     * Método responsável por realizar o login do usuário
     *
     * @param email E-mail do usuário
     * @param senha Senha do usuário
     * @author Felipe Carvalho Funck
     */
    public void logar(String email, String senha) {
        autenticacao.signInWithEmailAndPassword(
                email,
                senha
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast("Logado com sucesso!");
                    abrirTelaPrincipal();
                } else {
                    Toast("Erro ao fazer login :" + task.getException().getMessage());
                }
            }
        });
    }

    /**
     * Método responsável por cadastrar novo usuário
     *
     * @param email E-mail do usuário
     * @param senha Senha do usuário
     * @author Felipe Carvalho Funck
     */
    private void cadastrarNovoUsuario(String email, String senha) {
        Intent i = new Intent(AutenticacaoActivity.this, CadastroUsuarioActivity.class);
        Bundle extras = new Bundle();
        extras.putString("email", email);
        extras.putString("senha", senha);
        extras.putString("tipoUsuario", getTipoUsuario());
        i.putExtras(extras);
        startActivity(i);
    }

    /**
     * Método responsável por verificar se o usuário esta logados
     *
     * @author Felipe Carvalho Funck
     */
    private void verificaUsuarioLogado() {
        FirebaseUser usuarioAtual = autenticacao.getCurrentUser();
        if (usuarioAtual != null) {
            abrirTelaPrincipal();
        }
    }

    /**
     * Método responsável por abrir a tela principal do aplicativo
     *
     * @author Felipe Carvalho Funck
     */
    private void abrirTelaPrincipal() {
        UsuarioService.Callback callback = (usuario) -> {
            if (usuario != null) {
                if ("Comprador".equals(usuario.getTipoUsuario())) {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }
                if ("Vendedor".equals(usuario.getTipoUsuario())) {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }
            }
        };
        new UsuarioService().getUsuarioLogado(callback);
    }

    /**
     * Método resposavel por mostrar um toast na tela de autenticação
     *
     * @param mensagem Mensagem a ser exibida
     * @author Felipe Carvalho Funck
     */
    public void Toast(String mensagem) {
        Toast.makeText(
                AutenticacaoActivity.this,
                mensagem,
                Toast.LENGTH_SHORT
        ).show();
    }

    /**
     * Método responsável por retornr o tipo do usuario do cadastro/login
     *
     * @return Tipo de usuario
     * @author Felipe Carvalho Funck
     */
    public String getTipoUsuario() {
        return tipoUsuario.isChecked() ? "Vendedor" : "Comprador";
    }

}
