package edu.wit.mobileapp.pulsemonitoringjava.ui.login;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import edu.wit.mobileapp.pulsemonitoringjava.R;

public class SignUpActivity extends AppCompatActivity {

    private AuthenticateViewModel authenticateViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ViewModelProvider viewModelProvider = new ViewModelProvider(this, new AuthenticateViewModelFactory());
        authenticateViewModel = viewModelProvider.get(AuthenticateViewModel.class);


        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final EditText confirmpasswordEditText = findViewById(R.id.confirm_password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        authenticateViewModel.getLoginFormState().observe(this, new Observer<AuthenticateFormState>() {
            @Override
            public void onChanged(@Nullable AuthenticateFormState authenticateFormState) {
                if (authenticateFormState == null) {
                    return;
                }
                loginButton.setEnabled(authenticateFormState.isDataValid());
                if (authenticateFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(authenticateFormState.getUsernameError()));
                }
                if (authenticateFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(authenticateFormState.getPasswordError()));
                }
            }
        });

        authenticateViewModel.getLoginResult().observe(this, new Observer<AuthenticateResult>() {
            @Override
            public void onChanged(@Nullable AuthenticateResult authenticateResult) {
                if (authenticateResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (authenticateResult.getError() != null) {
                    showLoginFailed(authenticateResult.getError());
                }
                if (authenticateResult.getSuccess() != null) {
                    updateUiWithUser(authenticateResult.getSuccess());
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
                authenticateViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    authenticateViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                authenticateViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
