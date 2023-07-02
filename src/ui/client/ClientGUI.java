package ui.client;

import java.awt.BorderLayout;

import javax.swing.JTabbedPane;

import entity.Client;
import ui.GUI;
import ui.login.LogoutCallback;

public class ClientGUI extends GUI {
    private Client user;

    private JTabbedPane tabbedPane;
    private ScheduledAppointmentsPanel appointmentsPanel;
    private CanceledAppointmentsPanel canceledAppointmentsPanel;
    private CompletedAppointmentsPanel completedAppointmentsPanel;
    private SchedulePanel schedulePanel;
    private GeneralInfoPanel generalInfoPanel;
    
    public ClientGUI(Client user, LogoutCallback logoutCallback) {
        super(logoutCallback);
        this.user = user;
        setTitle("Klijent - " + user.toString());
        setSize(800, 600);
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        tabbedPane = new JTabbedPane();
        appointmentsPanel = new ScheduledAppointmentsPanel(user, this);
        canceledAppointmentsPanel = new CanceledAppointmentsPanel(user);
        completedAppointmentsPanel = new CompletedAppointmentsPanel(user);
        schedulePanel = new SchedulePanel(user, this);
        generalInfoPanel = new GeneralInfoPanel(user);
    }

    private void setupLayout() {
        tabbedPane.addTab("Zakazani tretmani", appointmentsPanel);
        tabbedPane.addTab("Otkazani tretmani", canceledAppointmentsPanel);
        tabbedPane.addTab("Završeni tretmani", completedAppointmentsPanel);
        tabbedPane.addTab("Zakaži tretman", schedulePanel);
        tabbedPane.addTab("Opšti podaci", generalInfoPanel);
        add(tabbedPane, BorderLayout.CENTER);
    }

    protected void updateTables(){
        appointmentsPanel.updateTable();
        canceledAppointmentsPanel.updateTables();
        completedAppointmentsPanel.updateTable();
    }

}
