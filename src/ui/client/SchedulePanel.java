package ui.client;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
import entity.Client;
import entity.CosmeticTreatment;
import entity.Cosmetologist;
import entity.TreatmentType;
import net.miginfocom.swing.MigLayout;
import service.AppointmentService;
import service.BeautySalonService;
import service.CosmeticTreatmentService;
import service.CosmetologistService;
import service.PriceListService;
import service.ServiceRegistry;
import service.TreatmentTypeService;

public class SchedulePanel extends JPanel {
    private Client user;
    private ClientGUI parent;

    private int currentStep = 0;
    private CosmeticTreatment selectedCosmeticTreatment;

    private AppointmentService appointmentService;
    private CosmeticTreatmentService cosmeticTreatmentService;
    private CosmetologistService cosmetologistService;
    private PriceListService priceListService;
    private BeautySalonService beautySalonService;
    private TreatmentTypeService treatmentTypeService;

    private CardLayout cardLayout;
    private JPanel contentPanel;

    private JButton backButton;
    private JButton nextButton;

    private JTable table;
    private TableRowSorter<AbstractTableModel> tableSorter = new TableRowSorter<AbstractTableModel>();
    private JList<Cosmetologist> cosmetologistList;
    private DatePicker datePicker;
    private JComboBox<LocalTime> timeComboBox;

    private JComboBox<TreatmentType> treatmentTypeFilterComboBox;
    private JTextField minPriceFilterTextField;
    private JTextField maxPriceFilterTextField;
    private JTextField minDurationFilterTextField;
    private JTextField maxDurationFilterTextField;

    public SchedulePanel(Client user, ClientGUI parent) {
        this.user = user;
        this.parent = parent;
        ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();
        appointmentService = serviceRegistry.getAppointmentService();
        cosmetologistService = serviceRegistry.getCosmetologistService();
        cosmeticTreatmentService = serviceRegistry.getCosmeticTreatmentService();
        priceListService = serviceRegistry.getPriceListService();
        beautySalonService = serviceRegistry.getBeautySalonService();
        treatmentTypeService = serviceRegistry.getTreatmentTypeService();
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    private void initializeComponents() {
        backButton = new JButton("Nazad");
        backButton.setEnabled(false);
        nextButton = new JButton("Nastavi");
        nextButton.setEnabled(false);
        ArrayList<CosmeticTreatment> cosmeticTreatments = cosmeticTreatmentService.getData();
        table = new JTable(new CosmeticTreatmentTableModel(cosmeticTreatments, priceListService));
        table.getTableHeader().setReorderingAllowed(false);
        tableSorter.setModel((AbstractTableModel) table.getModel());
        table.setRowSorter(tableSorter);
        cosmetologistList = new JList<Cosmetologist>();
        datePicker = new DatePicker();
        timeComboBox = new JComboBox<LocalTime>();

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

        PlainDocument document3 = new PlainDocument() {
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

        PlainDocument document4 = new PlainDocument() {
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

        minDurationFilterTextField = new JTextField(document3, null, 0);
        maxDurationFilterTextField = new JTextField(document4, null, 0);
    }

    private void setupLayout() {
        setLayout(new MigLayout("fill", "", "[grow][]"));
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // first step
        JPanel cosmeticTreatmentChoicePanel = new JPanel(new MigLayout("fill"));
        JScrollPane scrollPane = new JScrollPane(table);
        cosmeticTreatmentChoicePanel.add(scrollPane, "grow, wrap");

        cosmeticTreatmentChoicePanel.add(new JLabel("Tip tretmana:"), "grow, wrap");
        cosmeticTreatmentChoicePanel.add(treatmentTypeFilterComboBox, "grow, wrap");

        // price range inputs in one line
        cosmeticTreatmentChoicePanel.add(new JLabel("Cena:"), "grow, wrap");
        cosmeticTreatmentChoicePanel.add(minPriceFilterTextField, "split 3, center, grow");
        cosmeticTreatmentChoicePanel.add(new JLabel("≤ cena ≤"), "center");
        cosmeticTreatmentChoicePanel.add(maxPriceFilterTextField, "center, wrap, grow");

        // duration range inputs in one line
        cosmeticTreatmentChoicePanel.add(new JLabel("Trajanje:"), "grow, wrap");
        cosmeticTreatmentChoicePanel.add(minDurationFilterTextField, "split 3, center, grow");
        cosmeticTreatmentChoicePanel.add(new JLabel("≤ trajanje ≤"), "center");
        cosmeticTreatmentChoicePanel.add(maxDurationFilterTextField, "center, wrap, grow");

        // second step
        JPanel cosmetologistChoicePanel = new JPanel(new MigLayout("fill, wrap 2", "", "[][]30[]30[]"));
        cosmetologistChoicePanel.add(new JLabel("Izaberite kozmetičara:"), "wrap");
        cosmetologistChoicePanel.add(cosmetologistList, "grow, span 2, wrap");
        cosmetologistChoicePanel.add(new JLabel("Izaberite datum:"));
        cosmetologistChoicePanel.add(datePicker, "width 200px, wrap");
        cosmetologistChoicePanel.add(new JLabel("Izaberite vreme:"));
        cosmetologistChoicePanel.add(timeComboBox, "width 200px, wrap");

        contentPanel.add(cosmeticTreatmentChoicePanel);
        contentPanel.add(cosmetologistChoicePanel);

        add(contentPanel, "grow, wrap");
        add(backButton, "split 2, sg button, center");
        add(nextButton, "sg button");
    }

    private void loadAvailableTimes(){
        Cosmetologist cosmetologist = cosmetologistList.getSelectedValue();
        if(cosmetologist == null){
            timeComboBox.removeAllItems();
            return;
        }
        nextButton.setEnabled(true);
        LocalDate date = datePicker.getDate();
        if(date == null){
            timeComboBox.removeAllItems();
            return;
        }
        
        timeComboBox.removeAllItems();
        if(date.isBefore(LocalDate.now())){
            timeComboBox.setEnabled(false);
            return;
        }

        ArrayList<Appointment> appointments = appointmentService.getByDateAndCosmetologist(date, cosmetologist);
        int openingHour = beautySalonService.getBeautySalon().getOpeningHour();
        int closingHour = beautySalonService.getBeautySalon().getClosingHour();
        ArrayList<Integer> times = cosmetologist.getFreeHours(appointments, date, openingHour, closingHour, selectedCosmeticTreatment.getDuration());
        for (Integer time : times) {
            timeComboBox.addItem(LocalTime.of(time, 0));
        }
        timeComboBox.setEnabled(true);
    }

    private void filterTableData(){
        TreatmentType treatmentType = (TreatmentType) treatmentTypeFilterComboBox.getSelectedItem();
        double minPrice = minPriceFilterTextField.getText().trim().isEmpty() ? 0 : Double.parseDouble(minPriceFilterTextField.getText().trim());
        double maxPrice = maxPriceFilterTextField.getText().trim().isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxPriceFilterTextField.getText().trim());
        int minDuration = minDurationFilterTextField.getText().trim().isEmpty() ? 0 : Integer.parseInt(minDurationFilterTextField.getText().trim());
        int maxDuration = maxDurationFilterTextField.getText().trim().isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(maxDurationFilterTextField.getText().trim());
        
        ArrayList<CosmeticTreatment> cosmeticTreatments = cosmeticTreatmentService.getBy(null, treatmentType, minPrice, maxPrice, minDuration, maxDuration);

        table.setModel(new CosmeticTreatmentTableModel(cosmeticTreatments, priceListService));
        tableSorter.setModel((AbstractTableModel)table.getModel());
        table.setRowSorter(tableSorter);
        table.repaint();
    }

    private void setupListeners() {
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                cardLayout.previous(contentPanel);
                currentStep--;
                if(currentStep == 0){
                    backButton.setEnabled(false);
                }
                nextButton.setText("Nastavi");
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                if(currentStep == 0){
                    if(selectedCosmeticTreatment == null){
                        JOptionPane.showMessageDialog(null, "Morate izabrati tretman", "Greška", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    cardLayout.next(contentPanel);
                    currentStep++;
                    backButton.setEnabled(true);
                    nextButton.setText("Zakaži");
                    nextButton.setEnabled(false);
                    timeComboBox.setEnabled(false);
                } else if(currentStep == 1){
                    Cosmetologist cosmetologist = cosmetologistList.getSelectedValue();
                    if(cosmetologist == null){
                        JOptionPane.showMessageDialog(null, "Morate izabrati kozmetičara", "Greška", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    LocalDate date = datePicker.getDate();
                    if(date == null){
                        JOptionPane.showMessageDialog(null, "Morate izabrati datum", "Greška", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    LocalTime time = (LocalTime) timeComboBox.getSelectedItem();
                    if(time == null){
                        JOptionPane.showMessageDialog(null, "Morate izabrati vrijeme", "Greška", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    appointmentService.schedule(selectedCosmeticTreatment, cosmetologist, user, date, time);
                    JOptionPane.showMessageDialog(null, "Uspješno ste zakazali tretman", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
                    cardLayout.previous(contentPanel);
                    currentStep = 0;
                    backButton.setEnabled(false);
                    nextButton.setText("Nastavi");
                    nextButton.setEnabled(false);
                    timeComboBox.setEnabled(false);
                    cosmetologistList.clearSelection();
                    datePicker.setDate(null);
                    timeComboBox.removeAllItems();
                    table.clearSelection();
                    parent.updateTables();
                }
            }
        });

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                if(!e.getValueIsAdjusting()){
                    int row = table.getSelectedRow();
                    if(row == -1){
                        nextButton.setEnabled(false);
                    } else {
                        nextButton.setEnabled(true);
                        selectedCosmeticTreatment = ((CosmeticTreatmentTableModel) table.getModel()).getCosmeticTreatment(row);
                        ArrayList<Cosmetologist> cosmetologists = cosmetologistService.getByTreatmentType(selectedCosmeticTreatment.getTreatmentType());
                        cosmetologistList.setListData(cosmetologists.toArray(new Cosmetologist[cosmetologists.size()]));
                    }
                }
            }
        });

        cosmetologistList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                if(!e.getValueIsAdjusting()){
                    loadAvailableTimes();
                }
            }
        });

        datePicker.addDateChangeListener(new DateChangeListener() {
            @Override
            public void dateChanged(DateChangeEvent event) {
                loadAvailableTimes();
            }
        });

        timeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalTime time = (LocalTime) timeComboBox.getSelectedItem();
                if(time == null){
                    nextButton.setEnabled(false);
                } else {
                    nextButton.setEnabled(true);
                }
            }
        });

        treatmentTypeFilterComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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

        minDurationFilterTextField.getDocument().addDocumentListener(new DocumentListener() {
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

        maxDurationFilterTextField.getDocument().addDocumentListener(new DocumentListener() {
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
