package ui.manager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.optionalusertools.DateChangeListener;
import com.github.lgooddatepicker.zinternaltools.DateChangeEvent;

import entity.Appointment;
import entity.AppointmentStatus;
import entity.Client;
import entity.CosmeticTreatment;
import entity.Cosmetologist;
import entity.Manager;
import entity.TreatmentType;
import net.miginfocom.swing.MigLayout;
import service.AppointmentService;
import service.BeautySalonService;
import service.ClientService;
import service.CosmeticTreatmentService;
import service.CosmetologistService;
import service.ServiceRegistry;
import service.TreatmentTypeService;
import ui.receptionist.AppointmentTableModel;

public class AppointmentPanel extends JPanel {
    private Manager user;

    private ServiceRegistry serviceRegistry;
    private AppointmentService appointmentService;
    private CosmeticTreatmentService cosmeticTreatmentService;
    private ClientService clientService;
    private CosmetologistService cosmetologistService;
    private BeautySalonService beautySalonService;
    private TreatmentTypeService treatmentTypeService;

    private boolean isEditing = false;
    private Appointment appointmentToEdit;

    private JTable table;
    private TableRowSorter<AbstractTableModel> tableSorter = new TableRowSorter<AbstractTableModel>();
    private JButton scheduleAppointmentButton;
    private JButton editAppointmentButton;
    private JButton cancelAppointmentButton;
    private JButton deleteAppointmentButton;

    private JComboBox<CosmeticTreatment> cosmeticTreatmentFilterComboBox;
    private JComboBox<TreatmentType> treatmentTypeFilterComboBox;
    private JTextField minPriceFilterTextField;
    private JTextField maxPriceFilterTextField;

    private JComboBox<CosmeticTreatment> cosmeticTreatmentComboBox;
    private JComboBox<Client> clientComboBox;
    private JComboBox<Cosmetologist> cosmetologistComboBox;
    private DatePicker datePicker;
    private JComboBox<LocalTime> timeComboBox;
    private JTextField priceField;
    private JComboBox<AppointmentStatus> statusComboBox;
    private JButton saveButton;
    private JButton cancelButton;

    public AppointmentPanel(Manager user) {
        this.user = user;
        serviceRegistry = ServiceRegistry.getInstance();
        appointmentService = serviceRegistry.getAppointmentService();
        cosmeticTreatmentService = serviceRegistry.getCosmeticTreatmentService();
        clientService = serviceRegistry.getClientService();
        cosmetologistService = serviceRegistry.getCosmetologistService();
        beautySalonService = serviceRegistry.getBeautySalonService();
        treatmentTypeService = serviceRegistry.getTreatmentTypeService();
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    private void initializeComponents() {
        ArrayList<Appointment> appointments = appointmentService.getData();
        table = new JTable(new AppointmentTableModel(appointments));
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        tableSorter.setModel((AbstractTableModel) table.getModel());
        table.setRowSorter(tableSorter);

        cosmeticTreatmentFilterComboBox = new JComboBox<CosmeticTreatment>();
        cosmeticTreatmentFilterComboBox.addItem(null);
        ArrayList<CosmeticTreatment> cosmeticTreatments = cosmeticTreatmentService.getData();
        for (CosmeticTreatment cosmeticTreatment : cosmeticTreatments) {
            cosmeticTreatmentFilterComboBox.addItem(cosmeticTreatment);
        }

        treatmentTypeFilterComboBox = new JComboBox<TreatmentType>();
        treatmentTypeFilterComboBox.addItem(null);
        ArrayList<TreatmentType> treatmentTypes = treatmentTypeService.getData();
        for (TreatmentType treatmentType : treatmentTypes) {
            treatmentTypeFilterComboBox.addItem(treatmentType);
        }

        PlainDocument document1 = new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (str == null)
                    return;
                String newValue = getText(0, getLength()) + str;
                try {
                    if (newValue.isEmpty() || Integer.parseInt(newValue) >= 0) {
                        super.insertString(offs, str, a);
                    }
                } catch (NumberFormatException e) {
                    // Ignore invalid input
                }
            }
        };

        PlainDocument document2 = new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (str == null)
                    return;
                String newValue = getText(0, getLength()) + str;
                try {
                    if (newValue.isEmpty() || Integer.parseInt(newValue) >= 0) {
                        super.insertString(offs, str, a);
                    }
                } catch (NumberFormatException e) {
                    // Ignore invalid input
                }
            }
        };

        minPriceFilterTextField = new JTextField(document1, null, 0);
        maxPriceFilterTextField = new JTextField(document2, null, 0);

        scheduleAppointmentButton = new JButton("Zakaži");
        editAppointmentButton = new JButton("Izmeni");
        editAppointmentButton.setEnabled(false);
        cancelAppointmentButton = new JButton("Otkaži");
        cancelAppointmentButton.setEnabled(false);
        deleteAppointmentButton = new JButton("Obriši");
        deleteAppointmentButton.setEnabled(false);

        cosmeticTreatmentComboBox = new JComboBox<CosmeticTreatment>();
        cosmeticTreatmentComboBox.addItem(null);
        for (CosmeticTreatment cosmeticTreatment : cosmeticTreatments) {
            cosmeticTreatmentComboBox.addItem(cosmeticTreatment);
        }

        clientComboBox = new JComboBox<Client>();
        clientComboBox.addItem(null);
        ArrayList<Client> clients = clientService.getData();
        for (Client client : clients) {
            clientComboBox.addItem(client);
        }
        
        cosmetologistComboBox = new JComboBox<Cosmetologist>();
        datePicker = new DatePicker();
        timeComboBox = new JComboBox<LocalTime>();
        priceField = new JTextField();
        statusComboBox = new JComboBox<AppointmentStatus>();
        saveButton = new JButton("Sačuvaj");
        cancelButton = new JButton("Odustani");

        cosmeticTreatmentComboBox.setEnabled(false);   
        clientComboBox.setEnabled(false);
        cosmetologistComboBox.setEnabled(false);
        datePicker.setEnabled(false);
        datePicker.getComponentDateTextField().setEditable(false);
        timeComboBox.setEnabled(false);   
        priceField.setEnabled(false);
        statusComboBox.setEnabled(false);
        saveButton.setEnabled(false);
        cancelButton.setEnabled(false);   
        
        statusComboBox.addItem(AppointmentStatus.SCHEDULED);
        statusComboBox.addItem(AppointmentStatus.COMPLETED);
        statusComboBox.addItem(AppointmentStatus.NOT_SHOWED_UP);
        statusComboBox.addItem(AppointmentStatus.CANCELED_BY_CLIENT);
        statusComboBox.addItem(AppointmentStatus.CANCELED_BY_SALON);
    }

    private void setupLayout() {
        setLayout(new MigLayout("wrap 2, center", "[center, grow]30[center, grow]", "[]30[bottom, grow]"));

        // ========= LEFT PANEL =========
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new MigLayout("wrap 1, center", "[center, grow]"));

        JScrollPane scrollPane = new JScrollPane(table);
        leftPanel.add(scrollPane, "grow, wrap, span 2");

        leftPanel.add(new JLabel("Kozmetički tretman:"), "grow, wrap");
        leftPanel.add(cosmeticTreatmentFilterComboBox, "grow, wrap");

        leftPanel.add(new JLabel("Tip tretmana:"), "grow, wrap");
        leftPanel.add(treatmentTypeFilterComboBox, "grow, wrap");

        // price range inputs in one line
        leftPanel.add(new JLabel("Cena:"), "grow, wrap");
        leftPanel.add(minPriceFilterTextField, "split 3, center, grow");
        leftPanel.add(new JLabel("≤ cena ≤"), "center");
        leftPanel.add(maxPriceFilterTextField, "center, wrap, grow");

        leftPanel.add(scheduleAppointmentButton, "grow, wrap");
        leftPanel.add(editAppointmentButton, "grow, wrap");
        leftPanel.add(cancelAppointmentButton, "grow, wrap");
        leftPanel.add(deleteAppointmentButton, "grow, wrap");

        // ========= RIGHT PANEL =========
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new MigLayout("wrap 1, center", "[center, grow]", "[]30[][]10[][]10[][]10[][]10[][]10[][]10[][]30[][]"));

        rightPanel.add(new JLabel("Kozmetički tretman:"), "wrap, center");

        rightPanel.add(new JLabel("Kozmetički tretman:"), "grow, wrap");
        rightPanel.add(cosmeticTreatmentComboBox, "grow, wrap");

        rightPanel.add(new JLabel("Klijent:"), "grow, wrap");
        rightPanel.add(clientComboBox, "grow, wrap");

        rightPanel.add(new JLabel("Kozmetičar:"), "grow, wrap");
        rightPanel.add(cosmetologistComboBox, "grow, wrap");

        rightPanel.add(new JLabel("Datum:"), "grow, wrap");
        rightPanel.add(datePicker, "grow, wrap");

        rightPanel.add(new JLabel("Vrijeme:"), "grow, wrap");
        rightPanel.add(timeComboBox, "grow, wrap");

        rightPanel.add(new JLabel("Cijena:"), "grow, wrap");
        rightPanel.add(priceField, "grow, wrap");
        
        rightPanel.add(new JLabel("Status:"), "grow, wrap");
        rightPanel.add(statusComboBox, "grow, wrap");

        rightPanel.add(saveButton, "grow, wrap");
        rightPanel.add(cancelButton, "grow, wrap");
    

        add(leftPanel, "span 1 2, grow");
        add(rightPanel, "span 1 2, grow");
    }

    private void enableInputs(){
        // enable right panel
        cosmeticTreatmentComboBox.setEnabled(true);   
        clientComboBox.setEnabled(true);
        cosmetologistComboBox.setEnabled(true);
        datePicker.setEnabled(true);
        timeComboBox.setEnabled(true);
        priceField.setEnabled(true);
        statusComboBox.setEnabled(true);
        saveButton.setEnabled(true);
        cancelButton.setEnabled(true);

        // disable left panel
        table.setEnabled(false);
        scheduleAppointmentButton.setEnabled(false);
        editAppointmentButton.setEnabled(false);
        cancelAppointmentButton.setEnabled(false);
        deleteAppointmentButton.setEnabled(false);
    }

    private void disableInputs(){
        // disable left panel
        cosmeticTreatmentComboBox.setEnabled(false);   
        clientComboBox.setEnabled(false);
        cosmetologistComboBox.setEnabled(false);
        datePicker.setEnabled(false);
        timeComboBox.setEnabled(false);  
        priceField.setEnabled(false); 
        statusComboBox.setEnabled(false);
        saveButton.setEnabled(false);
        cancelButton.setEnabled(false);
        
        // enable right panel
        table.setEnabled(true);
        scheduleAppointmentButton.setEnabled(true);
        editAppointmentButton.setEnabled(true);
        cancelAppointmentButton.setEnabled(true);
        deleteAppointmentButton.setEnabled(true);
    }

    private void clearInputs(){
        cosmeticTreatmentComboBox.setSelectedItem(null);
        clientComboBox.setSelectedItem(null);
        cosmetologistComboBox.removeAllItems();
        datePicker.setDate(null);
        timeComboBox.removeAllItems();
        priceField.setText("");
        statusComboBox.setSelectedItem(AppointmentStatus.SCHEDULED);
    }

    private void fillInputs(Appointment appointment){
        cosmeticTreatmentComboBox.setSelectedItem(appointment.getCosmeticTreatment());
        clientComboBox.setSelectedItem(appointment.getClient());
        cosmetologistComboBox.setSelectedItem(appointment.getCosmetologist());
        datePicker.setDate(appointment.getDate());
        timeComboBox.setSelectedItem(appointment.getTime());
        priceField.setText(String.valueOf(appointment.getPrice()));
        statusComboBox.setSelectedItem(appointment.getStatus());
    }

    private void loadAvailableTimes(){
        LocalDate date = datePicker.getDate();
        if(date == null){
            timeComboBox.removeAllItems();
            timeComboBox.setEnabled(false);
            return;
        }
        
        Cosmetologist cosmetologist = (Cosmetologist) cosmetologistComboBox.getSelectedItem();
        if(cosmetologist == null){
            timeComboBox.removeAllItems();
            timeComboBox.setEnabled(false);
            return;
        }

        CosmeticTreatment cosmeticTreatment = (CosmeticTreatment) cosmeticTreatmentComboBox.getSelectedItem();
        if(cosmeticTreatment == null){
            timeComboBox.removeAllItems();
            timeComboBox.setEnabled(false);
            return;
        }

        ArrayList<Appointment> appointments = appointmentService.getByDateAndCosmetologist(date, cosmetologist);
        int openingHour = beautySalonService.getBeautySalon().getOpeningHour();
        int closingHour = beautySalonService.getBeautySalon().getClosingHour();
        ArrayList<Integer> times = cosmetologist.getFreeHours(appointments, date, openingHour, closingHour, cosmeticTreatment.getDuration());
        timeComboBox.removeAllItems();
        for (Integer time : times) {
            timeComboBox.addItem(LocalTime.of(time, 0));
        }
        timeComboBox.setEnabled(true);
    }

    private void filterTableData(){
        CosmeticTreatment cosmeticTreatment = (CosmeticTreatment) cosmeticTreatmentFilterComboBox.getSelectedItem();
        TreatmentType treatmentType = (TreatmentType) treatmentTypeFilterComboBox.getSelectedItem();
        double minPrice = minPriceFilterTextField.getText().trim().isEmpty() ? 0 : Double.parseDouble(minPriceFilterTextField.getText().trim());
        double maxPrice = maxPriceFilterTextField.getText().trim().isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxPriceFilterTextField.getText().trim());

        ArrayList<Appointment> appointments = null;
        if(cosmeticTreatment == null && treatmentType == null){
            appointments = appointmentService.getInPriceRange(minPrice, maxPrice);
        }else if(cosmeticTreatment != null && treatmentType == null){
            System.out.println(minPrice + " " + maxPrice + " " + cosmeticTreatment);
            appointments = appointmentService.getByCosmeticTreatmentInPriceRange(minPrice, maxPrice, cosmeticTreatment);
        }else if(cosmeticTreatment == null && treatmentType != null){
            appointments = appointmentService.getByCosmeticTreatmentTypeInPriceRange(minPrice, maxPrice, treatmentType);
        }
        
        table.setModel(new AppointmentTableModel(appointments));
        tableSorter.setModel((AbstractTableModel)table.getModel());
        table.setRowSorter(tableSorter);
        table.repaint();
    }

    private void setupListeners(){
        cosmeticTreatmentComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(cosmeticTreatmentComboBox.getSelectedItem() == null){
                    cosmetologistComboBox.removeAllItems();
                    cosmeticTreatmentComboBox.setEnabled(false);
                    return;
                }

                CosmeticTreatment cosmeticTreatment = (CosmeticTreatment) cosmeticTreatmentComboBox.getSelectedItem();
                ArrayList<Cosmetologist> cosmetologists = cosmetologistService.getByTreatmentType(cosmeticTreatment.getTreatmentType());
                cosmetologistComboBox.removeAllItems();
                for (Cosmetologist cosmetologist : cosmetologists) {
                    cosmetologistComboBox.addItem(cosmetologist);
                }
                cosmetologistComboBox.setEnabled(true);
            }
        });

        datePicker.addDateChangeListener(new DateChangeListener() {
            @Override
            public void dateChanged(DateChangeEvent event) {
                loadAvailableTimes();
            }
        });

        cosmetologistComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadAvailableTimes();
            }
        });

        scheduleAppointmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isEditing = false;
                clearInputs();
                enableInputs();
                table.clearSelection();
                
                statusComboBox.setSelectedItem(AppointmentStatus.SCHEDULED);
                statusComboBox.setEnabled(false);
                cosmetologistComboBox.setEnabled(false);
                cosmetologistComboBox.removeAllItems();
                timeComboBox.setEnabled(false);
                timeComboBox.removeAllItems();
                priceField.setEnabled(false);
            }
        });

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int row = table.getSelectedRow();
                if(row == -1){
                    editAppointmentButton.setEnabled(false);
                    cancelAppointmentButton.setEnabled(false);
                    deleteAppointmentButton.setEnabled(false);
                    return;
                }

                appointmentToEdit = ((AppointmentTableModel) table.getModel()).getAppointment(row);
                
                editAppointmentButton.setEnabled(true);
                deleteAppointmentButton.setEnabled(true);
                if(appointmentToEdit.getStatus() == AppointmentStatus.SCHEDULED)
                    cancelAppointmentButton.setEnabled(true);
                else
                    cancelAppointmentButton.setEnabled(false);
                
                fillInputs(appointmentToEdit);
            }
        });

        editAppointmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isEditing = true;
                enableInputs();
            }
        });

        cancelAppointmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if(row == -1){
                    JOptionPane.showMessageDialog(null, "Morate selektovati red u tabeli.", "Greška", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Appointment appointment = ((AppointmentTableModel) table.getModel()).getAppointment(row);
                int option = JOptionPane.showConfirmDialog(null, "Da li ste sigurni da želite da otkažete tretman?", "Potvrda brisanja", JOptionPane.YES_NO_OPTION);
                if(option == JOptionPane.YES_OPTION){
                    appointmentService.cancel(appointment, user);
                    filterTableData();
                    JOptionPane.showMessageDialog(null, "Tretman je uspješno otkazan.", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        deleteAppointmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                int row = table.getSelectedRow();
                if(row == -1){
                    JOptionPane.showMessageDialog(null, "Morate selektovati red u tabeli.", "Greška", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int option = JOptionPane.showConfirmDialog(null, "Da li ste sigurni da želite da obrišete tretman?", "Potvrda brisanja", JOptionPane.YES_NO_OPTION);
                if(option == JOptionPane.YES_OPTION){
                    Appointment appointment = ((AppointmentTableModel) table.getModel()).getAppointment(row);
                    appointmentService.remove(appointment);
                    filterTableData();
                    JOptionPane.showMessageDialog(null, "Tretman je uspješno obrisan.", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disableInputs();
                if(isEditing)
                    fillInputs(appointmentToEdit);
                else
                    clearInputs();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                CosmeticTreatment cosmeticTreatment = (CosmeticTreatment) cosmeticTreatmentComboBox.getSelectedItem();
                Client client = (Client) clientComboBox.getSelectedItem();
                Cosmetologist cosmetologist = (Cosmetologist) cosmetologistComboBox.getSelectedItem();
                LocalDate date = datePicker.getDate();
                LocalTime time = (LocalTime) timeComboBox.getSelectedItem();
                AppointmentStatus status = (AppointmentStatus) statusComboBox.getSelectedItem();

                if(cosmeticTreatment == null || client == null || cosmetologist == null || date == null || time == null || status == null){
                    JOptionPane.showMessageDialog(null, "Morate popuniti sva polja.", "Greška", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if(isEditing){
                    try{
                        appointmentToEdit.setPrice(Double.parseDouble(priceField.getText()));
                    } catch(NumberFormatException ex){
                        JOptionPane.showMessageDialog(null, "Cijena mora biti broj.", "Greška", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    appointmentToEdit.setCosmeticTreatment(cosmeticTreatment);
                    appointmentToEdit.setClient(client);
                    appointmentToEdit.setCosmetologist(cosmetologist);
                    appointmentToEdit.setDate(date);
                    appointmentToEdit.setTime(time);
                    appointmentToEdit.setStatus(status);
                } else {
                    appointmentService.schedule(cosmeticTreatment, cosmetologist, client, date, time);
                }
                table.setModel(new AppointmentTableModel(appointmentService.getData()));
                disableInputs();
                table.clearSelection();
                clearInputs();
                editAppointmentButton.setEnabled(false);
                cancelAppointmentButton.setEnabled(false);
                deleteAppointmentButton.setEnabled(false);
                serviceRegistry.saveData();
            }
        });

        cosmeticTreatmentFilterComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CosmeticTreatment cosmeticTreatment = (CosmeticTreatment) cosmeticTreatmentFilterComboBox.getSelectedItem();
                if(cosmeticTreatment == null){
                    treatmentTypeFilterComboBox.setEnabled(true);
                } else {
                    // treatmentTypeFilterComboBox.setSelectedItem(cosmeticTreatment.getTreatmentType());
                    treatmentTypeFilterComboBox.setEnabled(false);
                }
                filterTableData();
            }
        });

        treatmentTypeFilterComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TreatmentType treatmentType = (TreatmentType) treatmentTypeFilterComboBox.getSelectedItem();
                if(treatmentType == null){
                    cosmeticTreatmentFilterComboBox.setEnabled(true);
                } else {
                    cosmeticTreatmentFilterComboBox.setSelectedItem(null);
                    cosmeticTreatmentFilterComboBox.setEnabled(false);
                }
                filterTableData();
            }
        });

        minPriceFilterTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTableData();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTableData();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTableData();
            }
        });

        maxPriceFilterTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTableData();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTableData();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTableData();
            }
        });
    }

}
