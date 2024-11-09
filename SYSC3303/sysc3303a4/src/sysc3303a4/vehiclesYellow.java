package sysc3303a4;

public class vehiclesYellow extends State{
    private static vehiclesYellow instance = new vehiclesYellow();

    public static State instance(){
        return instance;
    }

    public void nextState(Context c){
        c.setCurrrentState(new pedestriansWalk());
    }

    public void start(Context c){
        System.out.println("Yellow Light!");
        c.timeout(3000);
        
        // Next State, RedLight, Pedestrian Walking
        nextState(c);
    }

}
