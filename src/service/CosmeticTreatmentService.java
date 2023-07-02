package service;

import java.io.IOException;
import java.util.ArrayList;

import entity.CosmeticTreatment;
import entity.TreatmentType;
import utils.CsvUtil;

public class CosmeticTreatmentService extends Service<CosmeticTreatment> {
    TreatmentTypeService treatmentTypeService = ServiceRegistry.getInstance().getTreatmentTypeService();
    PriceListService priceListService = ServiceRegistry.getInstance().getPriceListService();

    public CosmeticTreatment add(String name, TreatmentType treatmentType, double price, int duration){
        CosmeticTreatment cosmeticTreatment = new CosmeticTreatment(getNextId(), name, treatmentType, duration);
        add(cosmeticTreatment);
        incrementNextId();
        priceListService.add(cosmeticTreatment.getId(), price);
        return cosmeticTreatment;
    }

    @Override
    public void remove(CosmeticTreatment cosmeticTreatment){
        priceListService.remove(cosmeticTreatment.getId());
        super.remove(cosmeticTreatment);
        AppointmentService appointmentService = ServiceRegistry.getInstance().getAppointmentService();
        if(appointmentService != null){
            appointmentService.removeByCosmeticTreatment(cosmeticTreatment);
        }
    }

    public void removeByTreatmentType(TreatmentType treatmentType){
        ArrayList<CosmeticTreatment> cosmeticTreatments = new ArrayList<CosmeticTreatment>(getData());
        for(CosmeticTreatment cosmeticTreatment : cosmeticTreatments){
            if(cosmeticTreatment.getTreatmentType().equals(treatmentType)){
                remove(cosmeticTreatment);
            }
        }
    }

    public CosmeticTreatment update(CosmeticTreatment cosmeticTreatment, String name, TreatmentType treatmentType, double price, int duration){
        cosmeticTreatment.setName(name);
        cosmeticTreatment.setTreatmentType(treatmentType);
        cosmeticTreatment.setDuration(duration);
        priceListService.update(cosmeticTreatment.getId(), price);
        return cosmeticTreatment;
    }

    // @param name - null if you don't want to filter by name
    // @param treatmentType - null if you don't want to filter by treatmentType
    // @param price - -1 if you don't want to filter by price
    // @param duration - -1 if you don't want to filter by duration
    // @return ArrayList<CosmeticTreatment> - list of cosmetic treatments that match the criteria
    public ArrayList<CosmeticTreatment> getBy(String name, TreatmentType treatmentType, double price, int duration){
        ArrayList<CosmeticTreatment> cosmeticTreatments = new ArrayList<CosmeticTreatment>(getData());
        ArrayList<CosmeticTreatment> result = new ArrayList<CosmeticTreatment>();
        for(CosmeticTreatment cosmeticTreatment : cosmeticTreatments){
            if(name != null && !cosmeticTreatment.getName().equals(name)){
                continue;
            }
            if(treatmentType != null && !cosmeticTreatment.getTreatmentType().equals(treatmentType)){
                continue;
            }
            if(price != -1 && priceListService.getPrice(cosmeticTreatment) != price){
                continue;
            }
            if(duration != -1 && cosmeticTreatment.getDuration() != duration){
                continue;
            }
            result.add(cosmeticTreatment);
        }
        return result;
    }

    // @param name - null if you don't want to filter by name
    // @param treatmentType - null if you don't want to filter by treatmentType
    // @param minPrice - -1 if you don't want to filter by minPrice
    // @param maxPrice - -1 if you don't want to filter by maxPrice
    // @param minDuration - -1 if you don't want to filter by minDuration
    // @param maxDuration - -1 if you don't want to filter by maxDuration
    // @return ArrayList<CosmeticTreatment> - list of cosmetic treatments that match the criteria
    public ArrayList<CosmeticTreatment> getBy(String name, TreatmentType treatmentType, double minPrice, double maxPrice, int minDuration, int maxDuration){
        ArrayList<CosmeticTreatment> cosmeticTreatments = new ArrayList<CosmeticTreatment>(getData());
        ArrayList<CosmeticTreatment> result = new ArrayList<CosmeticTreatment>();
        for(CosmeticTreatment cosmeticTreatment : cosmeticTreatments){
            if(name != null && !cosmeticTreatment.getName().equals(name)){
                continue;
            }
            if(treatmentType != null && !cosmeticTreatment.getTreatmentType().equals(treatmentType)){
                continue;
            }
            double price = priceListService.getPrice(cosmeticTreatment);
            if(minPrice != -1 && price < minPrice){
                continue;
            }
            if(maxPrice != -1 && price > maxPrice){
                continue;
            }
            if(minDuration != -1 && cosmeticTreatment.getDuration() < minDuration){
                continue;
            }
            if(maxDuration != -1 && cosmeticTreatment.getDuration() > maxDuration){
                continue;
            }
            result.add(cosmeticTreatment);
        }
        return result;
    }

    @Override
    protected String getFilename() {
        return appSettings.getCosmeticTreatmentFilename();
    }

    @Override
    public void loadData() {
        try{
            ArrayList<String[]> cosmeticTreatmentStrings = CsvUtil.loadData(appSettings.getCosmeticTreatmentFilename(), appSettings.getDelimiter());
            for(String[] cosmeticTreatmentString : cosmeticTreatmentStrings) {
                TreatmentType treatmentType = treatmentTypeService.getById(Integer.parseInt(cosmeticTreatmentString[2]));
                CosmeticTreatment cosmeticTreatment = CosmeticTreatment.parseFromCsv(cosmeticTreatmentString, treatmentType);
                add(cosmeticTreatment);
            }
            loadNextId();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
