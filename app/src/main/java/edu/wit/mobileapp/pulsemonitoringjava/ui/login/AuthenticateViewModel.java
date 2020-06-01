package edu.wit.mobileapp.pulsemonitoringjava.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import edu.wit.mobileapp.pulsemonitoringjava.data.AuthenticateRepository;
import edu.wit.mobileapp.pulsemonitoringjava.data.Result;
import edu.wit.mobileapp.pulsemonitoringjava.data.model.LoggedInUser;
import edu.wit.mobileapp.pulsemonitoringjava.R;

public class AuthenticateViewModel extends ViewModel {

    private MutableLiveData<AuthenticateFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<AuthenticateResult> loginResult = new MutableLiveData<>();
    private AuthenticateRepository authenticateRepository;

    AuthenticateViewModel(AuthenticateRepository authenticateRepository) {
        this.authenticateRepository = authenticateRepository;
    }

    LiveData<AuthenticateFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<AuthenticateResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = authenticateRepository.login(username, password);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new AuthenticateResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            loginResult.setValue(new AuthenticateResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new AuthenticateFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new AuthenticateFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new AuthenticateFormState(true));
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
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}
