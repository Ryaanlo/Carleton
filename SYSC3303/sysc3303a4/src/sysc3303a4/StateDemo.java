package sysc3303a4;

public class StateDemo {
    public static void main(String[] args){
        var context = new Context();
        Thread contextThread = new Thread(context, "Context");
        System.out.println("Starting StateDemo!");
        contextThread.start();

        context.pedestrianWaiting();
        // Testing, sending pedestrian waiting events
        context.timeout(50000);
        context.pedestrianWaiting();
        context.timeout(60000);
        context.pedestrianWaiting();
        context.timeout(70000);
        context.pedestrianWaiting();
    }
}
