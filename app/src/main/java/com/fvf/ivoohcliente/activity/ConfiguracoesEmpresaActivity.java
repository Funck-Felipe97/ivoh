package com.fvf.ivoohcliente.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fvf.ivoohcliente.R;
import com.fvf.ivoohcliente.Service.EstabelecimentoService;
import com.fvf.ivoohcliente.Service.UsuarioService;
import com.fvf.ivoohcliente.helper.FirebaseConfig;
import com.fvf.ivoohcliente.model.Estabelecimento;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class ConfiguracoesEmpresaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText edtNomeEmpresa;
    private EditText edtCategoriaEmpresa;
    private EditText edtTempoEntrega;
    private EditText edtTaxaEntrega;
    private ImageView imagemPerfil;
    private static final int SELECAO_GALERIA = 200;

    private StorageReference storageReference;
    private String urlImagemSelecionada = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes_empresa);

        inicializarComponentes();

        storageReference = FirebaseConfig.getFirebaseStorage();

        // Configuração action bar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações");
        setSupportActionBar(toolbar);
        // abilitando botão de voltar na action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imagemPerfil.setOnClickListener((View v) -> {
            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            );
            if (i.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(i, SELECAO_GALERIA);
            }
        });

        recuperarDadosEstabelecimento();

    }

    private void recuperarDadosEstabelecimento() {
        EstabelecimentoService.Callback callback = (estabelecimento) -> {
            if (estabelecimento != null) {
                edtTempoEntrega.setText(estabelecimento.getTempo());
                edtTaxaEntrega.setText(estabelecimento.getPrecoEntrega().toString());
                edtNomeEmpresa.setText(estabelecimento.getNome());
                edtCategoriaEmpresa.setText(estabelecimento.getCategoria());

                urlImagemSelecionada = estabelecimento.getUrlImagem();
                if (urlImagemSelecionada != null && urlImagemSelecionada != "") {
                    Picasso.get()
                            .load(urlImagemSelecionada)
                            .into(imagemPerfil);
                }
            }
        };
        new EstabelecimentoService().getEstabelecimento(callback);
    }

    public void validarDadosVendedor(View v) {
        String nome = edtNomeEmpresa.getText().toString();
        Double taxa = 0D;
        String categoria = edtCategoriaEmpresa.getText().toString();
        String tempo = edtTempoEntrega.getText().toString();

        try {
            taxa = Double.valueOf(edtTaxaEntrega.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            Toast("Digite um valor válido para taxa de entrega");
            return;
        }
        if (nome.isEmpty()) {
            Toast("Digite um nome para o estabelecimento");
            return;
        }
        if (tempo.isEmpty()) {
            Toast("Digite um tempo de entrega");
            return;
        }
        if (categoria.isEmpty()) {
            Toast("Digite uma categoria para o estabelecimento");
            return;
        }

        Estabelecimento estabelecimento = new Estabelecimento();
        estabelecimento.setIdUsuario(UsuarioService.getIdUsuario());
        estabelecimento.setNome(nome);
        estabelecimento.setCategoria(categoria);
        estabelecimento.setPrecoEntrega(taxa);
        estabelecimento.setTempo(tempo);
        estabelecimento.setUrlImagem(urlImagemSelecionada);

        boolean estabelecimentoSalvo = new EstabelecimentoService().save(estabelecimento);
        if (estabelecimentoSalvo) {
            Toast("Informações do estabelecimento salvo com sucesso");
        } else {
            Toast("Falha ao salvar dados do estabelecimento");
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap imagem = null;

            try {
                switch (requestCode) {
                    case SELECAO_GALERIA:
                        Uri localImagem = data.getData();
                        imagem = MediaStore.Images
                                .Media
                                .getBitmap(
                                   getContentResolver(),
                                   localImagem
                                );
                        break;
                }
                salvarImagem(imagem, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Método responsável por salvar uma imagem no FireStoew
     *
     * @param data Intent de retorno com imagem da galeria
     * @param imagem Imagem a ser salva
     * @author Felipe Carvalho Funck
     */
    public void salvarImagem(Bitmap imagem, Intent data) {
        if (imagem != null) {
            imagemPerfil.setImageBitmap(imagem);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            byte[] dadosImagem = baos.toByteArray();

            StorageReference imagemRef = storageReference
                    .child("imagens")
                    .child("empresas")
                    .child(UsuarioService.getIdUsuario() + ".jpeg");

            UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
            uploadTask.addOnFailureListener(new OnFailureListener() {

                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast("Erro ao fazer upload da imagem");
                }

            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast("Imagem atualizada!");
                }

            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    Task<Uri> uriTask = task.getResult().getStorage().getDownloadUrl();
                    while(!uriTask.isComplete());
                    urlImagemSelecionada = uriTask.getResult().toString();
                }
            });
        }
    }

    /**
     * Método resposavel por inicializar os componentes do layout
     *
     * @author Felipe Carvalho Funck
     */
    private void inicializarComponentes() {
        edtNomeEmpresa = findViewById(R.id.edtNomeEstabelecimento);
        edtCategoriaEmpresa = findViewById(R.id.edtCategoriaEstabelecimento);
        edtTaxaEntrega = findViewById(R.id.edtValorEntrega);
        edtTempoEntrega = findViewById(R.id.edtTempoEntrega);
        imagemPerfil = findViewById(R.id.imagePerfilEmpresaId);
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
