package ui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;

import entity.Client;

public class ClientGUI extends GUI {
    private Client user;
    
    public ClientGUI(Client user, LogoutCallback logoutCallback) {
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
