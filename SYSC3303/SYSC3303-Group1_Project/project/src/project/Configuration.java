package project;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Configuration {
	public static final Configuration shared = new Configuration();

	public final String floorRequestsFilePath = System.getProperty("user.dir") + "/src/service_requests.txt";

	public final int numberOfElevators = 4;
	public final int    numberOfFloors = 22;
	public final double    floorHeight = 4;

	/** The time it takes to open or close the door. */
	public final double doorOpenCloseTime = 2;
	/** The time it lasts for a door being blocked. */
	public final double     doorBlockTime = 10;
	/** The time it takes for passengers to enter and leave the elevator. */
	public final double       loadingTime = 8;
	public final double          maxSpeed = 1.6;
	public final double  accelerationRate = 0.32;

	public final InetAddress  schedulerAddress;
	public final int    schedulerPortForFloors = 3303;
	public final int schedulerPortForElevators = 3304;

	private Configuration() {
		InetAddress address = null;
		try {
			// address = InetAddress.getByName("127.0.0.1");
			address = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		}
		schedulerAddress = address;
	}
}
