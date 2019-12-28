package comp1406a2;

/** Helper class to compare two Temperature objects by their
    temperature values */
public class Compare{
 /** Defines "close enough" for two floating point numbers to be equal for
     this problem */
	public static final double EPSILON = 0.001;

	/** Compares two Temperature objects.
	  *
		* @param t1 is a non-null Temperature object
		* @param t2 is a non-null Temperature object
		* @return 0 if they are close enough (abs(t1-t2) &lt; EPSILON), otherwise
		*         returns -1 when t1 &lt; t2, and +1 when t1 &gt; t2.
		*/
	public static int compare(Temperature t1, Temperature t2){
		// first make sure that they are in the same scale
		t1.setScale(Scale.CELSIUS);
		double t1temp = t1.getTemp();
		t2.setScale(Scale.CELSIUS);
		double t2temp = t2.getTemp();
		if( Math.abs( t1temp - t2temp ) < EPSILON ){
			return 0;
		}else if( t1temp < t2temp ){
			return -1;
		}else{
			return 1;
		}
	}

}
