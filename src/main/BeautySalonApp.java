package main;

import entity.User;
import service.ServiceRegistry;
import ui.login.LoginCallback;
import ui.login.LoginGUI;
import ui.login.LogoutCallback;

public class BeautySalonApp implements LoginCallback, LogoutCallback {
    ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();

    public BeautySalonApp() {
        serviceRegistry.loadData();
    }

    public void login(){
        LoginGUI loginGUI = new LoginGUI(this);
        loginGUI.setVisible(true);
    }

    @Override
    public void onLoginSuccess(User loggedInUser) {
        loggedInUser.showGUI(this);
    }

    @Override
    public void onLogout() {
        login();
    }

    public void exit(){
        System.out.println("Bye!");
        serviceRegistry.saveData();
    }

	public static void main(String[] args) {
        BeautySalonApp app = new BeautySalonApp();
        app.login();
	}

}
