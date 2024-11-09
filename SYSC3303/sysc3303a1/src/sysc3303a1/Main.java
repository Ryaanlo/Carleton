package sysc3303a1;

import java.util.HashSet;

public class Main {
	
	public static int sandwich = 0;

	public static void main(String[] args) {
		// Initialize threads and starts them
		Box box = new Box(new HashSet<String>());
		
		Thread BreadChefThread = new Chef("Chef 1", "Bread", box);
		Thread PeanutButterChefThread = new Chef("Chef 2", "Peanut", box);
		Thread JamChefThread = new Chef("Chef 3", "Jam", box);
		
		Thread AgentThread = new Agent("Agent", box);
		
		System.out.println("Begin making sandwiches!");
		
		AgentThread.start();
		
		BreadChefThread.start();
		PeanutButterChefThread.start();
		JamChefThread.start();
		
	}

}
