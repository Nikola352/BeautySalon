package ui.manager;

import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import entity.BeautySalon;
import entity.DayOfWeek;
import entity.Manager;
import net.miginfocom.swing.MigLayout;
import service.BeautySalonService;
import service.ServiceRegistry;
import ui.manager.report.AppointmentsByStatusReport;
import ui.manager.report.CosmetologistReport;
import ui.manager.report.LastYearIncomeReport;

public class SalonPanel extends JPanel {
    Manager user;

    private BeautySalonService beautySalonService;

    private JTextField nameTextField;
    private JSpinner openingHourSpinner;
    private JSpinner closingHourSpinner;

    private JCheckBox mondayCheckBox;
    private JCheckBox tuesdayCheckBox;
    private JCheckBox wednesdayCheckBox;
    private JCheckBox thursdayCheckBox;
    private JCheckBox fridayCheckBox;
    private JCheckBox saturdayCheckBox;
    private JCheckBox sundayCheckBox;

    private JSpinner bonusThresholdSpinner;
    private JSpinner bonusAmountSpinner;
    private JSpinner loyaltyCardThresholdSpinner;
    private JSpinner loyaltyCardDiscountSpinner;

    private JButton saveButton;

    private JButton updateBonusButton;
    private JButton updateLoyaltyCardButton;

    private JButton report1Button;
    private JButton report2Button;
    private JButton report3Button;

    public SalonPanel(Manager user) {
        this.user = user;
        ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();
        beautySalonService = serviceRegistry.getBeautySalonService();
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    void initializeComponents(){
        BeautySalon beautySalon = beautySalonService.getBeautySalon();
        nameTextField = new JTextField(beautySalon.getName());
        openingHourSpinner = new JSpinner(new SpinnerNumberModel(beautySalon.getOpeningHour(), 0, 23, 1));
        closingHourSpinner = new JSpinner(new SpinnerNumberModel(beautySalon.getClosingHour(), 0, 23, 1));
        
        ArrayList<DayOfWeek> workingDays = beautySalon.getWorkingDays();
        mondayCheckBox = new JCheckBox("Ponedeljak", workingDays.contains(DayOfWeek.MONDAY));
        tuesdayCheckBox = new JCheckBox("Utorak", workingDays.contains(DayOfWeek.TUESDAY));
        wednesdayCheckBox = new JCheckBox("Sreda", workingDays.contains(DayOfWeek.WEDNESDAY)); 
        thursdayCheckBox = new JCheckBox("Četvrtak", workingDays.contains(DayOfWeek.THURSDAY));
        fridayCheckBox = new JCheckBox("Petak", workingDays.contains(DayOfWeek.FRIDAY));
        saturdayCheckBox = new JCheckBox("Subota", workingDays.contains(DayOfWeek.SATURDAY));
        sundayCheckBox = new JCheckBox("Nedelja", workingDays.contains(DayOfWeek.SUNDAY));
    
        bonusThresholdSpinner = new JSpinner(new SpinnerNumberModel(beautySalon.getBonusThreshold(), 0, 100000, 100));
        bonusAmountSpinner = new JSpinner(new SpinnerNumberModel(beautySalon.getBonusAmount(), 0, 10000, 100));
        loyaltyCardThresholdSpinner = new JSpinner(new SpinnerNumberModel(beautySalon.getLoyaltyCardThreshold(), 0, 100000, 100));
        loyaltyCardDiscountSpinner = new JSpinner(new SpinnerNumberModel(beautySalon.getLoyaltyCardDiscount()*100, 0, 100, 1));
    
        saveButton = new JButton("Sačuvaj");
        updateBonusButton = new JButton("Dodijeli bonuse zaposlenima");
        updateLoyaltyCardButton = new JButton("Dodijeli kartice lojalnosti");
        report1Button = new JButton("Prihod za prethodnih 12 mjeseci po tipu tretmana");
        report2Button = new JButton("Prikaz angazovanja po kozmeticarima");
        report3Button = new JButton("Prikaz tretmana po statusu");
    }

    void setupLayout(){
        setLayout(new MigLayout("wrap 2, center", "20[][grow,fill]20", "[center]30[]20[]20[]20[]20[]20[]20[]20"));

        JLabel titleLabel = new JLabel("Nazi salona:");
        titleLabel.setFont(titleLabel.getFont().deriveFont(18f));
        add(titleLabel, "align label");
        nameTextField.setFont(nameTextField.getFont().deriveFont(18f));
        add(nameTextField, "growx, span 2");

        add(new JLabel("Radno vreme:"), "align label");
        add(new JLabel("od"), "align label, split 4");
        add(openingHourSpinner, "growx");
        add(new JLabel("do"), "align label");
        add(closingHourSpinner, "growx");

        add(new JLabel("Radni dani:"), "align label");
        add(mondayCheckBox, "split 7");
        add(tuesdayCheckBox);
        add(wednesdayCheckBox);
        add(thursdayCheckBox);
        add(fridayCheckBox);
        add(saturdayCheckBox);
        add(sundayCheckBox);

        add(new JLabel("Prag za bonus zaposlenih:"), "align label");
        add(bonusThresholdSpinner, "split 2");
        add(new JLabel("RSD"));

        add(new JLabel("Iznos bonusa za zaposlene:"), "align label");
        add(bonusAmountSpinner, "split 2");
        add(new JLabel("RSD"));

        add(new JLabel("Prag za popust na karticu:"), "align label");
        add(loyaltyCardThresholdSpinner, "split 2");
        add(new JLabel("RSD"));

        add(new JLabel("Iznos popusta na karticu:"), "align label");
        add(loyaltyCardDiscountSpinner, "split 2");
        add(new JLabel("%"));

        add(saveButton, "span 2, align center, grow");

        add(new JLabel("Ažuriranje podataka:"), "align label");
        add(updateBonusButton, "split 2, align center");
        add(updateLoyaltyCardButton, "align center");

        add(new JLabel("Izvještaji:"), "align label");
        add(report1Button, "split 3, align center");
        add(report2Button, "align center");
        add(report3Button, "align center");
    }

    void setupListeners(){
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if(nameTextField.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Morate uneti naziv salona.", "Greška", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if((int)openingHourSpinner.getValue() >= (int)closingHourSpinner.getValue()){
                    JOptionPane.showMessageDialog(null, "Radno vreme nije ispravno.", "Greška", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                BeautySalon beautySalon = beautySalonService.getBeautySalon();
                beautySalon.setName(nameTextField.getText());
                beautySalon.setOpeningHour((int)openingHourSpinner.getValue());
                beautySalon.setClosingHour((int)closingHourSpinner.getValue());
                ArrayList<DayOfWeek> workingDays = new ArrayList<DayOfWeek>();
                if(mondayCheckBox.isSelected()) workingDays.add(DayOfWeek.MONDAY);
                if(tuesdayCheckBox.isSelected()) workingDays.add(DayOfWeek.TUESDAY);
                if(wednesdayCheckBox.isSelected()) workingDays.add(DayOfWeek.WEDNESDAY);
                if(thursdayCheckBox.isSelected()) workingDays.add(DayOfWeek.THURSDAY);
                if(fridayCheckBox.isSelected()) workingDays.add(DayOfWeek.FRIDAY);
                if(saturdayCheckBox.isSelected()) workingDays.add(DayOfWeek.SATURDAY);
                if(sundayCheckBox.isSelected()) workingDays.add(DayOfWeek.SUNDAY);
                beautySalon.setWorkingDays(workingDays);
                beautySalon.setBonusThreshold((double)bonusThresholdSpinner.getValue());
                beautySalon.setBonusAmount((double)bonusAmountSpinner.getValue());
                beautySalon.setLoyaltyCardThreshold((double)loyaltyCardThresholdSpinner.getValue());
                beautySalon.setLoyaltyCardDiscount((double)loyaltyCardDiscountSpinner.getValue()/100.0);
                beautySalonService.updateEmployeeBonus();
                beautySalonService.updateLoyaltyCardStatus();
                beautySalonService.saveBeautySalon();
                JOptionPane.showMessageDialog(null, "Podaci su uspešno sačuvani.", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        updateBonusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                beautySalonService.updateEmployeeBonus();
                JOptionPane.showMessageDialog(null, "Bonusi su uspešno dodijeljeni.", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        updateLoyaltyCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                beautySalonService.updateLoyaltyCardStatus();
                JOptionPane.showMessageDialog(null, "Kartice lojalnosti su uspešno dodijeljene.", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        report1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                new LastYearIncomeReport().setVisible(true);
            }
        });

        report2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                new CosmetologistReport().setVisible(true);
            }
        });

        report3Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                new AppointmentsByStatusReport().setVisible(true);
            }
        });
    }


}


