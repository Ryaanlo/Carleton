package comp1406a2;

/** Builds a sample database that you can use to help 
 with your own coding/debugging
 */
public class BuildDatabase{

	/** Build a small database of weather stations
	  * @param args is not used in this program
		*/
  public static void main(String[] args){
    // sample database
    WeatherDatabase db = new WeatherDatabase();

    // some WeatherStations
    WeatherStation[] stations = new WeatherStation[3];
    stations[0] = new WeatherStation("YOW-Airport", 1);
    stations[1] = new WeatherStation("Downtown", 2);
    stations[2] = new WeatherStation("Fallowfield-Railway-Station", 3);


    int[] days = {1,2,2,2,4,7,11,20,30,32};
    double[] temps = {10.0, 11, 12, 10, 9.2, 28.1, 10.5, 20, 23, 24};

    // populate YOW with some weather reports
    System.out.println("Building YOW WeatherStation");
    for(int i=0; i<days.length; i+=1){
      Temperature t = new Temperature(temps[i]);
      TimeStamp time = new TimeStamp(days[i],7, 15+i);
      WeatherReport wr = new WeatherReport(t, time, stations[0]);
      System.out.println( "  " + wr.toString() );
      stations[0].addReport(wr);
    }

    // populate Down town weather station
    days = new int[]{1,2,4,7};
    temps = new double[]{12.1, 12.3, 15.2, 16.0, 17.0};
    System.out.println("Building Downtown WeatherStation");
    for(int i=0; i<days.length; i+=1){
      Temperature t = new Temperature(temps[i]);
      TimeStamp time = new TimeStamp(days[i],8, i);
      WeatherReport wr = new WeatherReport(t, time, stations[1]);
      System.out.println( "  " + wr.toString() );
      stations[1].addReport(wr);
    }

    // populate Fallowfield
    days = new int[]{1,1,1,2,2,2,3,3,3,4,4,4,5,5,6,6,6,6,7,7,7,8,8,8};
    temps = new double[]{45, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1,-1,
                         10,10,10,10,20,20,20,25,25,25};
    System.out.println("Building Fallowfield WeatherStation");
    for(int i=0; i<days.length; i+=1){
      Temperature t = new Temperature(temps[i]);
      TimeStamp time = new TimeStamp(days[i],12, 30+i);
      WeatherReport wr = new WeatherReport(t, time, stations[2]);
      System.out.println( "  " + wr.toString() );
      stations[2].addReport(wr);
    }

    // add stations to database
    for(int i=0; i<stations.length; i+=1){
      db.addWeatherStation(stations[i]);
      System.out.println("adding " + stations[i].toString());
    }
	db.addWeatherStation(stations[2]);
	System.out.println(db.averageTemperature(14,12));
	System.out.println(db.getMaxTemperature(10));
	System.out.println(db.removeWeatherStation(2));
	System.out.println(db.getMaxTemperature().getTemp());	
	
	
	//System.out.println(db.getWeatherStations()[0].getReports().toString());
	
	for(int i=0; i<db.getWeatherStations().length; i+=1){
		System.out.println(db.getWeatherStations()[i]);
	}
	
/*	System.out.println(db.removeWeatherStation(3));
	
	WeatherStation[] hi = db.getWeatherStations();
	
	for (int i = 0; i < hi.length; i++){
		System.out.println(hi[i]);
	}
	*/
	
  }



}
