package entity;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment implements IdAssignable, CsvConvertible {
    private int id;
    private CosmeticTreatment cosmeticTreatment;
    private Cosmetologist cosmetologist;
    private Client client;
    private LocalDate date;
    private LocalTime time;
    private double price;
    private CosmeticTreatmentStatus status;

    public Appointment() {}

    public Appointment(int id, CosmeticTreatment cosmeticTreatment, Cosmetologist cosmetologist, Client client, LocalDate date, LocalTime time, double price, CosmeticTreatmentStatus status){
        setId(id);
        setCosmeticTreatment(cosmeticTreatment);
        setCosmetologist(cosmetologist);
        setClient(client);
        setDate(date);
        setTime(time);
        setPrice(price);
        setStatus(status);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CosmeticTreatment getCosmeticTreatment() {
        return this.cosmeticTreatment;
    }

    public void setCosmeticTreatment(CosmeticTreatment cosmeticTreatment) {
        this.cosmeticTreatment = cosmeticTreatment;
    }

    public Cosmetologist getCosmetologist() {
        return this.cosmetologist;
    }

    public void setCosmetologist(Cosmetologist cosmetologist) {
        this.cosmetologist = cosmetologist;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return this.time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public CosmeticTreatmentStatus getStatus() {
        return this.status;
    }

    public void setStatus(CosmeticTreatmentStatus status) {
        this.status = status;
    }

    public static double calculatePrice(double price, Client client, double discount){
        if(client.isHasLoyaltyCard()){
            return price * (1 - discount);
        } else {
            return price;
        }
    }

    public static Appointment parseFromCsv(String[] line, CosmeticTreatment cosmeticTreatment, Cosmetologist cosmetologist, Client client){
        return new Appointment(
            Integer.parseInt(line[0]),
            cosmeticTreatment,
            cosmetologist,
            client,
            LocalDate.parse(line[4]),
            LocalTime.parse(line[5]),
            Double.parseDouble(line[6]),
            CosmeticTreatmentStatus.valueOf(line[7])
        );
    }

    public String[] toCsv(){
        return new String[]{
            Integer.toString(getId()),
            Integer.toString(getCosmeticTreatment().getId()),
            Integer.toString(getCosmetologist().getId()),
            Integer.toString(getClient().getId()),
            getDate().toString(),
            getTime().toString(),
            Double.toString(getPrice()),
            getStatus().toString()
        };
    }
}
