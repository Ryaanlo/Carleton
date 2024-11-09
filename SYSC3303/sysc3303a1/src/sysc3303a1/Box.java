package sysc3303a1;

import java.util.Set;

class Box {
    
    private Set<String> sandwichBox;
    
    public Box(Set<String> sandwich) {
    	this.sandwichBox = sandwich;
    }
    
    /**
     * Adds ingredient to sandwichBox
     * if it already has ingredients then
     * waits on chef
     */
    public synchronized void add(String item) {
        while (this.sandwichBox.size() == 2) {
            try {
            	// Wait on chef to make the sandwich
                wait();
            } catch (InterruptedException e) {
                return;
            }
        }
        
        sandwichBox.add(item);
        notifyAll();
    }
    
    /**
     * Makes the sandwich with remaining ingredient
     * If the missing ingredient matches then make
     * the sandwich
     */
    public synchronized void makeSandwich(String name, String ingred) {
    	
		while (sandwichBox.size() < 2 || sandwichBox.contains(ingred)) {
			try {
				wait();
			} catch (InterruptedException e) {
				return;
			}
		}
		System.out.println(sandwichBox);
		
		System.out.println(name + " has made and ate a sandwich by adding " + ingred);
		
		notifyAll();
		sandwichBox.clear();
    	
    	Main.sandwich ++;
    	System.out.println("Total number of sandwiches made: " + Main.sandwich);
    }
    
    public boolean contains(String ingre) {
    	return sandwichBox.contains(ingre);
    }

}
