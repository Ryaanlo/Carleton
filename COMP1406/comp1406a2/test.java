package comp1406a2;

/** Gives names to the three temperature scales we will consider in this assignment */
public class test{

	public static void main(String[] args){
		Temperature t = new Temperature(45);
		System.out.println(t.temperature);
		System.out.println(t.unit);
		System.out.println(t.getScale());
		System.out.println(t);
		t.setScale(Scale.KELVIN); 
		System.out.println(t); 
		System.out.println(t.getScale()); 
		t.setTemp(12.25, "fahr"); 
		System.out.println(t.toString()); 
		Temperature temp = new Temperature(t);
		System.out.println(temp.getScale()); 
		System.out.println(temp.toString()); 
		System.out.println(temp.unit);
		
		
	}
	
}