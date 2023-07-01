package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import ui.cosmetologist.CosmetologistGUI;
import ui.login.LogoutCallback;
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

    public void setTreatmentTypes(ArrayList<TreatmentType> treatmentTypes) {
        this.treatmentTypes = treatmentTypes;
    }

    public void addTreatmentType(TreatmentType treatmentType){
        getTreatmentTypes().add(treatmentType);
    }

    public void removeTreatmentType(TreatmentType treatmentType){
        getTreatmentTypes().remove(treatmentType);
    }

    public ArrayList<Integer[]> getTimetable(ArrayList<Appointment> appointments, LocalDate datum){
        ArrayList<Integer[]> timetable = new ArrayList<Integer[]>();
        for (Appointment appointment : appointments) {
            if (appointment.getCosmetologist().equals(this) && appointment.getDate().equals(datum) && appointment.getStatus().equals(AppointmentStatus.SCHEDULED)){
                timetable.add(new Integer[]{appointment.getTime().getHour(), appointment.getCosmeticTreatment().getDuration()});
            }
        }
        Collections.sort(timetable, (a, b) -> a[0] - b[0]);
        return timetable;
    }

    public ArrayList<Integer> getFreeHours(ArrayList<Appointment> appointments, LocalDate datum, int openingHour, int closingHour, int treatmentDuration){
        ArrayList<Integer> freeHours = new ArrayList<Integer>();
        ArrayList<Integer[]> timetable = getTimetable(appointments, datum); // [0]->start hour, [1]->duration in minutes
        for(int hour = openingHour; hour < closingHour; hour++){
            boolean isFree = true;
            for (Integer[] appointment : timetable) {
                if (hour >= appointment[0] && hour < appointment[0] + appointment[1] / 60){
                    isFree = false;
                    break;
                } else if (hour + treatmentDuration > appointment[0] && hour + treatmentDuration <= appointment[0] + appointment[1] / 60){
                    isFree = false;
                    break;
                }
            }
            if (isFree){
                freeHours.add(hour);
            }
        }
        return freeHours;
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
    public void showGUI(LogoutCallback logoutCallback){
        CosmetologistGUI cosmetologistGUI = new CosmetologistGUI(this, logoutCallback);
        cosmetologistGUI.setVisible(true);
    }
}
