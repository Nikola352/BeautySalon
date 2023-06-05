package ui;

import entity.User;
import net.miginfocom.swing.MigLayout;
import service.ClientService;
import service.CosmetologistService;
import service.ManagerService;
import service.ReceptionistService;
import service.ServiceRegistry;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LoginGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

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

        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    private void initializeComponents() {
        usernameField = new JTextField(12);
        passwordField = new JPasswordField(12);
        loginButton = new JButton("Ulogujte se");
        registerButton = new JButton("Registracija");
    }

    private void setupLayout() {
        JPanel panel = new JPanel();
        
        panel.setLayout(new MigLayout("align 50% 50%"));

        panel.add(new JLabel("Korisničko ime:"), "align label");
        panel.add(usernameField, "wrap");

        panel.add(new JLabel("Lozinka:"), "align label");
        panel.add(passwordField, "wrap");

        panel.add(loginButton, "span 2, split 2, align center, sizegroup btn");
        panel.add(registerButton, "align center, sizegroup btn");

        getContentPane().add(panel);
    }

    private void setupListeners() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if(username.trim().isEmpty() || password.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(LoginGUI.this, "Morate uneti korisničko ime i lozinku.", "Greška", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                User loggedInUser = authenticateUser(username, password);
                if (loggedInUser != null) {
                    dispose();
                    loginCallback.onLoginSuccess(loggedInUser);
                } else {
                    JOptionPane.showMessageDialog(LoginGUI.this, "Pogrešno korisničko ime ili lozinka.", "Greška", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new RegisterGUI(loginCallback).setVisible(true);
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
