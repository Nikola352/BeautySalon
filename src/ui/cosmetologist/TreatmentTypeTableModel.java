package ui.cosmetologist;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import entity.TreatmentType;

public class TreatmentTypeTableModel extends AbstractTableModel {
    private ArrayList<TreatmentType> treatmentTypes;
    private String[] columnNames = {"Naziv", "Opis"};

    public TreatmentTypeTableModel(ArrayList<TreatmentType> treatmentTypes) {
        this.treatmentTypes = treatmentTypes;
    }

    @Override
    public int getRowCount() {
        return treatmentTypes.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        TreatmentType treatmentType = treatmentTypes.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return treatmentType.getName();
            case 1:
                return treatmentType.getDescription();
            default:
                return null;
        }
    }

    public void setTreatmentTypes(ArrayList<TreatmentType> treatmentTypes) {
        this.treatmentTypes = treatmentTypes;
        fireTableDataChanged();
    }

    public TreatmentType getTreatmentType(int rowIndex) {
        return treatmentTypes.get(rowIndex);
    }

    public void addTreatmentType(TreatmentType treatmentType) {
        treatmentTypes.add(treatmentType);
        fireTableDataChanged();
    }

    public void removeTreatmentType(int rowIndex) {
        treatmentTypes.remove(rowIndex);
        fireTableDataChanged();
    }
}
