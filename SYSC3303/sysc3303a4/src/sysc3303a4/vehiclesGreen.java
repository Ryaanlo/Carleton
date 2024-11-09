package sysc3303a4;

public class vehiclesGreen extends State{
    private static vehiclesGreen instance = new vehiclesGreen();

    public static State instance(){
        return instance;
    }

    public void nextState(Context c){
        c.setCurrrentState(new vehiclesYellow());
    }
    
    public void start(Context c){
        System.out.println("Green Light!");
        c.timeout(10000);

        while (!(c.isPedestrianWaiting())){
            System.out.println("No Pedestrian Waiting!");
            c.timeout(10000);
        }

        System.out.println("Pedestrian Waiting!");

        // Next State, Yellow Light
        nextState(c);
    }

}
