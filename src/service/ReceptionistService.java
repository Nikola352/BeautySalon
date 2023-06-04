package service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import entity.Gender;
import entity.LevelOfEducation;
import entity.Receptionist;
import utils.CsvUtil;

public class ReceptionistService extends Service<Receptionist> {
    public Receptionist add(String username, String password, String name, String lastname, Gender gender, String phoneNum, String address, LevelOfEducation levelOfEducation, int yearsOfExperience, double baseSalary, double bonus, double salary){
        Receptionist receptionist = new Receptionist(getNextId(), username, password, name, lastname, gender, phoneNum, address, levelOfEducation, yearsOfExperience, baseSalary, bonus, salary);
        add(receptionist);
        incrementNextId();
        return receptionist;
    }

    public Receptionist add(String username, String password, String name, String lastname, Gender gender, String phoneNum, String address, LevelOfEducation levelOfEducation, int yearsOfExperience, double baseSalary){
        Receptionist receptionist = new Receptionist(getNextId(), username, password, name, lastname, gender, phoneNum, address, levelOfEducation, yearsOfExperience, baseSalary, 0.0);
        add(receptionist);
        incrementNextId();
        return receptionist;
    }

    public Receptionist getByUsername(String username) {
        for (Receptionist receptionist : getData()) {
            if (receptionist.getUsername().equals(username)) {
                return receptionist;
            }
        }
        return null;
    }

    public double getTotalSalaryForTimePeriod(LocalDate startDate, LocalDate endDate) {
        int firstDays = 0;
        LocalDate firstDayOfMonth = startDate;
        while (firstDayOfMonth.isBefore(endDate)) {
            if (firstDayOfMonth.getDayOfMonth() == 1) {
                firstDays++;
            }
            firstDayOfMonth = firstDayOfMonth.plusDays(1);
        }

        double totalSalary = 0;
        for (Receptionist receptionist : getData()) {
            totalSalary += receptionist.getSalary() * firstDays;
        }
        return totalSalary;
    }

    public void updateBonus(double bonusThreashold, double bonusValue) {
        for (Receptionist receptionist : getData()) {
            receptionist.updateBonus(bonusThreashold, bonusValue);
        }
    }

    @Override
    protected String getFilename() {
        return appSettings.getReceptionistFilename();
    }

    @Override
    public void loadData() {
        try {
            ArrayList<String[]> receptionistStrings = CsvUtil.loadData(getFilename(), appSettings.getDelimiter());
            for (String[] receptionistString : receptionistStrings) {
                Receptionist receptionist = Receptionist.parseFromCsv(receptionistString);
                add(receptionist);
            }
            loadNextId();
        } catch (IOException e) {
            System.out.println("Error loading receptionists");
        }
    }
}
