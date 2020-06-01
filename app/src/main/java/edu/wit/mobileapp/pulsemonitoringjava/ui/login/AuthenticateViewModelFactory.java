package edu.wit.mobileapp.pulsemonitoringjava.ui.login;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import edu.wit.mobileapp.pulsemonitoringjava.data.AuthenticateDataSource;
import edu.wit.mobileapp.pulsemonitoringjava.data.AuthenticateRepository;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class AuthenticateViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AuthenticateViewModel.class)) {
            return (T) new AuthenticateViewModel(AuthenticateRepository.getInstance(new AuthenticateDataSource()));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
