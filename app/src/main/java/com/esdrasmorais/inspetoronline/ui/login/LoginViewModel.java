package com.esdrasmorais.inspetoronline.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.util.Patterns;

import com.esdrasmorais.inspetoronline.data.repository.LoginRepository;
import com.esdrasmorais.inspetoronline.data.Result;
import com.esdrasmorais.inspetoronline.data.model.Login;
import com.esdrasmorais.inspetoronline.R;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        Result<Login> result = loginRepository.login(username, password);

        if (result instanceof Result.Success) {
            Login data = ((Result.Success<Login>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(String username, String password) {
        Boolean isValid = false;
        try {
            username = username.replace("\"", "");
            Long phone = Long.parseLong(username);
            if (username.length() == 13 && phone > 0)
                isValid = true;
        } catch (Exception ex) {
            isValid = false;
        }

        if (!isValid && !isUserNameValid(username)) {
            loginFormState.setValue(
                new LoginFormState(R.string.invalid_username, null)
            );
        } else if (!isValid && !isPasswordValid(password)) {
            loginFormState.setValue(
                new LoginFormState(
                    null, R.string.invalid_password
                )
            );
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty()
                && username.trim().length() == 13;
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}