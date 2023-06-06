package ui.manager;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;

import entity.Manager;
import ui.GUI;
import ui.login.LogoutCallback;

public class ManagerGUI extends GUI {
    private Manager user;

    public ManagerGUI(Manager user, LogoutCallback logoutCallback) {
        super(logoutCallback);
        this.user = user;
        setTitle("Client");
        initializeComponents();
    }

    private void initializeComponents() {
        JLabel usernameLabel = new JLabel("Username: " + user.getUsername());
        usernameLabel.setHorizontalAlignment(JLabel.CENTER);
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {logout();}); 

        this.add(usernameLabel, BorderLayout.CENTER);
        this.add(logoutButton, BorderLayout.SOUTH);
    }

}
