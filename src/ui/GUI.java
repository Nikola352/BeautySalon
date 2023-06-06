package ui;

import javax.swing.JFrame;

import service.ServiceRegistry;
import ui.login.LogoutCallback;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class GUI extends JFrame {
    private LogoutCallback logoutCallback;

    public GUI(LogoutCallback logoutCallback) {
        this.logoutCallback = logoutCallback;
        setSize(600, 600);
        setLocationRelativeTo(null);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ServiceRegistry.getInstance().saveData();
                dispose();
            }
        });
    }

    protected void logout() {
        logoutCallback.onLogout();
        dispose();
    }
}
