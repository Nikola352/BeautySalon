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

public class CompletedAppointmentsPanel extends JPanel {
    private Client user;

    private AppointmentService appointmentService;

    private JTable table;
    private TableRowSorter<AbstractTableModel> tableSorter = new TableRowSorter<AbstractTableModel>();
    private JLabel emptyLabel; 

    public CompletedAppointmentsPanel(Client user) {
        this.user = user;
        appointmentService = ServiceRegistry.getInstance().getAppointmentService();
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents(){
        emptyLabel = new JLabel("Nema kompletiranih tretmana.");
        emptyLabel.setVisible(false);

        table = new JTable(new AppointmentTableModel(appointmentService.getByClientAndStatus(user, AppointmentStatus.COMPLETED)));
        table.getTableHeader().setReorderingAllowed(false);
        tableSorter.setModel((AbstractTableModel) table.getModel());
        table.setRowSorter(tableSorter);

        if (table.getRowCount() == 0) {
            emptyLabel.setVisible(true);
        }
    }

    private void setupLayout(){
        setLayout(new MigLayout("fill"));
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, "grow, push, wrap");
        add(emptyLabel, "center");
    }

    public void updateTable(){
        ((AppointmentTableModel) table.getModel()).setAppointments((appointmentService.getByClientAndStatus(user, AppointmentStatus.COMPLETED)));
        if (table.getRowCount() == 0) {
            emptyLabel.setVisible(true);
        } else {
            emptyLabel.setVisible(false);
        }
    }
}
