package service;

import java.io.IOException;
import java.util.ArrayList;

import entity.BeautySalon;
import utils.AppSettings;
import utils.CsvUtil;

public class BeautySalonService {
    private BeautySalon beautySalon;
    private AppSettings appSettings;

    public BeautySalonService() {
        beautySalon = new BeautySalon();
        appSettings = AppSettings.getInstance();
    }
    
    public BeautySalon getBeautySalon() {
        return this.beautySalon;
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
