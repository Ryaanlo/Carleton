package comp2402a1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public class Part7 {
	
	/**
	 *  Read the input one line at a time and output the current line 
	 * if and only if you have already read at least 1000 lines greater 
	 * than the current line and at least 1000 lines less than the current line. 
	 * (Again, greater than and less than are with respect to the ordering defined by String.compareTo().) 
	 * @param r the reader to read from
	 * @param w the writer to write to
	 * @throws IOException
	 */
	public static void doIt(BufferedReader r, PrintWriter w) throws IOException {
		// Arraylist to store inputs
		ArrayList<String> list = new ArrayList<String>();
		// index for upper limit
		int counter = 1000;

        for (String line = r.readLine(); line != null; line = r.readLine()) {
			// if the size < 2000 then just keep adding
			if (list.size() < 1999){
				list.add(line);
			// if size is at 2000 then sort the array
			}else if (list.size() == 1999){
				list.add(line);
				Collections.sort(list);
			// if size is > 2000
			}else{
				// binary search for the index of line
				int index = Collections.binarySearch(list, line);

				// since the string is not present, return would be negative index - 1
				// so we need to reverse it and subtract one to get index
				if (index < 0){
					index = (index * (-1)) - 1;
				// if its a duplicate
				}else{
					// set the index to the last index for all duplicates
					// so that it could be inserted to the back of dups
					index = list.lastIndexOf(line) + 1;
				}

				// if the index lies in the middle where there is
				// at least 1000 less than and 1000 greater than
				if ((index >= 1000) && (index <= counter)){
					w.println(line);
				// if less than 1000 or greater than the upper limit
				}else{
					// increase counter for index of upper limit
					// where > 1000 is
					counter ++;
					list.add(index, line);		
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
