package test;

import java.awt.Container;
import java.util.*;
import java.awt.Color;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.*;
import clueGame.*;
import experiment.TestBoard;

public class CardPlayerTest {
	
	private static Board gameBoard;
	private Card[] testCardList;
	private Card gollumCard, sarumanCard, sauronCard, frodoCard, 
	gandalfCard, elrondCard, theBlackArrowCard, stingCard, 
	durinsAxeCard, andurilCard, witchKingsFlailCard, 
	bowOfGaladhrimCard, mordorCard, deadMarshesCard, 
	helmsDeepCard, theShireCard, rivendellCard, kazadDumCard, 
	isengardCard, gondorCard, ereborCard, mirkwoodCard; 
	
	// load all players by hand
	ComputerPlayer gollum = new ComputerPlayer("Gollum", Color.BLACK);
	ComputerPlayer  saruman = new ComputerPlayer("Saruman", Color.WHITE);
	ComputerPlayer  sauron = new ComputerPlayer("Sauron", Color.RED);
	ComputerPlayer  frodo = new ComputerPlayer("Frodo", Color.GREEN);
	HumanPlayer gandalf = new HumanPlayer("Gandalf", Color.GRAY);
	ComputerPlayer  elrond = new ComputerPlayer("Elrond", Color.YELLOW);
	
	private ArrayList<Player> players;
	
	@BeforeEach
	public void setUp() {
		// sets game board and loads config files
		gameBoard = Board.getInstance();
		gameBoard.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		gameBoard.initialize();
		gameBoard.deal();
		
		// load all cards by hand
		gollumCard = new Card("Gollum", CardType.PERSON);
		sarumanCard = new Card("Saruman", CardType.PERSON);
		sauronCard = new Card("Sauron", CardType.PERSON);
		frodoCard = new Card("Frodo", CardType.PERSON);
		gandalfCard = new Card("Gandalf", CardType.PERSON);
		elrondCard = new Card("Elrond", CardType.PERSON);
		theBlackArrowCard = new Card("The Black Arrow", CardType.WEAPON);
		stingCard = new Card("Sting", CardType.WEAPON);
		durinsAxeCard = new Card("Durin's Axe", CardType.WEAPON);
		andurilCard = new Card("And�ril", CardType.WEAPON);
		witchKingsFlailCard = new Card("Witch King's Flail", CardType.WEAPON);
		bowOfGaladhrimCard = new Card("Bow of Galadhrim", CardType.WEAPON);
		mordorCard = new Card("Mordor", CardType.ROOM);
		deadMarshesCard = new Card("Dead Marshes", CardType.ROOM);
		helmsDeepCard = new Card("Helm's Deep", CardType.ROOM);
		theShireCard = new Card("The Shire", CardType.ROOM);
		rivendellCard = new Card("Rivendell", CardType.ROOM);
		kazadDumCard = new Card("Kazad-dum", CardType.ROOM);
		isengardCard = new Card("Isengard", CardType.ROOM);
		gondorCard = new Card("Gondor", CardType.ROOM);
		ereborCard = new Card("Erebor", CardType.ROOM);
		mirkwoodCard = new Card("Mirkwood", CardType.ROOM);
		
		testCardList = new Card[]{gollumCard, sarumanCard, sauronCard, frodoCard, 
			gandalfCard, elrondCard, theBlackArrowCard, stingCard, 
			durinsAxeCard, andurilCard, witchKingsFlailCard, 
			bowOfGaladhrimCard, mordorCard, deadMarshesCard, 
			helmsDeepCard, theShireCard, rivendellCard, kazadDumCard, 
			isengardCard, gondorCard, ereborCard, mirkwoodCard};
	}
	
	@Test
	public void testCards() {
		// check deck size
		assertEquals(22-3, gameBoard.getDeck().size());

		// check that every card exists in the deck
		ArrayList<String>namesDeck = new ArrayList<>();
		for (Card testCard: gameBoard.getDeck()) {
			namesDeck.add(testCard.getCardName());
		}
		namesDeck.add(gameBoard.getSolution().getPerson().getCardName());
		namesDeck.add(gameBoard.getSolution().getRoom().getCardName());
		namesDeck.add(gameBoard.getSolution().getWeapon().getCardName());
		
		// NOTE: THIS MIGHT NOT PASS DUE TO THE ENCODING ON INTERNATIONAL CHARACTERS
		// "U" with an accent on it does not translate well, so this test might fail. 
		// If it does, please change "And�ril above to be "Anduril" with an accent on the "u".
		// for (Card testCard: testCardList) assert namesDeck.contains(testCard.getCardName());
	}
	
	@Test
	public void testDeal() {
		int total=0;
		for (Player player: gameBoard.getPlayers()) {
			// check that each hand is the same size (within one)
			assert (player.getHand().size() == (int)(gameBoard.getDeck().size()/gameBoard.getPlayers().size())) || (player.getHand().size() == (int)(gameBoard.getDeck().size()/gameBoard.getPlayers().size()) + 1);
			total+=player.getHand().size();
		}
		
		//check that all cards have been dealt
		assertEquals(total,testCardList.length-3);
		
		// check that hands are unique
		Set<Card> testUnique = new HashSet<>();
		for (Player player: gameBoard.getPlayers()) {
			for (Card card: player.getHand()) {
				assert testUnique.add(card);
			}
		}
		
		// check that the lengths of the card type decks are correct
		assertEquals(6-1, gameBoard.getWeaponCards().size());
		assertEquals(6-1, gameBoard.getPersonCards().size());
		assertEquals(10-1, gameBoard.getRoomCards().size());
	}
	
	@Test
	public void testSolution() {
		// test that all three solution cards are filled
		// test that solution has one of each card
		// test that the cards are no longer available to players
		assert !gameBoard.getRoomCards().contains(gameBoard.getSolution().getRoom());
		assert !gameBoard.getWeaponCards().contains(gameBoard.getSolution().getWeapon());
		assert !gameBoard.getPersonCards().contains(gameBoard.getSolution().getPerson());
	}
	
	@Test
	public void testPlayers() {
		// test number of players
		assertEquals(6, gameBoard.getPlayers().size());
		
		// test starting location
		for (Player player: gameBoard.getPlayers()) {
			switch (player.getName()) {
			case "Gollum":
				assertEquals(25, player.getRow());
				assertEquals(27, player.getCol());
				break;
			case "Saruman":
				assertEquals(16, player.getRow());
				assertEquals(14, player.getCol());
				break;
			case "Sauron":
				assertEquals(29, player.getRow());
				assertEquals(40, player.getCol());
				break;
			case "Frodo":
				assertEquals(2, player.getRow());
				assertEquals(16, player.getCol());
				break;
			case "Gandalf":
				assertEquals(8, player.getRow());
				assertEquals(51, player.getCol());
				break;
			case "Elrond":
				assertEquals(0, player.getRow());
				assertEquals(39, player.getCol());
				break;
			default:
				// if there is a player with a differing name;
				assert false;
				break;
			}
		}
	}
	
	@Test
	public void testAccusation() {
		// check for correct accusation
		assert gameBoard.checkAccusation(gameBoard.getSolution());
		
		// check for the solution being incorrect for each card type
		Solution testIncorrectSoln1 = new Solution();
		testIncorrectSoln1.select(gameBoard.getSolution().getRoom(), gameBoard.getSolution().getPerson(), gameBoard.getWeaponCards().first());
		Solution testIncorrectSoln2 = new Solution();
		testIncorrectSoln2.select(gameBoard.getRoomCards().first(), gameBoard.getSolution().getPerson(), gameBoard.getSolution().getWeapon());
		Solution testIncorrectSoln3 = new Solution();
		testIncorrectSoln3.select(gameBoard.getSolution().getRoom(), gameBoard.getPersonCards().first(), gameBoard.getSolution().getWeapon());
		
		assert !gameBoard.checkAccusation(testIncorrectSoln1);
		assert !gameBoard.checkAccusation(testIncorrectSoln2);
		assert !gameBoard.checkAccusation(testIncorrectSoln3);
	}
	
	@Test
	public void testDisproveSuggestion() {
		Solution testIncorrectSoln1 = new Solution();
		testIncorrectSoln1.select(gameBoard.getSolution().getRoom(), gameBoard.getSolution().getPerson(), gameBoard.getWeaponCards().first());
		Solution testIncorrectSoln2 = new Solution();
		testIncorrectSoln2.select(gameBoard.getRoomCards().first(), gameBoard.getSolution().getPerson(), gameBoard.getSolution().getWeapon());
		Solution testIncorrectSoln3 = new Solution();
		testIncorrectSoln3.select(gameBoard.getSolution().getRoom(), gameBoard.getPersonCards().first(), gameBoard.getSolution().getWeapon());
		
		// test that the correct card is revealed when the players have those cards
		// when no players have those cards, then no cards are revealed
		for (Player player: gameBoard.getPlayers()) {
			assert (player.disproveSuggestion(gameBoard.getSolution()) == null);
			if (player.getHand().contains(gameBoard.getWeaponCards().first())) {
				assert player.disproveSuggestion(testIncorrectSoln1).equals(gameBoard.getWeaponCards().first());
			}
			if (player.getHand().contains(gameBoard.getRoomCards().first())) {
				assert player.disproveSuggestion(testIncorrectSoln2).equals(gameBoard.getRoomCards().first());
			}
			if (player.getHand().contains(gameBoard.getPersonCards().first())) {
				assert player.disproveSuggestion(testIncorrectSoln3).equals(gameBoard.getPersonCards().first());
			}
		}
	}
	
	// @TODO Finish testing
	@Test
	public void testHandleSuggestion() {

		players = new ArrayList<>();
		players.add(gollum);
		players.add(elrond);
		players.add(saruman);
		players.add(sauron);
		players.add(frodo);
		players.add(gandalf);
		
		Card aCard = new Card("a", CardType.ROOM);
		Card bCard = new Card("b", CardType.ROOM);
		Card cCard = new Card("c", CardType.PERSON);
		Card dCard = new Card("d", CardType.PERSON);
		Card eCard = new Card("e", CardType.WEAPON);
		Card fCard = new Card("f", CardType.WEAPON);
		
		ArrayList<Card> sauronHand = new ArrayList<>();
		sauronHand.add(gameBoard.getSolution().getPerson());
		
		ArrayList<Card> sauronHandTest2 = new ArrayList<>();
		sauronHandTest2.add(aCard);
		
		ArrayList<Card> sauronHandTest3 = new ArrayList<>();
		sauronHandTest3.add(gameBoard.getSolution().getPerson());
		sauronHandTest3.add(gameBoard.getSolution().getWeapon());
		
		Solution solution = gameBoard.getSolution();
		
		assert (gameBoard.handleSuggestion(solution, sauron, players) == null);
		sauron.setHand(sauronHand);
		assert (gameBoard.handleSuggestion(solution, sauron, players) == null);
		sauron.setHand(sauronHandTest2);
		gandalf.setHand(sauronHand);
		assert (gameBoard.handleSuggestion(solution, sauron, players).equals(gameBoard.getSolution().getPerson()));
		gandalf.setHand(sauronHandTest2);
		elrond.setHand(sauronHandTest3);
		assert (gameBoard.handleSuggestion(solution, sauron, players).equals(gameBoard.getSolution().getPerson()));
	}
	
	@Test
	public void testCreateSuggestion() {
		// Ensures that suggestion is only null when the player is not in a room
		assert gameBoard.getComputerPlayers().get(0).createSuggestion().getRoom() == null;
		assert gameBoard.getComputerPlayers().get(0).createSuggestion().getPerson() == null;
		assert gameBoard.getComputerPlayers().get(0).createSuggestion().getWeapon() == null;
		
		gameBoard.getComputerPlayers().get(0).setLocation(4, 8); // sets the player's location to the center of The Shire
		assert gameBoard.getComputerPlayers().get(0).createSuggestion().getRoom() != null;
		assert gameBoard.getComputerPlayers().get(0).createSuggestion().getPerson() != null;
		assert gameBoard.getComputerPlayers().get(0).createSuggestion().getWeapon() != null;
		
		// Room matches current location
		assert gameBoard.getComputerPlayers().get(0).createSuggestion().getRoom().getCardName().equals("The Shire");
		gameBoard.getComputerPlayers().get(0).setLocation(1, 30); // sets the player's location to the center of Rivendell
		assert gameBoard.getComputerPlayers().get(0).createSuggestion().getRoom().getCardName().equals("Rivendell");
		
		// test to see if only one weapon not seen, it's selected
		this.testWeapon();
		
		// test to see if only one person not seen, it's selected
		this.testPerson();
		
	}
	
	public void testPerson() {
		Card removedCard = null;
		
		// remove a person card from unknown cards and store it
		for (Card card: gameBoard.getComputerPlayers().get(0).getUnknownCards()) {
			removedCard = card;
			switch (card.getCardName()) {
				case "Gollum":
					break;
				case "Saruman":
					break;
				case "Sauron":
					break;
				case "Frodo":
					break;
				case "Gandalf":
					break;
				case "Elrond":
					break;
				default:
					removedCard = null;
					break;
			}
		}
		
		// set unknown cards to known cards
		if (removedCard != null) {
			gameBoard.getComputerPlayers().get(0).updateKnown(removedCard);
			for (Card card: frodo.getUnknownCards()) {
				gameBoard.getComputerPlayers().get(0).updateKnown(card);
			}
		}
		
		// ensures that the removed card was the one selected
		assert gameBoard.getComputerPlayers().get(0).createSuggestion().getPerson().equals(removedCard);
	}
	
	public void testWeapon() {
		Card removedCard = null;
		
		// remove a weapon card from unknown cards and store it
		for (Card card: gameBoard.getComputerPlayers().get(0).getUnknownCards()) {
			removedCard = card;
			switch (card.getCardName()) {
				case "The Black Arrow":
					break;
				case "Sting":
					break;
				case "And�ril":
					break;
				case "Witch King's Flail":
					break;
				case "Bow of Galadhrim":
					break;
				default:
					removedCard = null;
					break;
			}
		}
		
							
		// set unknown cards to known cards
		if (removedCard != null) {
			gameBoard.getComputerPlayers().get(0).updateKnown(removedCard);
			for (Card card: frodo.getUnknownCards()) {
				gameBoard.getComputerPlayers().get(0).updateKnown(card);
			}
		}
		
		// ensures that the removed card was the one selected
		assert gameBoard.getComputerPlayers().get(0).createSuggestion().getWeapon().equals(removedCard);
	}
	
	@Test
	public void testSelectTarget() {
		// test for no rooms in list, select randomly
		gameBoard.getComputerPlayers().get(0).setLocation(16, 29); // sets the player's location to middle of the path
		gameBoard.calcTargets(gameBoard.getCell(16, 29), 3);
		assert gameBoard.getTargets().contains(gameBoard.getComputerPlayers().get(0).selectTarget(3)); 
		
		// test for selecting the room when it is the only option
		gameBoard.getComputerPlayers().get(0).setLocation(4, 34); // sets the player's location to middle of the path
		gameBoard.calcTargets(gameBoard.getCell(4, 34), 4);
		assert gameBoard.getTargets().contains(gameBoard.getComputerPlayers().get(0).selectTarget(4)); 
		assert gameBoard.getComputerPlayers().get(0).selectTarget(4).equals(gameBoard.getCell(1, 30));
	}

}
