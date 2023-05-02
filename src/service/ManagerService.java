package service;

import java.io.IOException;
import java.util.ArrayList;

import entity.Gender;
import entity.LevelOfEducation;
import entity.Manager;
import utils.CsvUtil;

public class ManagerService extends Service<Manager> {
    public void addManager(String username, String password, String name, String lastname, Gender gender, String phoneNum, String address, LevelOfEducation levelOfEducation, int yearsOfExperience, double baseSalary, double bonus, double salary){
        Manager manager = new Manager(getNextId(), username, password, name, lastname, gender, phoneNum, address, levelOfEducation, yearsOfExperience, baseSalary, bonus, salary);
        add(manager);
        incrementNextId();
    }

    public Manager getByUsername(String username) {
        for (Manager manager : getData()) {
            if (manager.getUsername().equals(username)) {
                return manager;
            }
        }
        return null;
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
