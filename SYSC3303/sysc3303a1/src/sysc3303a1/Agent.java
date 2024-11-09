package sysc3303a1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

class Agent extends Thread{
	
	private ArrayList<String> sandwichInMaking;
	private Box box;
	private int count= 0;
	
	private String[] ingredients = {"Bread", "Peanut", "Jam"};

	public Agent(String name, Box box) {
		super(name);
		this.box = box;
		this.sandwichInMaking = new ArrayList<String>();
	}
	
    /**
     * Runs the thread
     * Add ingredients to the sandwich box
     */
	public void run() {
		
		while (Main.sandwich < 20) {
			provideIngredient();
			for (int i=0; i < sandwichInMaking.size(); i++) {
				box.add(sandwichInMaking.get(i));
			}
			sandwichInMaking.clear();
		}

	}
	
    /**
     * The agent providing 2 ingredients
     * and adding it to the sandwichInMaking
     */
	public void provideIngredient() {

		// Pick 2 random ingredients and adds to array
		Random random = new Random();
		while (count < 2) {
			String ingredient = ingredients[random.nextInt(0,3)];
			if (!sandwichInMaking.contains(ingredient)) {
				sandwichInMaking.add(ingredient);
			}
			count ++;
		}
		
		count = 0;
		
		// Sleep the thread to show results
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}
	}
}
