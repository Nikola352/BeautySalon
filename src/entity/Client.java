package entity;

import ui.ClientCLI;

public class Client extends User {
    private double totalSpent;
    private boolean hasLoyaltyCard;

    public Client() {
        super();
    }

    public Client(int id, String username, String password, String name, String lastname, Gender gender, String phoneNum, String address){
        super(id, username, password, name, lastname, gender, phoneNum, address);
        setTotalSpent(0);
        setHasLoyaltyCard(false);
    }

    public Client(int id, String username, String password, String name, String lastname, Gender gender, String phoneNum, String address, double totalSpent, boolean hasLoyaltyCard){
        super(id, username, password, name, lastname, gender, phoneNum, address);
        setTotalSpent(totalSpent);
        setHasLoyaltyCard(hasLoyaltyCard);
    }

    public double getTotalSpent() {
        return this.totalSpent;
    }

    public void setTotalSpent(double totalSpent) {
        this.totalSpent = totalSpent;
    }

    public void addTotalSpent(double totalSpent) {
        this.totalSpent += totalSpent;
    }

    public boolean isHasLoyaltyCard() {
        return this.hasLoyaltyCard;
    }

    public boolean getHasLoyaltyCard() {
        return this.hasLoyaltyCard;
    }

    public void setHasLoyaltyCard(boolean hasLoyaltyCard) {
        this.hasLoyaltyCard = hasLoyaltyCard;
    }

    public void updateLoyaltyCardStatus(double loyaltyCardThreshold){
        if (getTotalSpent() >= loyaltyCardThreshold)
            setHasLoyaltyCard(true);
        else 
            setHasLoyaltyCard(false);
    }

    public static Client parseFromCsv(String[] line){
        return new Client(
            Integer.parseInt(line[0]),
            line[1],
            line[2],
            line[3],
            line[4],
            Gender.valueOf(line[5]),
            line[6],
            line[7],
            Double.parseDouble(line[8]),
            Boolean.parseBoolean(line[9])
        );
    }

    public String[] toCsv(){
        return new String[] {
            String.valueOf(getId()),
            getUsername(),
            getPassword(),
            getName(),
            getLastname(),
            getGender().toString(),
            getPhoneNum(),
            getAddress(),
            String.valueOf(getTotalSpent()),
            String.valueOf(getHasLoyaltyCard())
        };
    }

    @Override
    public boolean showCLI(){
        ClientCLI clientCLI = new ClientCLI(this);
        return clientCLI.run();
    }
}
