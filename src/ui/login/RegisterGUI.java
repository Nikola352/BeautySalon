package ui.login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import entity.Client;
import entity.Gender;
import net.miginfocom.swing.MigLayout;
import service.ClientService;
import service.ServiceRegistry;

public class RegisterGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JComboBox<String> genderComboBox;
    private JTextField phoneNumberField;
    private JTextField addressField;
    private JButton registerButton;
    private JButton loginButton;

    private LoginCallback loginCallback;

    private ClientService clientService;
    
    public RegisterGUI(LoginCallback loginCallback) {
        this.loginCallback = loginCallback;
        clientService = ServiceRegistry.getInstance().getClientService();

        setTitle("Registracija");
        setSize(300, 360);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    private void initializeComponents(){
        usernameField = new JTextField(12);
        passwordField = new JPasswordField(12);
        firstNameField = new JTextField(12);
        lastNameField = new JTextField(12);
        genderComboBox = new JComboBox<String>(new String[]{"-- Pol --", "Muški", "Ženski"});
        phoneNumberField = new JTextField(12);
        addressField = new JTextField(12);
        registerButton = new JButton("Registrujte se");
        loginButton = new JButton("Prijava");
    }

    private void setupLayout(){
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("align 50% 50%"));

        panel.add(new JLabel("Registracija"), "span, align center");

        panel.add(new JLabel("Korisničko ime:"), "align label");
        panel.add(usernameField, "wrap");

        panel.add(new JLabel("Lozinka:"), "align label");
        panel.add(passwordField, "wrap");

        panel.add(new JLabel("Ime:"), "align label");
        panel.add(firstNameField, "wrap");

        panel.add(new JLabel("Prezime:"), "align label");
        panel.add(lastNameField, "wrap");

        panel.add(new JLabel("Pol:"), "align label");
        panel.add(genderComboBox, "wrap");

        panel.add(new JLabel("Broj telefona:"), "align label");
        panel.add(phoneNumberField, "wrap");

        panel.add(new JLabel("Adresa:"), "align label");
        panel.add(addressField, "wrap");

        panel.add(registerButton, "span 2, split 2, align center, sizegroup btn");
        panel.add(loginButton, "align center, sizegroup btn");

        getContentPane().add(panel);
    }

    private void setupListeners(){
        loginButton.addActionListener(e -> {
            new LoginGUI(loginCallback).setVisible(true);
            dispose();
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String gender = (String) genderComboBox.getSelectedItem();
                String phoneNumber = phoneNumberField.getText();
                String address = addressField.getText();

                if(validateInput(username, password, firstName, lastName, gender, phoneNumber, address)){
                    Gender gender2 = (gender == "Muški" ? Gender.MALE : Gender.FEMALE);
                    Client user = clientService.add(username, password, firstName, lastName, gender2, phoneNumber, address);
                    JOptionPane.showMessageDialog(RegisterGUI.this, "Uspešno ste se registrovali.", "Uspešno", JOptionPane.INFORMATION_MESSAGE);
                    loginCallback.onLoginSuccess(user);
                    dispose();
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

    private boolean validateInput(String username, String password, String firstName, String lastName, String gender, String phoneNumber, String address){
        if(username.trim().isEmpty()){
            JOptionPane.showMessageDialog(RegisterGUI.this, "Korisničko ime je obavezno.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(clientService.getByUsername(username) != null){
            JOptionPane.showMessageDialog(RegisterGUI.this, "Korisničko ime je zauzeto.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(password.trim().isEmpty()){
            JOptionPane.showMessageDialog(RegisterGUI.this, "Lozinka je obavezna.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(password.length() < 8){
            JOptionPane.showMessageDialog(RegisterGUI.this, "Lozinka mora imati najmanje 8 karaktera.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(firstName.trim().isEmpty()){
            JOptionPane.showMessageDialog(RegisterGUI.this, "Ime je obavezno.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(lastName.trim().isEmpty()){
            JOptionPane.showMessageDialog(RegisterGUI.this, "Prezime je obavezno.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(gender.equals("-- Pol --")){
            JOptionPane.showMessageDialog(RegisterGUI.this, "Pol je obavezan.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(phoneNumber.trim().isEmpty()){
            JOptionPane.showMessageDialog(RegisterGUI.this, "Broj telefona je obavezan.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(!phoneNumber.matches("[0-9]+")){
            JOptionPane.showMessageDialog(RegisterGUI.this, "Broj telefona može sadržati samo cifre.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(address.trim().isEmpty()){
            JOptionPane.showMessageDialog(RegisterGUI.this, "Adresa je obavezna.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

}
