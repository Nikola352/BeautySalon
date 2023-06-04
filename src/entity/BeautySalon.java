package entity;

import java.util.ArrayList;

import utils.AppSettings;

public class BeautySalon implements CsvConvertible {
    private String name;

    private int openingHour;
    private int closingHour;
    private ArrayList<DayOfWeek> workingDays;

    private double totalIncome;
    private double totalExpenses;
    private double totalProfit;

    // for employees
    private double bonusThreshold; 
    private double bonusAmount;

    // for clients
    private double loyaltyCardThreshold;
    private double loyaltyCardDiscount;

    public BeautySalon() {}

    public BeautySalon(String name, int openingHour, int closingHour, ArrayList<DayOfWeek> workingDays,  double totalIncome, double totalExpenses, double totalProfit, double bonusThreashold, double bonusValue, double loyaltyCardDiscount, double loyaltyCardThreshold){
        setName(name);
        setOpeningHour(openingHour);
        setClosingHour(closingHour);
        setWorkingDays(workingDays);
        setTotalIncome(totalIncome);
        setTotalExpenses(totalExpenses);
        setTotalProfit(totalProfit);
        setBonusThreshold(bonusThreashold);
        setBonusAmount(bonusValue);
        setLoyaltyCardDiscount(loyaltyCardDiscount);
        setLoyaltyCardThreshold(loyaltyCardThreshold);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOpeningHour() {
        return this.openingHour;
    }

    public void setOpeningHour(int openingHour) {
        this.openingHour = openingHour;
    }

    public int getClosingHour() {
        return this.closingHour;
    }

    public void setClosingHour(int closingHour) {
        this.closingHour = closingHour;
    }

    public ArrayList<DayOfWeek> getWorkingDays() {
        return this.workingDays;
    }

    public void setWorkingDays(ArrayList<DayOfWeek> workingDays) {
        this.workingDays = workingDays;
    }

    public void addWorkingDay(DayOfWeek day){
        this.workingDays.add(day);
    }

    public void removeWorkingDay(DayOfWeek day){
        this.workingDays.remove(day);
    }

    public DayOfWeek getOpeningDay() {
        return this.workingDays.get(0);
    }

    public DayOfWeek getClosingDay() {
        return this.workingDays.get(this.workingDays.size() - 1);
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

    public double getBonusThreshold() {
        return this.bonusThreshold;
    }

    public void setBonusThreshold(double bonusThreashold) {
        this.bonusThreshold = bonusThreashold;
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
        calculateProfit();
    }
    
    public void addExpense(double expense){
        setTotalExpenses(getTotalExpenses() + expense);
        calculateProfit();
    }

    public double calculateProfit(){
        setTotalProfit(getTotalIncome() - getTotalExpenses());
        return getTotalProfit();
    }

    public static BeautySalon parseFromCsv(String[] line){
        String[] workingDaysStrings = line[3].split(AppSettings.getInstance().getInnerDelimiter());
        ArrayList<DayOfWeek> workingDays = new ArrayList<DayOfWeek>();
        for(String day : workingDaysStrings){
            workingDays.add(DayOfWeek.valueOf(day));
        }

        return new BeautySalon(
            line[0],
            Integer.parseInt(line[1]),
            Integer.parseInt(line[2]),
            workingDays,
            Double.parseDouble(line[4]),
            Double.parseDouble(line[5]),
            Double.parseDouble(line[6]),
            Integer.parseInt(line[7]),
            Double.parseDouble(line[8]),
            Double.parseDouble(line[9]),
            Double.parseDouble(line[10])
        );
    }

    public String[] toCsv(){
        StringBuilder sb = new StringBuilder();
        for(DayOfWeek day : getWorkingDays()){
            sb.append(day.toString());
            sb.append(AppSettings.getInstance().getInnerDelimiter());
        }
        sb.deleteCharAt(sb.length() - 1); // remove last delimiter (,)

        return new String[]{
            getName(),
            Integer.toString(getOpeningHour()),
            Integer.toString(getClosingHour()),
            sb.toString(),
            Double.toString(getTotalIncome()),
            Double.toString(getTotalExpenses()),
            Double.toString(getTotalProfit()),
            Double.toString(getBonusThreshold()),
            Double.toString(getBonusAmount()),
            Double.toString(getLoyaltyCardDiscount()),
            Double.toString(getLoyaltyCardThreshold())
        };
    }

}
