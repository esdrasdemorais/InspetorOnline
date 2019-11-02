package com.esdrasmorais.inspetoronline.ui.login;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.esdrasmorais.inspetoronline.R;
import com.esdrasmorais.inspetoronline.data.AppDatabase;
import com.esdrasmorais.inspetoronline.data.AppExecutors;
import com.esdrasmorais.inspetoronline.data.BasicApp;
import com.esdrasmorais.inspetoronline.data.DataRepository;
import com.esdrasmorais.inspetoronline.data.model.Company;
import com.esdrasmorais.inspetoronline.ui.TaskManagerActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LoginActivity
    extends AppCompatActivity
    implements View.OnClickListener
{
    private LoginViewModel loginViewModel;

    private FirebaseAuth firebaseAuth;
    private EditText usernameEditText;
    Boolean verificationInProgress = false;
    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    FirebaseUser currentUser = null;
    ProgressBar loadingProgressBar;

    private static final String KEY_VERIFY_IN_PROGRESS =
        "key_verify_in_progress";

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;

    private static final String TAG = "LoginActivity";

    @NonNull
    private Application application;

    @NonNull
    private DataRepository dataReposity;

    private EditText verificationField;
    private Button loginButton;
    private Button verifyButton;
    private Button resendButton;
    private TextView detailText;
    private EditText passwordEditText;

    public LiveData<List<Company>> companies;

    public LoginActivity() {

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            updateUI(STATE_SIGNIN_SUCCESS, user);
                        } else {
                            Log.w(TAG, "signInWithCredential:failure",
                                task.getException());
                            if (task.getException() instanceof
                                FirebaseAuthInvalidCredentialsException
                            ) {
                                verificationField.setError("Invalid code.");
                            }
                            updateUI(STATE_SIGNIN_FAILED);
                        }
                    }
                }
            );
    }

    private void startDb() {
        //BasicApp basicApp = new BasicApp(getApplicationContext());
        //basicApp.getDatabase();
        this.application = (Application) this.getApplicationContext();
        this.dataReposity = ((BasicApp) application).getRepository();
    }

    private List<Company> companiesList = new ArrayList<Company>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        startDb();
        this.dataReposity.getCompanies().
            observe(this, new Observer<List<Company>>() {
                @Override
                public void onChanged(@Nullable List<Company> companies) {
                    if (companies != null) {
                        companiesList = companies;
                    }
                }
            }
        );

        if (currentUser != null) return;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginViewModel = ViewModelProviders.of(
            this, new LoginViewModelFactory()
        ).get(LoginViewModel.class);

        firebaseAuth = FirebaseAuth.getInstance();

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);

        verifyButton = findViewById(R.id.verify);
        resendButton = findViewById(R.id.resend);
        verificationField = findViewById(R.id.verification);
        detailText = findViewById(R.id.detail);

        loginViewModel.getLoginFormState().observe(
            this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(
                        getString(loginFormState.getUsernameError())
                    );
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(
                        getString(loginFormState.getPasswordError())
                    );
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
            if (loginResult == null) {
                return;
            }
            loadingProgressBar.setVisibility(View.GONE);
            if (loginResult.getError() != null) {
                showLoginFailed(loginResult.getError());
            }
            if (loginResult.getSuccess() != null) {
                updateUiWithUser(loginResult.getSuccess());
                launchMain();
            }
            setResult(Activity.RESULT_OK);

            //Complete and destroy login activity once successful
            finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(
                    usernameEditText.getText().toString(),
                    passwordEditText.getText().toString()
                );
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(
            new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
                }
                return false;
                }
            }
        );

//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            loadingProgressBar.setVisibility(View.VISIBLE);
//            loginViewModel.login(usernameEditText.getText().toString(),
//                passwordEditText.getText().toString());
//            }
//        });
        loginButton.setOnClickListener(this);
        verifyButton.setOnClickListener(this);
        resendButton.setOnClickListener(this);

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);
                verificationInProgress = false;
                updateUI(STATE_VERIFY_SUCCESS, phoneAuthCredential);
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                verificationInProgress = false;
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    usernameEditText.setError("Invalid phone number.");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Snackbar.make(findViewById(android.R.id.content),
                    "Quota exceeded.", Snackbar.LENGTH_SHORT).show();
                }
                updateUI(STATE_VERIFY_FAILED);
            }

            @Override
            public void onCodeSent(
                String mVerificationId,
                PhoneAuthProvider.ForceResendingToken token
            ) {
                verificationId = mVerificationId;
                resendToken = token;

                Log.d(TAG, "onCodeSent:" + verificationId);

                updateUI(STATE_CODE_SENT);
            }
        };
    }

    private void launchMain() {
        Intent intent = new Intent(this, TaskManagerActivity.class);
        startActivity(intent);
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        //@TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void signOut() {
        firebaseAuth.signOut();
        updateUI(STATE_INITIALIZED);
    }

    private void updateUI(int uiState) {
        updateUI(uiState, firebaseAuth.getCurrentUser(), null);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            updateUI(STATE_SIGNIN_SUCCESS, user);
        } else {
            currentUser = firebaseAuth.getCurrentUser();
            updateUI(STATE_INITIALIZED);
        }
    }

    private void updateUI(int uiState, FirebaseUser user) {
        updateUI(uiState, user, null);
    }

    private void updateUI(int uiState, PhoneAuthCredential cred) {
        updateUI(uiState, null, cred);
    }

    private void enableViews(View... views) {
        for (View v : views) {
            v.setEnabled(true);
            v.setVisibility(View.VISIBLE);
        }
    }

    private void disableViews(View... views) {
        for (View v : views) {
            v.setEnabled(false);
            v.setVisibility(View.INVISIBLE);
        }
    }

    private void updateUI(
        Integer uiState, FirebaseUser user, PhoneAuthCredential cred
    ) {
        switch (uiState) {
            case STATE_INITIALIZED:
                // Initialized state, show only the phone number field and start button
                enableViews(loginButton, usernameEditText);
                disableViews(verifyButton, resendButton,
                    passwordEditText, verificationField);
                detailText.setText(null);
                break;
            case STATE_CODE_SENT:
                // Code sent state, show the verification field, the
                loadingProgressBar.setVisibility(View.GONE);
                enableViews(verifyButton, resendButton, usernameEditText,
                    verificationField);
                disableViews(loginButton, passwordEditText);
                detailText.setText(R.string.status_code_sent);
                break;
            case STATE_VERIFY_FAILED:
                // Verification has failed, show all options
                enableViews(loginButton, verifyButton, resendButton,
                    usernameEditText, verificationField);
                detailText.setText(R.string.status_verification_failed);
                break;
            case STATE_VERIFY_SUCCESS:
                // Verification has succeeded, proceed to firebase sign in
                loadingProgressBar.setVisibility(View.GONE);
                disableViews(loginButton, verifyButton, resendButton,
                    usernameEditText, verificationField, passwordEditText);
                detailText.setText(R.string.status_verification_succeeded);

                if (cred != null) {
                    if (cred.getSmsCode() != null) {
                        verificationField.setText(cred.getSmsCode());
                    } else {
                        verificationField.setText(R.string.instant_validation);
                    }
                }

                break;
            case STATE_SIGNIN_FAILED:
                loadingProgressBar.setVisibility(View.GONE);
                detailText.setText(R.string.status_sign_in_failed);
                break;
            case STATE_SIGNIN_SUCCESS:
                loadingProgressBar.setVisibility(View.GONE);
                loginViewModel.login(
                    usernameEditText.getText().toString(),
                    verificationField.getText().toString()
                );
                break;
        }

        if (user == null) {
            // Signed out
//            mPhoneNumberViews.setVisibility(View.VISIBLE);
//            mSignedInViews.setVisibility(View.GONE);
//
//            mStatusText.setText(R.string.signed_out);
        } else {
            // Signed in
//            mPhoneNumberViews.setVisibility(View.GONE);
//            mSignedInViews.setVisibility(View.VISIBLE);

//            enableViews(usernameEditText, verificationField);

//            mPhoneNumberField.setText(null);
//            mVerificationField.setText(null);
//
//            mStatusText.setText(R.string.signed_in);
            //detailText.setText(getString(R.string.firebase_status_fmt, user.getUid()));
        }
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+" + phoneNumber.replace("\"", ""), // Phone number to verify
            77,              // Timeout duration
            TimeUnit.SECONDS,   // Unit of timeout
            this,       // Activity (for callback binding)
            callbacks           // OnVerificationStateChangedCallbacks
        );
        verificationInProgress = true;
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = usernameEditText.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            usernameEditText.setError("Invalid phone number.");
            return false;
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        updateUI(currentUser);

        if (verificationInProgress && validatePhoneNumber()) {
            startPhoneNumberVerification(usernameEditText.getText().toString());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, verificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        verificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(
            verificationId, code
        );
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(
        String phoneNumber,
        PhoneAuthProvider.ForceResendingToken token
    ) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+" + phoneNumber.replace("\"", ""),        // Phone number to verify
            77,              // Timeout duration
            TimeUnit.SECONDS,   // Unit of timeout
            this,       // Activity (for callback binding)
            callbacks,         // OnVerificationStateChangedCallbacks
            token              // ForceResendingToken from callbacks
        );
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                if (!validatePhoneNumber()) {
                    return;
                }
                startPhoneNumberVerification(
                    usernameEditText.getText().toString()
                );
                break;
            case R.id.verify:
                loadingProgressBar.setVisibility(View.VISIBLE);
                String code = verificationField.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    verificationField.setError("Cannot be empty.");
                    return;
                }
                if (verificationId != null && verificationId.length() > 0 &&
                    code != null && code.length() > 0
                )
                    verifyPhoneNumberWithCode(verificationId, code);
                break;
            case R.id.resend:
                loadingProgressBar.setVisibility(View.VISIBLE);
                resendVerificationCode(
                    usernameEditText.getText().toString(), resendToken
                );
                break;
//            case R.id.signOutButton:
            default:
                signOut();
                break;
        }
    }
}