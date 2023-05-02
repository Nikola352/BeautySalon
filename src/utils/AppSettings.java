package utils;

public class AppSettings {
    private String treatmentTypeFilename;
    private String cosmeticTreatmentFilename;
    private String appointmentFilename;
    private String clientFilename;
    private String receptionistFilename;
    private String cosmetologistFilename;
    private String managerFilename;
    private String beautySalonFilename;

    private String delimiter;
    private String innerDelimiter;

    private AppSettings() {
        setTreatmentTypeFilename("data/treatment_types.csv");
        setCosmeticTreatmentFilename("data/cosmetic_treatments.csv");
        setAppointmentFilename("data/appointments.csv");
        setClientFilename("data/clients.csv");
        setReceptionistFilename("data/receptionists.csv");
        setCosmetologistFilename("data/cosmetologists.csv");
        setManagerFilename("data/managers.csv");
        setBeautySalonFilename("data/beauty_salon.csv");

        setDelimiter(";");
        setInnerDelimiter(",");
    }

    private static AppSettings instance;

    public static AppSettings getInstance() {
        if (instance == null) {
            instance = new AppSettings();
        }
        return instance;
    }

    public String getTreatmentTypeFilename() {
        return this.treatmentTypeFilename;
    }

    public String getCosmeticTreatmentFilename() {
        return this.cosmeticTreatmentFilename;
    }

    public String getAppointmentFilename() {
        return this.appointmentFilename;
    }

    public String getClientFilename() {
        return this.clientFilename;
    }

    public String getReceptionistFilename() {
        return this.receptionistFilename;
    }

    public String getCosmetologistFilename() {
        return this.cosmetologistFilename;
    }

    public String getManagerFilename() {
        return this.managerFilename;
    }

    public String getDelimiter() {
        return this.delimiter;
    }

    public String getInnerDelimiter() {
        return this.innerDelimiter;
    }

    public String getBeautySalonFilename() {
        return this.beautySalonFilename;
    }

    // setters are private unitl I see a need to change them
    private void setTreatmentTypeFilename(String treatmentTypeFilename) {
        this.treatmentTypeFilename = treatmentTypeFilename;
    }

    private void setCosmeticTreatmentFilename(String cosmeticTreatmentFilename) {
        this.cosmeticTreatmentFilename = cosmeticTreatmentFilename;
    }

    private void setAppointmentFilename(String appointmentFilename) {
        this.appointmentFilename = appointmentFilename;
    }   

    private void setClientFilename(String clientFilename) {
        this.clientFilename = clientFilename;
    }

    private void setReceptionistFilename(String receptionistFilename) {
        this.receptionistFilename = receptionistFilename;
    }

    private void setCosmetologistFilename(String cosmetologistFilename) {
        this.cosmetologistFilename = cosmetologistFilename;
    }

    private void setManagerFilename(String managerFilename) {
        this.managerFilename = managerFilename;
    }

    private void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    private void setInnerDelimiter(String innerDelimiter) {
        this.innerDelimiter = innerDelimiter;
    }

    private void setBeautySalonFilename(String beautySalonFilename) {
        this.beautySalonFilename = beautySalonFilename;
    }
}
