package ui;

import java.util.Scanner;

import entity.Gender;
import entity.LevelOfEducation;
import entity.Manager;
import service.ReceptionistService;
import service.ServiceRegistry;

public class ManagerCLI implements CLI {
    private Manager user;
    private Scanner scanner = new Scanner(System.in);
    private ReceptionistService receptionistService;

    public ManagerCLI(Manager user) {
        this.user = user;
        ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();
        receptionistService = serviceRegistry.getReceptionistService();
    }

    @Override
    public boolean run() {
        System.out.println("Manager CLI");
        System.out.println("Welcome " + user.getUsername());
    
        while(true){
            System.out.println("1. Logout");
            System.out.println("2. Exit");
            System.out.println("3. Register new employee");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    return true;
                case 2:
                    return false;
                case 3:
                    register();
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private void register(){
        System.out.println("Do you want to register cosmetologist, receptionist or manager?");
        String type = scanner.next().toLowerCase();
        switch(type){
            case "cosmetologist":
                registerCosmetologist();
                break;
            case "receptionist":
                registerReceptionist();
                break;
            case "manager":
                registerManager();
                break;
            default:
                System.out.println("Invalid type");
        }
    }

    private void registerCosmetologist(){
        // TODO: implement
    }

    private void registerReceptionist(){
        System.out.println("Enter username:");
        String username = scanner.next();
        System.out.println("Enter password:");
        String password = scanner.next();
        System.out.println("Enter name:");
        String name = scanner.next();
        System.out.println("Enter surname:");
        String surname = scanner.next();
        System.out.println("Enter phone number:");
        String phoneNumber = scanner.next();
        System.out.println("Enter address:");
        String address = scanner.next();
        System.out.println("Enter gender:");
        String gender = scanner.next().toUpperCase();

        System.out.println("Enter base salary:");
        double baseSalary = scanner.nextDouble();
        System.out.println("Enter years of experience:");
        int yearsOfExperience = scanner.nextInt();
        System.out.println("Enter level of education:");
        String levelOfEducation = scanner.next().toUpperCase();

        Gender g = Gender.valueOf(gender);
        LevelOfEducation lvl = LevelOfEducation.valueOf(levelOfEducation);

        // TODO: input validation

        receptionistService.add(username, password, surname, name, g, phoneNumber, address, lvl, yearsOfExperience, baseSalary);
        receptionistService.saveData();
    }

    private void registerManager(){
        // TODO: implement   
    }
}
