package comp1406a1;

/** Assignment 1 - Winter 2019
  * <p>
  * Problem 3
  * <p> In the provided Grades.java files, complete the finalExamGradeNeeded method.
  * <p>
  * This method takes several input parameters: a target letter grade (String), 
  * a list of assignment grades (double[]), a list of tutorial grades (double[]) 
  * and a list of midterm grades (double[]). 
  * <p>
  * The method returns the minimum grade needed on the final exam to achieve the 
  * target letter grade based on the other input grades. 
  * <p>
  * You must use the grading scheme as specified in the course outline. 
  * Be careful to read and understand the condition for passing the course. 
  * If the needed final exam grade is more than 100.0, then the method should return -1.0. 
  * If the target grade will be achieved (or surpassed) regardless of the final exam mark, 
  * the method should return 0.0.
  */


public class Grades{
  
  /** Computes the minimum final exam grade needed to achieve a desired 
    * target letter grade based on provided assignment, tutorial and midterm 
    * grades.
    * <p>
    * The Grade determination is based on that given in the course outline.
    * (See Piazza and be sure to read the course outline!)
    * 
    * @param target is the desired letter grade.
    * @param assignments contains all nine assignment grades (each out of 100). The first six are the main assignments and the last three are the study assignments.
    * @param tutorials contains all 10 tutorial grades (each out of 100). 
    * @param midterms contains all 3 midterm grades (each out of 100).
    * 
    * @return the minimum grade needed to achieve the target letter grade based on 
    *         the provided assignment/tutorial/midterm grades and the description 
    *         provided in the course outline. If the needed final exam grade is more 
    *         than 100.0, return -1.0. If the target grade will be achieved (or surpassed) 
    *         regardless of the final exam mark, returns 0.0.
    */
  public static double finalExamGradeNeeded(String   target,
                                            double[] assignments,
                                            double[] tutorials,
                                            double[] midterms){
    double assigntotal = 0.0;
	double studytotal = 0.0;
	double tutorialstotal = 0.0;
	double midtermstotal = 0.0;
	double beforefinal = 0.0;
	double grade = 0.0;
	
	for ( int i = 0; i<(assignments.length-3); i++){
		assigntotal += (assignments[i] * 0.05);
	}

	System.out.println(assigntotal);
	
	double smallest = 0.0;
	if (assignments[6] <= assignments[7]){
		smallest = assignments[6];
	}else{
		smallest = assignments[7];
	}
	if (assignments[8] < smallest){
		smallest = assignments[8];
	}
	
	for (int i = 6; i<assignments.length; i++){
		studytotal += (assignments[i] * 0.01);
	}
	studytotal -= (smallest * 0.01);
	
	System.out.println(studytotal);
	
	
	smallest = tutorials[0];
	double smallest2 = 0.0;
	
	for (int i = 0; i<(tutorials.length-1); i++){
		if (tutorials[i+1] < smallest){
			smallest = tutorials[i+1];
		}
	}
	if (smallest == tutorials[0]){
		smallest2 = tutorials[1];
	}else{
		smallest2 = tutorials[0];
	}

	for (int i = 0; i<(tutorials.length-1); i++){
		if (tutorials[i] != smallest){
			if (tutorials[i] < smallest2){
				smallest2 = tutorials[i];
			}
		}
	}
	
	for (int i = 0; i<tutorials.length; i++){
		tutorialstotal += (tutorials[i] * 0.01);
	}
	tutorialstotal -= (smallest * 0.01);
	tutorialstotal -= (smallest2 * 0.01);
	
	System.out.println(tutorialstotal);
	
	smallest = 0;
	if (midterms[0] <= midterms[1]){
		smallest = midterms[0];
	}else{
		smallest = midterms[1];
	}
	if (midterms[2] < smallest){
		smallest = midterms[2];
	}
	
	int counter = 0;
	
	for (int i = 0; i<midterms.length; i++){
		midtermstotal += (midterms[i] * 0.15);
	}
	midtermstotal -= (smallest * 0.15);
	
	
	System.out.println(midtermstotal);
	
	beforefinal = assigntotal + studytotal + tutorialstotal + midtermstotal;
	System.out.println(beforefinal);
	
	switch(target){
		case "A+":
			grade = (90.0 - beforefinal);
			break;
		case "A":
			grade = (85.0 - beforefinal);
			break;
		case "A-":
			grade = (80.0 - beforefinal);
			break;
		case "B+":
			grade = (77.0 - beforefinal);
			break;
		case "B":
			grade = (73.0 - beforefinal);
			break;
		case "B-":
			grade = (70.0 - beforefinal);
			break;
		case "C+":
			grade = (67.0 - beforefinal);
			break;
		case "C":
			grade = (63.0 - beforefinal);
			break;
		case "C-":
			grade = (60.0 - beforefinal);
			break;
		case "D+":
			grade = (57.0 - beforefinal);
			break;
		case "D":
			grade = (53.0 - beforefinal);
			break;
		case "D-":
			grade = (50.0 - beforefinal);
			break;
		case "F":
			grade = (49.0 - beforefinal);
			break;
		default:
			grade = -1;
			break;
	}
	
	System.out.println(grade);
	
	
	if ((grade + midtermstotal) < 30.0){
		grade = 30.0 - midtermstotal;
		//System.out.print("The required final exam mark is ");
		System.out.println("Grade is" + grade);
		return grade;
	}
	
	grade /= .3;
	
	System.out.println(grade);
	if (grade > 100){
		grade = -1;
	}else if(grade < 0){
		grade = 0;
	}
	
	System.out.print("The required final exam mark is ");
	System.out.println(grade);
	
	return grade;
  }
  public static void main(String[] args){
	String target = "B+";
//	double[] assignments = {30.0,39.0,59.0,27.0,51.0,51.0,26.0,48.0,45.0};
//	double[] tutorials = {45.0,50.0,18.0,17.0,50.0,44.0,48.0,22.0,56.0,26.0};
//	double[] midterms = {48.0,57.0,48.0};
	double[] assignments = {90.0,92.0,98.0,96.0,92.0,99.0,92.0,96.0,92.0};
	double[] tutorials = {97.0,97.0,90.0,93.0,92.0,92.0,99.0,94.0,97.0,92.0};
	double[] midterms = {94.0,93.0,90.0};
	double[] a = new double[]{60.0, 70.0, 60.0, 70.0, 80.0, 50.0, 70.0, 60.0, 90.0};
	double[] m = new double[]{80.0, 70.0, 0.0};
	double[] t = new double[]{60.0, 80.0, 60.0, 50.0, 40.0, 0.0, 10.0, 0.0, 100.0, 0.0};
	finalExamGradeNeeded(target, a, t, m);
  }
}