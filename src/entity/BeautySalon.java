package entity;

public class BeautySalon implements CsvConvertible {
    private double totalIncome;
    private double totalExpenses;
    private double totalProfit;

    // for employees
    private int bonusThreashold; 
    private double bonusAmount;

    // for clients
    private double loyaltyCardThreshold;
    private double loyaltyCardDiscount;

    public BeautySalon() {}

    public BeautySalon(double totalIncome, double totalExpenses, double totalProfit, int bonusThreashold, double bonusValue, double loyaltyCardDiscount, double loyaltyCardThreshold){
        setTotalIncome(totalIncome);
        setTotalExpenses(totalExpenses);
        setTotalProfit(totalProfit);
        setBonusThreashold(bonusThreashold);
        setBonusAmount(bonusValue);
        setLoyaltyCardDiscount(loyaltyCardDiscount);
        setLoyaltyCardThreshold(loyaltyCardThreshold);
    }

    public double getTotalIncome() {
        return this.totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public double getTotalExpenses() {
        return this.totalExpenses;
    }

    public void setTotalExpenses(double totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public double getTotalProfit() {
        return this.totalProfit;
    }

    public void setTotalProfit(double totalProfit) {
        this.totalProfit = totalProfit;
    }

    public int getBonusThreashold() {
        return this.bonusThreashold;
    }

    public void setBonusThreashold(int bonusThreashold) {
        this.bonusThreashold = bonusThreashold;
    }

    public double getBonusAmount() {
        return this.bonusAmount;
    }

    public void setBonusAmount(double bonusValue) {
        this.bonusAmount = bonusValue;
    }

    public double getLoyaltyCardDiscount() {
        return this.loyaltyCardDiscount;
    }

    public void setLoyaltyCardDiscount(double loyaltyCardDiscount) {
        this.loyaltyCardDiscount = loyaltyCardDiscount;
    }

    public double getLoyaltyCardThreshold() {
        return this.loyaltyCardThreshold;
    }

    public void setLoyaltyCardThreshold(double loyaltyCardThreshold) {
        this.loyaltyCardThreshold = loyaltyCardThreshold;
    }

    public void addIncome(double income){
        setTotalIncome(getTotalIncome() + income);
    }
    
    public void addExpense(double expense){
        setTotalExpenses(getTotalExpenses() + expense);
    }

    public double calculateProfit(){
        setTotalProfit(getTotalIncome() - getTotalExpenses());
        return getTotalProfit();
    }

    public static BeautySalon parseFromCsv(String[] line){
        return new BeautySalon(
            Double.parseDouble(line[0]),
            Double.parseDouble(line[1]),
            Double.parseDouble(line[2]),
            Integer.parseInt(line[3]),
            Double.parseDouble(line[4]),
            Double.parseDouble(line[5]),
            Double.parseDouble(line[6])
        );
    }

    public String[] toCsv(){
        return new String[]{
            String.valueOf(getTotalIncome()),
            String.valueOf(getTotalExpenses()),
            String.valueOf(getTotalProfit()),
            String.valueOf(getBonusThreashold()),
            String.valueOf(getBonusAmount()),
            String.valueOf(getLoyaltyCardDiscount()),
            String.valueOf(getLoyaltyCardThreshold())
        };
    }

}
