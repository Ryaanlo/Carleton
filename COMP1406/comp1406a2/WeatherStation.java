package comp1406a2;

/** A weather station keeps a collection of weather reports.
  * The actual weather station it models will make temperature
  * observations which provides the data for the reports.
  *
  * A weather station only needs to remember the last 10
  * reports added to it (in the order that they are added),
  * in addition to the report with the highest temperature that
  * has ever been and recorded/added to this weather station.
  * That is, it only needs to store 11 weather reports in total.
  *
  * Notes: The highest ever temperature record might be one of the
  *        last 10 added reports but it also might not.
  *        You must store the last 10 reports in an array.
  *        The highest ever temperature report does not need to be
  *        sotred in this array (unless it was one of the last 10).
  */
public class WeatherStation{

/* --------------------------------------------------------------------------/
/* --------------------------------------------------------------------------/
/* ----------------------------------------------------- */
/*                                                       */
/* BEGIN  --  do NOT change anything until the end of    */
/*            this block of attributes, constructor and  */
/*            and methods                                */
/*                                                       */
/* ----------------------------------------------------- */

	/** The name of this weather station */
	protected final String name;

	/** The id number of this weather station. Should be unique. */
	protected final int    id;

	/** Initializes this weather station's name and id  */
	public WeatherStation(String nameOfStation, int idOfStation){
		this.name = nameOfStation;
		this.id   = idOfStation;
	}

  /** Getter for this weather station's name
   * @return the name of this weather station
   */
	public String getName(){ return this.name;  }

  /** Getter for this weather station's id number
   * @return the ID of this weather station
   */
	public int    getID(){ return this.id; }
  
	
	// My Attributes for WeatherStation Class
	private WeatherReport[] reports = new WeatherReport[10];	// array of 10 reports
	private WeatherReport latest;								// stores the latest added report
	private int counter = 0;									// counter for number of reports in the array
	private WeatherReport highestTemp = null;					// stores the report for highest temperature 
	private WeatherReport[] allReports = new WeatherReport[0];	// array that stores all the reports

  /** Creates a weather report
   * @param temperature is a valid temperature object correspinding to
   *                    an obervation at this weather station.
   * @param time is the time that the temperature was recorded.
   * @return a weather report for this weather station recording
   *         the temperature and time of when the obervation was made.
   */
	public WeatherReport makeReport(Temperature temperature, TimeStamp time){
		return new WeatherReport(temperature, time, this);
	}

	/** A nice String representation of a weather station object */
  @Override
  public String toString(){
    return this.name + " [id:" + this.id + "]";
  }
/* ----------------------------------------------------- */
/*                                                       */
/* END - complete the methods below these comments       */
/*                                                       */
/* ----------------------------------------------------- */
/* --------------------------------------------------------------------------/*
/* --------------------------------------------------------------------------/*

	

  /** Adds a report to the station.
   *
   * @param report is a WeatherReport to be added to this WeatherStation
   * @return this WeatherStation. (Do NOT alter the return statement of this
   *         method.)
   *
   */
	public WeatherStation addReport(WeatherReport report){	// method to add report to weather station
	  
		this.latest = report;	// assigns latest report
	  
		if (counter > 9){		// if array exceed the max reports it can hold
			counter --;			// reduce the counter
			for (int i = 0; i < reports.length - 1; i++){	// loop over the array of reports
				reports[i] = reports[i+1];	// move all the reports over
			}
			reports[reports.length - 1] = report;	// set the last one to the newest report
		}else{					// if array still has space
			this.reports[counter] = report;	// add it to the next spot
		}
	
		counter ++;		// increase the counter for number of reports
		
		// Next part is to check for the report with the highest temperature
		if (counter == 1){		// if there is only one report
			highestTemp = report;	// set that as the highest temp
		}else{ 					// if there are more than one report
			if (this.reports[counter - 1].getTemperature().getTemp() > highestTemp.getTemperature().getTemp()){	// compare the new report temperature with the current highest temperature
				highestTemp = this.reports[counter - 1];	// if higher then set to new highest temperature
			}
		}
		
		// Next part is to add to the array of all reports
		WeatherReport[] copy = allReports;	// creates a copy of all the reports
		
		this.allReports = new WeatherReport[copy.length+1];	// makes new array with one extra spot
		
		for (int i = 0; i < copy.length; i++){	// loop over the old array
			this.allReports[i] = copy[i];		// copy over the reports
		}
		allReports[allReports.length-1] = report;	// set the last spot to the new report
	  
		return this;  // do NOT alter the return statement
	}


    /** Returns the most recently added report.
      *
      *
      * @return the most recently added WeatherReport to this weatherstation.
      *         If no reports have ever been added then returns null.
      */
    public WeatherReport getMostRecentReport(){		// method that returns most recent report
		
		if (counter == 0){	// if there are no reports
			return null;	// return null
		}else{				// if there are reports
			return latest;	// return the latest one
		}

    }


  /** Returns the last 10 weather getReports added to this weather station.
	 * <p>
	 * If there haven't been 10 reports added then return as many as has been added.
   *
   * @return an array of the last 10 added WeatherReports (in the order
   *         that they were added). The most recently added report is the
   *         first element in the array.
   *         If there has been less than 10 reports added to this
   *         weatherstation, then returns as many reports as there is.
   *         The returned array must be the same size as the number of
   *         reports returned.
   */
	public WeatherReport[] getReports(){	// method that returns last 10 reports
	  
		WeatherReport[] latestReports = new WeatherReport[counter];	// creates an array with size equal to number of reports in array
		int counter2 = 0;											// index counter
	  
		for (int i = counter; i > 0; i--){			// loop over from the back to the beginning of array
			latestReports[counter2] = reports[i - 1];	// assigns the reports in reverse
			counter2 ++;					// increase index
		}
	  
		return latestReports;	// returns the array of latest reports
	  
	}


  /** Returns a weather report with highest recorded temperature
   * of any report ever added to this WeatherStation.  If there have been
   * multiple reports with the same highest temperature, the most recently
   * added report is returned,.
   *
   * @return a WeatherReport that has the highest recorded temperature
   *         of any report ever added to this WeatherStation. Returns
   *         null if no report has ever been added to this weatherstation.
   */
	public WeatherReport getReportWithMaxTemp(){	// method to return report with highest temperature
		return highestTemp;							// returns the report
	}
  
  
  /**
	* Returns an array of all reports ever recorded 
	* new method added
	* @return a WeatherReport array with all reports
	*/
	public WeatherReport[] getAllReports(){		// method for returning all reports
		return allReports;						// returns array of all reports
	}


}
