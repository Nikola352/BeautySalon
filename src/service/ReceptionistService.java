package service;

import java.io.IOException;
import java.util.ArrayList;

import entity.Gender;
import entity.LevelOfEducation;
import entity.Receptionist;
import utils.CsvUtil;

public class ReceptionistService extends Service<Receptionist> {
    public void add(String username, String password, String name, String lastname, Gender gender, String phoneNum, String address, LevelOfEducation levelOfEducation, int yearsOfExperience, double baseSalary, double bonus, double salary){
        Receptionist receptionist = new Receptionist(getNextId(), username, password, name, lastname, gender, phoneNum, address, levelOfEducation, yearsOfExperience, baseSalary, bonus, salary);
        add(receptionist);
        incrementNextId();
    }

    public void add(String username, String password, String name, String lastname, Gender gender, String phoneNum, String address, LevelOfEducation levelOfEducation, int yearsOfExperience, double baseSalary){
        Receptionist receptionist = new Receptionist(getNextId(), username, password, name, lastname, gender, phoneNum, address, levelOfEducation, yearsOfExperience, baseSalary, 0.0);
        add(receptionist);
        incrementNextId();
    }

    public Receptionist getByUsername(String username) {
        for (Receptionist receptionist : getData()) {
            if (receptionist.getUsername().equals(username)) {
                return receptionist;
            }
        }
        return null;
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
