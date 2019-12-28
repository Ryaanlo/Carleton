package comp1406a4;

public class SeniorTaxes extends Taxes{

    protected double income;
    protected double health_costs;

    public SeniorTaxes(String name, int sin, double income, double health_costs){
        super(name, sin);
        this.income = income;
        this.health_costs = health_costs;
    }

    @Override
    public long taxesOwed(){
        long tax;
        if (income < 20000.0){
            tax = (long)(Math.round(this.health_costs * -1.0));
        }else if(income >= 20000.0 && income < 50000.0){
            tax = (long)(Math.round(this.income*0.10 - this.health_costs*0.75));
        }else{
            tax = (long)(Math.round(this.income*0.40 - this.health_costs*0.50));
        }
        return tax;
    }

}