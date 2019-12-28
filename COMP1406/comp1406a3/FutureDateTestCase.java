package comp1406a3;

public class FutureDateTestCase{
	
	Date now;
	int  daysInFuture;
	Date expected;
	
	public Date getNow(){ return this.now; }
	public int  getDays(){ return this.daysInFuture; }
	public Date getExpected(){ return this.expected; }
	
	/** A test case for both the futureDate method
	  *
		*
		* @param target is the target sequence. 
		*        It is an array of one or more integers.
		* @param array is an array of zero or more integers.
		* @param expectedFind is the expected output of Find.locateSequence(target, array)
		* @param expectedFindAgain is the expected output of 
		*        Find.locateAllSequenceLocations(target.array)
		*/
	public FutureDateTestCase(Date now, int daysInFuture, Date expected){
		this.now = now;
		this.daysInFuture = daysInFuture;
		this.expected = expected;
	}
	
	
}