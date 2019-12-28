package comp1406a3;

public class TestFind{

	/** Generate test cases for the (modified) Find class.
		* @return an array of test cases for the (modified) Find class.
		*/
	public static FindTestCase[] makeTestCases(){
		int index = 0;
		FindTestCase[] allTestCases = new FindTestCase[6];
		
		int[] t = {0};							// target
		int[] a = {0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0};							// array
		int f = 11;								// find output
		int[] fa = {9, 0, 1, 3, 4, 5, 7, 8, 9, 11};							// findagain output
		FindTestCase testcase = new FindTestCase(t, a, f, fa);	
		allTestCases[index] = testcase;	
		index ++;

		t = new int[]{0};
		a = new int[]{0};
		f = 0;
		fa = new int[]{1, 0};
		testcase = new FindTestCase(t, a, f, fa);	
		allTestCases[index] = testcase;	
		index ++;

		t = new int[]{1};
		a = new int[]{0};
		f = -1;
		fa = new int[]{0};
		testcase = new FindTestCase(t, a, f, fa);	
		allTestCases[index] = testcase;	
		index ++;


		t = new int[]{1};
		a = new int[]{0};
		f = -1;
		fa = new int[]{0};
		testcase = new FindTestCase(t, a, f, fa);	
		allTestCases[index] = testcase;	
		index ++;

		t = new int[]{1, 1};
		a = new int[]{0};
		f = -1;
		fa = new int[]{0};
		testcase = new FindTestCase(t, a, f, fa);	
		allTestCases[index] = testcase;	
		index ++;

		t = new int[]{1, 1};
		a = new int[]{1, 1, 1, 1, 1};
		f = 3;
		fa = new int[]{4, 0, 1, 2, 3};
		testcase = new FindTestCase(t, a, f, fa);	
		allTestCases[index] = testcase;	
		index ++;

		return allTestCases;
	}

}