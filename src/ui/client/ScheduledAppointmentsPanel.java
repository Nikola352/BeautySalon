package ui.client;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import entity.Appointment;
import entity.AppointmentStatus;
import entity.Client;
import net.miginfocom.swing.MigLayout;
import service.AppointmentService;
import service.ServiceRegistry;

public class ScheduledAppointmentsPanel extends JPanel {
    private Client user;
    private ClientGUI parent;

    private AppointmentService appointmentService;

    private JTable table;
    private TableRowSorter<AbstractTableModel> tableSorter = new TableRowSorter<AbstractTableModel>();
    private JButton cancelButton;
    private JLabel emptyLabel;

    public ScheduledAppointmentsPanel(Client user, ClientGUI parent) {
        this.user = user;
        this.parent = parent;
        ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();
        appointmentService = serviceRegistry.getAppointmentService();
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    private void initializeComponents(){
        ArrayList<Appointment> appointments = appointmentService.getByClientAndStatus(user, AppointmentStatus.SCHEDULED);
        table = new JTable(new AppointmentTableModel(appointments));
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        tableSorter.setModel((AbstractTableModel) table.getModel());
        table.setRowSorter(tableSorter);
        cancelButton = new JButton("Otkaži tretman");
        cancelButton.setEnabled(false);
        emptyLabel = new JLabel("Nema zakazanih tretmana.");
        if(appointments.size() == 0){
            emptyLabel.setVisible(true);
        } else {
            emptyLabel.setVisible(false);
        }
    }

    private void setupLayout(){
        setLayout(new MigLayout("fill"));
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, "grow, push, wrap");
        add(emptyLabel, "center, wrap");
        add(cancelButton, "right");
    }

    private void setupListeners(){
        cancelButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if(selectedRow != -1){
                int option = JOptionPane.showConfirmDialog(null, "Da li ste sigurni da želite da otkažete tretman?", "Potvrda", JOptionPane.YES_NO_OPTION);
                if(option == JOptionPane.NO_OPTION){
                    return;
                }
                Appointment appointment = ((AppointmentTableModel) table.getModel()).getAppointment(selectedRow);
                appointmentService.cancel(appointment, user);
                ((AppointmentTableModel) table.getModel()).removeAppointment(selectedRow);
                JOptionPane.showMessageDialog(null, "Uspješno ste otkazali tretman", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
                parent.updateTables();
                if(table.getRowCount() == 0){
                    emptyLabel.setVisible(true);
                }
            }
        });
        table.getSelectionModel().addListSelectionListener(e -> {
            if(table.getSelectedRow() != -1){
                cancelButton.setEnabled(true);
            } else {
                cancelButton.setEnabled(false);
            }
        });
    }

    public void updateTable(){
        ArrayList<Appointment> appointments = appointmentService.getByClientAndStatus(user, AppointmentStatus.SCHEDULED);
        ((AppointmentTableModel) table.getModel()).setAppointments(appointments);
        if(appointments.size() == 0){
            emptyLabel.setVisible(true);
        } else {
            emptyLabel.setVisible(false);
        }
    }
}
