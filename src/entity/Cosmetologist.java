package entity;

import java.util.ArrayList;

import ui.CosmetologistCLI;
import utils.AppSettings;

public class Cosmetologist extends Employee {
    ArrayList<TreatmentType> treatmentTypes;

    public Cosmetologist() {
        super();
    }

    public Cosmetologist(int id, String username, String password, String name, String lastname, Gender gender, String phoneNum, String address, LevelOfEducation levelOfEducation, int yearsOfExperience, double baseSalary, double bonus, double salary){
        super(id, username, password, name, lastname, gender, phoneNum, address, levelOfEducation, yearsOfExperience, baseSalary, bonus, salary);
        setTreatmentTypes(new ArrayList<TreatmentType>());
    }

    public Cosmetologist(int id, String username, String password, String name, String lastname, Gender gender, String phoneNum, String address, LevelOfEducation levelOfEducation, int yearsOfExperience, double baseSalary, double bonus, double salary, ArrayList<TreatmentType> treatmentTypes){
        super(id, username, password, name, lastname, gender, phoneNum, address, levelOfEducation, yearsOfExperience, baseSalary, bonus, salary);
        setTreatmentTypes(treatmentTypes);
    }

    public Cosmetologist(int id, String username, String password, String name, String lastname, Gender gender, String phoneNum, String address, LevelOfEducation levelOfEducation, int yearsOfExperience, double baseSalary, double bonus){
        super(id, username, password, name, lastname, gender, phoneNum, address, levelOfEducation, yearsOfExperience, baseSalary, bonus);
        setTreatmentTypes(new ArrayList<TreatmentType>());
    }

    public Cosmetologist(int id, String username, String password, String name, String lastname, Gender gender, String phoneNum, String address, LevelOfEducation levelOfEducation, int yearsOfExperience, double baseSalary, double bonus, ArrayList<TreatmentType> treatmentTypes){
        super(id, username, password, name, lastname, gender, phoneNum, address, levelOfEducation, yearsOfExperience, baseSalary, bonus);
        setTreatmentTypes(treatmentTypes);
    }

    public ArrayList<TreatmentType> getTreatmentTypes() {
        return this.treatmentTypes;
    }

    private void setTreatmentTypes(ArrayList<TreatmentType> treatmentTypes) {
        this.treatmentTypes = treatmentTypes;
    }

    public void addTreatmentType(TreatmentType treatmentType){
        getTreatmentTypes().add(treatmentType);
    }

    public void removeTreatmentType(TreatmentType treatmentType){
        getTreatmentTypes().remove(treatmentType);
    }

    public static Cosmetologist parseFromCsv(String[] line, ArrayList<TreatmentType> treatmentTypes){
        return new Cosmetologist(
            Integer.parseInt(line[0]),
            line[1],
            line[2],
            line[3],
            line[4],
            Gender.valueOf(line[5]),
            line[6],
            line[7],
            LevelOfEducation.valueOf(line[8]),
            Integer.parseInt(line[9]),
            Double.parseDouble(line[10]),
            Double.parseDouble(line[11]),
            Double.parseDouble(line[12]),
            treatmentTypes
        );
    }

    public String[] toCsv(){
        ArrayList<String> treatmentTypeIds = new ArrayList<String>();
        for (TreatmentType treatmentType : getTreatmentTypes()) {
            treatmentTypeIds.add(Integer.toString(treatmentType.getId()));
        }
        String treatmentTypesString = String.join(AppSettings.getInstance().getInnerDelimiter(), treatmentTypeIds);
        return new String[]{
            Integer.toString(getId()),
            getUsername(),
            getPassword(),
            getName(),
            getLastname(),
            getGender().toString(),
            getPhoneNum(),
            getAddress(),
            getLevelOfEducation().toString(),
            Integer.toString(getYearsOfExperience()),
            Double.toString(getBaseSalary()),
            Double.toString(getBonus()),
            Double.toString(getSalary()),
            treatmentTypesString
        };
    }

    @Override
    public boolean showCLI(){
        CosmetologistCLI CosmetologistCLI = new CosmetologistCLI(this);
        return CosmetologistCLI.run();
    }
}
