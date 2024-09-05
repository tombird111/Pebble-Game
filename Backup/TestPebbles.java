import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestPebbles {
	
	pebbles pebbleTester;
	
	@Before
	public void setUp() throws Exception {
		pebbleTester = new pebbles(); //Create a new pebble game
		pebbles.bags = new ArrayList<pebbleBag>(); //Set the array list of bags to a new list
		pebbles.exitBool = false;
	}

	@After
	public void tearDown() throws Exception {
		pebbleTester = null;
	}

	@Test
	public void testPlayerCountHand() {
		pebbles.player testPlayer = pebbleTester.new player("playertest"); //Create a testPlayer
		testPlayer.playerHand.add(new pebble (50)); //Add two pebbles with a value of 50 to the players hand
		testPlayer.playerHand.add(new pebble (50));
		assertEquals(testPlayer.countHand(), 100); //Check that the player counts their hand
	}
	
	@Test
	public void testPlayerHandStringSingle() {
		pebbles.player testPlayer = pebbleTester.new player("playertest"); //Create a testPlayer
		testPlayer.playerHand.add(new pebble (50)); //Add a pebble with value 50 to the players hand
		assertEquals(testPlayer.handString(), "50");
	}
	
	@Test
	public void testPlayerHandStringMultiple() {
		pebbles.player testPlayer = pebbleTester.new player("playertest"); //Create a testPlayer
		testPlayer.playerHand.add(new pebble (50)); //Add a pebble with value 50 to the players hand
		testPlayer.playerHand.add(new pebble (60)); //Add a pebble with value 60 to the players hand
		testPlayer.playerHand.add(new pebble (70)); //Add a pebble with value 70 to the players hand
		assertEquals(testPlayer.handString(), "50, 60, 70");
	}
	
	@Test
	public void testRefillBag() {
		ArrayList<Integer> intList = makeBasicIntList(3);
		makeEmptyBagList(3);
		pebbles.bags.add(new pebbleBag(intList, "3")); //Add a new bag constructed with the array list
		pebbles.refillBag(0); //Refill the empty bag 0 from its counterpart (3)
		int expPebbleWeight = 0; //Starting from 0
		for(pebble currentPebble : pebbles.bags.get(0).getBagContents()) { //Check each pebble in bag 0
			assertEquals(currentPebble.getWeight(), expPebbleWeight); //Check that the pebbles match a corresponding matching pebble
			expPebbleWeight++; //Iterate along one pebble
		}
		assertTrue(pebbles.bags.get(3).isEmpty()); //Check that the back the pebbles were emptied from is empty
	}
	
	@Test
	public void testPlayerDrawPebbleValid() {
		pebbles.player testPlayer = pebbleTester.new player("playertest"); //Make a new player
		ArrayList<Integer> intList = makeBasicIntList(3); //Make an array list of integers
		pebbles.bags.add(new pebbleBag(intList, "TestBag")); //Create a new bag from the array list
		testPlayer.drawFromBag(0); //Draw from the first bag
		int pebbleWeight = testPlayer.playerHand.get(0).getWeight(); //Find the weight of the drawn pebble
		assertEquals(1, pebbleWeight, 1); //Assert that the pebble should be 0, 1 or 2
	}
	
	@Test
	public void testPlayerDrawRandomEmpty() {
		pebbles.player testPlayer = pebbleTester.new player("playertest"); //Make a new player
		makeEmptyBagList(3);
		ArrayList<Integer> intList = makeBasicIntList(3); //Make an array list of integers
		for(int i = 0; i < 3; i++) { //Add 3 full bags to the end of the bag list
			pebbles.bags.add(new pebbleBag(intList, "TestBag")); //Create a new bag
		}
		for(int bagIndex = 0; bagIndex < 6; bagIndex++) { //For each bag
			if(bagIndex < 3) { //If it is one of the first bags, check it is empty
				assertTrue(pebbles.bags.get(bagIndex).isEmpty());
			} else { //If it is one of the last 3 bags, check that it is not empty
				assertFalse(pebbles.bags.get(bagIndex).isEmpty());
			}
		}
		testPlayer.drawFromBag(0); //Attempt to draw from a random bag
		int pebbleWeight = testPlayer.playerHand.get(0).getWeight(); //Find the weight of the drawn pebble
		assertEquals(1, pebbleWeight, 1); //Assert that the pebble should be 0, 1 or 2
	}
	
	@Test
	public void testValidNumberInputCheckValid() {
		String inputCheck = "1"; //The minimum number of players for a game is 1
		assertTrue(pebbles.validNumberInputCheck(inputCheck));
	}
	
	@Test
	public void testValidNumberInputCheckBoundary() {
		String inputCheck = "0"; //Check whether the input can be 0
		assertFalse(pebbles.validNumberInputCheck(inputCheck));
	}
	
	@Test
	public void testValidNumberInputCheckNegative() {
		String inputCheck = "-1"; //Check whether the input can be negative
		assertFalse(pebbles.validNumberInputCheck(inputCheck));
	}
	
	@Test
	public void testValidNumberInputCheckE() {
		String inputCheck = "E"; //Check that if E is inputed, that pebbles.exitBool will be true
		assertFalse(pebbles.validNumberInputCheck(inputCheck));
		assertTrue(pebbles.exitBool);
	}
	
	@Test
	public void testValidNumberInputCheckInvalid() {
		String inputCheck = "invalid"; //Check that if E is inputed, that pebbles.exitBool will be true
		assertFalse(pebbles.validNumberInputCheck(inputCheck));
	}
	
	@Test
	public void testMakeBagValid() {
		pebbleBag testBag = pebbles.makeBag(2, "testBag", "testfilevalid.txt"); //Using testfilevalid.txt, make a bag
		pebbleBag expectedBag = new pebbleBag(makeBasicIntList(23), "expectedBag"); //Make a new bag with pebbles of weights 0 to 22
		int pebbleCounter = 0; //From the first pebble
		for(pebble testPebble : testBag.getBagContents()) { //For each pebble in the testBag
			assertEquals(testPebble.getWeight(), expectedBag.getBagContents().get(pebbleCounter).getWeight()); 
			//Check that each pebble in the test bag matches each pebble in the expected bag
			pebbleCounter++; //Iterate along the expected bag
		}
	}
	
	@Test
	public void testMakeBagNegative() {
		pebbleBag testBag = pebbles.makeBag(2, "testBag", "testfilenegative.txt"); //Using testfilenegative.txt, make a bag
		pebbleBag expectedBag = new pebbleBag(makeBasicIntList(23), "expectedBag"); //Make a new bag with pebbles of weights 0 to 22
		int pebbleCounter = 0; //From the first pebble
		for(pebble testPebble : testBag.getBagContents()) { //For each pebble in the testBag
			assertEquals(testPebble.getWeight(), expectedBag.getBagContents().get(pebbleCounter).getWeight()); 
			//Check that each pebble in the test bag matches each pebble in the expected bag
			pebbleCounter++; //Iterate along the expected bag
		}
	}
	
	@Test
	public void testMakeBagNoFile() {
		pebbleBag testBag = pebbles.makeBag(2, "testBag", ""); //Using no file string, make a bag
		assertNull(testBag); //Check that the bag is null
	}
	
	@Test
	public void testMakeBagSmall() {
		pebbleBag testBag = pebbles.makeBag(2, "testBag", "testfilesmall.txt"); //Using testfilesmall.txt, make a bag
		assertNull(testBag); //Check that the bag is null
	}
	
	@Test
	public void testMakeBagInvalid() {
		pebbleBag testBag = pebbles.makeBag(2, "testBag", "testfilecharacter.txt"); //Using no file string, make a bag
		assertNull(testBag); //Check that the bag is null
	}
	
	@Test
	public void testDiscardToBag() {
		makeEmptyBagList(6); //Make 6 empty bags
		pebbles.player testPlayer = pebbleTester.new player("TestPlayer"); //Create a new player
		for(int pebbleLoop = 0; pebbleLoop < 2; pebbleLoop++) { //Add two pebbles to the players hand
			testPlayer.playerHand.add(new pebble(1));
		}
		testPlayer.discardToBag(); //Discard a pebble from the hand of the player
		boolean pebbleFound = false; //Note that you have not yet found the pebble
		for(int bagIndex = 3; bagIndex < 6; bagIndex++) { //For the last 3 bags in the list
			if(!pebbles.bags.get(bagIndex).isEmpty()){ //Check that the bag isn't empty
				pebbleFound = true; //If the bag is not empty, note that you have found the pebble
			}
		}
		assertTrue(pebbleFound);
	}
	
	@Test
	public void testWritePlayerFile() {
		pebbles.players = new ArrayList<pebbles.player>();
		pebbles.players.add(pebbleTester.new player("TestPlayer"));
		makeEmptyBagList(6); //Make 6 empty bags
		pebbles.bags.get(0).addPebble(new pebble(100));
		pebbles.players.get(0).drawRandom();
		try{
			File currentFile = new File("TestPlayer_Output.txt");
			Scanner fileReader = new Scanner(currentFile);
			assertEquals(fileReader.nextLine(), "TestPlayer has drawn a 100 from bag 0");
			fileReader.close();
		} catch(FileNotFoundException e) {
			System.out.println("There was an error when testing the file");
		}
	}
	
	ArrayList<Integer> makeBasicIntList(int listSize){
		ArrayList<Integer> intList = new ArrayList<Integer>();
		for(int i = 0; i < listSize; i++){ //Create an array list of integers up to the listSize
			intList.add(i);
		}
		return intList;
	}
	
	public void makeEmptyBagList(int listSize){
		for(int i = 0; i < listSize; i++){ //Add number of empty bags to the bag list equal to listSize
			pebbles.bags.add(new pebbleBag(String.valueOf(i)));
		}
	}
}
