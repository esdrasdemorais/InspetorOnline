package com.esdrasmorais.inspetoronline.data;

import android.util.Log;

import com.esdrasmorais.inspetoronline.data.model.Login;
import com.esdrasmorais.inspetoronline.data.repository.LoginRepository;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.List;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource extends LoginRepository {

    private List<Login> login = null;

    public LoginDataSource(Class<Login> clas) {
        super(clas);
    }

    public Task<QuerySnapshot> callLogin(String username, String password) {
        try {
            //java.util.UUID.randomUUID().toString()
            CollectionReference citiesRef = db.collection("Login");
            Query query = citiesRef.whereEqualTo(
                "phone",
                 Long.parseLong(username.replace("+", ""))
            );
            Task<QuerySnapshot> task = query.get();
            return task;
        } catch (Exception e) {
            return null;//return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
    }

}