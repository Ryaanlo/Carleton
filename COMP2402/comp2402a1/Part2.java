package comp2402a1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Part2 {
	
	/**
	 * Read the input one line at a time and output the current line
	 *  if and only if it is smaller than any other line read so far.  
	 * (Here, smaller is with respect to the usual order on Strings, 
	 * as defined by String.compareTo().) 
	 * @param r the reader to read from
	 * @param w the writer to write to
	 * @throws IOException
	 */
	public static void doIt(BufferedReader r, PrintWriter w) throws IOException {
		// int counter as a flag
		int counter = 0;
		// String to store the smallest string
		String smallest = "";

        for (String line = r.readLine(); line != null; line = r.readLine()) {
			// on very first line
			if (counter == 0){
				smallest = line; // set to the smallest
				counter ++; // change flag
				w.println(line); // print
			// everything else
			}else{
				// compare current smallest with the new text and if smaller
				if (line.compareTo(smallest) < 0){
					smallest = line; // change current smallest
					w.println(line); // print
				}
			}
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
