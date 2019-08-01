package com.esdrasmorais.inspetoronline.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.esdrasmorais.inspetoronline.data.LoginDataSource;
import com.esdrasmorais.inspetoronline.data.Result;
import com.esdrasmorais.inspetoronline.data.model.Employee;
import com.esdrasmorais.inspetoronline.data.model.EmployeeType;
import com.esdrasmorais.inspetoronline.data.model.Login;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import javax.annotation.Nullable;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository
    extends Repository<Login>
    implements ILogin
{
    private static volatile LoginRepository instance;

    private LoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private Login user = null;

    private Employee employee = null;

    private Login login = new Login();

    public LoginRepository() {

    }

    public LoginRepository(Class<Login> clas) {
        super(clas);
    }

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setLoggedInUser(Login user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public interface ICallback {
        public void onCallback(Login login);
    }

    protected Task<QuerySnapshot> task = null;

    public Task<QuerySnapshot> getTask() {
        return task;
    }

    public void login(String username, String password) {
        task = dataSource.callLogin(username, password);
    }

    private void setCallback(ICallback callback, QuerySnapshot snapshot) {
        getLogin(callback, snapshot);
    }

    private void setEmployee(DocumentSnapshot snapshot) {
        employee = new Employee();

        String a = snapshot.get("address").toString();
        //employee.setAddress();

        employee.setLines(null);
        employee.setPassword(snapshot.get("password").toString());
        //employee.setType((EmployeeType) snapshot.get("type"));
        employee.setUsername(snapshot.get("username").toString());
        employee.setId(snapshot.get("id").toString());
    }

    private void getEmployee(final ICallback callback, QuerySnapshot snapshot) {
        DocumentReference employeeDocumentReference =
            snapshot.getDocuments().get(0).
                getDocumentReference("employee");

        EventListener<DocumentSnapshot> eventListener =
            new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(
                    @Nullable DocumentSnapshot documentSnapshot,
                    @Nullable FirebaseFirestoreException ex
                ) {
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        setEmployee(documentSnapshot);
                        callback.onCallback(login);
                    }
                }
            };

        employeeDocumentReference.addSnapshotListener(eventListener);
    }

    private void getLogin(ICallback callback, QuerySnapshot snapshot) {
        List<DocumentSnapshot> document = snapshot.getDocuments();
        login.setDisplayName(document.get(0).get("displayName").toString());
        login.setId(document.get(0).get("id").toString());
        login.setPhone(Long.parseLong(document.get(0).get("phone").toString()));
        getEmployee(callback, snapshot);
    }

    public void login(
        final ICallback callback, final Task<QuerySnapshot> task
    ) {
        task.addOnSuccessListener(
            new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot snapshot) {
                    try {
                        setCallback(callback, snapshot);
                    } catch (Exception ex) {
                        Log.e("LoginRepository", ex.getMessage());
                    }
                }
            }
        );
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception ex) {
                Log.e("LoginRepository", ex.getMessage());
            }
        });
    }

    @Override
    public Result<Login> getByUsername(String username) {
//        if (result instanceof Result.Success) {
//            setLoggedInUser(((Result.Success<Login>) result).getData());
//        }
        return new Result.Success<>(user);
    }
}