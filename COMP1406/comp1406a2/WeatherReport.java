package comp1406a2;

/** A weather report stores the temperature at a given date/time (TimeStamp) */
public class WeatherReport{
  protected Temperature    temperature;
  protected TimeStamp      time;
  protected WeatherStation station;

	/** Creates a weather report */
  public WeatherReport(Temperature temperature, TimeStamp time, WeatherStation station){
    this.temperature = temperature;
    this.time = time;
    this.station = station;
  }

	/** Getter for the temperature */
  public Temperature getTemperature(){ return this.temperature; }
  
	/** Getter for the date/time */
	public TimeStamp   getTime(){ return this.time; }
 
	/** Returns which weather station created the report */ 
  public WeatherStation getStation(){ return this.station; }

  /* this will let us print a weather report nicely */
  @Override
  public String toString(){
    this.temperature.setScale(Scale.CELSIUS);
    String out = this.temperature.toString() + " on " + this.time.toString();
    out += " at " + this.station.getName();
    return out;
  }

}
