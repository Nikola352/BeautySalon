package ui;

import entity.User;
import service.ClientService;
import service.CosmetologistService;
import service.ManagerService;
import service.ReceptionistService;
import service.ServiceRegistry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LoginGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    private LoginCallback loginCallback;
    private ClientService clientService;
    private CosmetologistService cosmetologistService;
    private ReceptionistService receptionistService;
    private ManagerService managerService;

    public LoginGUI(LoginCallback loginCallback) {
        ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();
        clientService = serviceRegistry.getClientService();
        cosmetologistService = serviceRegistry.getCosmetologistService();
        receptionistService = serviceRegistry.getReceptionistService();
        managerService = serviceRegistry.getManagerService();

        this.loginCallback = loginCallback;

        initializeComponents();
        setupLayout();
        setupListeners();
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ServiceRegistry.getInstance().saveData();
            }
        });
    }

    private void initializeComponents() {
        usernameField = new JTextField(10);
        passwordField = new JPasswordField(10);
        loginButton = new JButton("Ulogujte se");
    }

    private void setupLayout() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        panel.add(new JLabel("Korisničko ime:"));
        panel.add(usernameField);
        panel.add(new JLabel("Lozinka:"));
        panel.add(passwordField);
        panel.add(new JLabel());
        panel.add(loginButton);

        getContentPane().add(panel);
    }

    private void setupListeners() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if(username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginGUI.this, "Morate uneti korisničko ime i lozinku.");
                    return;
                }
                User loggedInUser = authenticateUser(username, password);
                if (loggedInUser != null) {
                    dispose();
                    loginCallback.onLoginSuccess(loggedInUser);
                } else {
                    JOptionPane.showMessageDialog(LoginGUI.this, "Pogrešno korisničko ime ili lozinka.");
                }
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ServiceRegistry.getInstance().saveData();
                dispose();
            }
        });
    }

    private User authenticateUser(String username, String password) {
        User user = clientService.getByUsername(username);
        if(user == null) {
            user = cosmetologistService.getByUsername(username);
        } 
        if(user == null) {
            user = receptionistService.getByUsername(username);
        }
        if(user == null) {
            user = managerService.getByUsername(username);
        }
        if(user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
