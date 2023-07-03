package ui.cosmetologist;

import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import entity.Appointment;
import entity.AppointmentStatus;
import entity.Cosmetologist;
import net.miginfocom.swing.MigLayout;
import service.AppointmentService;
import service.ServiceRegistry;

public class AppointmentPanel extends JPanel {
    private Cosmetologist user;

    private AppointmentService appointmentService;

    private JTable table;
    private TableRowSorter<AbstractTableModel> tableSorter = new TableRowSorter<AbstractTableModel>();
    private JCheckBox showOnlyScheduledCheckBox;
    private JButton cancelButton;
    private JButton completeButton;

    public AppointmentPanel(Cosmetologist user) {
        this.user = user;
        appointmentService = ServiceRegistry.getInstance().getAppointmentService();
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    private void initializeComponents() {
        table = new JTable(new AppointmentTableModel(appointmentService.getByCosmetologist(user)));
        table.getTableHeader().setReorderingAllowed(false);
        tableSorter.setModel((AbstractTableModel) table.getModel());
        table.setRowSorter(tableSorter);
        showOnlyScheduledCheckBox = new JCheckBox("Prikaži samo zakazane");
        showOnlyScheduledCheckBox.setSelected(false);
        cancelButton = new JButton("Otkaži");
        completeButton = new JButton("Kompletiraj");
        cancelButton.setEnabled(false);
        completeButton.setEnabled(false);
    }

    private void setupLayout() {
        setLayout(new MigLayout("fill"));
        add(new JScrollPane(table), "grow, push, wrap");
        add(showOnlyScheduledCheckBox, "wrap");
        add(cancelButton, "split 2, center");
        add(completeButton, "wrap");
    }

    private void setupListeners() {
        showOnlyScheduledCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                ArrayList<Appointment> appointments = appointmentService.getByCosmetologist(user);
                if (showOnlyScheduledCheckBox.isSelected()) {
                    appointments = appointmentService.getByCosmetologistAndStatus(user, AppointmentStatus.SCHEDULED);
                } 
                table.setModel(new AppointmentTableModel(appointments));
                tableSorter.setModel((AbstractTableModel)table.getModel());
                table.setRowSorter(tableSorter);
                table.repaint();
            }
        });

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(table.getSelectedRow() == -1){
                    cancelButton.setEnabled(false);
                    completeButton.setEnabled(false);
                    return;
                }
                Appointment appointment = ((AppointmentTableModel) table.getModel()).getAppointment(table.getSelectedRow());
                if(appointment == null || appointment.getStatus() != AppointmentStatus.SCHEDULED){
                    cancelButton.setEnabled(false);
                    completeButton.setEnabled(false);
                } else {
                    cancelButton.setEnabled(true);
                    completeButton.setEnabled(true);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int option = JOptionPane.showConfirmDialog(AppointmentPanel.this, "Da li ste sigurni da želite da otkažete zakazani termin?", "Potvrda", JOptionPane.YES_NO_OPTION);
                    if(option == JOptionPane.NO_OPTION){
                        return;
                    }
                    Appointment appointment = ((AppointmentTableModel) table.getModel()).getAppointment(selectedRow);
                    appointmentService.cancel(appointment, user);
                    table.repaint();
                    cancelButton.setEnabled(false);
                    completeButton.setEnabled(false);
                    JOptionPane.showMessageDialog(AppointmentPanel.this, "Uspešno ste otkazali zakazani termin.");
                }
            }
        });

        completeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int option = JOptionPane.showConfirmDialog(AppointmentPanel.this, "Da li ste sigurni da želite da kompletirate zakazani termin?", "Potvrda", JOptionPane.YES_NO_OPTION);
                    if(option == JOptionPane.NO_OPTION){
                        return;
                    }
                    Appointment appointment = ((AppointmentTableModel) table.getModel()).getAppointment(selectedRow);
                    appointment.setStatus(AppointmentStatus.COMPLETED);
                    table.repaint();
                    cancelButton.setEnabled(false);
                    completeButton.setEnabled(false);
                    JOptionPane.showMessageDialog(AppointmentPanel.this, "Uspešno ste kompletirali zakazani termin.");
                }
            }
        });
    }
}
