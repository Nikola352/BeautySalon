package service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import entity.BeautySalon;
import entity.Client;
import utils.AppSettings;
import utils.CsvUtil;

public class BeautySalonService {
    private BeautySalon beautySalon = new BeautySalon();
    private AppSettings appSettings = AppSettings.getInstance();
    
    private ClientService clientService;
    private CosmetologistService cosmetologistService;
    private ReceptionistService receptionistService;
    private ManagerService managerService;

    public BeautySalonService() {
        ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();
        clientService = serviceRegistry.getClientService();
        cosmetologistService = serviceRegistry.getCosmetologistService();
        receptionistService = serviceRegistry.getReceptionistService();
        managerService = serviceRegistry.getManagerService();
    }

    public BeautySalon getBeautySalon() {
        return this.beautySalon;
    }

    public void updateLoyaltyCardStatus(){
        ArrayList<Client> clients = clientService.getData();
        for (Client client : clients) {
            client.updateLoyaltyCardStatus(beautySalon.getLoyaltyCardThreshold());
        }
    }

    public double getTotalSalaryForTimePeriod(LocalDate startDate, LocalDate endDate) {
        double totalSalary = 0;
        totalSalary += cosmetologistService.getTotalSalaryForTimePeriod(startDate, endDate);
        totalSalary += receptionistService.getTotalSalaryForTimePeriod(startDate, endDate);
        totalSalary += managerService.getTotalSalaryForTimePeriod(startDate, endDate);
        return totalSalary;
    }

    public void updateEmployeeBonus() {
        cosmetologistService.updateBonus(beautySalon.getBonusThreshold(), beautySalon.getBonusAmount());
        receptionistService.updateBonus(beautySalon.getBonusThreshold(), beautySalon.getBonusAmount());
        managerService.updateBonus(beautySalon.getBonusThreshold(), beautySalon.getBonusAmount());
    }

    public void loadBeautySalon() {
        try{
            String[] beautySalonString = CsvUtil.loadData(appSettings.getBeautySalonFilename(), appSettings.getDelimiter()).get(0);
            beautySalon = BeautySalon.parseFromCsv(beautySalonString);
        } catch(IOException e) {
            System.out.println("Error loading beauty salon data");
        }
    }

    public void saveBeautySalon(){
        try{
            ArrayList<String[]> beautySalonString = new ArrayList<String[]>();
            beautySalonString.add(beautySalon.toCsv());
            CsvUtil.saveData(beautySalonString, appSettings.getBeautySalonFilename(), appSettings.getDelimiter());
        } catch(IOException e) {
            System.out.println("Error saving beauty salon data");
        }
    }
}
