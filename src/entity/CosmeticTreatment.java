package entity;

public class CosmeticTreatment implements IdAssignable, CsvConvertible {
    private int id;
    private String name;
    private TreatmentType treatmentType;
    // private double price;
    private int duration; // in minutes

    public CosmeticTreatment() {}

    public CosmeticTreatment(int id, String name, TreatmentType treatmentType, int duration){
        setId(id);
        setName(name);
        setTreatmentType(treatmentType);
        setDuration(duration);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        if(id < 0) throw new IllegalArgumentException("Id cannot be negative");
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if(name == null) throw new IllegalArgumentException("Name cannot be null");
        this.name = name;
    }

    public TreatmentType getTreatmentType() {
        return this.treatmentType;
    }

    public void setTreatmentType(TreatmentType treatmentType) {
        this.treatmentType = treatmentType;
    }

    // public double getPrice() {
    //     return this.price;
    // }

    // public void setPrice(double price) {
    //     if(price < 0) throw new IllegalArgumentException("Price cannot be negative");
    //     this.price = price;
    // }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        if(duration < 0) throw new IllegalArgumentException("Duration cannot be negative");
        this.duration = duration;
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null) return false;
        if(!(obj instanceof CosmeticTreatment)) return false;
        CosmeticTreatment cosmeticTreatment = (CosmeticTreatment) obj;
        return this.id == cosmeticTreatment.id;
    }

    public static CosmeticTreatment parseFromCsv(String[] line, TreatmentType treatmentType) {
        return new CosmeticTreatment(
            Integer.parseInt(line[0]),
            line[1],
            treatmentType,
            Integer.parseInt(line[3])
        );
    }

    public String[] toCsv() {
        return new String[] {
            Integer.toString(getId()),
            getName(),
            Integer.toString(getTreatmentType().getId()),
            Integer.toString(getDuration())
        };
    }

    @Override
    public String toString(){
        return String.format("%s (%d min)", getName(), getDuration());
    }
}
