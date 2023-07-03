package service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import entity.Client;
import entity.CosmeticTreatment;
import entity.AppointmentStatus;
import entity.Cosmetologist;
import entity.TreatmentType;
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
        cosmetologist.incrementCurrentMonthIncome(price);
        beautySalonService.getBeautySalon().addIncome(price);
        return add(cosmeticTreatment, cosmetologist, client, date, time, price, AppointmentStatus.SCHEDULED);
    }

    public void cancel(Appointment appointment, User user){
        if(user instanceof Client){
            appointment.setStatus(AppointmentStatus.CANCELED_BY_CLIENT);
            // return 90% of the price to the client
            appointment.getClient().addTotalSpent(-0.9 * appointment.getPrice());
            appointment.getCosmetologist().incrementCurrentMonthIncome(-0.9 * appointment.getPrice());
            beautySalonService.getBeautySalon().addExpense(0.9 * appointment.getPrice());
        } else {
            appointment.setStatus(AppointmentStatus.CANCELED_BY_SALON);
            // return 100% of the price to the client
            appointment.getClient().addTotalSpent(-appointment.getPrice());
            appointment.getCosmetologist().incrementCurrentMonthIncome(-appointment.getPrice());
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

    public ArrayList<Appointment> getByCosmetologist(Cosmetologist cosmetologist){
        ArrayList<Appointment> appointments = new ArrayList<Appointment>(getData());
        ArrayList<Appointment> result = new ArrayList<Appointment>();
        for(Appointment appointment : appointments){
            if(appointment.getCosmetologist().equals(cosmetologist)){
                result.add(appointment);
            }
        }
        return result;
    }

    public ArrayList<Appointment> getByCosmetologistAndStatus(Cosmetologist cosmetologist, AppointmentStatus status){
        ArrayList<Appointment> appointments = new ArrayList<Appointment>(getData());
        ArrayList<Appointment> result = new ArrayList<Appointment>();
        for(Appointment appointment : appointments){
            if(appointment.getCosmetologist().equals(cosmetologist) && appointment.getStatus().equals(status)){
                result.add(appointment);
            }
        }
        return result;
    }

    public ArrayList<Appointment> getByClient(Client client){
        ArrayList<Appointment> appointments = new ArrayList<Appointment>(getData());
        ArrayList<Appointment> result = new ArrayList<Appointment>();
        for(Appointment appointment : appointments){
            if(appointment.getClient().equals(client)){
                result.add(appointment);
            }
        }
        return result;
    }

    public ArrayList<Appointment> getByClientAndStatus(Client client, AppointmentStatus status){
        ArrayList<Appointment> appointments = new ArrayList<Appointment>(getData());
        ArrayList<Appointment> result = new ArrayList<Appointment>();
        for(Appointment appointment : appointments){
            if(appointment.getClient().equals(client) && appointment.getStatus().equals(status)){
                result.add(appointment);
            }
        }
        return result;
    }

    public ArrayList<Appointment> getByDateAndCosmetologist(LocalDate date, Cosmetologist cosmetologist){
        ArrayList<Appointment> appointments = new ArrayList<Appointment>(getData());
        ArrayList<Appointment> result = new ArrayList<Appointment>();
        for(Appointment appointment : appointments){
            if(appointment.getDate().equals(date) && appointment.getCosmetologist().equals(cosmetologist)){
                result.add(appointment);
            }
        }
        return result;
    }

    public ArrayList<Appointment> getByStatus(AppointmentStatus status){
        ArrayList<Appointment> appointments = new ArrayList<Appointment>(getData());
        ArrayList<Appointment> result = new ArrayList<Appointment>();
        for(Appointment appointment : appointments){
            if(appointment.getStatus().equals(status)){
                result.add(appointment);
            }
        }
        return result;
    }

    public ArrayList<Appointment> getByCosmeticTreatment(CosmeticTreatment cosmeticTreatment){
        ArrayList<Appointment> appointments = new ArrayList<Appointment>(getData());
        ArrayList<Appointment> result = new ArrayList<Appointment>();
        for(Appointment appointment : appointments){
            if(appointment.getCosmeticTreatment().equals(cosmeticTreatment)){
                result.add(appointment);
            }
        }
        return result;
    }

    public ArrayList<Appointment> getByCosmetologistForTimePeriod(Cosmetologist cosmetologist, LocalDate startDate, LocalDate endDate){
        ArrayList<Appointment> appointments = new ArrayList<Appointment>(getData());
        ArrayList<Appointment> result = new ArrayList<Appointment>();
        for(Appointment appointment : appointments){
            if(appointment.getCosmetologist().equals(cosmetologist) && 
                appointment.getStatus().equals(AppointmentStatus.COMPLETED) &&
                (appointment.getDate().isAfter(startDate) || appointment.getDate().isEqual(startDate)) &&
                (appointment.getDate().isBefore(endDate) || appointment.getDate().isEqual(endDate))
            ){
                result.add(appointment);
            }
        }
        return result;
    }

    public ArrayList<Appointment> getByStatusForTimePeriod(AppointmentStatus status, LocalDate starDate, LocalDate endDate){
        ArrayList<Appointment> appointments = new ArrayList<Appointment>(getData());
        ArrayList<Appointment> result = new ArrayList<Appointment>();
        for(Appointment appointment : appointments){
            if(appointment.getStatus().equals(status) &&
                (appointment.getDate().isAfter(starDate) || appointment.getDate().isEqual(starDate)) &&
                (appointment.getDate().isBefore(endDate) || appointment.getDate().isEqual(endDate))
            ){
                result.add(appointment);
            }
        }
        return result;
    }

    public double getTotalIncomeForTimePeriod(LocalDate startDate, LocalDate endDate){
        ArrayList<Appointment> appointments = new ArrayList<Appointment>(getData());
        double totalIncome = 0;
        for(Appointment appointment : appointments){
            if(appointment.getDate().isBefore(startDate) || appointment.getDate().isAfter(endDate))
                continue;
            totalIncome += appointment.getPrice();
        }
        return totalIncome;
    }

    public double getTotalExpensesForTimePeriod(LocalDate startDate, LocalDate endDate){
        ArrayList<Appointment> appointments = new ArrayList<Appointment>(getData());
        double totalExpenses = 0;
        for(Appointment appointment : appointments){
            if(appointment.getDate().isBefore(startDate) || appointment.getDate().isAfter(endDate))
                continue;
            if(appointment.getStatus().equals(AppointmentStatus.CANCELED_BY_CLIENT)){
                totalExpenses += 0.9 * appointment.getPrice();
            } else if(appointment.getStatus().equals(AppointmentStatus.CANCELED_BY_SALON)) {
                totalExpenses += appointment.getPrice();
            }
        }

        totalExpenses += beautySalonService.getTotalSalaryForTimePeriod(startDate, endDate);

        return totalExpenses;
    }

    public double getTotalProfitForTimePeriod(LocalDate startDate, LocalDate endDate){
        return getTotalIncomeForTimePeriod(startDate, endDate) - getTotalExpensesForTimePeriod(startDate, endDate);
    }

    public int getNumByCosmetologistForTimePeriod(Cosmetologist cosmetologist, LocalDate startDate, LocalDate endDate){
        ArrayList<Appointment> appointments = new ArrayList<Appointment>(getData());
        int num = 0;
        for(Appointment appointment : appointments){
            if(appointment.getCosmetologist().equals(cosmetologist) && 
                appointment.getStatus().equals(AppointmentStatus.COMPLETED) &&
                (appointment.getDate().isAfter(startDate) || appointment.getDate().isEqual(startDate)) &&
                (appointment.getDate().isBefore(endDate) || appointment.getDate().isEqual(endDate))
            ){
                num++;
            }
        }
        return num;
    }

    public double getTotalProfitByCosmetologistForTimePeriod(Cosmetologist cosmetologist, LocalDate startDate, LocalDate endDate){
        ArrayList<Appointment> appointments = new ArrayList<Appointment>(getData());
        double totalIncome = 0;
        for(Appointment appointment : appointments){
            if(appointment.getCosmetologist().equals(cosmetologist) && 
                (appointment.getDate().isAfter(startDate) || appointment.getDate().isEqual(startDate)) &&
                (appointment.getDate().isBefore(endDate) || appointment.getDate().isEqual(endDate))
            ){
                if(appointment.getStatus().equals(AppointmentStatus.CANCELED_BY_CLIENT)){
                    totalIncome += 0.1 * appointment.getPrice();
                } else if(!appointment.getStatus().equals(AppointmentStatus.CANCELED_BY_SALON)) {
                    totalIncome += appointment.getPrice();
                }
            }
        }
        return totalIncome;
    }

    public int getNumByStatusForTimePeriod(AppointmentStatus status, LocalDate startDate, LocalDate endDate){
        ArrayList<Appointment> appointments = new ArrayList<Appointment>(getData());
        int num = 0;
        for(Appointment appointment : appointments){
            if(appointment.getStatus().equals(status) &&
                (appointment.getDate().isAfter(startDate) || appointment.getDate().isEqual(startDate)) &&
                (appointment.getDate().isBefore(endDate) || appointment.getDate().isEqual(endDate))
            ){
                num++;
            }
        }
        return num;
    }

    public int getNumByCosmeticTreatmentForTimePeriod(CosmeticTreatment cosmeticTreatment, LocalDate startDate, LocalDate endDate){
        ArrayList<Appointment> appointments = new ArrayList<Appointment>(getData());
        int num = 0;
        for(Appointment appointment : appointments){
            if(appointment.getCosmeticTreatment().equals(cosmeticTreatment) && 
                (appointment.getDate().isAfter(startDate) || appointment.getDate().isEqual(startDate)) &&
                (appointment.getDate().isBefore(endDate) || appointment.getDate().isEqual(endDate))
            ){
                num++;
            }
        }
        return num;
    }

    public double getTotalProfitByCosmeticTreatmentForTimePeriod(CosmeticTreatment cosmeticTreatment, LocalDate startDate, LocalDate endDate){
        ArrayList<Appointment> appointments = new ArrayList<Appointment>(getData());
        double totalIncome = 0;
        for(Appointment appointment : appointments){
            if(appointment.getCosmeticTreatment().equals(cosmeticTreatment) && 
                (appointment.getDate().isAfter(startDate) || appointment.getDate().isEqual(startDate)) &&
                (appointment.getDate().isBefore(endDate) || appointment.getDate().isEqual(endDate))
            ){
                if(appointment.getStatus().equals(AppointmentStatus.CANCELED_BY_CLIENT)){
                    totalIncome += 0.1 * appointment.getPrice();
                } else if(!appointment.getStatus().equals(AppointmentStatus.CANCELED_BY_SALON)) {
                    totalIncome += appointment.getPrice();
                }
            }
        }
        return totalIncome;
    }

    // @return ArrayList<Appointment> - appointments with price in range [minPrice, maxPrice]
    public ArrayList<Appointment> getInPriceRange(double minPrice, double maxPrice){
        ArrayList<Appointment> appointments = new ArrayList<Appointment>(getData());
        ArrayList<Appointment> result = new ArrayList<Appointment>();
        for(Appointment appointment : appointments){
            if(appointment.getPrice() >= minPrice && appointment.getPrice() <= maxPrice){
                result.add(appointment);
            }
        }
        return result;
    }

    // @return ArrayList<Appointment> - appointments with price in range [minPrice, maxPrice] and CosmeticTreatment
    public ArrayList<Appointment> getByCosmeticTreatmentInPriceRange(double minPrice, double maxPrice, CosmeticTreatment cosmeticTreatment){
        ArrayList<Appointment> appointments = new ArrayList<Appointment>(getData());
        ArrayList<Appointment> result = new ArrayList<Appointment>();
        for(Appointment appointment : appointments){
            if(appointment.getPrice() >= minPrice && 
                appointment.getPrice() <= maxPrice && 
                appointment.getCosmeticTreatment().equals(cosmeticTreatment)
            ){
                result.add(appointment);
            }
        }
        return result;
    }

    // @return ArrayList<Appointment> - appointments with price in range [minPrice, maxPrice] and TreatmentType
    public ArrayList<Appointment> getByCosmeticTreatmentTypeInPriceRange(double minPrice, double maxPrice, TreatmentType cosmeticTreatmentType){
        ArrayList<Appointment> appointments = new ArrayList<Appointment>(getData());
        ArrayList<Appointment> result = new ArrayList<Appointment>();
        for(Appointment appointment : appointments){
            if(appointment.getPrice() >= minPrice && 
                appointment.getPrice() <= maxPrice && 
                appointment.getCosmeticTreatment().getTreatmentType().equals(cosmeticTreatmentType)
            ){
                result.add(appointment);
            }
        }
        return result;
    }

    public double[] getIncomeForLast12Months(){
        double[] income = new double[12];
        LocalDate today = LocalDate.now();
        ArrayList<Appointment> appointments = new ArrayList<Appointment>(getData());
        for(Appointment appointment : appointments){
            if(appointment.getDate().isAfter(today.minusMonths(12)) &&
                appointment.getDate().isBefore(today.plusDays(1))
            ){
                double incomeForMonth = appointment.getPrice();
                if(appointment.getStatus().equals(AppointmentStatus.CANCELED_BY_CLIENT)){
                    incomeForMonth *= 0.1;
                } else if(appointment.getStatus().equals(AppointmentStatus.CANCELED_BY_SALON)){
                    incomeForMonth = 0;
                }
                income[appointment.getDate().getMonthValue() - 1] += incomeForMonth;
            }
        }        
        return income;
    }

    public double[] getIncomeForLast12MonthsByCosmeticTreatment(TreatmentType treatmentType){
        double[] income = new double[12];
        LocalDate today = LocalDate.now();
        ArrayList<Appointment> appointments = new ArrayList<Appointment>(getData());
        for(Appointment appointment : appointments){
            if(appointment.getCosmeticTreatment().getTreatmentType().equals(treatmentType) &&
                appointment.getDate().isAfter(today.minusMonths(12)) &&
                appointment.getDate().isBefore(today.plusDays(1))
            ){
                double incomeForMonth = appointment.getPrice();
                if(appointment.getStatus().equals(AppointmentStatus.CANCELED_BY_CLIENT)){
                    incomeForMonth *= 0.1;
                } else if(appointment.getStatus().equals(AppointmentStatus.CANCELED_BY_SALON)){
                    incomeForMonth = 0;
                }
                income[appointment.getDate().getMonthValue() - 1] += incomeForMonth;
            }
        }        
        return income;
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
