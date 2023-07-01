package ui.manager;

import javax.swing.JTabbedPane;

import entity.Manager;
import ui.GUI;
import ui.login.LogoutCallback;

public class ManagerGUI extends GUI {
    private Manager user;

    private JTabbedPane tabbedPane;
    private SalonPanel salonPanel;
    private CosmeticTreatmentPanel cosmeticTreatmentPanel;
    private AppointmentPanel appointmentPanel;
    private EmployeePanel employeePanel;
    private ClientPanel clientPanel;

    public ManagerGUI(Manager user, LogoutCallback logoutCallback) {
        super(logoutCallback);
        this.user = user;
        setTitle("Menadžer - " + user.toString());
        setSize(1200, 650);
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        tabbedPane = new JTabbedPane();
        salonPanel = new SalonPanel(user);
        cosmeticTreatmentPanel = new CosmeticTreatmentPanel(user);
        appointmentPanel = new AppointmentPanel(user);
        employeePanel = new EmployeePanel(user);
        clientPanel = new ClientPanel(user);
    }

    private void setupLayout() {
        tabbedPane.addTab("Salon", salonPanel);
        tabbedPane.addTab("Kozmetički tretmani", cosmeticTreatmentPanel);
        tabbedPane.addTab("Zakazani tretmani", appointmentPanel);
        tabbedPane.addTab("Zaposleni", employeePanel);
        tabbedPane.addTab("Klijenti", clientPanel);
        add(tabbedPane);
    }

}
