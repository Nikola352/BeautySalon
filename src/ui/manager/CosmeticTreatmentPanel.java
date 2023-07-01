package ui.manager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import entity.CosmeticTreatment;
import entity.Manager;
import entity.TreatmentType;
import net.miginfocom.swing.MigLayout;
import service.CosmeticTreatmentService;
import service.PriceListService;
import service.ServiceRegistry;
import service.TreatmentTypeService;
import ui.manager.model.CosmeticTreatmentTableModel;

public class CosmeticTreatmentPanel extends JPanel {
    Manager user;

    private CosmeticTreatmentService cosmeticTreatmentService;
    private PriceListService priceListService;
    private TreatmentTypeService treatmentTypeService;

    boolean isEditing = false;
    CosmeticTreatment cosmeticTreatmentToEdit = null;

    private JTable table;
    private TableRowSorter<AbstractTableModel> tableSorter = new TableRowSorter<AbstractTableModel>();
    private JButton addButton;
    private JButton editButton;
    private JButton removeButton;

    private JTextField nameField;
    private JComboBox<TreatmentType> treatmentTypeComboBox;
    private JTextField priceField;
    private JSpinner durationSpinner;
    private JButton saveButton;
    private JButton cancelButton;

    public CosmeticTreatmentPanel(Manager user) {
        this.user = user;
        ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();
        cosmeticTreatmentService = serviceRegistry.getCosmeticTreatmentService();
        priceListService = serviceRegistry.getPriceListService();
        treatmentTypeService = serviceRegistry.getTreatmentTypeService();
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    private void initializeComponents(){
        ArrayList<CosmeticTreatment> cosmeticTreatments = cosmeticTreatmentService.getData();
        CosmeticTreatmentTableModel cosmeticTreatmentTableModel = new CosmeticTreatmentTableModel(cosmeticTreatments, priceListService);
        table = new JTable(cosmeticTreatmentTableModel);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        tableSorter.setModel(cosmeticTreatmentTableModel);
        table.setRowSorter(tableSorter);

        addButton = new JButton("Dodaj");
        editButton = new JButton("Izmeni");
        removeButton = new JButton("Ukloni");

        nameField = new JTextField(20);

        ArrayList<TreatmentType> treatmentTypes = treatmentTypeService.getData();
        treatmentTypeComboBox = new JComboBox<TreatmentType>();
        treatmentTypeComboBox.addItem(null);
        for (TreatmentType treatmentType : treatmentTypes) {
            this.treatmentTypeComboBox.addItem(treatmentType);
        }
        
        priceField = new JTextField(20);
        durationSpinner = new JSpinner();

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

        rightPanel.add(new JLabel("Kozmetički tretman"), "wrap, center");

        rightPanel.add(new JLabel("Ime"), "grow, wrap");
        rightPanel.add(nameField, "grow, wrap");

        rightPanel.add(new JLabel("Tip tretmana"), "grow, wrap");
        rightPanel.add(treatmentTypeComboBox, "grow, wrap");

        rightPanel.add(new JLabel("Cena"), "grow, wrap");
        rightPanel.add(priceField, "grow, wrap");

        rightPanel.add(new JLabel("Trajanje"), "grow, wrap");
        rightPanel.add(durationSpinner, "grow, wrap");

        rightPanel.add(saveButton, "grow, wrap");
        rightPanel.add(cancelButton, "grow, wrap");

        add(rightPanel, "span 1 2, grow");
    }

    private void enableInputs(){
        // enable right panel
        nameField.setEnabled(true);
        treatmentTypeComboBox.setEnabled(true);
        priceField.setEnabled(true);
        durationSpinner.setEnabled(true);
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
        nameField.setEnabled(false);
        treatmentTypeComboBox.setEnabled(false);
        priceField.setEnabled(false);
        durationSpinner.setEnabled(false);
        saveButton.setEnabled(false);
        cancelButton.setEnabled(false);

        // enable left panel
        table.setEnabled(true);
        addButton.setEnabled(true);
        editButton.setEnabled(true);
        removeButton.setEnabled(true);
    }

    private void clearInputs(){
        nameField.setText("");
        treatmentTypeComboBox.setSelectedItem(null);
        priceField.setText("");
        durationSpinner.setValue(0);
    }

    private void fillInputs(CosmeticTreatment cosmeticTreatment){
        nameField.setText(cosmeticTreatment.getName());
        treatmentTypeComboBox.setSelectedItem(cosmeticTreatment.getTreatmentType());
        priceField.setText(Double.toString(priceListService.getPrice(cosmeticTreatment)));
        durationSpinner.setValue(cosmeticTreatment.getDuration());
    }

    private void setupListeners(){
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isEditing = false;
                clearInputs();
                enableInputs();
                table.clearSelection();
                nameField.requestFocus();
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

                cosmeticTreatmentToEdit = ((CosmeticTreatmentTableModel)table.getModel()).getCosmeticTreatment(row);

                editButton.setEnabled(true);
                removeButton.setEnabled(true);
                fillInputs(cosmeticTreatmentToEdit);
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isEditing = true;
                enableInputs();
                nameField.requestFocus();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if(row == -1){
                    JOptionPane.showMessageDialog(CosmeticTreatmentPanel.this, "Morate selektovati klijenta za brisanje!", "Greška", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int option = JOptionPane.showConfirmDialog(CosmeticTreatmentPanel.this, "Da li ste sigurni da želite da obrišete klijenta?", "Brisanje klijenta", JOptionPane.YES_NO_OPTION);
                if(option == JOptionPane.YES_OPTION){
                    CosmeticTreatment cosmeticTreatment = ((CosmeticTreatmentTableModel)table.getModel()).getCosmeticTreatment(row);
                    cosmeticTreatmentService.remove(cosmeticTreatment);
                    table.setModel(new CosmeticTreatmentTableModel(cosmeticTreatmentService.getData(), priceListService));
                    clearInputs();
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                String name = CosmeticTreatmentPanel.this.nameField.getText().trim();
                TreatmentType treatmentType = (TreatmentType) treatmentTypeComboBox.getSelectedItem();
                double price = 0;
                try{
                    price = Double.parseDouble(priceField.getText().trim());
                } catch (NumberFormatException ex){
                    JOptionPane.showMessageDialog(CosmeticTreatmentPanel.this, "Cena mora biti broj!", "Greška", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int duration = (int) durationSpinner.getValue();

                if(!validateInput(name, treatmentType, price, duration)){
                    return;
                }

                if(isEditing){
                    cosmeticTreatmentToEdit.setName(name);
                    cosmeticTreatmentToEdit.setTreatmentType(treatmentType);
                    priceListService.add(cosmeticTreatmentToEdit.getId(), price);
                    cosmeticTreatmentToEdit.setDuration(duration);
                } else {
                    cosmeticTreatmentService.add(name, treatmentType, price, duration);
                }
                table.setModel(new CosmeticTreatmentTableModel(cosmeticTreatmentService.getData(), priceListService));
                disableInputs();
                clearInputs();
                table.clearSelection();
                editButton.setEnabled(false);
                removeButton.setEnabled(false);
                cosmeticTreatmentService.saveData();
                JOptionPane.showMessageDialog(CosmeticTreatmentPanel.this, "Podaci su uspešno sačuvani.", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disableInputs();
                if(isEditing){
                    fillInputs(cosmeticTreatmentToEdit);
                } else {
                    clearInputs();
                    table.clearSelection();
                    editButton.setEnabled(false);
                    removeButton.setEnabled(false);
                }
            }
        });
    }

    private boolean validateInput(String name, TreatmentType treatmentType, double price, int duration){
        if(name.trim().isEmpty()){
            JOptionPane.showMessageDialog(CosmeticTreatmentPanel.this, "Morate uneti ime!", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(treatmentType == null){
            JOptionPane.showMessageDialog(CosmeticTreatmentPanel.this, "Morate izabrati tip tretmana!", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(price <= 0){
            JOptionPane.showMessageDialog(CosmeticTreatmentPanel.this, "Cena mora biti veća od 0!", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(duration <= 0){
            JOptionPane.showMessageDialog(CosmeticTreatmentPanel.this, "Trajanje mora biti veće od 0!", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}
