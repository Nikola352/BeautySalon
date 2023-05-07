package entity;

import java.util.HashMap;

import utils.AppSettings;

public class PriceList implements CsvConvertible {
    HashMap<Integer, Double> priceList = new HashMap<Integer, Double>();

    public PriceList(){}

    public PriceList(HashMap<Integer, Double> priceList){
        setPriceList(priceList);
    }

    public HashMap<Integer, Double> getPriceList() {
        return this.priceList;
    }

    private void setPriceList(HashMap<Integer, Double> priceList) {
        this.priceList = priceList;
    }

    public void addPrice(int treatmentId, double price){
        priceList.put(treatmentId, price);
    }

    public void removePrice(int treatmentId){
        priceList.remove(treatmentId);
    }

    public double getPrice(int treatmentId){
        return priceList.get(treatmentId);
    }

    public void updatePrice(int treatmentId, double price){
        priceList.replace(treatmentId, price);
    }

    public static PriceList parseFromCsv(String[] line){
        HashMap<Integer, Double> priceList = new HashMap<Integer, Double>();
        for(int i = 0; i < line.length; i++){
            String[] price = line[i].split(AppSettings.getInstance().getInnerDelimiter());
            priceList.put(Integer.parseInt(price[0]), Double.parseDouble(price[1]));
        }
        return new PriceList(priceList);
    }

    @Override
    public String[] toCsv(){
        String[] priceListString = new String[priceList.size()];
        int i = 0;
        for(int treatmentId : priceList.keySet()){
            priceListString[i] = treatmentId + AppSettings.getInstance().getInnerDelimiter() + priceList.get(treatmentId);
            i++;
        }
        return priceListString;
    }
}
