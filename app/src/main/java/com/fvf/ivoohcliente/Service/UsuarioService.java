package com.fvf.ivoohcliente.Service;

import android.support.annotation.NonNull;

import com.fvf.ivoohcliente.helper.FirebaseConfig;
import com.fvf.ivoohcliente.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Classe service para Usuario
 *
 * @author Felipe Carvalho Funck
 */
public class UsuarioService {

    /**
     * Interface funcional para retornar um usuario
     *
     * @author Felipe Carvalho Funck
     */
    public interface Callback {
        void getUsuario(Usuario usuario);
    }

    private DatabaseReference getFireBaseReference() {
        return FirebaseConfig.getFirebase().child("usuarios");
    }

    public static String getIdUsuario() {
        return FirebaseConfig.getFirebaseAuth().getCurrentUser().getUid();
    }

    public boolean save(Usuario usuario) {
        try {
            getFireBaseReference()
                    .child(getIdUsuario())
                    .setValue(usuario);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void getUsuarioLogado(Callback callback) {
        getFireBaseReference()
                .child(getIdUsuario())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            callback.getUsuario(dataSnapshot.getValue(Usuario.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        callback.getUsuario(null);
                    }
                });
    }

}
