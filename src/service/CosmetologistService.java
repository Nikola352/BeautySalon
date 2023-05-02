package service;

import java.util.ArrayList;

import entity.Cosmetologist;
import entity.Gender;
import entity.LevelOfEducation;
import entity.TreatmentType;
import utils.CsvUtil;

public class CosmetologistService extends Service<Cosmetologist> {
    private TreatmentTypeService treatmentTypeService = ServiceRegistry.getInstance().getTreatmentTypeService();

    public void add(String username, String password, String name, String lastname, Gender gender, String phoneNum, String address, LevelOfEducation levelOfEducation, int yearsOfExperience, double baseSalary, double bonus, double salary, ArrayList<TreatmentType> treatmentTypes) {
        Cosmetologist cosmetologist = new Cosmetologist(getNextId(), username, password, name, lastname, gender, phoneNum, address, levelOfEducation, yearsOfExperience, baseSalary, bonus, salary, treatmentTypes);
        add(cosmetologist);
        incrementNextId();
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
