package ui;

import java.util.Scanner;

import entity.Client;
import entity.User;
import service.ClientService;
import service.CosmetologistService;
import service.ManagerService;
import service.ReceptionistService;
import service.ServiceRegistry;
import entity.Gender;

public class LoginCLI {
    private ClientService clientService;
    private ManagerService managerService;
    private CosmetologistService cosmetologistService;
    private ReceptionistService receptionistService;
    private Scanner scanner;

    public LoginCLI() {
        scanner = new Scanner(System.in);
        clientService = ServiceRegistry.getInstance().getClientService();
        managerService = ServiceRegistry.getInstance().getManagerService();
        cosmetologistService = ServiceRegistry.getInstance().getCosmetologistService();
        receptionistService = ServiceRegistry.getInstance().getReceptionistService();
    }

    public User run(){
        System.out.println("Welcome to Beauty Salon!");
        while(true){
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.println("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    return login();
                case 2:
                    return register();
                case 3:
                    return null;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private User login() {
        System.out.println("Enter username: ");
        String username = scanner.nextLine();
        System.out.println("Enter password: ");
        String password = scanner.nextLine();

        User user = clientService.getByUsername(username);
        if (user == null) {
            user = managerService.getByUsername(username);
        }
        if (user == null) {
            user = cosmetologistService.getByUsername(username);
        }
        if (user == null) {
            user = receptionistService.getByUsername(username);
        }

        if (user == null) {
            System.out.println("User not found!");
            return null;
        }

        if (!user.getPassword().equals(password)) {
            System.out.println("Wrong password!");
            return null;
        }

        return user;
    }

    private Client register() {
        System.out.println("Enter username: ");
        String username = scanner.nextLine();
        System.out.println("Enter password: ");
        String password = scanner.nextLine();

        User user = clientService.getByUsername(username);
        if (user == null) {
            user = managerService.getByUsername(username);
        }
        if (user == null) {
            user = cosmetologistService.getByUsername(username);
        }
        if (user == null) {
            user = receptionistService.getByUsername(username);
        }

        if (user != null) {
            System.out.println("User already exists!");
            return null;
        }

        System.out.println("Enter name: ");
        String name = scanner.nextLine();
        System.out.println("Enter surname: ");
        String lastname = scanner.nextLine();
        System.out.println("Enter phone number: ");
        String phoneNumber = scanner.nextLine();
        System.out.println("Enter address: ");
        String address = scanner.nextLine();

        Client client = clientService.add(username, password, name, lastname, Gender.MALE, phoneNumber, address);
        clientService.saveData();

        return client;
    }

}
