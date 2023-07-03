package ui.cosmetologist;

import java.awt.BorderLayout;

import javax.swing.JTabbedPane;

import entity.Cosmetologist;
import ui.GUI;
import ui.login.LogoutCallback;

public class CosmetologistGUI extends GUI {
    private Cosmetologist user;

    private JTabbedPane tabbedPane;
    private AppointmentPanel appointmentPanel;
    private TreatmentTypePanel treatmentTypePanel;

    public CosmetologistGUI(Cosmetologist user, LogoutCallback logoutCallback) {
        super(logoutCallback);
        this.user = user;
        setTitle(user.toString());
        setSize(700, 500);
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        tabbedPane = new JTabbedPane();
        appointmentPanel = new AppointmentPanel(user);
        treatmentTypePanel = new TreatmentTypePanel(user);
    }

    private void setupLayout() {
        tabbedPane.addTab("Zakazani tretmani", appointmentPanel);
        tabbedPane.addTab("Tipovi tretmana", treatmentTypePanel);
        add(tabbedPane, BorderLayout.CENTER);
    }
}
