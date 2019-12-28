package comp1406a3;

/**
   Stores a date (YYYY-MM-DD)
*/

public class Date{
	protected int year;
	protected int month;
	protected int day;

	/** Creates a date with the specified year, month and date.
	 *
	 * @param YYYY is the year of the date to be created.
	 * @param MM is the month of the date to be created.
	 * @param DD is the day of the date to be created.
	 */
	public Date(int YYYY, int MM, int DD){
		year = YYYY;
		month = MM;
		day = DD;
	}

	/** Getter for the year of this date 
	 * @return the year of this date 
	 */
	public int getYear(){ return this.year; }
	
	/** Getter for the month of this date 
	 * @return the month of this date 
	 */
	public int getMonth(){ return this.month; }

	/** Getter for the day of this date 
	 * @return the day of this date 
	 */
	public int getDay(){ return this.day; }

	/** Creates a new date object that is nDays in the future
	  * from this current date
		*	@param nDays is a non-negative integer (the number of days from this date that the
    *        new returned date object will be created).		
		* @return a new date object that is nDays past this current date.
		*/
	public Date futureDate(int nDays){
		return null;
	}

	/** A string representation of this date
	  * @return a string representation of this date as YYYY/MM/DD
		*/
	@Override
	public String toString(){
		return String.format("%04d", this.year) 
		         + "/" + String.format("%02d", this.month) 
						 + "/" + String.format("%02d", this.day);
  }

}
