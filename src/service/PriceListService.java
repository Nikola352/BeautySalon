package service;

import java.io.IOException;
import java.util.ArrayList;

import entity.PriceList;
import utils.AppSettings;
import utils.CsvUtil;

public class PriceListService {
    private PriceList priceList = new PriceList();
    private AppSettings appSettings = AppSettings.getInstance();

    public PriceList getPriceList() {
        return this.priceList;
    }

    public void add(int id, double price){
        priceList.addPrice(id, price);
    }

    public void remove(int id){
        priceList.removePrice(id);
    }

    public double getPrice(int id){
        return priceList.getPrice(id);
    }

    public void update(int id, double price){
        priceList.updatePrice(id, price);
    }

    public void loadPriceList(){
        try{
            String[] priceListString = CsvUtil.loadData(appSettings.getPriceListFilename(), appSettings.getDelimiter()).get(0);
            priceList = PriceList.parseFromCsv(priceListString);
        } catch(IOException e) {
            System.err.println("Error loading price list");
        }
    }

    public void savePriceList(){
        try{
            ArrayList<String[]> priceListString = new ArrayList<String[]>();
            priceListString.add(priceList.toCsv());
            CsvUtil.saveData(priceListString, appSettings.getPriceListFilename(), appSettings.getDelimiter());
        } catch(IOException e) {
            System.err.println("Error saving price list");
        }
    }
    
}
