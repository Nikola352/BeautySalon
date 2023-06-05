package ui;

import entity.User;

public interface LoginCallback {
    void onLoginSuccess(User loggedInUser);
}
