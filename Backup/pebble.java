public class pebble{
	private int weight;
	public pebble(int newWeight){ //Constructor for pebble
		this.weight = newWeight; //Set the weight of the pebble
	}
	public synchronized int getWeight(){return this.weight;}
}