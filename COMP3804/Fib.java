import java.lang.Math;

public class Fib {

    public static void main(String args[]){
        // System.out.println("test");

        int n = 47;

        long time;

        System.out.print("Fibonacci number: ");
        System.out.println(n);

        // fib1
        time = System.currentTimeMillis();
        System.out.println("fib1");
        System.out.println(fib1(n));
        System.out.print("Time taken: ");
        System.out.println(System.currentTimeMillis() - time);

        // fib2
        time = System.currentTimeMillis();
        System.out.println("fib2");
        System.out.println(fib2(n));
        System.out.print("Time taken: ");
        System.out.println(System.currentTimeMillis() - time);

        // golden ratio
        time = System.currentTimeMillis();
        System.out.println("golden ratio");
        System.out.println(goldenratio(n));
        System.out.print("Time taken: ");
        System.out.println(System.currentTimeMillis() - time);

    }

    public static int fib1(int n){
        if (n == 0){
            return 0;
        }
        if(n == 1){
            return 1;
        }
        return fib1(n-1) + fib1(n-2);
    }

    public static int fib2(int n){
        if (n == 0){
            return 0;
        }
        int[] array = new int[n+1];

        array[0] = 0;
        array[1] = 1;

        for (int i = 2; i <= n; i ++){
            array[i] = array[i-1] + array[i-2];
        }

        return array[n];

    }

    public static int goldenratio(int n){

        return (int) ( (Math.pow(1+(Math.sqrt(5)), n) - Math.pow(1-(Math.sqrt(5)), n)) / (Math.pow(2, n) * Math.sqrt(5)));
    }
}
