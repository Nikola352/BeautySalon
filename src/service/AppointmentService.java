package service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import entity.Client;
import entity.CosmeticTreatment;
import entity.CosmeticTreatmentStatus;
import entity.Cosmetologist;
import entity.Appointment;
import utils.CsvUtil;

public class AppointmentService extends Service<Appointment> {
    private ClientService clientService;
    private CosmetologistService cosmetologistService;
    private CosmeticTreatmentService cosmeticTreatmentService;
    private BeautySalonService beautySalonService;

    public AppointmentService() {
        ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();
        clientService = serviceRegistry.getClientService();
        cosmetologistService = serviceRegistry.getCosmetologistService();
        cosmeticTreatmentService = serviceRegistry.getCosmeticTreatmentService();
        beautySalonService = serviceRegistry.getBeautySalonService();
    }

    public Appointment add(CosmeticTreatment cosmeticTreatment, Cosmetologist cosmetologist, Client client, LocalDate data, LocalTime time, double price, CosmeticTreatmentStatus status){
        Appointment appointment = new Appointment(getNextId(), cosmeticTreatment, cosmetologist, client, data, time, price, status);
        add(appointment);
        incrementNextId();
        return appointment;
    }

    public Appointment add(CosmeticTreatment cosmeticTreatment, Cosmetologist cosmetologist, Client client, LocalDate date, LocalTime time){
        double discount = beautySalonService.getBeautySalon().getLoyaltyCardDiscount();
        double price = Appointment.calculatePrice(cosmeticTreatment, client, discount);
        return add(cosmeticTreatment, cosmetologist, client, date, time, price, CosmeticTreatmentStatus.SCHEDULED);
    }

    @Override
    protected String getFilename() {
        return appSettings.getAppointmentFilename();
    }

    @Override
    public void loadData() {
        try{
            ArrayList<String[]> appointmentStrings = CsvUtil.loadData(getFilename(), appSettings.getDelimiter());
            for(String[] appointmentString : appointmentStrings) {
                CosmeticTreatment cosmeticTreatment = cosmeticTreatmentService.getById(Integer.parseInt(appointmentString[2]));
                Client client = clientService.getById(Integer.parseInt(appointmentString[3]));
                Cosmetologist cosmetologist = cosmetologistService.getById(Integer.parseInt(appointmentString[4]));
                Appointment scheduledCosmeticTreatment = Appointment.parseFromCsv(appointmentString, cosmeticTreatment, cosmetologist, client);
                add(scheduledCosmeticTreatment);
            }
            loadNextId();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
