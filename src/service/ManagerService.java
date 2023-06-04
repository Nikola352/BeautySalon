package service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import entity.Gender;
import entity.LevelOfEducation;
import entity.Manager;
import utils.CsvUtil;

public class ManagerService extends Service<Manager> {
    public Manager add(String username, String password, String name, String lastname, Gender gender, String phoneNum, String address, LevelOfEducation levelOfEducation, int yearsOfExperience, double baseSalary, double bonus){
        Manager manager = new Manager(getNextId(), username, password, name, lastname, gender, phoneNum, address, levelOfEducation, yearsOfExperience, baseSalary, bonus);
        add(manager);
        incrementNextId();
        return manager;
    }

    public Manager getByUsername(String username) {
        for (Manager manager : getData()) {
            if (manager.getUsername().equals(username)) {
                return manager;
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
        for (Manager manager : getData()) {
            totalSalary += manager.getSalary() * firstDays;
        }
        return totalSalary;
    }

    public void updateBonus(double bonusThreashold, double bonusValue) {
        for (Manager manager : getData()) {
            manager.updateBonus(bonusThreashold, bonusValue);
        }
    }

    @Override
    protected String getFilename() {
        return appSettings.getManagerFilename();
    }

    @Override
    public void loadData(){
        try {
            ArrayList<String[]> managerStrings = CsvUtil.loadData(getFilename(), appSettings.getDelimiter());
            for (String[] managerString : managerStrings) {
                Manager manager = Manager.parseFromCsv(managerString);
                add(manager);
            }
            loadNextId();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
