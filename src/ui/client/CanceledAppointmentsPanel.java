package ui.client;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import entity.AppointmentStatus;
import entity.Client;
import net.miginfocom.swing.MigLayout;
import service.AppointmentService;
import service.ServiceRegistry;

public class CanceledAppointmentsPanel extends JPanel {
    private Client user;

    private AppointmentService appointmentService;

    private JTable canceledByClientTable;
    private JTable canceledBySalonTable;
    private TableRowSorter<AbstractTableModel> canceledByClientTableSorter = new TableRowSorter<AbstractTableModel>();
    private TableRowSorter<AbstractTableModel> canceledBySalonTableSorter = new TableRowSorter<AbstractTableModel>();
    private JLabel canceledByClientEmptyLabel;
    private JLabel canceledBySalonEmptyLabel;

    public CanceledAppointmentsPanel(Client user) {
        this.user = user;
        appointmentService = ServiceRegistry.getInstance().getAppointmentService();
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents(){
        canceledByClientEmptyLabel = new JLabel("Nema otkazanih tretmana.");
        canceledByClientEmptyLabel.setVisible(false);

        canceledBySalonEmptyLabel = new JLabel("Nema otkazanih tretmana.");
        canceledBySalonEmptyLabel.setVisible(false);

        canceledByClientTable = new JTable(new AppointmentTableModel(appointmentService.getByClientAndStatus(user, AppointmentStatus.CANCELED_BY_CLIENT)));
        canceledByClientTable.getTableHeader().setReorderingAllowed(false);
        canceledByClientTableSorter.setModel((AbstractTableModel) canceledByClientTable.getModel());
        canceledByClientTable.setRowSorter(canceledByClientTableSorter);

        canceledBySalonTable = new JTable(new AppointmentTableModel(appointmentService.getByClientAndStatus(user, AppointmentStatus.CANCELED_BY_SALON)));
        canceledBySalonTable.getTableHeader().setReorderingAllowed(false);
        canceledBySalonTableSorter.setModel((AbstractTableModel) canceledBySalonTable.getModel());
        canceledBySalonTable.setRowSorter(canceledBySalonTableSorter);

        if (canceledByClientTable.getRowCount() == 0) {
            canceledByClientEmptyLabel.setVisible(true);
        }

        if (canceledBySalonTable.getRowCount() == 0) {
            canceledBySalonEmptyLabel.setVisible(true);
        }
    }

    private void setupLayout(){
        setLayout(new MigLayout("fill"));
        add(new JLabel("Otkazani tretmani od strane klijenta:"), "wrap");
        add(new JScrollPane(canceledByClientTable), "grow, push, wrap");
        add(canceledByClientEmptyLabel, "center, wrap");
        add(new JLabel("Otkazani tretmani od strane salona:"), "wrap");
        add(new JScrollPane(canceledBySalonTable), "grow, push, wrap");
        add(canceledBySalonEmptyLabel, "center");
    }
    
    public void updateTables(){
        ((AppointmentTableModel) canceledByClientTable.getModel()).setAppointments((appointmentService.getByClientAndStatus(user, AppointmentStatus.CANCELED_BY_CLIENT)));
        ((AppointmentTableModel) canceledBySalonTable.getModel()).setAppointments((appointmentService.getByClientAndStatus(user, AppointmentStatus.CANCELED_BY_SALON)));
        if (canceledByClientTable.getRowCount() == 0) {
            canceledByClientEmptyLabel.setVisible(true);
        } else {
            canceledByClientEmptyLabel.setVisible(false);
        }
        if (canceledBySalonTable.getRowCount() == 0) {
            canceledBySalonEmptyLabel.setVisible(true);
        } else {
            canceledBySalonEmptyLabel.setVisible(false);
        }
    }
}
