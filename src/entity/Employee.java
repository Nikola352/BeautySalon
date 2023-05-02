package entity;

public abstract class Employee extends User {
    LevelOfEducation levelOfEducation;
    int yearsOfExperience;
    double baseSalary;
    double bonus;
    double salary;

    public Employee() {
        super();
    }

    public Employee(int id, String username, String password, String name, String lastname, Gender gender, String phoneNum, String address){
        super(id, username, password, name, lastname, gender, phoneNum, address);
        setLevelOfEducation(LevelOfEducation.UNSKILLED);
        setYearsOfExperience(0);
    }

    public Employee(int id, String username, String password, String name, String lastname, Gender gender, String phoneNum, String address, LevelOfEducation levelOfEducation, int yearsOfExperience, double baseSalary, double bonus, double salary){
        super(id, username, password, name, lastname, gender, phoneNum, address);
        setLevelOfEducation(levelOfEducation);
        setYearsOfExperience(yearsOfExperience);
        setBaseSalary(baseSalary);
        setBonus(bonus);
        setSalary(salary);
    }

    public Employee(int id, String username, String password, String name, String lastname, Gender gender, String phoneNum, String address, LevelOfEducation levelOfEducation, int yearsOfExperience, double baseSalary, double bonus){
        super(id, username, password, name, lastname, gender, phoneNum, address);
        setLevelOfEducation(levelOfEducation);
        setYearsOfExperience(yearsOfExperience);
        setBaseSalary(baseSalary);
        setBonus(bonus);
        setSalary(calculateSalary());
    }


    public LevelOfEducation getLevelOfEducation() {
        return this.levelOfEducation;
    }

    public void setLevelOfEducation(LevelOfEducation levelOfEducation) {
        this.levelOfEducation = levelOfEducation;
    }

    public int getYearsOfExperience() {
        return this.yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public double getBaseSalary() {
        return this.baseSalary;
    }

    public void setBaseSalary(double baseSalary) {
        this.baseSalary = baseSalary;
    }

    public double getBonus() {
        return this.bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public double getSalary() {
        return this.salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public double calculateSalary(){
        double educationCoefficient = 1;
        switch(levelOfEducation){
            case UNSKILLED:
                educationCoefficient = 1;
                break;
            case HIGH_SCHOOL:
                educationCoefficient = 1.1;
                break;
            case BACHELOR:
                educationCoefficient = 1.2;
                break;
            case MASTER:
                educationCoefficient = 1.3;
                break;
            case DOCTORATE:
                educationCoefficient = 1.4;
                break;
            default:
                break;
        }
        double experienceCoefficient = 1;
        if(yearsOfExperience > 0){
            experienceCoefficient = 1 + (yearsOfExperience * 0.1);
        }
        this.salary = baseSalary * educationCoefficient * experienceCoefficient + bonus;
        return this.salary;
    }
}
