package service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import entity.Client;
import entity.CosmeticTreatment;
import entity.AppointmentStatus;
import entity.Cosmetologist;
import entity.User;
import entity.Appointment;
import utils.CsvUtil;

public class AppointmentService extends Service<Appointment> {
    private ClientService clientService;
    private CosmetologistService cosmetologistService;
    private CosmeticTreatmentService cosmeticTreatmentService;
    private BeautySalonService beautySalonService;
    private PriceListService priceListService;

    public AppointmentService() {
        ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();
        clientService = serviceRegistry.getClientService();
        cosmetologistService = serviceRegistry.getCosmetologistService();
        cosmeticTreatmentService = serviceRegistry.getCosmeticTreatmentService();
        beautySalonService = serviceRegistry.getBeautySalonService();
        priceListService = serviceRegistry.getPriceListService();
    }

    public Appointment add(CosmeticTreatment cosmeticTreatment, Cosmetologist cosmetologist, Client client, LocalDate data, LocalTime time, double price, AppointmentStatus status){
        Appointment appointment = new Appointment(getNextId(), cosmeticTreatment, cosmetologist, client, data, time, price, status);
        add(appointment);
        incrementNextId();
        return appointment;
    }

    public Appointment schedule(CosmeticTreatment cosmeticTreatment, Cosmetologist cosmetologist, Client client, LocalDate date, LocalTime time){
        double discount = beautySalonService.getBeautySalon().getLoyaltyCardDiscount();
        double price = Appointment.calculatePrice(priceListService.getPrice(cosmeticTreatment.getId()), client, discount);
        client.addTotalSpent(price);
        beautySalonService.getBeautySalon().addIncome(price);
        return add(cosmeticTreatment, cosmetologist, client, date, time, price, AppointmentStatus.SCHEDULED);
    }

    public void cancel(Appointment appointment, User user){
        if(user instanceof Client){
            appointment.setStatus(AppointmentStatus.CANCELED_BY_CLIENT);
            // return 90% of the price to the client
            appointment.getClient().addTotalSpent(-0.9 * appointment.getPrice());
            beautySalonService.getBeautySalon().addExpense(0.9 * appointment.getPrice());
        } else {
            appointment.setStatus(AppointmentStatus.CANCELED_BY_SALON);
            // return 100% of the price to the client
            appointment.getClient().addTotalSpent(-appointment.getPrice());
            beautySalonService.getBeautySalon().addExpense(appointment.getPrice());
        }
    }

    public void removeByCosmeticTreatment(CosmeticTreatment cosmeticTreatment){
        ArrayList<Appointment> appointments = new ArrayList<Appointment>(getData());
        for(Appointment appointment : appointments){
            if(appointment.getCosmeticTreatment().equals(cosmeticTreatment)){
                remove(appointment);
            }
        }
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
                CosmeticTreatment cosmeticTreatment = cosmeticTreatmentService.getById(Integer.parseInt(appointmentString[1]));
                Cosmetologist cosmetologist = cosmetologistService.getById(Integer.parseInt(appointmentString[2]));
                Client client = clientService.getById(Integer.parseInt(appointmentString[3]));
                Appointment scheduledCosmeticTreatment = Appointment.parseFromCsv(appointmentString, cosmeticTreatment, cosmetologist, client);
                add(scheduledCosmeticTreatment);
            }
            loadNextId();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
