package ui;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

import entity.Client;
import entity.CosmeticTreatment;
import entity.Cosmetologist;
import service.AppointmentService;
import service.CosmeticTreatmentService;
import service.CosmetologistService;
import service.ServiceRegistry;

public class ClientCLI implements CLI {
    private Client user;
    private Scanner scanner = new Scanner(System.in);
    private AppointmentService appointmentService;
    private CosmeticTreatmentService cosmeticTreatmentService;
    private CosmetologistService cosmetologistService;

    public ClientCLI(Client user) {
        this.user = user;
        ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();
        appointmentService = serviceRegistry.getAppointmentService();
        cosmeticTreatmentService = serviceRegistry.getCosmeticTreatmentService();
        cosmetologistService = serviceRegistry.getCosmetologistService();
    }

    @Override
    public boolean run() {
        System.out.println("Client CLI");
        System.out.println("Welcome " + user.getUsername());

        while(true){
            System.out.println("1. Logout");
            System.out.println("2. Exit");
            System.out.println("3. Print username");
            System.out.println("4. Schedule an appointment");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    return true;
                case 2:
                    return false;
                case 3:
                    System.out.println(user.getUsername());
                    break;
                case 4:
                    scheduleAppointment();
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private void scheduleAppointment(){
        System.out.println("Schedule an appointment");

        System.out.println("Enter the date of the appointment (yyyy-mm-dd): ");
        String date = scanner.next();
        System.out.println("Enter the time of the appointment (hh:mm): ");
        String time = scanner.next();
        System.out.println("Enter the name of the cosmetologist: ");
        String cosmetologistName = scanner.next();
        System.out.println("Enter the id of the service: ");
        int serviceName = scanner.nextInt();

        Cosmetologist cosmetologist = cosmetologistService.getByUsername(cosmetologistName);
        if(cosmetologist == null){
            System.out.println("Cosmetologist not found");
            return;
        }
        CosmeticTreatment cosmeticTreatment = cosmeticTreatmentService.getById(serviceName);
        if(cosmeticTreatment == null){
            System.out.println("Cosmetic treatment not found");
            return;
        }

        LocalDate localDate = LocalDate.parse(date);
        LocalTime localTime = LocalTime.parse(time);

        appointmentService.add(cosmeticTreatment, cosmetologist, user, localDate, localTime);
        appointmentService.saveData();

        System.out.println("Appointment scheduled!");
    }
}
