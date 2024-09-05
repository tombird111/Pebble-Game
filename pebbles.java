import java.util.Scanner;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class pebbles{
	public static boolean exitBool;
	public static int bagCount;
	public static boolean gameWon;
	public static ArrayList<pebbleBag> bags;
	public static ArrayList<player> players;
	public static Scanner userReader = new Scanner(System.in);
	
	public static void main(String[] args){
		pebbles pebbleGame = new pebbles(); //Create a new pebbles
		exitBool = false; //Note that the user has chosen not to leave yet
		System.out.println("Welcome to the Pebble Game!!\n" +
                "You will be asked to enter the number of players.\n" +
                "and then for the location of three files in turn containing\n" +
                "comma separated integer values for the pebble weights.\n" +
                "The integer values must be strictly positive.\n" +
                "The game will then be simulated, and output\n" +
                "written to files in this directory." +
                "\n");
		int playerNumber = 0;
		boolean validNumberInput;
		do {
			System.out.println("Please enter the number of players:"); //Ask for the number of players
			String userPlayerInput = getUserInput(); //Get the inputed number
			validNumberInput = validNumberInputCheck(userPlayerInput); //Check if the input is valid
			if(!exitBool && validNumberInput) { //If it is valid, and the user has chosen not to exit
				playerNumber = Integer.parseInt(userPlayerInput); //Set the player number to the input
			}
		} while(validNumberInput == false && exitBool == false); //Repeat until the user chooses to exit, or they enter a valid input
		bagCount = 0; //Set the number of bags to 0
		String[] blackArray = {"X","Y","Z"}; //Create an array for naming the black bags
		bags = new ArrayList<pebbleBag>(); //Set the bag list to a new empty array list
		while(bagCount <= 2 && exitBool == false){ // Whilst there are less than 3 bags AND the user does not want to exit
			System.out.println("Please enter location of bag number " + bagCount + " to load:"); //Ask for the number of players
			String userBagInput= getUserInput(); //Take the input of the user
			pebbleBag bagToAdd = makeBag(playerNumber, blackArray[bagCount], userBagInput); //Attempt to make a bag with the inputed filename and bag name
			if(bagToAdd != null) { //If the bag is not null
				bags.add(bagToAdd); //Add it to the list of bags
			}
		}
		if(!exitBool){ //If the user has chosen not to exit
			String[] whiteArray = {"A","B","C"}; //Make the names in an array A, B, C
			for(int i = 0; i < 3; i++){ 
				pebbleBag whiteBag = new pebbleBag(whiteArray[i]); //Make 3 white bags
				bags.add(whiteBag); //Add them to the bag list
			}
			players = new ArrayList<player>(); //Create an empty list of players
			for(int curPlayerNum = 0; curPlayerNum < playerNumber; curPlayerNum++){
				pebbles.player newPlayer = pebbleGame.new player("player"+curPlayerNum); //Create a player
				players.add(newPlayer); //Add the player to the player list
			}
			gameWon = false; //Say that the game has not yet been won
			for(player curPlayer : players){ //For every player in the players list
				(new Thread (curPlayer)).start(); //Start the thread
			}
		}
	}
	
	/**
	This method gets the userInput and returns it as a string
	
	@param weightList - the list to base the pebble weights off
	@return the list of pebbles
	*/
	private static String getUserInput() {
		String userInput = userReader.nextLine();
		return userInput; //Return the next line the player inputs
	}
	
	/**
	This method takes a string as an argument and sees whether it is a valid input for the number of players
	
	@param playerCountString - checks the playerCountString
	@return a boolean on whether the input is valid (True) or not (False)
	*/
	public static boolean validNumberInputCheck(String playerCountString) {
		if(!playerCountString.equals("E")){ //If the input is not an E
			try{
				int playerCountInt = Integer.parseInt(playerCountString); //Try to parse the input as an integer
				if(playerCountInt > 1){ //If the input parses and is greater than one
					return true; //Return the number of players
				} else {
					System.out.println("Please input a positive number of players greater than 1"); //Display an error message if the input is less than or equal to 1
				}
			}catch (NumberFormatException e){ //If an exception is thrown trying to parse the input
				System.out.println("Not an integer or an E to exit, please try again."); //Display an error message
			}
		} else { //If the input is an E
			exitBool = true; //Mark that you want to exit the program
		}
		return false;
	}
	
	/**
    This method takes an integer representing a player, a string representing an empty bag and a string which is the location of the input file containing weights of pebbles and creates a bag filled with pebbles. If an invalid input or invalid file is entered it loops until user enters a valid file or enters E to exit the program

	@param playerNumber - the player number to use
	@param bagName - the name of the bag
	@param fileInputString - the name of the file to attempt to use
	@return the bag or null
    */
	public static pebbleBag makeBag(int playerNumber, String bagName, String fileInputString){
		boolean validInput; //Create a boolean to show whether the input is valid
		ArrayList<Integer> fileIntList = new ArrayList<Integer>(); //Create an empty list for integers
		validInput = true; //Mark the input as currently valid
		if(!fileInputString.equals("E")){ //If the input is not an E
			try{
				File currentFile = new File(fileInputString); //Read the current file based on the string
				Scanner fileReader = new Scanner(currentFile); //Make a new scanner for the file
				while(fileReader.hasNextLine()){
					String fileText = fileReader.nextLine(); //Read the file
					if(fileText != null && fileText != ""){ //If the file or line is not empty
						String[] fileNumbers = fileText.split(","); //Split the files text between commas and store in an array
						for(String fileNumber : fileNumbers){ //For each of the text splits
							try{
								int currentInt = Integer.parseInt(fileNumber.trim()); //Attempt to parse the current string without white spaces as an integer
								if(currentInt < 0){ //If the number is negative
									System.out.println("Added a negative number, please be aware"); //Display a warning message
								} else {
									fileIntList.add(currentInt); //Add the current integer to a list
								}
							}catch (NumberFormatException e){ //If an exception is thrown trying to parse the input
								System.out.println("An non-integer was found in the file"); 
								validInput = false; //Mark the file as invalid
							}
						}
					} else {
						validInput = false; //Mark the file as empty, and therefore invalid
						System.out.println("Text could not be found within the file");
					}
					if(fileIntList.size() < (playerNumber * 11) && validInput){ //Check that the file contains enough pebbles
						validInput = false; //Mark the file as invalid as it does not contain enough pebbles
						fileIntList.clear(); //Clear the fileIntList, as it is invalid
						System.out.println("The file needs 11 times as many numbers as players");
					}
					if(validInput){ //If the file was valid
						pebbleBag newPebbleBag = new pebbleBag(fileIntList, bagName); //Make a new black bag using the pebble weights from the file
						bagCount++; //Note that a new bag has been made
						return newPebbleBag; //Return the newly created bag
					}
				}
			} catch(FileNotFoundException e){ //Print an error message if the file cannot be found
				System.out.println("No file found with that name");
			}
		} else { //If the input is an E
			exitBool = true; //Mark that you want to exit the program
		}
		return null; //Return null if the input was invalid
	}
	
	public synchronized static void refillBag(int bagIndex){ //Refill an empty bag based on its ID
		int whiteBagIndex = bagIndex + 3; //Note that the respective white bag is 3 indexes up in the list
		bags.get(bagIndex).fillFromBag(bags.get(whiteBagIndex)); //Fill the black bag from its respective white bag
		bags.get(whiteBagIndex).emptyBag(); //Empty the white bag
	}
	
	public static void winGame(){
		gameWon = true; //Note that the game has been won
		/**for(player curPlayer : players){ //For every player
			curPlayer.writePlayerFile(); //Print the players file
		}*/
	}
	
	class player implements Runnable{
		ArrayList<pebble> playerHand;
		String playerName;
		String playerString;
		int lastDraw;
		boolean hasStarted;
		int startingBagId;
		
		player(String newPlayerName){
			ThreadLocalRandom rand = ThreadLocalRandom.current();
			this.playerHand = new ArrayList<pebble>();
			this.playerName = newPlayerName;
			this.playerString = "";
			this.hasStarted = false;
			this.startingBagId = rand.nextInt(3); //Generate a random bag to start from
		}
		
		public void run(){
			while(pebbles.gameWon == false){ //If the game has not been won
				if(hasStarted == false){ //Check if this player has drawn their starting hand
					if(playerHand.size() < 10){ //Check if the player has 10 pebbles in their hand
						synchronized(this){
							drawFromBag(startingBagId); //If they haven't draw a pebble
						}
					} else {
						hasStarted = true; //If they have 10 starting pebbles, note that they have started
					}
				} else if(pebbles.gameWon == false){ //If the game has not been won
					discardToBag(); //Discard a pebble
					drawRandom(); //Draw a random pebble
					victoryCheck(); //Check that the player has a winning hand
				}
			}
		}
		
		int countHand(){
			int weight = 0;
			for(pebble currentPebble : playerHand){
				weight = weight + currentPebble.getWeight();
			}
			return weight;
		}
		
		synchronized void victoryCheck(){
			if(this.countHand() == 100){
				pebbles.winGame();
			}
		}
		
		void drawRandom(){
			ThreadLocalRandom rand = ThreadLocalRandom.current();
			int bagIndex = rand.nextInt(3); //Generate a random index to draw from
			drawFromBag(bagIndex); //Draw from the bag
		}
		
		/**
        This method takes an integer representing the index of a bag and 
        adds a random pebble from the chosen bag to the hand of the player
        while removing the same pebble from the bag it is drawn from and
        discards it to its corresponding paired bag

        @param bagIndex - the index of the bag 
        */
		synchronized void drawFromBag(int bagIndex){
			ThreadLocalRandom rand = ThreadLocalRandom.current();
			boolean pebbleDrawn = false;
			int newBagIndex = 0;
			do{
				synchronized(pebbles.bags.get(bagIndex)) {
					if(!pebbles.bags.get(bagIndex).isEmpty()){ //Check if the bag is not empty
						int pebbleBound = pebbles.bags.get(bagIndex).getSize(); //Generate a random pebble index to draw from
						int pebbleIndex = rand.nextInt(pebbleBound); 
						playerHand.add(pebbles.bags.get(bagIndex).getPebble(pebbleIndex)); //From the outer classes pebbleBag, get the pebble from the bag
						playerString = playerString + playerName + " has drawn a " + pebbles.bags.get(bagIndex).getPebble(pebbleIndex).getWeight() +
											" from bag " + pebbles.bags.get(bagIndex).getName() + "\n" +
											playerName + " hand is " + handString() + "\n"; // Write to the player string
						writePlayerFile(playerString);
						pebbles.bags.get(bagIndex).removePebble(pebbleIndex); //Remove the pebble from the bag
						lastDraw = bagIndex; //Note the last bag the player has drawn from
						pebbleDrawn = true; //Mark that a pebble has successfully been drawn
					} else { //If the bag is empty
						synchronized(pebbles.bags.get(bagIndex + 3)) {
							pebbles.refillBag(bagIndex); //Refill the empty bag
							newBagIndex = rand.nextInt(3); //Pick a new bag to draw from
						}
					}
				}
				bagIndex = newBagIndex; //Set the bagIndex to be the same as the newly generated bag index
			} 
			while (!pebbleDrawn); //Repeat until a pebble has been drawn
		}
		
		/**
        This method discards a random pebble from the player hand into the bag that the player last drew a pebble from and prints a string containing the player hand with a pebble discarded
        */
		synchronized void discardToBag(){
			ThreadLocalRandom rand = ThreadLocalRandom.current();
			int discardPebbleIndex = rand.nextInt(playerHand.size()); //Generate a random number in range (0 to hand size)
			int discardIndex = lastDraw + 3;
			playerString = playerString + playerName + " has discarded a " + playerHand.get(discardPebbleIndex).getWeight() +
					" to bag " + pebbles.bags.get(discardIndex).getName() + "\n"; //Write to the player string
			writePlayerFile(playerString);
			synchronized(pebbles.bags.get(discardIndex)){
				pebbles.bags.get(discardIndex).addPebble(playerHand.get(discardPebbleIndex)); //Add the random pebble to the respective last bag the player drew from
			}
			playerHand.remove(discardPebbleIndex); //Remove the random pebble from your hand
			playerString = playerString + playerName + " hand is " + handString() + "\n"; //Write the changed hand to the player string
		}
		
		String handString(){
			String handString = ""; //Create an empty string
			for(int index = 0; index < playerHand.size() - 1; index++){ //For every pebble except the last in the players hand
				handString = handString + playerHand.get(index).getWeight() + ", "; //Append the weight and a , to the string
			}
			handString = handString + playerHand.get(playerHand.size() - 1).getWeight(); //Append the last pebbles weight
			return handString;
		}
		
		void writePlayerFile(String playerString){
			try{
				FileWriter playerFileWriter = new FileWriter(playerName + "_output.txt");
				playerFileWriter.write(playerString);
				playerFileWriter.close();
			} catch (IOException e){
				System.out.println("There was an error when trying to output the player files");
			}
		}
	}
}