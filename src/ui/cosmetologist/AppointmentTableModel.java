package ui.cosmetologist;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import entity.Appointment;
import entity.AppointmentStatus;

public class AppointmentTableModel extends AbstractTableModel {
    private ArrayList<Appointment> appointments;
    private String[] columnNames = {"Naziv", "Klijent", "Datum", "Vrijeme", "Trajanje", "Cijena", "Status"};
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");

    public AppointmentTableModel(ArrayList<Appointment> appointments) {
        this.appointments = appointments;
    }

    @Override
    public int getRowCount() {
        return appointments.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int row, int column) {
        Appointment appointment = appointments.get(row);
        switch (column) {
            case 0:
                return appointment.getCosmeticTreatment().getName();
            case 1:
                return appointment.getClient();
            case 2:
                return appointment.getDate().format(dateTimeFormatter);
            case 3:
                return appointment.getTime().toString();
            case 4:
                return appointment.getCosmeticTreatment().getDuration() + " min";
            case 5:
                return appointment.getPrice() + " rsd";
            case 6:
                AppointmentStatus status = appointment.getStatus();
                switch (status) {
                    case SCHEDULED:
                        return "Zakazan";
                    case CANCELED_BY_CLIENT:
                        return "Otkazao klijent";
                    case CANCELED_BY_SALON:
                        return "Otkazao salon";
                    case COMPLETED:
                        return "Završen";
                    case NOT_SHOWED_UP:
                        return "Nije se pojavio";
                    default:
                        return null;
                }
            default:
                return null;
        }
    }

    public void setAppointments(ArrayList<Appointment> appointments) {
        this.appointments = appointments;
        fireTableDataChanged();
    }

    public Appointment getAppointment(int row) {
        return appointments.get(row);
    }

    public void removeAppointment(int row) {
        appointments.remove(row);
        fireTableRowsDeleted(row, row);
    }
}
