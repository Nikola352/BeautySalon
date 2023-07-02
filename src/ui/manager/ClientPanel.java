package ui.manager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import entity.Client;
import entity.Gender;
import entity.Manager;
import net.miginfocom.swing.MigLayout;
import service.ClientService;
import service.ServiceRegistry;
import ui.BooleanTableCellRenderer;
import ui.manager.model.ClientTableModel;

public class ClientPanel extends JPanel {
    Manager user;

    private ServiceRegistry serviceRegistry;
    private ClientService clientService;

    boolean isEditing = false;
    Client clientToEdit = null;

    private JTable table;
    private TableRowSorter<AbstractTableModel> tableSorter = new TableRowSorter<AbstractTableModel>();
    private JButton addButton;
    private JButton editButton;
    private JButton removeButton;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JComboBox<Gender> genderComboBox;
    private JTextField phoneNumberField;
    private JTextField addressField;
    private JButton saveButton;
    private JButton cancelButton;

    public ClientPanel(Manager user) {
        this.user = user;
        serviceRegistry = ServiceRegistry.getInstance();
        clientService = serviceRegistry.getClientService();
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    private void initializeComponents(){
        ArrayList<Client> clients = clientService.getData();
        ClientTableModel clientTableModel = new ClientTableModel(clients);
        table = new JTable(clientTableModel);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        tableSorter.setModel(clientTableModel);
        table.setRowSorter(tableSorter);
        table.getColumnModel().getColumn(8).setCellRenderer(new BooleanTableCellRenderer());

        addButton = new JButton("Dodaj");
        editButton = new JButton("Izmeni");
        removeButton = new JButton("Ukloni");

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        firstNameField = new JTextField();
        lastNameField = new JTextField();
        genderComboBox = new JComboBox<Gender>(new Gender[]{null, Gender.MALE, Gender.FEMALE});
        phoneNumberField = new JTextField();
        addressField = new JTextField();
        saveButton = new JButton("Sačuvaj");
        cancelButton = new JButton("Otkaži");

        disableInputs();
        editButton.setEnabled(false);
        removeButton.setEnabled(false);
    }

    private void setupLayout(){
        setLayout(new MigLayout("wrap 2, center", "[center, grow]30[center, grow]", "[]30[bottom, grow]"));

        // ========= LEFT PANEL =========
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new MigLayout("wrap 1, center", "[center, grow]"));

        JScrollPane scrollPane = new JScrollPane(table);
        leftPanel.add(scrollPane, "grow, wrap, span 2");

        leftPanel.add(addButton, "grow, wrap");
        leftPanel.add(editButton, "grow, wrap");
        leftPanel.add(removeButton, "grow, wrap");

        add(leftPanel, "span 1 2, grow");

        // ========= RIGHT PANEL =========
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new MigLayout("wrap 1, center", "[center, grow]", "[]20[][][][][][][][][][][][][][]20[center, grow][center, grow]"));

        rightPanel.add(new JLabel("Podaci o korisniku"), "wrap, center");

        rightPanel.add(new JLabel("Korisničko ime"), "grow, wrap");
        rightPanel.add(usernameField, "grow, wrap");

        rightPanel.add(new JLabel("Lozinka"), "grow, wrap");
        rightPanel.add(passwordField, "grow, wrap");

        rightPanel.add(new JLabel("Ime"), "grow, wrap");
        rightPanel.add(firstNameField, "grow, wrap");

        rightPanel.add(new JLabel("Prezime"), "grow, wrap");
        rightPanel.add(lastNameField, "grow, wrap");

        rightPanel.add(new JLabel("Pol"), "grow, wrap");
        rightPanel.add(genderComboBox, "grow, wrap");

        rightPanel.add(new JLabel("Broj telefona"), "grow, wrap");
        rightPanel.add(phoneNumberField, "grow, wrap");

        rightPanel.add(new JLabel("Adresa"), "grow, wrap");
        rightPanel.add(addressField, "grow, wrap");

        rightPanel.add(saveButton, "growx, wrap");
        rightPanel.add(cancelButton, "growx, wrap");

        add(rightPanel, "span 1 2, grow");
    }

    private void enableInputs(){
        // enable right panel
        usernameField.setEnabled(true);
        passwordField.setEnabled(true);
        firstNameField.setEnabled(true);
        lastNameField.setEnabled(true);
        genderComboBox.setEnabled(true);
        phoneNumberField.setEnabled(true);
        addressField.setEnabled(true);
        saveButton.setEnabled(true);
        cancelButton.setEnabled(true);

        // disable left panel
        table.setEnabled(false);
        addButton.setEnabled(false);
        editButton.setEnabled(false);
        removeButton.setEnabled(false);
    }

    private void disableInputs(){
        // disable right panel
        usernameField.setEnabled(false);
        passwordField.setEnabled(false);
        firstNameField.setEnabled(false);
        lastNameField.setEnabled(false);
        genderComboBox.setEnabled(false);
        phoneNumberField.setEnabled(false);
        addressField.setEnabled(false);
        saveButton.setEnabled(false);
        cancelButton.setEnabled(false);

        // enable left panel
        table.setEnabled(true);
        addButton.setEnabled(true);
        editButton.setEnabled(true);
        removeButton.setEnabled(true);
    }

    private void clearInputs(){
        usernameField.setText("");
        passwordField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        genderComboBox.setSelectedItem(null);
        phoneNumberField.setText("");
        addressField.setText("");
    }

    private void fillInputs(Client client){
        usernameField.setText(client.getUsername());
        passwordField.setText(client.getPassword());
        firstNameField.setText(client.getName());
        lastNameField.setText(client.getLastname());
        genderComboBox.setSelectedItem(client.getGender());
        phoneNumberField.setText(client.getPhoneNum());
        addressField.setText(client.getAddress());
    }

    private void setupListeners(){
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isEditing = false;
                clearInputs();
                enableInputs();
                table.clearSelection();
                usernameField.requestFocus();
            }
        });

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int row = table.getSelectedRow();
                if(row == -1){
                    editButton.setEnabled(false);
                    removeButton.setEnabled(false);
                    return;
                }

                clientToEdit = ((ClientTableModel)table.getModel()).getClient(row);

                editButton.setEnabled(true);
                removeButton.setEnabled(true);
                fillInputs(clientToEdit);
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isEditing = true;
                enableInputs();
                usernameField.requestFocus();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if(row == -1){
                    JOptionPane.showMessageDialog(ClientPanel.this, "Morate selektovati klijenta za brisanje!", "Greška", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int option = JOptionPane.showConfirmDialog(ClientPanel.this, "Da li ste sigurni da želite da obrišete klijenta?", "Brisanje klijenta", JOptionPane.YES_NO_OPTION);
                if(option == JOptionPane.YES_OPTION){
                    Client client = ((ClientTableModel)table.getModel()).getClient(row);
                    clientService.remove(client);
                    table.setModel(new ClientTableModel(clientService.getData()));
                    table.getColumnModel().getColumn(8).setCellRenderer(new BooleanTableCellRenderer());
                    clearInputs();
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());
                String name = firstNameField.getText().trim();
                String lastname = lastNameField.getText().trim();
                Gender gender = (Gender)genderComboBox.getSelectedItem();
                String phoneNum = phoneNumberField.getText().trim();
                String address = addressField.getText().trim();

                if(!validateInput(username, password, lastname, lastname, lastname, phoneNum, address)){
                    return;
                }

                if(isEditing){
                    clientToEdit.setUsername(username);
                    clientToEdit.setPassword(password);
                    clientToEdit.setName(name);
                    clientToEdit.setLastname(lastname);
                    clientToEdit.setGender(gender);
                    clientToEdit.setPhoneNum(phoneNum);
                    clientToEdit.setAddress(address);
                } else {
                    clientService.add(username, password, name, lastname, gender, phoneNum, address);
                }
                table.setModel(new ClientTableModel(clientService.getData()));
                disableInputs();
                clearInputs();
                table.clearSelection();
                editButton.setEnabled(false);
                removeButton.setEnabled(false);
                clientService.saveData();
                JOptionPane.showMessageDialog(ClientPanel.this, "Podaci su uspešno sačuvani.", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disableInputs();
                if(isEditing){
                    fillInputs(clientToEdit);
                } else {
                    clearInputs();
                    table.clearSelection();
                    editButton.setEnabled(false);
                    removeButton.setEnabled(false);
                }
            }
        });
    }

    private boolean validateInput(String username, String password, String firstName, String lastName, String gender, String phoneNumber, String address){
        if(username.trim().isEmpty()){
            JOptionPane.showMessageDialog(ClientPanel.this, "Korisničko ime je obavezno.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(clientService.getByUsername(username) != null){
            JOptionPane.showMessageDialog(ClientPanel.this, "Korisničko ime je zauzeto.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(serviceRegistry.getManagerService().getByUsername(username) != null){
            JOptionPane.showMessageDialog(ClientPanel.this, "Korisničko ime je zauzeto.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(serviceRegistry.getCosmetologistService().getByUsername(username) != null){
            JOptionPane.showMessageDialog(ClientPanel.this, "Korisničko ime je zauzeto.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(serviceRegistry.getReceptionistService().getByUsername(username) != null){
            JOptionPane.showMessageDialog(ClientPanel.this, "Korisničko ime je zauzeto.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(password.trim().isEmpty()){
            JOptionPane.showMessageDialog(ClientPanel.this, "Lozinka je obavezna.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(password.length() < 8){
            JOptionPane.showMessageDialog(ClientPanel.this, "Lozinka mora imati najmanje 8 karaktera.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(firstName.trim().isEmpty()){
            JOptionPane.showMessageDialog(ClientPanel.this, "Ime je obavezno.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(lastName.trim().isEmpty()){
            JOptionPane.showMessageDialog(ClientPanel.this, "Prezime je obavezno.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(gender == null){
            JOptionPane.showMessageDialog(ClientPanel.this, "Pol je obavezan.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(phoneNumber.trim().isEmpty()){
            JOptionPane.showMessageDialog(ClientPanel.this, "Broj telefona je obavezan.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(!phoneNumber.matches("[0-9]+")){
            JOptionPane.showMessageDialog(ClientPanel.this, "Broj telefona može sadržati samo cifre.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(address.trim().isEmpty()){
            JOptionPane.showMessageDialog(ClientPanel.this, "Adresa je obavezna.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
