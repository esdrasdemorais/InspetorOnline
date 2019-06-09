package com.esdrasmorais.inspetoronline.data.repository;

import android.util.Log;

import com.esdrasmorais.inspetoronline.data.Constants;
import com.esdrasmorais.inspetoronline.data.model.Inspection;
import com.google.firebase.firestore.FirebaseFirestore;

public abstract class Repository<T> implements IRepository<T> {
    protected FirebaseFirestore db;

    public Repository() {
        this.db = FirebaseFirestore.getInstance();
    }

    public Boolean set(T object) {
        try {
            db.collection(Constants.DATABASE_NAME).document(
                object.getClass().getName()
            ).set(object);
            return true;
        } catch (Exception ex) {
            Log.e(
                "Repository<" + object.getClass().getName() + ">",
                ex.getMessage()
            );
        }
        return false;
    }
}