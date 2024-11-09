package sysc3303a4;

public class pedestriansFlash extends State{
    private static pedestriansWalk instance = new pedestriansWalk();

    public static State instance(){
        return instance;
    }    

    public void nextState(Context c){
        c.setCurrrentState(new vehiclesGreen());
    }

    public void start(Context c){
    	// Pedestrian Crossing Counter
        pedestriansFlashCtr = 7;

        while (pedestriansFlashCtr > 0){
            c.timeout(1000);
            
            System.out.println("TIMER: " + pedestriansFlashCtr);
            if ((pedestriansFlashCtr & 1) == 0){
                System.out.println("DONT_WALK!");
            }
            pedestriansFlashCtr --;
        }

        // Reset Pedestrian Crossing Button
        c.pedestrianNotWaiting();
        // Next State, GreenLight
        nextState(c);
    }

}
