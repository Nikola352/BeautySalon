package ui.cosmetologist;

import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import entity.Cosmetologist;
import entity.TreatmentType;

public class TreatmentTypePanel extends JPanel {
    private Cosmetologist user;

    private JTable table;
    private TableRowSorter<AbstractTableModel> tableSorter = new TableRowSorter<AbstractTableModel>();

    public TreatmentTypePanel(Cosmetologist user) {
        this.user = user;
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        ArrayList<TreatmentType> treatmentTypes = user.getTreatmentTypes();
        table = new JTable(new TreatmentTypeTableModel(treatmentTypes));
        table.getTableHeader().setReorderingAllowed(false);
        tableSorter.setModel((AbstractTableModel) table.getModel());
        table.setRowSorter(tableSorter);
    }

    private void setupLayout() {
        add(new JScrollPane(table), "grow, push, wrap");
    }
}
