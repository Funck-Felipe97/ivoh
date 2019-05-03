package com.fvf.ivoohcliente.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fvf.ivoohcliente.R;
import com.fvf.ivoohcliente.Service.UsuarioService;
import com.fvf.ivoohcliente.helper.FirebaseConfig;
import com.fvf.ivoohcliente.model.Endereco;
import com.fvf.ivoohcliente.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText email;
    private EditText senha;
    private EditText confirmarSenha;
    private EditText cpf;
    private EditText nome;
    private EditText cidade;
    private EditText endereco;
    private EditText telefone;
    private Button btnSalvarUsuario;
    private Usuario usuario;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        usuario = new Usuario();
        iniciarCampos(getIntent().getExtras());

    }

    /**
     * Método responsável por iniciar os atributos do layout
     *
     * @param extras Bundle com os parâmetros
     * @author Felipe Carvalho Funck
     */
    private void iniciarCampos(Bundle extras) {
        email = findViewById(R.id.edtCadastroEmail);
        senha = findViewById(R.id.edtCadastroSenha);
        confirmarSenha = findViewById(R.id.edtConfirmarSenha);
        cpf = findViewById(R.id.edtCadastroCpf);
        nome = findViewById(R.id.edtCadastroNome);
        cidade = findViewById(R.id.edtCadastroCidade);
        endereco = findViewById(R.id.edtCadastroEndereco);
        telefone = findViewById(R.id.edtCadastroTelefone);
        btnSalvarUsuario = findViewById(R.id.buttonCadastrarUsuario);
        btnSalvarUsuario.setOnClickListener(eventoCLickBotao());

        if (extras != null) {
            if (extras.get("email") != null) {
                email.setText(extras.get("email").toString());
            }
            if (extras.get("senha") != null) {
                senha.setText(extras.get("senha").toString());
            }
            if (extras.get("tipoUsuario") != null) {
                usuario.setTipoUsuario(extras.get("tipoUsuario").toString());
            }
        }
    }

    /**
     * Método responsável por cadastrar um novo usuário
     *
     * @author Felipe Carvalho Funck
     */
    public void cadastrarNovoUsuario() {
        FirebaseConfig.getFirebaseAuth().createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    new UsuarioService().save(usuario);
                   Toast("Cadastro realizado com sucesso");
                   abrirTelaPrincipal();
                } else {
                    String erro = "";
                    try {
                        throw  task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erro = "Digite uma senha mais forte!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erro = "Digite um e-mail válido!";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erro = "Esta conta já foi cadastrada!";
                    } catch (Exception e) {
                        erro = "Erro ao cadastrar usuário!";
                        e.printStackTrace();
                    }
                    Toast(erro);
                }
            }
        });
    }

    /**
     * Método responsável por gerar um evento de click pro botão de cadastrar
     *
     * @return Novo evento de click para Button
     * @author Felipe Carvalho Funck
     */
    public Button.OnClickListener eventoCLickBotao() {
        return (View v) -> {
            usuario.setEmail(email.getText().toString());
            usuario.setSenha(senha.getText().toString());
            usuario.setNome(nome.getText().toString());
            usuario.setCpf(cpf.getText().toString());
            usuario.setTelefone(telefone.getText().toString());

            Endereco endereco = new Endereco();
            endereco.setCidade(cidade.getText().toString());
            endereco.setDescricao(this.endereco.getText().toString());
            usuario.setEndereco(endereco);

            if (usuarioValido()) {
                cadastrarNovoUsuario();
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
        if (this.endereco.getText().toString() == null || "".equals(this.endereco.getText().toString())) {
            Toast("Preencha o campo endereço");
            return false;
        }
        if (this.cidade.getText().toString() == null || "".equals(this.cidade.getText().toString())) {
            Toast("Preencha o campo cidade");
            return false;
        }
        if (usuario.getSenha() == null || "".equals(usuario.getSenha())) {
            Toast("Preencha o campo senha");
            return false;
        }
        if (!usuario.getSenha().equals(confirmarSenha.getText().toString())) {
            Toast("As senhas são diferentes");
            return false;
        }
        return true;
    }

    /**
     * Método responsável por abrir a tela principal do aplicativo
     *
     * @author Felipe Carvalho Funck
     */
    private void abrirTelaPrincipal() {
        if ("Comprador".equals(usuario.getTipoUsuario())) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        } else {
            startActivity(new Intent(getApplicationContext(), VendedorActivity.class));
        }
    }

    /**
     * Método resposavel por mostrar um toast na tela de autenticação
     *
     * @param mensagem Mensagem a ser exibida
     * @author Felipe Carvalho Funck
     */
    public void Toast(String mensagem) {
        Toast.makeText(
                CadastroUsuarioActivity.this,
                mensagem,
                Toast.LENGTH_SHORT
        ).show();
    }

}
