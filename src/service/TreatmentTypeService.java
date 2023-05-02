package service;

import java.io.IOException;
import java.util.ArrayList;

import entity.TreatmentType;
import utils.CsvUtil;

public class TreatmentTypeService extends Service<TreatmentType> {
    public void addTreatmentType(String name, String description) {
        TreatmentType treatmentType = new TreatmentType(getNextId(), name, description);
        add(treatmentType);
        incrementNextId();
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
