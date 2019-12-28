package comp1406a3;

public class TestGrades{
	/** Generate test cases for the Grades class.
		* @return an array of test cases for the Grades class
		*/
	public static GradesTestCase[] makeTestCases(){

		GradesTestCase[] allTestCases = new GradesTestCase[82];

		double[] a = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		double[] m = {0.0, 0.0, 0.0};
		double[] t = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		double f = 0.0;
		GradesTestCase testcase = new GradesTestCase(a, m, t, f, "F", false);
		allTestCases[0] = testcase;

		a = new double[]{90.0, 92.0, 98.0, 96.0, 92.0, 99.0, 92.0, 96.0, 92.0};
		m = new double[]{94.0, 93.0, 90.0};
		t = new double[]{97.0, 97.0, 90.0, 93.0, 92.0, 92.0, 99.0, 94.0, 97.0, 92.0};
		f = 81.0;
		testcase = new GradesTestCase(a, m, t, f, "A+", true);
		allTestCases[1] = testcase;	

		a = new double[]{100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0};
		m = new double[]{40.0, 40.0, 40.0};
		t = new double[]{100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0};
		f = 50.0;
		testcase = new GradesTestCase(a, m, t, f, "B", false);
		allTestCases[2] = testcase;	
	
		a = new double[]{50.0, 50.0, 50.0, 50.0, 50.0, 50.0, 50.0, 50.0, 50.0};
		m = new double[]{50.0, 50.0, 50.0};
		t = new double[]{50.0, 50.0, 50.0, 50.0, 50.0, 50.0, 50.0, 50.0, 50.0, 50.0};
		f = 50.0;
		testcase = new GradesTestCase(a, m, t, f, "D-", false);
		allTestCases[3] = testcase;	

		a = new double[]{100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0};
		m = new double[]{100.0, 100.0, 100.0};
		t = new double[]{100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0};
		f = 100.0;
		testcase = new GradesTestCase(a, m, t, f, "A+", true);
		allTestCases[4] = testcase;	

		a = new double[]{100.0, 90.0, 80.0, 100.0, 80.0, 100.0, 90.0, 70.0, 85.0};
		m = new double[]{40.0, 30.0, 60.0};
		t = new double[]{100.0, 70.0, 100.0, 90.0, 80.0, 100.0, 90.0, 100.0, 90.0, 80.0};
		f = 71.0;
		testcase = new GradesTestCase(a, m, t, f, "B", true);
		allTestCases[5] = testcase;	

		a = new double[]{70.0, 90.0, 60.0, 80.0, 80.0, 80.0, 90.0, 70.0, 85.0};
		m = new double[]{70.0, 90.0, 50.0};
		t = new double[]{100.0, 70.0, 100.0, 90.0, 80.0, 100.0, 90.0, 100.0, 90.0, 80.0};
		f = 23.0;
		testcase = new GradesTestCase(a, m, t, f, "C", true);
		allTestCases[6] = testcase;	

		a = new double[]{70.0, 90.0, 60.0, 80.0, 80.0, 80.0, 90.0, 70.0, 85.0};
		m = new double[]{40.0, 40.0, 20.0};
		t = new double[]{100.0, 70.0, 100.0, 90.0, 80.0, 100.0, 90.0, 100.0, 90.0, 80.0};
		f = 23.0;
		testcase = new GradesTestCase(a, m, t, f, "F", false);
		allTestCases[7] = testcase;	

		a = new double[]{100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 0.0, 100.0};
		m = new double[]{50.0, 50.0, 50.0};
		t = new double[]{100.0, 100.0, 100.0, 0.0, 100.0, 100.0, 0.0, 100.0, 100.0, 100.0};
		f = 49.0;
		testcase = new GradesTestCase(a, m, t, f, "F", false);
		allTestCases[8] = testcase;	

		a = new double[]{100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 0.0};
		m = new double[]{50.0, 50.0, 0.0};
		t = new double[]{100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 0.0, 0.0};
		f = 54.0;
		testcase = new GradesTestCase(a, m, t, f, "B-", true);
		allTestCases[9] = testcase;	

		a = new double[]{100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 0.0, 100.0, 100.0};
		m = new double[]{0.0, 100.0, 100.0};
		t = new double[]{0.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 0.0, 100.0};
		f = 0.0;
		testcase = new GradesTestCase(a, m, t, f, "B-", true);
		allTestCases[10] = testcase;	

		a = new double[]{100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 0.0, 100.0};
		m = new double[]{100.0, 0.0, 100.0};
		t = new double[]{100.0, 0.0, 100.0, 100.0, 100.0, 100.0, 100.0, 0.0, 100.0, 100.0};
		f = 100.0;
		testcase = new GradesTestCase(a, m, t, f, "A+", true);
		allTestCases[11] = testcase;	

		a = new double[]{100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 0.0};
		m = new double[]{100.0, 100.0, 0.0};
		t = new double[]{100.0, 100.0, 0.0, 100.0, 100.0, 100.0, 0.0, 100.0, 100.0, 100.0};
		f = 50.0;
		testcase = new GradesTestCase(a, m, t, f, "A", true);
		allTestCases[12] = testcase;

		a = new double[]{100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 0.0};
		m = new double[]{50.0, 40.0, 0.0};
		t = new double[]{100.0, 100.0, 100.0, 0.0, 100.0, 0.0, 0.0, 100.0, 100.0, 100.0};
		f = 50.0;
		testcase = new GradesTestCase(a, m, t, f, "F", false);
		allTestCases[13] = testcase;

		a = new double[]{70.0, 60.0, 80.0, 80.0, 60.0, 70.0, 90.0, 70.0, 0.0};
		m = new double[]{50.0, 40.0, 0.0};
		t = new double[]{70.0, 90.0, 80.0, 100.0, 100.0, 0.0, 0.0, 100.0, 0.0, 100.0};
		f = 14.0;
		testcase = new GradesTestCase(a, m, t, f, "F", false);
		allTestCases[14] = testcase;

		a = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		m = new double[]{100.0, 100.0, 100.0};
		t = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		f = 100.0;
		testcase = new GradesTestCase(a, m, t, f, "C-", true);
		allTestCases[15] = testcase;

		a = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		m = new double[]{0.0, 0.0, 0.0};
		t = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		f = 100.0;
		testcase = new GradesTestCase(a, m, t, f, "F", false);
		allTestCases[16] = testcase;	

		a = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		m = new double[]{100.0, 0.0, 100.0};
		t = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		f = 0.0;
		testcase = new GradesTestCase(a, m, t, f, "F", false);
		allTestCases[17] = testcase;	

		a = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		m = new double[]{100.0, 0.0, 100.0};
		t = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		f = 100.0;
		testcase = new GradesTestCase(a, m, t, f, "C-", true);
		allTestCases[18] = testcase;	

		a = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		m = new double[]{0.0, 100.0, 100.0};
		t = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		f = 100.0;
		testcase = new GradesTestCase(a, m, t, f, "C-", true);
		allTestCases[19] = testcase;

		a = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		m = new double[]{0.0, 0.0, 0.0};
		t = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		f = 100.0;
		testcase = new GradesTestCase(a, m, t, f, "F", false);
		allTestCases[20] = testcase;

		a = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		m = new double[]{100.0, 0.0, 100.0};
		t = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		f = 0.0;
		testcase = new GradesTestCase(a, m, t, f, "F", false);
		allTestCases[21] = testcase;

		a = new double[]{100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 0.0, 0.0, 0.0};
		m = new double[]{100.0, 0.0, 100.0};
		t = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		f = 0.0;
		testcase = new GradesTestCase(a, m, t, f, "C-", true);
		allTestCases[22] = testcase;

		a = new double[]{100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 0.0, 0.0, 0.0};
		m = new double[]{0.0, 0.0, 0.0};
		t = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		f = 100.0;
		testcase = new GradesTestCase(a, m, t, f, "C-", true);
		allTestCases[23] = testcase;

		a = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		m = new double[]{50.0, 50.0, 50.0};
		t = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		f = 50.0;
		testcase = new GradesTestCase(a, m, t, f, "F", false);
		allTestCases[24] = testcase;

		a = new double[]{0.0, 100.0, 0.0, 100.0, 0.0, 100.0, 100.0, 0.0, 100.0};
		m = new double[]{50.0, 50.0, 50.0};
		t = new double[]{100.0, 0.0, 100.0, 0.0, 100.0, 0.0, 100.0, 0.0, 100.0, 0.0};
		f = 50.0;
		testcase = new GradesTestCase(a, m, t, f, "D-", false);
		allTestCases[25] = testcase;

		a = new double[]{0.0, 100.0, 0.0, 100.0, 0.0, 100.0, 100.0, 100.0, 100.0};
		m = new double[]{50.0, 50.0, 50.0};
		t = new double[]{100.0, 0.0, 100.0, 0.0, 100.0, 0.0, 100.0, 0.0, 100.0, 0.0};
		f = 50.0;
		testcase = new GradesTestCase(a, m, t, f, "D-", false);
		allTestCases[26] = testcase;

		a = new double[]{0.0, 100.0, 0.0, 100.0, 0.0, 100.0, 100.0, 100.0, 100.0};
		m = new double[]{50.0, 50.0, 50.0};
		t = new double[]{100.0, 100.0, 100.0, 100.0, 100.0, 0.0, 100.0, 0.0, 100.0, 0.0};
		f = 50.0;
		testcase = new GradesTestCase(a, m, t, f, "D", false);
		allTestCases[27] = testcase;

		a = new double[]{60.0, 70.0, 60.0, 70.0, 80.0, 50.0, 70.0, 60.0, 90.0};
		m = new double[]{50.0, 70.0, 50.0};
		t = new double[]{60.0, 80.0, 60.0, 50.0, 40.0, 0.0, 10.0, 0.0, 100.0, 0.0};
		f = 47.0;
		testcase = new GradesTestCase(a, m, t, f, "D+", false);
		allTestCases[28] = testcase;

		a = new double[]{60.0, 70.0, 60.0, 70.0, 80.0, 50.0, 70.0, 60.0, 90.0};
		m = new double[]{50.0, 70.0, 50.0};
		t = new double[]{60.0, 80.0, 60.0, 50.0, 40.0, 0.0, 10.0, 0.0, 100.0, 0.0};
		f = 67.0;
		testcase = new GradesTestCase(a, m, t, f, "C", true);
		allTestCases[29] = testcase;

		a = new double[]{60.0, 70.0, 60.0, 70.0, 80.0, 50.0, 70.0, 60.0, 90.0};
		m = new double[]{50.0, 70.0, 50.0};
		t = new double[]{60.0, 80.0, 60.0, 50.0, 40.0, 0.0, 10.0, 0.0, 100.0, 0.0};
		f = 100.0;
		testcase = new GradesTestCase(a, m, t, f, "B", true);
		allTestCases[30] = testcase;

		a = new double[]{60.0, 70.0, 60.0, 70.0, 80.0, 50.0, 70.0, 60.0, 90.0};
		m = new double[]{0.0, 70.0, 8.0};
		t = new double[]{60.0, 80.0, 60.0, 50.0, 40.0, 0.0, 10.0, 0.0, 100.0, 0.0};
		f = 99.0;
		testcase = new GradesTestCase(a, m, t, f, "B+", true);
		allTestCases[31] = testcase;

		a = new double[]{60.0, 70.0, 100.0, 70.0, 80.0, 100.0, 70.0, 60.0, 90.0};
		m = new double[]{50.0, 20.0, 50.0};
		t = new double[]{60.0, 80.0, 60.0, 100.0, 100.0, 100.0, 10.0, 100.0, 100.0, 0.0};
		f = 49.9;
		testcase = new GradesTestCase(a, m, t, f, "F", false);
		allTestCases[32] = testcase;

		a = new double[]{0.0, 0.0, 10.0, 70.0, 0.0, 10.0, 70.0, 60.0, 0.0};
		m = new double[]{50.0, 20.0, 50.0};
		t = new double[]{60.0, 0.0, 60.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 0.0};
		f = 60.0;
		testcase = new GradesTestCase(a, m, t, f, "F", false);
		allTestCases[33] = testcase;

		a = new double[]{100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0};
		m = new double[]{100.0, 100.0, 100.0};
		t = new double[]{100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0};
		f = 100.0;
		testcase = new GradesTestCase(a, m, t, f, "A+", false);
		allTestCases[34] = testcase;	

		a = new double[]{100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0};
		m = new double[]{100.0, 100.0, 100.0};
		t = new double[]{100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0};
		f = 100.0;
		testcase = new GradesTestCase(a, m, t, f, "A", false);
		allTestCases[35] = testcase;	

		a = new double[]{100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0};
		m = new double[]{100.0, 100.0, 100.0};
		t = new double[]{100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0};
		f = 100.0;
		testcase = new GradesTestCase(a, m, t, f, "F", false);
		allTestCases[36] = testcase;	

		a = new double[]{100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0};
		m = new double[]{100.0, 100.0, 100.0};
		t = new double[]{100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0};
		f = 100.0;
		testcase = new GradesTestCase(a, m, t, f, "F", true);
		allTestCases[37] = testcase;

		a = new double[]{100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0};
		m = new double[]{100.0, 100.0, 100.0};
		t = new double[]{100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0};
		f = 100.0;
		testcase = new GradesTestCase(a, m, t, f, "D-", true);
		allTestCases[38] = testcase;

		a = new double[]{50.0, 50.0, 50.0, 50.0, 50.0, 50.0, 50.0, 50.0, 50.0};
		m = new double[]{50.0, 50.0, 50.0};
		t = new double[]{50.0, 50.0, 50.0, 50.0, 50.0, 50.0, 50.0, 50.0, 50.0, 50.0};
		f = 50.0;
		testcase = new GradesTestCase(a, m, t, f, "D-", true);
		allTestCases[39] = testcase;	

		a = new double[]{50.0, 50.0, 50.0, 50.0, 50.0, 50.0, 50.0, 50.0, 50.0};
		m = new double[]{50.0, 50.0, 50.0};
		t = new double[]{50.0, 50.0, 50.0, 50.0, 50.0, 50.0, 50.0, 50.0, 50.0, 50.0};
		f = 50.0;
		testcase = new GradesTestCase(a, m, t, f, "A-", true);
		allTestCases[40] = testcase;	




		a = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		m = new double[]{0.0, 0.0, 0.0};
		t = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		f = 0.0;
		testcase = new GradesTestCase(a, m, t, f, "F", true);
		allTestCases[41] = testcase;

		a = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		m = new double[]{0.0, 0.0, 0.0};
		t = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		f = 0.0;
		testcase = new GradesTestCase(a, m, t, f, "A+", true);
		allTestCases[42] = testcase;

		a = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		m = new double[]{0.0, 0.0, 0.0};
		t = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		f = 0.0;
		testcase = new GradesTestCase(a, m, t, f, "A+", false);
		allTestCases[43] = testcase;

		a = new double[]{60.0, 70.0, 100.0, 70.0, 80.0, 100.0, 70.0, 60.0, 90.0};
		m = new double[]{50.0, 20.0, 50.0};
		t = new double[]{60.0, 80.0, 60.0, 100.0, 100.0, 100.0, 10.0, 100.0, 100.0, 0.0};
		f = 49.9;
		testcase = new GradesTestCase(a, m, t, f, "A", false);
		allTestCases[44] = testcase;

		a = new double[]{0.0, 100.0, 0.0, 100.0, 0.0, 100.0, 100.0, 100.0, 100.0};
		m = new double[]{50.0, 50.0, 50.0};
		t = new double[]{100.0, 100.0, 100.0, 100.0, 100.0, 0.0, 100.0, 0.0, 100.0, 0.0};
		f = 50.0;
		testcase = new GradesTestCase(a, m, t, f, "A", false);
		allTestCases[45] = testcase;

		a = new double[]{100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 0.0, 100.0};
		m = new double[]{50.0, 50.0, 50.0};
		t = new double[]{100.0, 100.0, 100.0, 0.0, 100.0, 100.0, 0.0, 100.0, 100.0, 100.0};
		f = 49.0;
		testcase = new GradesTestCase(a, m, t, f, "A", true);
		allTestCases[46] = testcase;	

		a = new double[]{100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 0.0, 100.0};
		m = new double[]{50.0, 50.0, 50.0};
		t = new double[]{100.0, 100.0, 100.0, 0.0, 100.0, 100.0, 0.0, 100.0, 100.0, 100.0};
		f = 49.0;
		testcase = new GradesTestCase(a, m, t, f, "F", true);
		allTestCases[47] = testcase;	

		a = new double[]{0.0, 100.0, 0.0, 100.0, 0.0, 100.0, 100.0, 100.0, 100.0};
		m = new double[]{50.0, 50.0, 50.0};
		t = new double[]{100.0, 100.0, 100.0, 100.0, 100.0, 0.0, 100.0, 0.0, 100.0, 0.0};
		f = 50.0;
		testcase = new GradesTestCase(a, m, t, f, "D", true);
		allTestCases[48] = testcase;



		a = new double[]{100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0};
		m = new double[]{40.0, 40.0, 40.0};
		t = new double[]{100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0};
		f = 50.0;
		testcase = new GradesTestCase(a, m, t, f, "B", true);
		allTestCases[49] = testcase;	

		a = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		m = new double[]{50.0, 50.0, 50.0};
		t = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		f = 50.0;
		testcase = new GradesTestCase(a, m, t, f, "F", true);
		allTestCases[50] = testcase;

		int counter = 51;

		a = new double[]{89.99, 89.99, 89.99, 89.99, 89.99, 89.99, 89.99, 0.0, 89.99};
		m = new double[]{89.99, 89.99, 89.99};
		t = new double[]{89.99, 89.99, 89.99, 89.99, 0.0, 89.99, 89.99, 0.0, 89.99, 89.99};
		f = 89.99;
		testcase = new GradesTestCase(a, m, t, f, "A", true);
		allTestCases[counter] = testcase;
		counter ++;

		// border cases and decimals

		a = new double[]{49.99, 49.99, 49.99, 49.99, 49.99, 49.99, 49.99, 4.99, 49.99};
		m = new double[]{9.99, 49.99, 49.99};
		t = new double[]{49.99, 49.99, 9.99, 49.99, 4.99, 49.99, 49.99, 49.99, 49.99, 49.99};
		f = 49.99;
		testcase = new GradesTestCase(a, m, t, f, "F", true);
		allTestCases[counter] = testcase;		
		counter ++;

		a = new double[]{49.99, 49.99, 49.99, 49.99, 49.99, 49.99, 49.99, 4.99, 49.99};
		m = new double[]{9.99, 49.99, 49.99};
		t = new double[]{49.99, 49.99, 9.99, 49.99, 4.99, 49.99, 49.99, 49.99, 49.99, 49.99};
		f = 49.99;
		testcase = new GradesTestCase(a, m, t, f, "F", false);
		allTestCases[counter] = testcase;		
		counter ++;

		a = new double[]{52.99, 52.99, 52.99, 52.99, 52.99, 52.99, 52.99, 52.99, 52.99};
		m = new double[]{52.99, 52.99, 52.99};
		t = new double[]{52.99, 52.99, 52.99, 52.99, 52.99, 52.99, 52.99, 52.99, 52.99, 52.99};
		f = 52.99;
		testcase = new GradesTestCase(a, m, t, f, "D-", false);
		allTestCases[counter] = testcase;		
		counter ++;

		a = new double[]{52.99, 52.99, 52.99, 52.99, 52.99, 52.99, 52.99, 52.99, 52.99};
		m = new double[]{52.99, 52.99, 52.99};
		t = new double[]{52.99, 52.99, 52.99, 52.99, 52.99, 52.99, 52.99, 52.99, 52.99, 52.99};
		f = 52.99;
		testcase = new GradesTestCase(a, m, t, f, "D-", true);
		allTestCases[counter] = testcase;		
		counter ++;

		a = new double[]{53.0, 53.0, 53.0, 53.0, 53.0, 53.0, 53.0, 53.0, 53.0};
		m = new double[]{53.0, 53.0, 53.0};
		t = new double[]{53.0, 53.0, 53.0, 53.0, 53.0, 53.0, 53.0, 53.0, 53.0, 53.0};
		f = 53.0;
		testcase = new GradesTestCase(a, m, t, f, "D", true);
		allTestCases[counter] = testcase;		
		counter ++;

		a = new double[]{53.0, 53.0, 53.0, 53.0, 53.0, 53.0, 53.0, 53.0, 53.0};
		m = new double[]{53.0, 53.0, 53.0};
		t = new double[]{53.0, 53.0, 53.0, 53.0, 53.0, 53.0, 53.0, 53.0, 53.0, 53.0};
		f = 53.0;
		testcase = new GradesTestCase(a, m, t, f, "D", false);
		allTestCases[counter] = testcase;		
		counter ++;

		a = new double[]{56.9, 56.9, 56.9, 56.9, 56.9, 56.9, 56.9, 56.9, 56.9};
		m = new double[]{56.9, 56.9, 56.9};
		t = new double[]{56.9, 56.9, 56.9, 56.9, 56.9, 56.9, 56.9, 56.9, 56.9, 56.9};
		f = 56.9;
		testcase = new GradesTestCase(a, m, t, f, "D", false);
		allTestCases[counter] = testcase;		
		counter ++;

		a = new double[]{56.9, 56.9, 56.9, 56.9, 56.9, 56.9, 56.9, 56.9, 56.9};
		m = new double[]{56.9, 56.9, 56.9};
		t = new double[]{56.9, 56.9, 56.9, 56.9, 56.9, 56.9, 56.9, 56.9, 56.9, 56.9};
		f = 56.9;
		testcase = new GradesTestCase(a, m, t, f, "D", true);
		allTestCases[counter] = testcase;		
		counter ++;

		a = new double[]{57.0, 57.0, 57.0, 57.0, 57.0, 57.0, 57.0, 57.0, 57.0};
		m = new double[]{57.0, 57.0, 57.0};
		t = new double[]{57.0, 57.0, 57.0, 57.0, 57.0, 57.0, 57.0, 57.0, 57.0, 57.0};
		f = 57.0;
		testcase = new GradesTestCase(a, m, t, f, "D+", true);
		allTestCases[counter] = testcase;		
		counter ++;

		a = new double[]{57.0, 57.0, 57.0, 57.0, 57.0, 57.0, 57.0, 57.0, 57.0};
		m = new double[]{57.0, 57.0, 57.0};
		t = new double[]{57.0, 57.0, 57.0, 57.0, 57.0, 57.0, 57.0, 57.0, 57.0, 57.0};
		f = 57.0;
		testcase = new GradesTestCase(a, m, t, f, "D+", false);
		allTestCases[counter] = testcase;		
		counter ++;

		a = new double[]{59.9, 59.9, 59.9, 59.9, 59.9, 59.9, 59.9, 59.9, 59.9};
		m = new double[]{59.9, 59.9, 59.9};
		t = new double[]{59.9, 59.9, 59.9, 59.9, 59.9, 59.9, 59.9, 59.9, 59.9, 59.9};
		f = 59.9;
		testcase = new GradesTestCase(a, m, t, f, "D+", true);
		allTestCases[counter] = testcase;		
		counter ++;

		a = new double[]{59.9, 59.9, 59.9, 59.9, 59.9, 59.9, 59.9, 59.9, 59.9};
		m = new double[]{59.9, 59.9, 59.9};
		t = new double[]{59.9, 59.9, 59.9, 59.9, 59.9, 59.9, 59.9, 59.9, 59.9, 59.9};
		f = 59.9;
		testcase = new GradesTestCase(a, m, t, f, "D+", false);
		allTestCases[counter] = testcase;		
		counter ++;

		a = new double[]{60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0};
		m = new double[]{60.0, 60.0, 60.0};
		t = new double[]{60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0};
		f = 60.0;
		testcase = new GradesTestCase(a, m, t, f, "C-", false);
		allTestCases[counter] = testcase;		
		counter ++;

		a = new double[]{60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0};
		m = new double[]{60.0, 60.0, 60.0};
		t = new double[]{60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0};
		f = 60.0;
		testcase = new GradesTestCase(a, m, t, f, "C-", true);
		allTestCases[counter] = testcase;		
		counter ++;

		a = new double[]{63.0, 63.0, 63.0, 63.0, 63.0, 63.0, 63.0, 63.0, 63.0};
		m = new double[]{63.0, 63.0, 63.0};
		t = new double[]{63.0, 63.0, 63.0, 63.0, 63.0, 63.0, 63.0, 63.0, 63.0, 63.0};
		f = 63.0;
		testcase = new GradesTestCase(a, m, t, f, "C", true);
		allTestCases[counter] = testcase;		
		counter ++;

		a = new double[]{63.0, 63.0, 63.0, 63.0, 63.0, 63.0, 63.0, 63.0, 63.0};
		m = new double[]{63.0, 63.0, 63.0};
		t = new double[]{63.0, 63.0, 63.0, 63.0, 63.0, 63.0, 63.0, 63.0, 63.0, 63.0};
		f = 63.0;
		testcase = new GradesTestCase(a, m, t, f, "C", false);
		allTestCases[counter] = testcase;		
		counter ++;

		a = new double[]{67.0, 67.0, 67.0, 67.0, 67.0, 67.0, 67.0, 67.0, 67.0};
		m = new double[]{67.0, 67.0, 67.0};
		t = new double[]{67.0, 67.0, 67.0, 67.0, 67.0, 67.0, 67.0, 67.0, 67.0, 67.0};
		f = 67.0;
		testcase = new GradesTestCase(a, m, t, f, "C+", false);
		allTestCases[counter] = testcase;		
		counter ++;

		a = new double[]{67.0, 67.0, 67.0, 67.0, 67.0, 67.0, 67.0, 67.0, 67.0};
		m = new double[]{67.0, 67.0, 67.0};
		t = new double[]{67.0, 67.0, 67.0, 67.0, 67.0, 67.0, 67.0, 67.0, 67.0, 67.0};
		f = 67.0;
		testcase = new GradesTestCase(a, m, t, f, "C+", true);
		allTestCases[counter] = testcase;		
		counter ++;

		a = new double[]{70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0};
		m = new double[]{70.0, 70.0, 70.0};
		t = new double[]{70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0};
		f = 70.0;
		testcase = new GradesTestCase(a, m, t, f, "B-", false);
		allTestCases[counter] = testcase;		
		counter ++;

		a = new double[]{70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0};
		m = new double[]{70.0, 70.0, 70.0};
		t = new double[]{70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0};
		f = 70.0;
		testcase = new GradesTestCase(a, m, t, f, "B-", true);
		allTestCases[counter] = testcase;		
		counter ++;

		a = new double[]{73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0};
		m = new double[]{73.0, 73.0, 73.0};
		t = new double[]{73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0};
		f = 73.0;
		testcase = new GradesTestCase(a, m, t, f, "B", false);
		allTestCases[counter] = testcase;		
		counter ++;

		a = new double[]{73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0};
		m = new double[]{73.0, 73.0, 73.0};
		t = new double[]{73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0};
		f = 73.0;
		testcase = new GradesTestCase(a, m, t, f, "B", true);
		allTestCases[counter] = testcase;		
		counter ++;

		a = new double[]{77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0};
		m = new double[]{77.0, 77.0, 77.0};
		t = new double[]{77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0};
		f = 77.0;
		testcase = new GradesTestCase(a, m, t, f, "B+", false);
		allTestCases[counter] = testcase;		
		counter ++;

		a = new double[]{77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0};
		m = new double[]{77.0, 77.0, 77.0};
		t = new double[]{77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0};
		f = 77.0;
		testcase = new GradesTestCase(a, m, t, f, "B+", true);
		allTestCases[counter] = testcase;		
		counter ++;

		a = new double[]{80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0};
		m = new double[]{80.0, 80.0, 80.0};
		t = new double[]{80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0};
		f = 80.0;
		testcase = new GradesTestCase(a, m, t, f, "A-", false);
		allTestCases[counter] = testcase;		
		counter ++;

		a = new double[]{80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0};
		m = new double[]{80.0, 80.0, 80.0};
		t = new double[]{80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0};
		f = 80.0;
		testcase = new GradesTestCase(a, m, t, f, "A-", true);
		allTestCases[counter] = testcase;		
		counter ++;

		a = new double[]{85.0, 85.0, 85.0, 85.0, 85.0, 85.0, 85.0, 85.0, 85.0};
		m = new double[]{85.0, 85.0, 85.0};
		t = new double[]{85.0, 85.0, 85.0, 85.0, 85.0, 85.0, 85.0, 85.0, 85.0, 85.0};
		f = 85.0;
		testcase = new GradesTestCase(a, m, t, f, "A", true);
		allTestCases[counter] = testcase;		
		counter ++;

		a = new double[]{85.0, 85.0, 85.0, 85.0, 85.0, 85.0, 85.0, 85.0, 85.0};
		m = new double[]{85.0, 85.0, 85.0};
		t = new double[]{85.0, 85.0, 85.0, 85.0, 85.0, 85.0, 85.0, 85.0, 85.0, 85.0};
		f = 85.0;
		testcase = new GradesTestCase(a, m, t, f, "A", false);
		allTestCases[counter] = testcase;		
		counter ++;

		a = new double[]{90.0, 90.0, 90.0, 90.0, 90.0, 90.0, 90.0, 90.0, 90.0};
		m = new double[]{90.0, 90.0, 90.0};
		t = new double[]{90.0, 90.0, 90.0, 90.0, 90.0, 90.0, 90.0, 90.0, 90.0, 90.0};
		f = 90.0;
		testcase = new GradesTestCase(a, m, t, f, "A+", true);
		allTestCases[counter] = testcase;		
		counter ++;

		a = new double[]{90.0, 90.0, 90.0, 90.0, 90.0, 90.0, 90.0, 90.0, 90.0};
		m = new double[]{90.0, 90.0, 90.0};
		t = new double[]{90.0, 90.0, 90.0, 90.0, 90.0, 90.0, 90.0, 90.0, 90.0, 90.0};
		f = 90.0;
		testcase = new GradesTestCase(a, m, t, f, "A+", false);
		allTestCases[counter] = testcase;		
		counter ++;





		return allTestCases;
	}

}