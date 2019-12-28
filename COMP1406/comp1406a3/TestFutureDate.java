package comp1406a3;

public class TestFutureDate{

	/** Generate black-box test cases for the futureDate method (in the Date class).
		* @return an array of test cases for the futureDate method (in the Date class).
		*/
	public static FutureDateTestCase[] makeTestCases(){
		
		FutureDateTestCase[] allTestCases = new FutureDateTestCase[54];

		Date day = new Date(2019, 1, 1);
		int daysInFuture = 2;
		Date expected = new Date(2019, 1, 1 + daysInFuture);
		FutureDateTestCase testcase = new FutureDateTestCase(day, daysInFuture, expected);
		allTestCases[0] = testcase;
		
		day = new Date(2019, 1, 1);
		daysInFuture = 0;
		expected = new Date(2019, 1, 1 + daysInFuture);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[1] = testcase;
		
		// leap years - 2000, 2004, 2008, 2012, ...

		day = new Date(2020, 2, 28);
		daysInFuture = 1;
		expected = new Date(2020, 2, 28 + daysInFuture);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[2] = testcase;

		day = new Date(2019, 12, 31);
		daysInFuture = 1;
		expected = new Date(2020, 1, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[3] = testcase;

		day = new Date(2019, 2, 28);
		daysInFuture = 1;
		expected = new Date(2019, 3, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[4] = testcase;

		day = new Date(2019, 1, 1);
		daysInFuture = 31;
		expected = new Date(2019, 2, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[5] = testcase;

		day = new Date(2019, 1, 1);
		daysInFuture = 31;
		expected = new Date(2019, 2, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[5] = testcase;

		day = new Date(2000, 1, 1);
		daysInFuture = 366;
		expected = new Date(2001, 1, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[6] = testcase;

		day = new Date(2019, 2, 1);
		daysInFuture = 1;
		expected = new Date(2019, 2, 2);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[7] = testcase;

		day = new Date(2019, 2, 1);
		daysInFuture = 29;
		expected = new Date(2019, 3, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[8] = testcase;

		day = new Date(2019, 3, 1);
		daysInFuture = 32;
		expected = new Date(2019, 4, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[9] = testcase;

		day = new Date(2019, 4, 1);
		daysInFuture = 31;
		expected = new Date(2019, 5, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[10] = testcase;

		day = new Date(2019, 5, 1);
		daysInFuture = 32;
		expected = new Date(2019, 6, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[11] = testcase;

		day = new Date(2019, 6, 1);
		daysInFuture = 31;
		expected = new Date(2019, 7, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[12] = testcase;

		day = new Date(2019, 7, 1);
		daysInFuture = 32;
		expected = new Date(2019, 8, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[13] = testcase;

		day = new Date(2019, 8, 1);
		daysInFuture = 32;
		expected = new Date(2019, 9, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[14] = testcase;

		day = new Date(2019, 9, 1);
		daysInFuture = 31;
		expected = new Date(2019, 10, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[15] = testcase;

		day = new Date(2019, 10, 1);
		daysInFuture = 32;
		expected = new Date(2019, 11, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[16] = testcase;

		day = new Date(2019, 11, 1);
		daysInFuture = 31;
		expected = new Date(2019, 12, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[17] = testcase;

		day = new Date(2019, 12, 1);
		daysInFuture = 32;
		expected = new Date(2020, 1, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[18] = testcase;
		
		// [daysInFuture<=500] is 0.5/2

		day = new Date(2019, 1, 1);
		daysInFuture = 500;
		expected = new Date(2020, 5, 15);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[19] = testcase;	

		day = new Date(2001, 1, 1);
		daysInFuture = 365;
		expected = new Date(2002, 1, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[20] = testcase;	

		day = new Date(2000, 12, 31);
		daysInFuture = 365;
		expected = new Date(2001, 12, 30);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[21] = testcase;

		day = new Date(2020, 2, 1);
		daysInFuture = 100;
		expected = new Date(2020, 4, 10);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[22] = testcase;

		day = new Date(2018, 12, 31);
		daysInFuture = 1;
		expected = new Date(2019, 1, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[23] = testcase;





		day = new Date(2019, 1, 31);
		daysInFuture = 1;
		expected = new Date(2019, 2, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[24] = testcase;

		day = new Date(2019, 2, 28);
		daysInFuture = 1;
		expected = new Date(2019, 3, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[25] = testcase;

		day = new Date(2019, 3, 31);
		daysInFuture = 1;
		expected = new Date(2019, 4, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[26] = testcase;

		day = new Date(2019, 4, 30);
		daysInFuture = 1;
		expected = new Date(2019, 5, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[27] = testcase;

		day = new Date(2019, 5, 31);
		daysInFuture = 1;
		expected = new Date(2019, 6, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[28] = testcase;

		day = new Date(2019, 6, 30);
		daysInFuture = 1;
		expected = new Date(2019, 7, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[29] = testcase;

		day = new Date(2019, 7, 31);
		daysInFuture = 1;
		expected = new Date(2019, 8, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[30] = testcase;

		day = new Date(2019, 8, 31);
		daysInFuture = 1;
		expected = new Date(2019, 9, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[31] = testcase;

		day = new Date(2019, 9, 30);
		daysInFuture = 1;
		expected = new Date(2019, 10, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[32] = testcase;

		day = new Date(2019, 10, 31);
		daysInFuture = 1;
		expected = new Date(2019, 11, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[33] = testcase;

		day = new Date(2019, 11, 30);
		daysInFuture = 1;
		expected = new Date(2019, 12, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[34] = testcase;

		day = new Date(2019, 12, 31);
		daysInFuture = 1;
		expected = new Date(2020, 1, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[35] = testcase;




		day = new Date(2020, 1, 31);
		daysInFuture = 1;
		expected = new Date(2020, 2, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[36] = testcase;

		day = new Date(2020, 2, 29);
		daysInFuture = 1;
		expected = new Date(2020, 3, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[37] = testcase;

		day = new Date(2020, 3, 31);
		daysInFuture = 1;
		expected = new Date(2020, 4, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[38] = testcase;

		day = new Date(2020, 4, 30);
		daysInFuture = 1;
		expected = new Date(2020, 5, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[39] = testcase;

		day = new Date(2020, 5, 31);
		daysInFuture = 1;
		expected = new Date(2020, 6, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[40] = testcase;

		day = new Date(2020, 6, 30);
		daysInFuture = 1;
		expected = new Date(2020, 7, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[41] = testcase;

		day = new Date(2020, 7, 31);
		daysInFuture = 1;
		expected = new Date(2020, 8, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[42] = testcase;

		day = new Date(2020, 8, 31);
		daysInFuture = 1;
		expected = new Date(2020, 9, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[43] = testcase;

		day = new Date(2020, 9, 30);
		daysInFuture = 1;
		expected = new Date(2020, 10, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[44] = testcase;

		day = new Date(2020, 10, 31);
		daysInFuture = 1;
		expected = new Date(2020, 11, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[45] = testcase;

		day = new Date(2020, 11, 30);
		daysInFuture = 1;
		expected = new Date(2020, 12, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[46] = testcase;

		day = new Date(2020, 12, 31);
		daysInFuture = 1;
		expected = new Date(2021, 1, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[47] = testcase;

		// > 30 futuredays

		day = new Date(2020, 1, 1);
		daysInFuture = 365;
		expected = new Date(2020, 12, 31);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[48] = testcase;		

		day = new Date(2020, 1, 1);
		daysInFuture = 59;
		expected = new Date(2020, 3, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[49] = testcase;	


		int counter = 50;
		day = new Date(2019, 1, 1);
		daysInFuture = 59;
		expected = new Date(2020, 3, 2);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[counter] = testcase;
		counter ++;	

		day = new Date(2016, 1, 1);
		daysInFuture = 58;
		expected = new Date(2016, 2, 29);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[counter] = testcase;
		counter ++;	

		day = new Date(2016, 2, 29);
		daysInFuture = 365;
		expected = new Date(2017, 3, 1);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[counter] = testcase;
		counter ++;	

		day = new Date(2016, 2, 29);
		daysInFuture = 364;
		expected = new Date(2017, 2, 28);
		testcase = new FutureDateTestCase(day, daysInFuture, expected);		
		allTestCases[counter] = testcase;
		counter ++;	

		return allTestCases;
	}

}