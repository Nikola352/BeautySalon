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

    public void remove(CosmeticTreatment cosmeticTreatment){
        priceListService.remove(cosmeticTreatment.getId());
        super.remove(cosmeticTreatment);
    }

    public void update(CosmeticTreatment cosmeticTreatment, String name, TreatmentType treatmentType, double price, int duration){
        cosmeticTreatment.setName(name);
        cosmeticTreatment.setTreatmentType(treatmentType);
        cosmeticTreatment.setDuration(duration);
        priceListService.update(cosmeticTreatment.getId(), price);
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
