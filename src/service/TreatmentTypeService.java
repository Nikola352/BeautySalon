package service;

import java.io.IOException;
import java.util.ArrayList;

import entity.TreatmentType;
import utils.CsvUtil;

public class TreatmentTypeService extends Service<TreatmentType> {
    public TreatmentType add(String name, String description) {
        TreatmentType treatmentType = new TreatmentType(getNextId(), name, description);
        add(treatmentType);
        incrementNextId();
        return treatmentType;
    }

    public TreatmentType getByName(String name) {
        for (TreatmentType treatmentType : getData()) {
            if (treatmentType.getName().equals(name)) {
                return treatmentType;
            }
        }
        return null;
    }

    @Override
    public void remove(TreatmentType treatmentType) {
        super.remove(treatmentType);
        CosmeticTreatmentService cosmeticTreatmentService = ServiceRegistry.getInstance().getCosmeticTreatmentService();
        if(cosmeticTreatmentService != null) {
            cosmeticTreatmentService.removeByTreatmentType(treatmentType);
        }
    }

    @Override
    protected String getFilename() {
        return appSettings.getTreatmentTypeFilename();
    }

    @Override
    public void loadData() {
        try{
            ArrayList<String[]> treatmentTypeStings = CsvUtil.loadData(appSettings.getTreatmentTypeFilename(), appSettings.getDelimiter());
            for(String[] treatmentTypeString : treatmentTypeStings) {
                TreatmentType treatmentType = TreatmentType.parseFromCsv(treatmentTypeString);
                add(treatmentType);
            }
            loadNextId();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
