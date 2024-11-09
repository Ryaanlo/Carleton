package sysc3303a4;

public class vehiclesEnabled extends State{
    public void start(Context c){
        c.nextState();
    }

    public void nextState(Context c){
        System.out.println("Vehicles are now Enabled!");
        c.setCurrrentState(new vehiclesGreen());
    }    
}
