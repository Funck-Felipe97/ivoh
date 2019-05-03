package com.fvf.ivoohcliente.Service;

import android.support.annotation.NonNull;

import com.fvf.ivoohcliente.helper.FirebaseConfig;
import com.fvf.ivoohcliente.model.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProdutoService {

    /**
     * Interface funcional para retornar produtos
     *
     * @author Felipe Carvalho Funck
     */
    public interface Callback {
        void getProdutos(List<Produto> produtos);
    }

    private DatabaseReference getFireBaseReference() {
        return FirebaseConfig.getFirebase().child("produtos");
    }

    public boolean save(Produto produto) {
        try {
            getFireBaseReference()
                    .child(UsuarioService.getIdUsuario())
                    .push()
                    .setValue(produto);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Produto produto) {
        try {
            getFireBaseReference()
                    .child(UsuarioService.getIdUsuario())
                    .child(produto.getIdProduto())
                    .setValue(produto);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void getProdutosByEmpresa(Callback callback) {
        List<Produto> produtos = new ArrayList<>();
        getFireBaseReference()
                .child(UsuarioService.getIdUsuario())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot ds: dataSnapshot.getChildren()) {
                                Produto produto = ds.getValue(Produto.class);
                                produto.setIdProduto(ds.getKey());
                                produtos.add(produto);
                            }
                            callback.getProdutos(produtos);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

}
