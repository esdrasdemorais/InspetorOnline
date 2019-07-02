package com.esdrasmorais.inspetoronline.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;

import com.esdrasmorais.inspetoronline.data.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Repository<T> implements IRepository<T> {
    private static final String TAG = "Repository";
    protected FirebaseFirestore db;
    protected String className;
    protected Class<T> clas = null;
    protected ObservableArrayList<T> objects;

    public Repository() {}

    public Repository(Class<T> clas) {
        this.clas = clas;
        this.className = this.setClassName();
        this.db = FirebaseFirestore.getInstance();
        this.objects = new ObservableArrayList<T>();
        this.search(objects);
    }

    protected String setClassName()
    {
//        return ((ParameterizedType) getClass().getGenericSuperclass())
//            .getActualTypeArguments()[0].toString();
        return clas.getSimpleName();
    }

    public Boolean set(T object) {
        try {
            db.collection(
                object.getClass().getSimpleName()
            ).document().set(object);
            return true;
        } catch (Exception ex) {
            Log.e(
                "Repository<" + object.getClass().getName() + ">",
                ex.getMessage()
            );
        }
        return false;
    }

    public void search(List<T> result) {
        //objects = result;
        db.collection(className).get().addOnCompleteListener(
            new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        try {
                            for (QueryDocumentSnapshot d : task.getResult()) {
                                objects.add(d.toObject(clas));
                            }
                            return;
                        } catch (Exception ex) {
                            Log.e(TAG, "Error documents: " +
                                ex.getMessage());
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ",
                            task.getException());
                    }
                }
            }
        );
    }

    public List<T> get() {
        return this.objects;
    }

    public Boolean update(T object) {
        try {
            return true;
        } catch (Exception ex) {
            Log.e(
                "Repository<" + object.getClass().getName() + ">",
                ex.getMessage()
            );
        }
        return false;
    }

    public Boolean delete(T object) {
        return false;
    }

    public T getById(Long id) {
        return null;
    }
}