package ui.manager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import entity.Cosmetologist;
import entity.Employee;
import entity.Gender;
import entity.LevelOfEducation;
import entity.Manager;
import entity.Receptionist;
import entity.TreatmentType;
import net.miginfocom.swing.MigLayout;
import service.ClientService;
import service.CosmetologistService;
import service.ManagerService;
import service.ReceptionistService;
import service.ServiceRegistry;
import service.TreatmentTypeService;
import ui.manager.model.EmployeeTableModel;

public class EmployeePanel extends JPanel {
    Manager user;

    private ManagerService managerService;
    private ReceptionistService receptionistService;
    private CosmetologistService cosmetologistService;
    private TreatmentTypeService treatmentTypeService;
    private ClientService clientService;

    private boolean isEditing = false;
    private Employee employeeToEdit = null;
    private boolean isRightPanelActive = false;

    private JTable table;
    private TableRowSorter<AbstractTableModel> tableSorter = new TableRowSorter<AbstractTableModel>();
    private JButton addButton;
    private JButton editButton;
    private JButton removeButton;

    private JComboBox<String> roleComboBox;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JComboBox<Gender> genderComboBox;
    private JTextField phoneNumberField;
    private JTextField addressField;
    private JSpinner yearsOfExperienceSpinner;
    private JComboBox<LevelOfEducation> levelOfEducationComboBox;
    private JTextField baseSalaryField;
    private JList<TreatmentType> treatmentTypesList;
    private JButton treatmentTypesListButton;
    private JButton saveButton;
    private JButton cancelButton;

    public EmployeePanel(Manager user) {
        this.user = user;
        ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();
        managerService = serviceRegistry.getManagerService();
        receptionistService = serviceRegistry.getReceptionistService();
        cosmetologistService = serviceRegistry.getCosmetologistService();
        treatmentTypeService = serviceRegistry.getTreatmentTypeService();
        clientService = serviceRegistry.getClientService();
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    private ArrayList<Employee> getEmployees(){
        ArrayList<Employee> employees = new ArrayList<Employee>();
        employees.addAll(managerService.getData());
        employees.addAll(receptionistService.getData());
        employees.addAll(cosmetologistService.getData());
        return employees;
    }

    private void saveEmployeeData(){
        managerService.saveData();
        receptionistService.saveData();
        cosmetologistService.saveData();
    }

    private void initializeComponents(){
        ArrayList<Employee> employees = getEmployees();
        EmployeeTableModel employeeTableModel = new EmployeeTableModel(employees);
        table = new JTable(employeeTableModel);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        tableSorter.setModel(employeeTableModel);
        table.setRowSorter(tableSorter);

        addButton = new JButton("Dodaj");
        editButton = new JButton("Izmeni");
        removeButton = new JButton("Ukloni");

        roleComboBox = new JComboBox<String>(new String[]{null, "Menadžer", "Recepcioner", "Kozmetičar"});

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        firstNameField = new JTextField();
        lastNameField = new JTextField();
        genderComboBox = new JComboBox<Gender>(new Gender[]{null, Gender.MALE, Gender.FEMALE});
        phoneNumberField = new JTextField();
        addressField = new JTextField();
        yearsOfExperienceSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        levelOfEducationComboBox = new JComboBox<LevelOfEducation>(new LevelOfEducation[]{null, LevelOfEducation.UNSKILLED, LevelOfEducation.HIGH_SCHOOL, LevelOfEducation.BACHELOR, LevelOfEducation.MASTER, LevelOfEducation.DOCTORATE});
        baseSalaryField = new JTextField();

        treatmentTypesList = new JList<TreatmentType>();
        treatmentTypesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        treatmentTypesList.setLayoutOrientation(JList.VERTICAL);
        treatmentTypesList.setVisibleRowCount(-1);
        ArrayList<TreatmentType> treatmentTypes = treatmentTypeService.getData();
        treatmentTypesList.setListData(treatmentTypes.toArray(new TreatmentType[treatmentTypes.size()]));

        treatmentTypesListButton = new JButton("Izaberi tretmane");

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

        rightPanel.add(new JLabel("Uloga"), "grow, wrap");
        rightPanel.add(roleComboBox, "grow, wrap");

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

        rightPanel.add(new JLabel("Radni staž"), "grow, wrap");
        rightPanel.add(yearsOfExperienceSpinner, "grow, wrap");

        rightPanel.add(new JLabel("Nivo obrazovanja"), "grow, wrap");
        rightPanel.add(levelOfEducationComboBox, "grow, wrap");

        rightPanel.add(new JLabel("Osnova plate"), "grow, wrap");
        rightPanel.add(baseSalaryField, "grow, wrap");

        rightPanel.add(new JLabel("Tipovi tretmana za koje je kozmetičar obučen:"), "grow, wrap");
        rightPanel.add(new JScrollPane(treatmentTypesListButton), "grow, wrap");

        rightPanel.add(saveButton, "grow, wrap");
        rightPanel.add(cancelButton, "grow, wrap");

        JScrollPane rightScrollPane = new JScrollPane(rightPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(rightScrollPane, "span 1 2, grow");    
    }

    private void enableInputs(){
        // enable right panel
        roleComboBox.setEnabled(true);
        usernameField.setEnabled(true);
        passwordField.setEnabled(true);
        firstNameField.setEnabled(true);
        lastNameField.setEnabled(true);
        genderComboBox.setEnabled(true);
        phoneNumberField.setEnabled(true);
        addressField.setEnabled(true);
        yearsOfExperienceSpinner.setEnabled(true);
        levelOfEducationComboBox.setEnabled(true);
        baseSalaryField.setEnabled(true);
        treatmentTypesListButton.setEnabled(true);
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
        roleComboBox.setEnabled(false);
        usernameField.setEnabled(false);
        passwordField.setEnabled(false);
        firstNameField.setEnabled(false);
        lastNameField.setEnabled(false);
        genderComboBox.setEnabled(false);
        phoneNumberField.setEnabled(false);
        addressField.setEnabled(false);
        yearsOfExperienceSpinner.setEnabled(false);
        levelOfEducationComboBox.setEnabled(false);
        baseSalaryField.setEnabled(false);
        treatmentTypesListButton.setEnabled(false);
        saveButton.setEnabled(false);
        cancelButton.setEnabled(false);

        // enable left panel
        table.setEnabled(true);
        addButton.setEnabled(true);
        editButton.setEnabled(true);
        removeButton.setEnabled(true);
    }

    private void clearInputs(){
        roleComboBox.setSelectedItem(null);
        usernameField.setText("");
        passwordField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        genderComboBox.setSelectedItem(null);
        phoneNumberField.setText("");
        addressField.setText("");
        yearsOfExperienceSpinner.setValue(0);
        levelOfEducationComboBox.setSelectedItem(null);
        baseSalaryField.setText("");
        treatmentTypesList.clearSelection();
    }

    private void fillInputs(Employee employee){
        usernameField.setText(employee.getUsername());
        passwordField.setText(employee.getPassword());
        firstNameField.setText(employee.getName());
        lastNameField.setText(employee.getLastname());
        genderComboBox.setSelectedItem(employee.getGender());
        phoneNumberField.setText(employee.getPhoneNum());
        addressField.setText(employee.getAddress());
        yearsOfExperienceSpinner.setValue(employee.getYearsOfExperience());
        levelOfEducationComboBox.setSelectedItem(employee.getLevelOfEducation());
        baseSalaryField.setText(String.valueOf(employee.getBaseSalary()));
        if(employee instanceof Cosmetologist){
            roleComboBox.setSelectedItem("Kozmetičar");
            for(TreatmentType treatmentType : ((Cosmetologist)employee).getTreatmentTypes()){
                treatmentTypesList.setSelectedValue(treatmentType, false);
            }
        } else if(employee instanceof Manager){
            roleComboBox.setSelectedItem("Menadžer");
        } else if(employee instanceof Receptionist){
            roleComboBox.setSelectedItem("Recepcioner");
        }
    }

    private void setupListeners(){
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isEditing = false;
                clearInputs();
                enableInputs();
                treatmentTypesListButton.setEnabled(false);
                table.clearSelection();
                usernameField.requestFocus();
                isRightPanelActive = true;
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

                employeeToEdit = ((EmployeeTableModel)table.getModel()).getEmployee(row);

                editButton.setEnabled(true);
                removeButton.setEnabled(true);
                fillInputs(employeeToEdit);
            }
        });

        roleComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!isRightPanelActive){
                    treatmentTypesListButton.setEnabled(false);
                    return;
                }
                if(roleComboBox.getSelectedItem() == null){
                    treatmentTypesListButton.setEnabled(false);
                } else if(roleComboBox.getSelectedItem().equals("Kozmetičar")){
                    treatmentTypesListButton.setEnabled(true);
                } else {
                    treatmentTypesListButton.setEnabled(false);
                }
            }
        });

        treatmentTypesListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(EmployeePanel.this, treatmentTypesList, "Tipovi tretmana", JOptionPane.PLAIN_MESSAGE);
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isEditing = true;
                isRightPanelActive = true;
                enableInputs();
                roleComboBox.setEnabled(false);
                if(employeeToEdit instanceof Cosmetologist){
                    treatmentTypesListButton.setEnabled(true);
                } else {
                    treatmentTypesListButton.setEnabled(false);
                }
                usernameField.requestFocus();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if(row == -1){
                    JOptionPane.showMessageDialog(EmployeePanel.this, "Morate selektovati zaposlenog za brisanje!", "Greška", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int option = JOptionPane.showConfirmDialog(EmployeePanel.this, "Da li ste sigurni da želite da obrišete zaposlenog?", "Brisanje klijenta", JOptionPane.YES_NO_OPTION);
                if(option == JOptionPane.YES_OPTION){
                    Employee employee = ((EmployeeTableModel)table.getModel()).getEmployee(row);
                    if(employee instanceof Manager){
                        managerService.remove((Manager)employee);
                    } else if(employee instanceof Receptionist){
                        receptionistService.remove((Receptionist)employee);
                    } else if(employee instanceof Cosmetologist){
                        cosmetologistService.remove((Cosmetologist)employee);
                    } else {
                        JOptionPane.showMessageDialog(EmployeePanel.this, "Greška prilikom brisanja zaposlenog!", "Greška", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    table.setModel(new EmployeeTableModel(getEmployees()));
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
                int yearsOfExperience = (int)yearsOfExperienceSpinner.getValue();
                LevelOfEducation levelOfEducation = (LevelOfEducation)levelOfEducationComboBox.getSelectedItem();
                double baseSalary = 0;
                try{
                    baseSalary = Double.parseDouble(baseSalaryField.getText().trim());
                } catch(NumberFormatException ex){
                    JOptionPane.showMessageDialog(EmployeePanel.this, "Plata mora biti broj!", "Greška", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if(!validateInput(username, password, lastname, lastname, lastname, phoneNum, address, yearsOfExperience, levelOfEducation, baseSalary)){
                    return;
                }

                if(isEditing){
                    employeeToEdit.setUsername(username);
                    employeeToEdit.setPassword(password);
                    employeeToEdit.setName(name);
                    employeeToEdit.setLastname(lastname);
                    employeeToEdit.setGender(gender);
                    employeeToEdit.setPhoneNum(phoneNum);
                    employeeToEdit.setAddress(address);
                    employeeToEdit.setYearsOfExperience(yearsOfExperience);
                    employeeToEdit.setLevelOfEducation(levelOfEducation);
                    employeeToEdit.setBaseSalary(baseSalary);
                    if(employeeToEdit instanceof Cosmetologist){
                        ArrayList<TreatmentType> treatmentTypes = new ArrayList<>();
                        for(TreatmentType treatmentType : treatmentTypesList.getSelectedValuesList()){
                            treatmentTypes.add(treatmentType);
                        }
                        ((Cosmetologist)employeeToEdit).setTreatmentTypes(treatmentTypes);
                    }
                } else { // new employee
                    if(usernameTaken(username)){
                        JOptionPane.showMessageDialog(EmployeePanel.this, "Korisničko ime je zauzeto!", "Greška", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    String role = (String)roleComboBox.getSelectedItem();
                    if(role.equals("Kozmetičar")){
                        cosmetologistService.add(username, password, name, lastname, gender, phoneNum, address, levelOfEducation, yearsOfExperience, baseSalary, 0.0, (ArrayList<TreatmentType>)treatmentTypesList.getSelectedValuesList());
                    } else if(role.equals("Menadžer")){
                        managerService.add(username, password, name, lastname, gender, phoneNum, address, levelOfEducation, yearsOfExperience, baseSalary, 0.0);
                    } else if(role.equals("Recepcioner")){
                        receptionistService.add(username, password, name, lastname, gender, phoneNum, address, levelOfEducation, yearsOfExperience, baseSalary);
                    }
                }
                table.setModel(new EmployeeTableModel(getEmployees()));
                disableInputs();
                clearInputs();
                table.clearSelection();
                editButton.setEnabled(false);
                removeButton.setEnabled(false);
                isRightPanelActive = false;
                saveEmployeeData();
                JOptionPane.showMessageDialog(EmployeePanel.this, "Podaci su uspešno sačuvani.", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disableInputs();
                isRightPanelActive = false;
                if(isEditing){
                    fillInputs(employeeToEdit);
                } else {
                    clearInputs();
                    table.clearSelection();
                    editButton.setEnabled(false);
                    removeButton.setEnabled(false);
                }
            }
        });
    }

    private boolean validateInput(String username, String password, String firstName, String lastName, String gender, String phoneNumber, String address, int yearsOfExperience, LevelOfEducation levelOfEducation, double baseSalary){
        if(username.trim().isEmpty()){
            JOptionPane.showMessageDialog(EmployeePanel.this, "Korisničko ime je obavezno.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(password.trim().isEmpty()){
            JOptionPane.showMessageDialog(EmployeePanel.this, "Lozinka je obavezna.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(password.length() < 8){
            JOptionPane.showMessageDialog(EmployeePanel.this, "Lozinka mora imati najmanje 8 karaktera.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(firstName.trim().isEmpty()){
            JOptionPane.showMessageDialog(EmployeePanel.this, "Ime je obavezno.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(lastName.trim().isEmpty()){
            JOptionPane.showMessageDialog(EmployeePanel.this, "Prezime je obavezno.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(gender == null){
            JOptionPane.showMessageDialog(EmployeePanel.this, "Pol je obavezan.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(phoneNumber.trim().isEmpty()){
            JOptionPane.showMessageDialog(EmployeePanel.this, "Broj telefona je obavezan.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(!phoneNumber.matches("[0-9]+")){
            JOptionPane.showMessageDialog(EmployeePanel.this, "Broj telefona može sadržati samo cifre.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(address.trim().isEmpty()){
            JOptionPane.showMessageDialog(EmployeePanel.this, "Adresa je obavezna.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(yearsOfExperience < 0){
            JOptionPane.showMessageDialog(EmployeePanel.this, "Godine iskustva moraju biti pozitivan broj.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(levelOfEducation == null){
            JOptionPane.showMessageDialog(EmployeePanel.this, "Nivo obrazovanja je obavezan.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(baseSalary < 0){
            JOptionPane.showMessageDialog(EmployeePanel.this, "Plata mora biti pozitivan broj.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private boolean usernameTaken(String username){
        for(Employee employee : getEmployees()){
            if(employee.getUsername().equals(username)){
                return true;
            }
        }
        return (clientService.getByUsername(username) != null);
    }
}
