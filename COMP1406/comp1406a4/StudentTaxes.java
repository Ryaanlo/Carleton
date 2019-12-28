package comp1406a4;

public class StudentTaxes extends Taxes{

    protected double income;
    protected double tuition;

    public StudentTaxes(String name, int sin, double income, double tuition){
        super(name, sin);
        this.income = income;
        this.tuition = tuition;
    }

    @Override
    public long taxesOwed(){
        long tax;
        if (income < 10000.0){
            tax = (long)(Math.round(this.income*0.10 - this.tuition));
        }else if(income >= 10000.0 && income < 30000.0){
            tax = (long)(Math.round(this.income*0.30 - this.tuition*0.50));
        }else{
            tax = (long)(Math.round(this.income*0.50 - this.tuition*0.25));
        }
        return tax;
    }

}