package com.fvf.ivoohcliente.Service;

import android.support.annotation.NonNull;

import com.fvf.ivoohcliente.helper.FirebaseConfig;
import com.fvf.ivoohcliente.model.Estabelecimento;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class EstabelecimentoService {

    /**
     * Interface funcional para retornar um estabelecimento
     *
     * @author Felipe Carvalho Funck
     */
    public interface Callback {
        void getEstabelecimento(Estabelecimento estabelecimento);
    }

    private DatabaseReference getFireBaseReference() {
        return FirebaseConfig.getFirebase().child("empresas");
    }

    public boolean save(Estabelecimento estabelecimento) {
        try {
            getFireBaseReference()
                    .child(UsuarioService.getIdUsuario())
                    .setValue(estabelecimento);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void getEstabelecimento(Callback callback) {
        getFireBaseReference()
                .child(UsuarioService.getIdUsuario())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            callback.getEstabelecimento(dataSnapshot.getValue(Estabelecimento.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        callback.getEstabelecimento(null);
                    }
                });
    }

}
