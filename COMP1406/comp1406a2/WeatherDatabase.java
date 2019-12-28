package comp1406a2;

/** A very simple database storing weather data */ 
public class WeatherDatabase{

	/* ----------------------------------- */
	/* do NOT change this constructor      */
	/* ----------------------------------- */
	
	/** Creates an empty weather database  */
	public WeatherDatabase(){}
	
	/* ----------------------------------- */




	//
	// You need to complete these methods.
	//
	// Unless stated, you will need to change the return 
	// value of each of these methods as well as add the actual 
	// body of the methods.
	//

	// Attributes for WeatherDatabase class
	private WeatherStation[] stations = new WeatherStation[0];	// array to store WeatherStation objects
	

	/** Returns all weather stations in the database
	* in no particular order
	*
	* @return an array containing all weather stations that are in
	*         this database. Note that the size of the array should be equal to
	*         the result of <code>this.numberOfWeatherStations()</code>.
	*/
	public WeatherStation[] getWeatherStations(){	// method for getting an array of all stations in database
		return this.stations;						// returns the array for the database
	}

	/** Returns the number of weather stations in the database
	*
	* @return the number of weather stations in this database
	*/
	public int numberOfWeatherStations(){	// method for getting the number of stations
		return stations.length;				// returns the length of the station array
	}


	/** adds a weather station to this database
		* @param station is the weather station to add. It will always be non-null.
		* @return this weather station.
		*/
	public WeatherDatabase addWeatherStation(WeatherStation station){	// method to add stations to database
		
		WeatherStation[] copy = stations;					// creates an array called copy with exact same things in stations
		
		this.stations = new WeatherStation[copy.length+1];	// creates a new array with one size bigger than before
		
		for (int i = 0; i < copy.length; i++){				// loops over the copied array
			this.stations[i] = copy[i];						// adds the stations to the new array
		}
		
		stations[stations.length-1] = station;				// sets the last station to the new station to be added
		
		return this;    // do NOT change the return statement
	}


	/** removes weather station with given id from this database
		* @param id is the ID number of the weather station to remove.
		* @return true if the specified weather station is successfully removed,
		*         returns false otherwise (i.e., if there was no weather station
		*         with the specified ID number in this database to begin with).
		*/
	public boolean removeWeatherStation(int id){	// method for removing a station
		
		boolean remove = false;				// boolean variable for any removed stations
		int counter = 0;					// counter for amount of stations removed
		int counter2 = 0;					// counter for copying to the new array
		
		for (int i = 0; i < this.stations.length; i++){		// loops over the number of stations
			if (stations[i].getID() == id){					// checking to see if id matches
				stations[i] = null;							// if matches, remove the station by setting to null
				counter ++;									// add to total removed counter
				remove = true;								// change boolean to true for indication of removed station
			}
		}
		
		WeatherStation[] copy = new WeatherStation[stations.length-counter];	// creates a new array with length subtracted by number of stations removed 
		
		if (remove){									// if any stations were removed
			for (int i = 0; i < stations.length; i++){	// loop over the length of the stations
				if (stations[i] != null){				// if that station is not null
					copy[counter2] = stations[i];		// copy the station over
					counter2 ++;						// increase the index
				}
			}
			this.stations = copy;						// assign the current station to the new array
		}
		
		return remove;				// return the boolean indication
	}


	/** returns the highest temperature ever recorded by any weather station
	  * in the data base.
	  */
	public Temperature getMaxTemperature(){		// method for getting the max temperature in database
		
		double highestTemperature = 0;			// variable for value of highest temperature
		Temperature highestTemp;				// initalize temperature object with highest temperature
		
		highestTemp = stations[0].getReportWithMaxTemp().getTemperature();	// set the base case of highest temp to the first station
		
		for (int i = 1; i < stations.length; i++){		// loop over the number of stations
			if (stations[i].getReportWithMaxTemp().getTemperature().getTemp() > highestTemp.getTemp()){	// compare the station with current highest temp station
				highestTemp = stations[i].getReportWithMaxTemp().getTemperature();	// if greater,  set it as new highest temp
			}
		}
		
		return highestTemp;		// returns the temperature object with highest temperature
	}


	/** returns the highest temperature on a specified day
		* that is recorded in a report in the weather stations in this database.
		*
		* @param day is the day to find the max temperature on. Note that this
		*            might be the special max temperature day that a
		*            weather station records.
		* @return  the temperature object with the highest recorded Temperature
		*          in the data base on the specified day.
		*/
	public Temperature getMaxTemperature(int day){	// method for getting the max temperature on a specific day
		
		Temperature maxTemperature = null;		// initalize the temperature object with max temperature
		WeatherReport[] allReports = null;		// initalize an array of all reports in that station
		boolean flag = true;					// flag for base case purposes
		
		for (int i = 0; i < stations.length; i ++){	// loop over all the stations
			allReports = stations[i].getAllReports();	// assign allReports to get an array of all the reports in that station
			
			for (int j = 0; j < allReports.length; j++){	// loop over all the reports in that station
				if (allReports[j].getTime().getDay() == day){	// check to see if the days of the report are the same
					if (flag){									// setting the base case for comparision
						maxTemperature = allReports[j].getTemperature();	// set the max temperature base case
						flag = false;							// ends base case assign
					}
					else{										// if have a base case
						if (allReports[j].getTemperature().getTemp() > maxTemperature.getTemp()){	// comparing report temp to current max temp
							maxTemperature = allReports[j].getTemperature();	// if greater, set report temperature to current max temperature
						}
					}
				}
			}
		}
		
		return maxTemperature;		// returns the object with max temperature
	}

	/** Computes the average temperature (over all weather stations) for the
	*  time period starting at startDay and ending at endDay (inclusive)
	*
	* @param startDay is the starting day
	* @param endDay is the ending endDay
	* @return the average temperature of all temperature reports for the time
	*         period startDay to endDay (inclusive) taken from all weather
	*         stations in this weather database.
	*/
	public double averageTemperature(int startDay, int endDay){	// method for getting the average temperature between two days
		
		double averageTemp = 0.0;		// variable for average temperature
		double counter = 0.0;			// variable for number of temperatures in between the given days
		WeatherReport[] allReports = null;	// initalizes an array to store all reports of a station
		
		for (int i = 0; i < stations.length; i++){	// loop of all the stations
			
			allReports = stations[i].getAllReports();	// gets all the reports in the current station
			for (int j = 0; j < allReports.length; j ++){	// loop over all the reports
				if (allReports[j].getTime().getDay() >= startDay && allReports[j].getTime().getDay() <= endDay){	// check to see if day of the report is within range
					averageTemp += allReports[j].getTemperature().getTemp();	// adds the temperature to the total amount
					counter += 1.0;		// add a counter for each
				}
			}
		}
		
		if (counter != 0){		// if there was a report within those days
			averageTemp = averageTemp / counter;	// divide the total temp by the amount added to get the average
		}
		
		return averageTemp;		// return the average temp
	}

}
