package com.esdrasmorais.inspetoronline.data;

import com.esdrasmorais.inspetoronline.data.model.Login;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    Login user;
    public LoginDataSource(Login login) {
        user = login;
    }

    public Result<Login> login() {
        try {
//                new Login(
//                    java.util.UUID.randomUUID().toString(),
//                    "Jane Doe");
            return new Result.Success<>(user);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
    }
}