package comp1406a4;

public class BasicTaxes extends Taxes{
  protected double income;
	protected double deductions;
  public BasicTaxes(String name, int sin, double income, double deductions){
		super(name, sin);
		this.income = income;
		this.deductions = deductions;
	}
		
  /** The basic tax rate is applied to this 
	 * individual. The taxes owed is 50% their income 
	 * less deductions. The amount may be negative.
   *	
	 * @return the basic taxes owed (50% of income less 
	 * deductions), rounded to the nearest dollar. The * amount may be negative (i.e., a return).
	 */
	@Override	
	public long taxesOwed(){
		return Math.round( this.income*0.5 - this.deductions );
	}

}
