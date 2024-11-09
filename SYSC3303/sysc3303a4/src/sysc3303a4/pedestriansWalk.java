package sysc3303a4;

public class pedestriansWalk extends State{
    private static pedestriansWalk instance = new pedestriansWalk();

    public static State instance(){
        return instance;
    }    

    public void nextState(Context c){
        c.setCurrrentState(new pedestriansFlash());
    }

    public void start(Context c){
        System.out.println("Red Light!");
        c.timeout(1000);

        System.out.println("WALK!");
        c.timeout(15000);
        
        // Next State, Pedestrian Crossing Flash
        nextState(c);
    }

}
