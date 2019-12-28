package comp1406a2;

/** Stores a simplified date/time holding the day (counting from Jan 1st)
 and time (hour/minute) */
public class TimeStamp{
  /** The day is a count of days since January 1st.
	 * <p>
	 * So, January 1st is day 0, January 2nd is day 1, ...
   * and February 1st is day 31, etc.
   */
	protected int day;

	/** The hour ranging from 0-23 */
	protected int hour;

	/** The minute ranging from 0-59 */
	protected int minute;

	/** Creates a timestamp with the given day, hour and minute.
	 * @param day is the day (jan 1st = 0, jan 2nd = 1, ..., feb 1st = 31, ...)
	 * @param hour is the hour (0,1,2,...23)
	 * @param minute is the minite (0,1,2,...59)
	 */
	public TimeStamp(int day, int hour, int minute){
		this.day = day;
		this.hour = hour;
		this.minute = minute;
	}

	/** Getter for the day
	  * @return the day stored in this TimeStamp
		*/
	public int getDay(){ return this.day; }

	/** Getter for the hour
	 * @return the hour stored in this timestamp
	 */
	public int getHour(){ return this.hour; }

	/** Getter for the minute
	 * @return the minute stored in this TimeStamp
	 */
	public int getMinute(){ return this.minute; }

  /* this will let us print a TimeStamp nicely */
	@Override
	public String toString(){
		return "{" + this.day + "," + this.hour + ":" + String.format("%02d",this.minute) + "}";
	}

}
