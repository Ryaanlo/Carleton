package comp1406a3;

public class Grades{
	protected double[] ass;
	protected double[] mid;
  protected double[] tut;
	protected double[] exam;

	/** Creates a Grades object with the specified grades.
	  *
		* @param assignments is a list of 9 assignments each in the range [0.0, 100.0].
		*    The first 6 are the normal assignments and the last 3 are the study assignments.
		* @param midterms is a list of 3 midterm grades each in the range [0.0, 100.0]
		* @param tutorials is a list of 10 tutorial grades each in the range [0.0, 100.0]
		* @param finalExam is the final exam grade in the range [0.0, 100.0]
		*/
	public Grades(double[] assignments, double[] midterms,
	              double[] tutorials, double finalExam){

	}

	/** Computes the letter grade based on the grades stored in this
   *  object using the description given in the course outline and using
   *  the letter grade table given in Assignment 1.
	 *
	 * @return the letter grade obtained with the grades in this object.
	 */
	public String finalGrade(){return "no grade";}


	/** Decides if the current grades are sufficient for a student to
	 *  progress to the 2nd year programming courses in SCS. That is,
	 *  do these current grades allow the student register in any of
	 *  COMP2401, 2402, 2404 or 2406?
   *
	 * @return true if the current grades are sufficient for a student to enrol in second
	 *         year SCS coding course; returns false otherwise.
	 */
	public boolean canProgressTo2ndYear(){ return true; }

}
