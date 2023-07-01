package ui.manager.model;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import entity.CosmeticTreatment;
import service.PriceListService;

public class CosmeticTreatmentTableModel extends AbstractTableModel {
    private ArrayList<CosmeticTreatment> cosmeticTreatments;
    private String[] columnNames = {"Naziv", "Cena", "Trajanje", "Opis"};
    private PriceListService priceListService;

    public CosmeticTreatmentTableModel(ArrayList<CosmeticTreatment> cosmeticTreatments, PriceListService priceListService) {
        this.cosmeticTreatments = cosmeticTreatments;
        this.priceListService = priceListService;
    }

    @Override
    public int getRowCount() {
        return cosmeticTreatments.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
    	return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(cosmeticTreatments.isEmpty() || rowIndex >= cosmeticTreatments.size())
            return null;
        CosmeticTreatment cosmeticTreatment = cosmeticTreatments.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return cosmeticTreatment.getName();
            case 1:
                return priceListService.getPrice(cosmeticTreatment) + " RSD";
            case 2:
                return cosmeticTreatment.getDuration() + " min";
            case 3:
                return cosmeticTreatment.getTreatmentType().getDescription();
            default:
                return null;
        }
    }

    public void setCosmeticTreatments(ArrayList<CosmeticTreatment> cosmeticTreatments) {
        this.cosmeticTreatments = cosmeticTreatments;
        fireTableDataChanged();
    }

    public CosmeticTreatment getCosmeticTreatment(int row) {
        return cosmeticTreatments.get(row);
    }

    public void addCosmeticTreatment(CosmeticTreatment cosmeticTreatment) {
        cosmeticTreatments.add(cosmeticTreatment);
        fireTableDataChanged();
    }

    public void removeCosmeticTreatment(int row) {
        cosmeticTreatments.remove(row);
        fireTableDataChanged();
    }
}
