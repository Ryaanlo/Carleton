package comp1406a2;

/**
 * A class to represent temperature (with a value and scale).
 * 
 * Ryan Lo (101117765)
 * COMP 1406
 * Winter 2019
 * Assignment 2
 */

public class Temperature{

	// Attributes for Temperature class
	public double temperature; 	// initializing temperature as double
	public Scale unit;			// initializing enum Scale as unit

	/** Initializes a temperature object with given value in Celsius
	 *  <p>
	 *  If the initial temperature is less than -273.15C then the temperature
	 *  object will be initialized with -273.15C.
   *
	 * @param temp is the initial temperature in Celsius.
	 */
	
	public Temperature(double temp){ 	// constructor with single param temp
	
		this.unit = Scale.CELSIUS; 		// initializes to default unit CELSIUS
	
		if (temp < -273.15){ 			// if the param temp is less than absolute zero 
			this.temperature = -273.15;	// set object's temp to absolute zero
		}else{							// if not less than absolute zero
			this.temperature = temp;	// set object's temp to given param temp
		}
	}
	/** Initializes a temperature object with given value using the specified scale
   * <p>
	 * If the temperature is lower than absolute zero, in the given scale,
	 * then set the temperature to absolute zero (in that scale).
   * <p>
 	 * Usage: new Temperature(12.3, Scale.KELVIN)
	 *
	 * @param temp is the initial temperature in the scale provided in
   *        the second argument.
	 * @param scale is the scale of initial temperature and must be a constant
	 *        defined in the Scale enum type.
	 */
	
	
	public Temperature(double temp, Scale scale){	// constructor with two params: the temperature
												// and the scale
		this.unit = scale;	// sets the object's unit to the given param scale
	  
		if (scale == Scale.CELSIUS){	// if the param scale if equalled to CELSIUS

			if (temp < -273.15){		// check to see if below absolute zero
				this.temperature = -273.15;	// if so set to absolute zero
			}else{					// if not below absolute zero
				this.temperature = temp;	// use the given param temp for object
			}

		}else if (scale == Scale.FAHRENHEIT){	// if the param scale if equalled to FAHRENHEIT

			if (temp < -459.67){			// check to see if below absolute zero
				this.temperature = -459.67;	// if so set to absolute zero
			}else{					// if not below absolute zero
				this.temperature = temp;	// use the given param temp for object
			}

		}else{			// if the param scale if equalled to KELVIN

			if (temp < 0){		// check to see if below absolute zero
				this.temperature = 0;		// if so set to absolute zero
			}else{			// if not below absolute zero
				this.temperature = temp;		// use the given param temp for object
			}
		}
	}

	/** Initializes a new temperature object that is a copy of the
   *  temperature object parameter.
   *
   * That is, it makes a new object that is a copy of the input object.
   *
	 * @param temp is a non-null temperature object
	 */
	public Temperature(Temperature temp){		// constructor with one param with is another Temperature object
	  
		this.temperature = temp.temperature;	// set the temp of object to the same temp as param object
		this.unit = temp.unit;				// set the unit of object to the same unit as param object
	  
	}



	/** getter for the scale
	 * <p>
	 * The output of this getter method must always be the first letter of one
	 * of the names in the Scales enum class. It must be the upper case letter.
	 * <p>
	 * Example: t = new Temperature(12.3, Scale.KELVIN);
	 *          t.getScale() will then output 'K'
	 *
	 * @return the first letter (in upper case) of the string representation of the
	 *         current scale of this object.
	 */
	public char getScale(){				// method for getting the char corresponding to the scale 
	
		if (this.unit == Scale.KELVIN){		// if the object's unit is in KELVIN
			return 'K';						// returns the char K
		}
		else if (this.unit == Scale.CELSIUS){	// if the object's unit is in CELSIUS
			return 'C';						// returns the char C
		}
		else{								// if the object's unit is in FAHRENHEIT
			return 'F';						// returns the char F
		}
	}

	/** getter for the temperature
	 *
	 * @return the temperature of the object using the current scale
	 */
	public double getTemp(){	// method for getting the temperature of the object
	
		return this.temperature;	// returns the temperature
	
	}


  /** setter for scale
	 *
	 * @param scale is the new scale of the temperature and must be
	 *        a constant from the Scale enum type. The next time you
	 *        call getTemp(), the temperature will be output in this scale.
   * @return a reference to this object.
	 */
	public Temperature setScale(Scale scale){			// method for setting the scale of the object
	
		if (!this.unit.equals(scale)){					// if the units being changed are different

			if (this.unit.equals(Scale.CELSIUS)){		// if the current unit is CELSIUS
				if (scale.equals(Scale.FAHRENHEIT)){	// if it is being changed to FAHRENHEIT
					this.unit = Scale.FAHRENHEIT;		// set the unit to FAHRENHEIT
					this.temperature =  (this.temperature * 9.0 / 5.0) + 32;	// conversion to FAHRENHEIT calculation
				}else{									// if it is being change to KELVIN
					this.unit = Scale.KELVIN;			// set the unit to KELVIN
					this.temperature = this.temperature + 273.15;	// conversion to KELVIN calculation
				}
			}
			else if(this.unit.equals(Scale.FAHRENHEIT)){	// if the current unit is FAHRENHEIT
				if (scale.equals(Scale.CELSIUS)){		// if it is being changed to CELSIUS
					this.unit = Scale.CELSIUS;			// set the unit to CELSIUS
					this.temperature = (this.temperature - 32) * 5.0 / 9.0;		// conversion to CELSIUS calculation
				}else{									// if it is being changed to KELVIN
					this.unit = Scale.KELVIN;			// set the unit to KELVIN
					this.temperature = (this.temperature) * 5.0 / 9.0 + 273.15;	// conversion to KELVIN calculation
				}
			}
			else{										// if the current unit is KELVIN
				if (scale.equals(Scale.FAHRENHEIT)){	// if it is being changed to FAHRENHEIT
					this.unit = Scale.FAHRENHEIT;		// set the unit to FAHRENHEIT
					this.temperature = (this.temperature - 273.15) * 9.0 / 5.0 + 32;	// conversion to FAHRENHEIT calculation
				}else{									// if it is being changed to CELSIUS
					this.unit = Scale.CELSIUS;			// set the unit to CELSIUS
					this.temperature = this.temperature - 273.15;	// conversion to CELSIUS calculation
				}
			}
		}
		
    return this;  // do NOT change this return statement.
	}


	/** setter for temperature
	 *
	 * @param temp is the new temperature (in the object's current scale)
   * @return a reference to this object.
	 */
	public Temperature setTemp(double temp){		// method for changing the temperature
	
		if (this.unit.equals(Scale.CELSIUS)){		// if current unit is CELSIUS
			if (temp < -273.15){					// check to see if below absolute zero
				this.temperature = -273.15;			// if so, set to absolute zero
			}else{									// if not below absolute zero
				this.temperature = temp;			// set temperature to given temp
			}
		}
		else if(this.unit.equals(Scale.KELVIN)){	// if current unit is KELVIN
			if (temp < 0){							// check to see if below absolute zero
				this.temperature = 0;				// if so, set to absolute zero
			}else{									// if not below absolute zero
				this.temperature = temp;			// set temperature to given temp
			}				
		}
		else{										// if current unit is FAHRENHEIT
			if (temp < -459.67){					// check to see if below absolute zero
				this.temperature = -459.67;			// if so, set to absolute zero
			}else{									// if not below absolute zero
				this.temperature = temp;			// set temperature to given temp
			}
		}
		
		return this;  // do NOT change this return statement.
	}

	/** setter for temperature
	 *
	 * @param temp is the new temperature
	 * @param scale is the scale of the new temperature. It must be
	 *        a constant from the Scale enum type.
   * @return a reference to this object.
   */
	public Temperature setTemp(double temp, Scale scale){	// method for setting the temperature and scale
	
		this.unit = scale;									// set current unit to scale
	
		if (this.unit.equals(Scale.CELSIUS)){				// if the current unit equals to CELSIUS
			if (temp < -273.15){							// check to see if below absolute zero
				this.temperature = -273.15;					// if so, set to absolute zero
			}else{											// if not below absolute zero
				this.temperature = temp;					// set temperature to given temp
				}
		}
		else if(this.unit.equals(Scale.FAHRENHEIT)){		// if the current unit equals to FAHRENHEIT
			if (temp < -459.67){							// check to see if below absolute zero
				this.temperature = -459.67;					// if so, set to absolute zero
			}else{											// if not below absolute zero
				this.temperature = temp;					// set temperature to given temp
			}
		}
		else{												// if the current unit equals to KELVIN
			if (temp < 0){									// check to see if below absolute zero
				this.temperature = 0;						// if so, set to absolute zero
			}else{											// if not below absolute zero
				this.temperature = temp;					// set temperature to given temp
			}
		}
	
		return this;  // do NOT change this return statement.
	}

	/** setter for temperature
	 *
	 * @param temp is the new temperature.
	 * @param scale is a string representing one of the three scales.
   * @return a reference to this object.
   */
	public Temperature setTemp(double temp, String scale){	// method for setting the temperature and scale
	
		String scale1 = "CELSIUS";				// CELSIUS string used for comparision with string param
		String scale2 = "KELVIN";				// KELVIN string used for comparision with string param
		String scale3 = "FAHRENHEIT";			// FAHRENHEIT string used for comparision with string param
	
		this.temperature = temp;				// sets the current temp to the given temp
	
		scale = scale.toUpperCase();			// change the param string to all uppercase
	
		if (scale.charAt(0) == 'C'){			// take the first char in param string and compare it with char C
			if (scale1.contains(scale)){		// if the param string is in the string scale1 
				scale = "CELSIUS";				// set the scale to CELSIUS
				if (temp < -273.15){			// check for below absolute zero
					this.temperature = -273.15;	// set to absolute zero
				}else{							// if not below absolute zero
					this.temperature = temp;	// set to param temp
				}
			}
		}
		else if (scale.charAt(0) == 'K'){		// take the first char in param string and compare it with char K
			if (scale2.contains(scale)){		// if the param string is in the string scale2
				scale = "KELVIN";				// set the scale to KELVIN
				if (temp < 0){					// check for below absolute zero
					this.temperature = 0;		// set to absolute zero
				}else{							// if not below absolute zero
					this.temperature = temp;	// set to param temp
				}
			}
		}	
		else if (scale.charAt(0) == 'F'){		// take the first char in param string and compare it with char F
			if (scale3.contains(scale)){		// if the param string is in the string scale3
				scale = "FAHRENHEIT";			// set the scale to FAHRENHEIT
				if (temp < -459.67){			// check for below absolute zero
					this.temperature = -459.67;	// set to absolute zero
				}else{							// if not below absolute zero
					this.temperature = temp;	// set to param temp
				}
			}
		}	
	
		this.unit = Scale.valueOf(scale);		// convert the string into the enum for Scale
	
		return this;  // do NOT change this return statement.
  }



	/* ------------------------------------------------- */
	/* ------------------------------------------------- */
  /* do not change anything below this                 */
  /* ------------------------------------------------- */
	/* ------------------------------------------------- */

  /** String representation of a temperature object    */
	@Override
  public String toString(){
    return "" + this.getTemp() + this.getScale();
  }

}
