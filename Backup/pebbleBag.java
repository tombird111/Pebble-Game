import java.util.ArrayList;

public class pebbleBag{
	private String name;
	private ArrayList<pebble> pebbleList;
	
	public pebbleBag(ArrayList<Integer> weightList, String name){ //For creating black bags
		this.name = name;
		this.pebbleList = fillNewPebbles(weightList);
	}
	
	public pebbleBag(String name){ //For creating white bags
		this.name = name;
		this.pebbleList = new ArrayList<pebble>();
	}
	
	/**
	This method returns a list of pebbles with weights based on the inputed array list of integers
	
	@param weightList - the list to base the pebble weights off
	@return the list of pebbles
	*/
	public ArrayList<pebble> fillNewPebbles(ArrayList<Integer> weightList){
		ArrayList<pebble> newPebbles = new ArrayList<pebble>(); //Create a new empty list of pebbles
		for (int newWeight : weightList){ //For every integer in the weightList
			pebble newPebble = new pebble(newWeight); //Create a new pebble with the weight from the weightList
			newPebbles.add(newPebble); //Append it to the list
		}
		return newPebbles; //Return the list of newPebbles
	}
	
	public synchronized ArrayList<pebble> getBagContents(){return this.pebbleList;}
	public synchronized pebble getPebble(int index){return this.pebbleList.get(index);}
	public int getSize(){return this.pebbleList.size();}
	public String getName(){return this.name;}
	public synchronized void addPebble(pebble pebbleToAdd){pebbleList.add(pebbleToAdd);}
	public synchronized void removePebble(int index){pebbleList.remove(index);}
	
	/**
	This method takes a pebbleBag and adds its contents to this bags contents
	
	@param newBag - the bag to copy the contents from
	*/
	public synchronized void fillFromBag(pebbleBag newBag){
		for(pebble curPebble : newBag.getBagContents()) {
			this.pebbleList.add(curPebble);
		}
	}
	public void emptyBag(){this.pebbleList.clear();}
	
	public synchronized boolean isEmpty(){
		if(pebbleList.size() == 0){ //If there are no pebbles in the pebble list
			return true; //Return that the bag is empty
		}
		return false; //Return that the bag is not empty
	}
}