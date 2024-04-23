package main;

import entity.User;
import service.ServiceRegistry;
import ui.login.LoginCallback;
import ui.login.LoginGUI;
import ui.login.LogoutCallback;

import com.formdev.flatlaf.FlatIntelliJLaf;
import javax.swing.*;


public class BeautySalonApp implements LoginCallback, LogoutCallback {
    private ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();

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

	public static void main(String[] args) {
		try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch(UnsupportedLookAndFeelException e) {
            System.err.println("Failed to initialize theme");
        }
		
        BeautySalonApp app = new BeautySalonApp();
        app.login();
	}

}
