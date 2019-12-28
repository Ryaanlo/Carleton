package comp2402a1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public class Part3 {
	
	/**
	 *  Read the input one line at a time and output only 
	 * the last 9999 lines in the order they appear.  
	 * If there are fewer than 9999 lines, output them all.  
	 * For full marks, your code should be fast 
	 * and should never store more than 9999 lines. 
	 * @param r the reader to read from
	 * @param w the writer to write to
	 * @throws IOException
	 */
	public static void doIt(BufferedReader r, PrintWriter w) throws IOException {
		// Queue to store text
		Queue<String> q = new LinkedList<String>();
		
		// reading through all text
        for (String line = r.readLine(); line != null; line = r.readLine()) {
			// if queue is getting too big
			if (q.size() == 9999){
				q.remove(); // remove oldest one
				q.add(line); // add the newest one
			}else{
				q.add(line);
			}
        }

		// go through queue and print
        for (String text : q) {
            w.println(text);
        }		

	}

	/**
	 * The driver.  Open a BufferedReader and a PrintWriter, either from System.in
	 * and System.out or from filenames specified on the command line, then call doIt.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			BufferedReader r;
			PrintWriter w;
			if (args.length == 0) {
				r = new BufferedReader(new InputStreamReader(System.in));
				w = new PrintWriter(System.out);
			} else if (args.length == 1) {
				r = new BufferedReader(new FileReader(args[0]));
				w = new PrintWriter(System.out);				
			} else {
				r = new BufferedReader(new FileReader(args[0]));
				w = new PrintWriter(new FileWriter(args[1]));
			}
			long start = System.nanoTime();
			doIt(r, w);
			w.flush();
			long stop = System.nanoTime();
			System.out.println("Execution time: " + 10e-9 * (stop-start));
		} catch (IOException e) {
			System.err.println(e);
			System.exit(-1);
		}
	}
}
