package sysc3303a4;

public abstract class State {
    protected int pedestriansFlashCtr;

    public abstract void start(Context c);

    public abstract void nextState(Context c);
    
}
