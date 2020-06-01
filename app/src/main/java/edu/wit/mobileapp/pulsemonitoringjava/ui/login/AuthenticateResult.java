package edu.wit.mobileapp.pulsemonitoringjava.ui.login;

import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */
class AuthenticateResult {
    @Nullable
    private LoggedInUserView success;
    @Nullable
    private Integer error;

    AuthenticateResult(@Nullable Integer error) {
        this.error = error;
    }

    AuthenticateResult(@Nullable LoggedInUserView success) {
        this.success = success;
    }

    @Nullable
    LoggedInUserView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
