package comp1406a3;

public class GradesTestCase{
	
	double[] assignments;
	double[] midterms;
	double[] tutorials;
	double   finalExam;
	
	String        expectedLetter;
  boolean       expectedDecision; 	
	
	public double[] getA(){ return this.assignments; }
	public double[] getM(){ return this.midterms; }
	public double[] getT(){ return this.tutorials; }
	public double   getF(){ return this.finalExam; }
	public String   getExpectedGrade(){ return this.expectedLetter; }
	public boolean  getExpectedDecision(){ return this.expectedDecision; }
	
	public GradesTestCase(double[] assignments,  double[] midterms, 
                        double[] tutorials,    double finalExam,
                        String expectedLetter, boolean expectedDecision)
 {
		this.assignments = assignments;
		this.midterms = midterms;
		this.tutorials = tutorials;
		this.finalExam = finalExam;										
		this.expectedLetter = expectedLetter;
		this.expectedDecision = expectedDecision;
	}

	public double[] getGrades(){
		// Against my own advice, I have used i++ while indexing an array. 		
		double[] answer = new double[assignments.length + midterms.length + tutorials.length + 1];
		int i = 0;
		for(double g: assignments){ answer[i++] = g; }
		for(double g: midterms){ answer[i++] = g; }
		for(double g: tutorials){ answer[i++] = g; }
		answer[i] = finalExam;
		return answer;
	}

	
}