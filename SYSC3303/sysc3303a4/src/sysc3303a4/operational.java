package sysc3303a4;

public class operational extends State{
    public void start(Context c){
        c.nextState();
    }

    public void nextState(Context c){
        System.out.println("Everything is now Operational!");
        c.setCurrrentState(new vehiclesEnabled());
    }
}
