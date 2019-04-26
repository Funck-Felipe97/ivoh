package com.fvf.ivoohcliente.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseConfig {

    private static DatabaseReference referenciaFirebase;
    private static FirebaseAuth referenciaAutenticacao;
    private static StorageReference referenciaStorage;


    /**
     * Método responsavél por retornar a referência do firebase
     *
     * @return Referência do FirebaseDatabase
     * @author Felipe Carvalho Funck
     */
    public static DatabaseReference getFirebase() {
        if (referenciaFirebase == null) {
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }
        return referenciaFirebase;
    }

    /**
     * Método responsavél por retornar a referência do FirebaseAuth
     *
     * @return Referência do FirebaseAuth
     * @author Felipe Carvalho Funck
     */
    public static FirebaseAuth getFirebaseAuth() {
        if (referenciaAutenticacao == null) {
            referenciaAutenticacao = FirebaseAuth.getInstance();
        }
        return referenciaAutenticacao;
    }

    /**
     * Método responsavél por retornar a referência do storage
     *
     * @return Referência do FirebaseStorage
     * @author Felipe Carvalho Funck
     */
    public static StorageReference getFirebaseStorage() {
        if (referenciaStorage == null) {
            referenciaStorage = FirebaseStorage.getInstance().getReference();
        }
        return referenciaStorage;
    }

}
