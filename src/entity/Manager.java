package entity;

public class Manager extends Employee {
    public Manager() {
        super();
    }

    public Manager(int id, String username, String password, String name, String lastname, Gender gender, String phoneNum, String address, LevelOfEducation levelOfEducation, int yearsOfExperience, double baseSalary, double bonus, double salary){
        super(id, username, password, name, lastname, gender, phoneNum, address, levelOfEducation, yearsOfExperience, baseSalary, bonus, salary);
    }

    public static Manager parseFromCsv(String[] line){
        return new Manager(
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
            Double.parseDouble(line[12])
        );
    }

    public String[] toCsv(){
        return new String[]{
            String.valueOf(getId()),
            getUsername(),
            getPassword(),
            getName(),
            getLastname(),
            getGender().toString(),
            getPhoneNum(),
            getAddress(),
            getLevelOfEducation().toString(),
            String.valueOf(getYearsOfExperience()),
            String.valueOf(getBaseSalary()),
            String.valueOf(getBonus()),
            String.valueOf(getSalary())
        };
    }
}
