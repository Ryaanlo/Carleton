package sysc3303a4;

public class Context implements Runnable{

    private State currentState;
    private boolean isPedestrianWaiting;

    public Context(){
        currentState = new operational();
        isPedestrianWaiting = false;
    }

    public void run(){
        while (true){
            currentState.start(this);
        }
    }

    public void nextState(){
        currentState.nextState(this);
    }

    public void setCurrrentState(State s){
        currentState = s;
    }

    public void timeout(int seconds){
        try {
            Thread.sleep(seconds);
        } catch (InterruptedException e ) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void pedestrianWaiting(){
        isPedestrianWaiting = true;
    }

    public boolean isPedestrianWaiting(){
        return this.isPedestrianWaiting;
    }

    public void pedestrianNotWaiting(){
        isPedestrianWaiting = false;
    }
}
