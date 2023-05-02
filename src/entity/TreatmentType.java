package entity;

public class TreatmentType implements IdAssignable, CsvConvertible {
    private int id;
    public String name;
    public String description;

    public TreatmentType() {}

    public TreatmentType(int id, String name, String description){
        setId(id);
        setName(name);
        setDescription(description);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null) return false;
        if(!(obj instanceof TreatmentType)) return false;
        TreatmentType treatmentType = (TreatmentType) obj;
        return this.getId() == treatmentType.getId();
    }

    public static TreatmentType parseFromCsv(String[] line) {
        return new TreatmentType(
            Integer.parseInt(line[0]),
            line[1],
            line[2]
        );
    }

    public String[] toCsv() {
        return new String[] {
            Integer.toString(getId()),
            getName(),
            getDescription()
        };
    }
}
