package comp1406a3;

public class FindTestCase{
	
	int[] target;
	int[] array;
  int   expectedFind;
  int[] expectedFindAgain;

	public int[] getTarget(){ return this.target; }
	public int[] getArray(){ return this.array; }
	public int   getExpFind(){ return this.expectedFind; }
	public int[] getExpFindAgain(){ return this.expectedFindAgain; }
	
	/** A test case for both the 
	  *
		*
		* @param target is the target sequence. 
		*        It is an array of one or more integers.
		* @param array is an array of zero or more integers.
		* @param expectedFind is the expected output of Find.locateSequence(target, array)
		* @param expectedFindAgain is the expected output of 
		*        Find.locateAllSequenceLocations(target.array)
		*/
	public FindTestCase(int[] target, int[] array, 
	                     int expectedFind, int[] expectedFindAgain){
	  this.target = target;
		this.array = array;
		this.expectedFind = expectedFind;
		this.expectedFindAgain = expectedFindAgain;
	}
	
	
}