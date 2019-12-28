package comp2402a1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.TreeSet;

public class Part6 {
	
	/**
	 * Read the input one line at a time and output the 
	 * current line if and only if it is not a suffix of some previous line.  
	 * For example, if the some line is "0xdeadbeef" and some subsequent line is "beef", 
	 * then the subsequent line should not be output. 
	 * @param r the reader to read from
	 * @param w the writer to write to
	 * @throws IOException
	 */
	public static void doIt(BufferedReader r, PrintWriter w) throws IOException {
		TreeSet<String> set = new TreeSet<String>();

        for (String line = r.readLine(); line != null; line = r.readLine()) {
			String reverse = "";

			// Reversing the inputted text to change comparison to prefixes instead of suffixes
			for (int i = line.length(); i > 0 ; i --){
				reverse = reverse + line.charAt(i - 1);
			}

			// Checking for prefixes 
			int test = 0;

			// get an element of the ceiling 
			// of the reverse input in the set
			String ceiling = set.ceiling(reverse);
			// if theres no such element
			if (ceiling == null){
				w.println(line);
			// Checking for matching prefixes
			}else{
				// checking each char to see if they match
				for (int i = 0; i < reverse.length(); i ++){
					if (reverse.charAt(i) != ceiling.charAt(i)){
						test = 1;
						break;
					}
				}
			}
			// If it's not a prefix then output
			if (test == 1){
				w.println(line);
			}
			// Add to set
			set.add(reverse);
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
