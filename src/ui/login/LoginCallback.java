package ui.login;

import entity.User;

public interface LoginCallback {
    void onLoginSuccess(User loggedInUser);
}
