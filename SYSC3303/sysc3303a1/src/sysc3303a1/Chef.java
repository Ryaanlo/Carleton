package sysc3303a1;

class Chef extends Thread{

	private Box box;
	private String ingredient;
	
	public Chef(String name, String ingredient, Box box) {
		super(name);
		this.ingredient = ingredient;
		this.box = box;
	}
	
    /**
     * Runs the thread
     * Chef tries to make sandwich with their ingredient
     */
	public void run() {
		
		while (Main.sandwich < 20) {
			System.out.println(getName() + " is ready to make sandwich.");
			
			box.makeSandwich(getName(), ingredient);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
		}	
	}
	
}
