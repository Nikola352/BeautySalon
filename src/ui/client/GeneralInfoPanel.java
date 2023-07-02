package ui.client;

import javax.swing.JLabel;
import javax.swing.JPanel;

import entity.Client;
import entity.Gender;
import net.miginfocom.swing.MigLayout;
import java.awt.Font;

public class GeneralInfoPanel extends JPanel {
    Client user;

    public GeneralInfoPanel(Client user) {
        this.user = user;
        setupLayout();
    }

    private void setupLayout() {
        setLayout(new MigLayout("wrap 2, center"));

        JLabel titleLabel = new JLabel("Podaci o korisniku");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, "center, wrap, span 2");

        Font labelFont = new Font("Arial", Font.PLAIN, 20);

        add(new JLabel("Ime:"), "align label, split 2");
        JLabel nameValueLabel = new JLabel(user.getName());
        nameValueLabel.setFont(labelFont);
        add(nameValueLabel, "align center, wrap");

        add(new JLabel("Prezime:"), "align label, split 2");
        JLabel lastnameValueLabel = new JLabel(user.getLastname());
        lastnameValueLabel.setFont(labelFont);
        add(lastnameValueLabel, "align center, wrap");

        add(new JLabel("Korisničko ime"), "align label, split 2");
        JLabel usernameValueLabel = new JLabel(user.getUsername());
        usernameValueLabel.setFont(labelFont);
        add(usernameValueLabel, "align center, wrap");

        add(new JLabel("Adresa:"), "align label, split 2");
        JLabel addressValueLabel = new JLabel(user.getAddress());
        addressValueLabel.setFont(labelFont);
        add(addressValueLabel, "align center, wrap");

        add(new JLabel("Telefon:"), "align label, split 2");
        JLabel phoneNumValueLabel = new JLabel(user.getPhoneNum());
        phoneNumValueLabel.setFont(labelFont);
        add(phoneNumValueLabel, "align center, wrap");
        
        add(new JLabel("Pol:"), "align label, split 2");
        String gender = (user.getGender() == Gender.MALE ? "Muški" : "Ženski");
        JLabel genderValueLabel = new JLabel(gender);
        genderValueLabel.setFont(labelFont);
        add(genderValueLabel, "align center, wrap");

        add(new JLabel("Ukupno potrošeno:"), "align label, split 2");
        JLabel totalSpentValueLabel = new JLabel(String.format("%.2f", user.getTotalSpent()));
        totalSpentValueLabel.setFont(labelFont);
        add(totalSpentValueLabel, "align center, wrap");

        add(new JLabel("Ima karticu:"), "align label, split 2");
        JLabel hasLoyaltyCardValueLabel = new JLabel(user.getHasLoyaltyCard() ? "Da" : "Ne");
        hasLoyaltyCardValueLabel.setFont(labelFont);
        add(hasLoyaltyCardValueLabel, "align center, wrap");   
    }
}

