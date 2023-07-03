package ui.manager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import entity.TreatmentType;
import entity.Manager;
import net.miginfocom.swing.MigLayout;
import service.TreatmentTypeService;
import service.ServiceRegistry;
import ui.cosmetologist.TreatmentTypeTableModel;

public class TreatmentTypePanel extends JPanel {
    Manager user;

    private TreatmentTypeService treatmentTypeService;

    boolean isEditing = false;
    TreatmentType treatmentTypeToEdit = null;

    private JTable table;
    private TableRowSorter<AbstractTableModel> tableSorter = new TableRowSorter<AbstractTableModel>();
    private JButton addButton;
    private JButton editButton;
    private JButton removeButton;

    private JTextField nameField;
    private JTextField descriptionField;
    private JButton saveButton;
    private JButton cancelButton;

    public TreatmentTypePanel(Manager user) {
        this.user = user;
        ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();
        treatmentTypeService = serviceRegistry.getTreatmentTypeService();
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    private void initializeComponents(){
        ArrayList<TreatmentType> treatmentTypes = treatmentTypeService.getData();
        TreatmentTypeTableModel treatmentTypeTableModel = new TreatmentTypeTableModel(treatmentTypes);
        table = new JTable(treatmentTypeTableModel);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        tableSorter.setModel(treatmentTypeTableModel);
        table.setRowSorter(tableSorter);

        addButton = new JButton("Dodaj");
        editButton = new JButton("Izmeni");
        removeButton = new JButton("Ukloni");

        nameField = new JTextField(20);
        descriptionField = new JTextField(20);

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
        rightPanel.setLayout(new MigLayout("wrap 1, center", "[center, grow]", "[]20[][][][]20[center][center]"));

        rightPanel.add(new JLabel("Kozmetički tretman"), "wrap, center");

        rightPanel.add(new JLabel("Ime"), "grow, wrap");
        rightPanel.add(nameField, "grow, wrap");

        rightPanel.add(new JLabel("Opis"), "grow, wrap");
        rightPanel.add(descriptionField, "grow, wrap");

        rightPanel.add(saveButton, "grow, wrap");
        rightPanel.add(cancelButton, "grow, wrap");

        add(rightPanel, "span 1 2, grow");
    }

    private void enableInputs(){
        // enable right panel
        nameField.setEnabled(true);
        descriptionField.setEnabled(true);
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
        descriptionField.setEnabled(false);
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
        descriptionField.setText("");
    }

    private void fillInputs(TreatmentType treatmentType){
        nameField.setText(treatmentType.getName());
        descriptionField.setText(treatmentType.getDescription());
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

                treatmentTypeToEdit = ((TreatmentTypeTableModel)table.getModel()).getTreatmentType(row);

                editButton.setEnabled(true);
                removeButton.setEnabled(true);
                fillInputs(treatmentTypeToEdit);
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
                    JOptionPane.showMessageDialog(TreatmentTypePanel.this, "Morate selektovati klijenta za brisanje!", "Greška", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int option = JOptionPane.showConfirmDialog(TreatmentTypePanel.this, "Da li ste sigurni da želite da obrišete klijenta?", "Brisanje klijenta", JOptionPane.YES_NO_OPTION);
                if(option == JOptionPane.YES_OPTION){
                    TreatmentType treatmentType = ((TreatmentTypeTableModel)table.getModel()).getTreatmentType(row);
                    treatmentTypeService.remove(treatmentType);
                    table.setModel(new TreatmentTypeTableModel(treatmentTypeService.getData()));
                    clearInputs();
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                String name = TreatmentTypePanel.this.nameField.getText().trim();
                String description = TreatmentTypePanel.this.descriptionField.getText().trim();

                if(name.equals("")){
                    JOptionPane.showMessageDialog(TreatmentTypePanel.this, "Morate uneti ime klijenta!", "Greška", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if(description.equals("")){
                    JOptionPane.showMessageDialog(TreatmentTypePanel.this, "Morate uneti prezime klijenta!", "Greška", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if(isEditing){
                    treatmentTypeToEdit.setName(name);
                    treatmentTypeToEdit.setDescription(description);
                } else {
                    treatmentTypeService.add(name, description);
                }
                table.setModel(new TreatmentTypeTableModel(treatmentTypeService.getData()));
                disableInputs();
                clearInputs();
                table.clearSelection();
                editButton.setEnabled(false);
                removeButton.setEnabled(false);
                treatmentTypeService.saveData();
                JOptionPane.showMessageDialog(TreatmentTypePanel.this, "Podaci su uspešno sačuvani.", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disableInputs();
                if(isEditing){
                    fillInputs(treatmentTypeToEdit);
                } else {
                    clearInputs();
                    table.clearSelection();
                    editButton.setEnabled(false);
                    removeButton.setEnabled(false);
                }
            }
        });
    }
}
