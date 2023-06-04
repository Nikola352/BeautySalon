package service;

import java.util.ArrayList;

import entity.Cosmetologist;
import entity.Gender;
import entity.LevelOfEducation;
import entity.TreatmentType;
import utils.CsvUtil;

public class CosmetologistService extends Service<Cosmetologist> {
    private TreatmentTypeService treatmentTypeService = ServiceRegistry.getInstance().getTreatmentTypeService();

    public Cosmetologist add(String username, String password, String name, String lastname, Gender gender, String phoneNum, String address, LevelOfEducation levelOfEducation, int yearsOfExperience, double baseSalary, double bonus, double salary, ArrayList<TreatmentType> treatmentTypes) {
        Cosmetologist cosmetologist = new Cosmetologist(getNextId(), username, password, name, lastname, gender, phoneNum, address, levelOfEducation, yearsOfExperience, baseSalary, bonus, salary, treatmentTypes);
        add(cosmetologist);
        incrementNextId();
        return cosmetologist;
    }

    public Cosmetologist add(String username, String password, String name, String lastname, Gender gender, String phoneNum, String address, LevelOfEducation levelOfEducation, int yearsOfExperience, double baseSalary, double bonus, ArrayList<TreatmentType> treatmentTypes) {
        Cosmetologist cosmetologist = new Cosmetologist(getNextId(), username, password, name, lastname, gender, phoneNum, address, levelOfEducation, yearsOfExperience, baseSalary, bonus, treatmentTypes);
        add(cosmetologist);
        incrementNextId();
        return cosmetologist;
    }

    public Cosmetologist getByUsername(String username) {
        ArrayList<Cosmetologist> cosmetologists = getData();
        for (Cosmetologist cosmetologist : cosmetologists) {
            if (cosmetologist.getUsername().equals(username)) {
                return cosmetologist;
            }
        }
        return null;
    }

    public ArrayList<Cosmetologist> getByTreatmentType(TreatmentType treatmentType) {
        ArrayList<Cosmetologist> cosmetologists = getData();
        ArrayList<Cosmetologist> result = new ArrayList<Cosmetologist>();
        for (Cosmetologist cosmetologist : cosmetologists) {
            if (cosmetologist.getTreatmentTypes().contains(treatmentType)) {
                result.add(cosmetologist);
            }
        }
        return result;
    }

    public Cosmetologist getRandomByTreatmentType(TreatmentType treatmentType) {
        ArrayList<Cosmetologist> cosmetologists = getByTreatmentType(treatmentType);
        if (cosmetologists.size() == 0) {
            return null;
        }
        return cosmetologists.get((int) (Math.random() * cosmetologists.size()));
    }

    @Override
    protected String getFilename() {
        return appSettings.getCosmetologistFilename();
    }

    @Override
    public void loadData(){
        try{
            ArrayList<String[]> cosmetologistStrings = CsvUtil.loadData(getFilename(), appSettings.getDelimiter());
            for (String[] cosmetologistString : cosmetologistStrings) {
                ArrayList<TreatmentType> treatmentTypes = new ArrayList<>();
                for (String treatmentTypeId : cosmetologistString[13].split(appSettings.getInnerDelimiter())) {
                    treatmentTypes.add(treatmentTypeService.getById(Integer.parseInt(treatmentTypeId)));
                }
                Cosmetologist cosmetologist = Cosmetologist.parseFromCsv(cosmetologistString, treatmentTypes);
                add(cosmetologist);
            }
            loadNextId();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
