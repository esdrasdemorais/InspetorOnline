package com.esdrasmorais.inspetoronline.data.repository;

import com.esdrasmorais.inspetoronline.data.LoginDataSource;
import com.esdrasmorais.inspetoronline.data.Result;
import com.esdrasmorais.inspetoronline.data.model.Login;

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
        if(instance == null){
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

    public Result<Login> login(String username, String password) {
        // handle login
        Result<Login> result = dataSource.login();
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<Login>) result).getData());
        }
        return result;
    }
}