package project;

import java.time.Instant;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
	public static void log(String subsystem, String component, String statement) {
		// Create new log file if it doesn't exist
		final var fileName = "log.txt";
		try {
			File logger = new File(fileName);
			if (logger.createNewFile()){
				System.out.println("Log created: " + logger.getName());
			}
		} catch (IOException e) {
			System.out.println("An error has occurred.");
			e.printStackTrace();
		}
		// Write to the log file
		try {
			FileWriter writer = new FileWriter(fileName, true);
			String message = "[" + Instant.now().toString() + "]"
					+ " [" + subsystem + "]"
					+ " [" + component + "]"
					+ " [" + statement + "]";

			System.out.println(message);

			writer.write(message + "\n");
			writer.close();
		} catch (IOException e) {
			System.out.println("An error has occurred.");
			e.printStackTrace();
		}
	}
}
