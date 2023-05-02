package service;

import java.io.IOException;
import java.util.ArrayList;

import entity.IdAssignable;
import entity.CsvConvertible;
import utils.AppSettings;
import utils.CsvUtil;

public abstract class Service<T extends IdAssignable & CsvConvertible>{
    public ArrayList<T> data = new ArrayList<T>();
    private int nextId = 1;

    protected AppSettings appSettings = AppSettings.getInstance();

    public ArrayList<T> getData() {
        return this.data;
    }

    public void add(T item) {
        getData().add(item);
    }

    public void remove(T item) {
        getData().remove(item);
    }

    public T getById(int id) {
        for (T item : getData()) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public int getNextId() {
        return nextId;
    }

    private void setNextId(int nextId) {
        if (nextId > this.nextId) {
            this.nextId = nextId;
        }
    }

    public void incrementNextId() {
        nextId++;
    }

    public void loadNextId() {
        int maxId = 0;
        for (T item : getData()) {
            if (item.getId() > maxId) {
                maxId = item.getId();
            }
        }
        setNextId(maxId + 1);
    }

    protected abstract String getFilename();
    
    public abstract void loadData();

    public void saveData(){
        ArrayList<String[]> appointmentStrings = new ArrayList<String[]>();
        for(T appointment : getData()) {
            appointmentStrings.add(appointment.toCsv());
        }
        try {
            CsvUtil.saveData(appointmentStrings, getFilename(), appSettings.getDelimiter());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    };

}