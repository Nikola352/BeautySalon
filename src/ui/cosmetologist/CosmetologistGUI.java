package ui.cosmetologist;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;

import entity.Cosmetologist;
import ui.GUI;
import ui.login.LogoutCallback;

public class CosmetologistGUI extends GUI {
    private Cosmetologist user;

    public CosmetologistGUI(Cosmetologist user, LogoutCallback logoutCallback) {
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
