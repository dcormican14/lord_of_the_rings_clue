package clueGame;
import java.io.*;
import java.util.*;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import gui.GameControl;
import jdk.internal.org.jline.utils.InputStreamReader;

public class Board extends JPanel {
	private static Board theInstance = new Board();
	private Solution theAnswer;
	private Player currPlayer;
	
	// holders for board objects
	private ArrayList<ArrayList<BoardCell>> gameBoard;
	private ArrayList<ComputerPlayer> computerPlayers;
	private ArrayList<HumanPlayer> humanPlayers;
	private Map<Character, Room> roomMap;
	private Set<BoardCell> targetsForMove;
	private Set<BoardCell> visitedCells;
	
	// card sets
	private TreeSet<Card> weaponCards;
	private TreeSet<Card> roomCards;
	private TreeSet<Card> personCards;
	private ArrayList<Card> deck;
	
	// setup variables
	private String layoutConfigFile;
	private String setupConfigFile;
	private char pathwayChar;
	private char outOfBoundsChar;
	
	//GUI variables
	private int heightGUI;
	private int widthGUI;

	private Board () {
		super();
	}

	public void initialize () {
		/*
		 * Initialize calls loadSetupConfig, loadLayoutConfig, checkBoardSizeConfig, linkDoorways
		 * Handles exceptions that are thrown by these functions individually.
		 */
		
		computerPlayers = new ArrayList<>();
		humanPlayers = new ArrayList<>();
		deck = new ArrayList<>();
		weaponCards = new TreeSet<>();
		personCards = new TreeSet<>();
		roomCards = new TreeSet<>();
		outOfBoundsChar = 'X';
		theAnswer = new Solution();
		
		// handles errors for loadSetupConfig
		try { 
			this.loadSetupConfig();
		} catch (Exception e) {
			System.out.println("There was an error while loading the setup configuration file. \n \t" + e);
		}
		
		// handles errors for loadLayoutConfig
		try { 
			this.loadLayoutConfig(); 
		} catch (Exception e) { 
			System.out.println("There was an error while loading the layout configuration file. \n \t" + e); 
		}
		
//		this.checkBoardSizeConfig();
		this.linkDoorways();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		JPanel board = new JPanel();
	}
	
	// check board size validity here
	public void checkBoardSizeConfig() {
		/*
		 * checks that each row is the same length. Throws an exception otherwise
		 */
		try {
			int currLength = gameBoard.get(0).size();
			for (ArrayList<BoardCell> row: gameBoard) {
				if (row.size() != currLength) throw new BadConfigFormatException("Uneven rows found in layout configuration file.");
			}
		} catch (Exception e) { 
			System.out.println("There was an error while validating configuration files. \n \t" + e); 
		}
	}
	
	// loop through built board and link doorways to rooms
	public void linkDoorways () { 
		/*
		 * Adds the door adjacencies for each room.
		 */
		for (int row=0; row<gameBoard.size(); row++) {
			for (int col=0; col<gameBoard.get(0).size(); col++) {
				switch (gameBoard.get(row).get(col).getDoorDirection()) {
					case UP:
						roomMap.get(gameBoard.get(row-1).get(col).getInitial()).addDoor(gameBoard.get(row).get(col));
						break;
					case DOWN:
						roomMap.get(gameBoard.get(row+1).get(col).getInitial()).addDoor(gameBoard.get(row).get(col));
						break;
					case LEFT:
						roomMap.get(gameBoard.get(row).get(col-1).getInitial()).addDoor(gameBoard.get(row).get(col));
						break;
					case RIGHT:
						roomMap.get(gameBoard.get(row).get(col+1).getInitial()).addDoor(gameBoard.get(row).get(col));
						break;
					default:
						break;
				}
			}
		}
	}

	public void loadSetupConfig () throws BadConfigFormatException {
		/*
		 * Reads the setup text file from the data folder
		 * Adds rooms, their corresponding initial characters, and sets the pathway character, 
		 * Adds player, and weapon cards
		 * 
		 * LAYOUT FOR CONFIG FILE:
		 * 
		 * Room, name, char
		 * ...
		 * Room, name, char
		 * Space, name, char (out of bounds tiles use the character 'X')
		 * 
		 * PEOPLE
		 * Person, color, row, col
		 * ...
		 * Person, color, row, col
		 * 
		 * WEAPONS
		 * Weapon
		 * ...
		 * Weapon
		 */
		roomMap = new HashMap<Character, Room>();
		String line;
		boolean load_rooms = true;
		boolean load_people = false;
		boolean load_weapons = false;
		
		// In case there is an IO exception while reading the file
		try {
			BufferedReader setup = new BufferedReader( new InputStreamReader(this.getClass().getResourceAsStream("/data/" + setupConfigFile)));
			while ((line = setup.readLine()) != null) {
				if (line.equals(null) || (line.charAt(0) == line.charAt(1) && line.charAt(0) == '/')) continue;
				String[] split_line = line.split(",");
				if (load_rooms) {
					// check to see if we are at the next section
					if (line.equals("PEOPLE")) {
						load_rooms = false;
						load_people = true;
					} else {
						if (!split_line[0].toLowerCase().equals("room") && !split_line[0].toLowerCase().equals("space")) {
							throw new BadConfigFormatException("Invalid room type. Room type: " + split_line[0].toLowerCase() + " not found.");
						}
						if (split_line[0].toLowerCase().equals("space") && split_line[2].charAt(1) != outOfBoundsChar) {
							pathwayChar = split_line[2].charAt(1); // Sets the character for general walkways
						}
						if (split_line[0].toLowerCase().equals("room")) {
							roomCards.add(new Card(split_line[1].substring(1), CardType.ROOM));	//add all rooms to the room cards
						}
						roomMap.put(split_line[2].charAt(1), new Room(split_line[1].substring(1))); // this assumes space after each comma
					}
				} else if (load_people) {
					// check to see if we are at the next section
					if (line.equals("WEAPONS")) {
						load_people = false;
						load_weapons = true;
						continue;
					} else {
						personCards.add(new Card(split_line[0], CardType.PERSON));
						ComputerPlayer npc = new ComputerPlayer(split_line[0], this.getColor(split_line[1]));
						npc.setLocation(Integer.parseInt(split_line[2]), Integer.parseInt(split_line[3]));
						computerPlayers.add(npc);
					}
				} else if (load_weapons) {
					weaponCards.add(new Card(split_line[0], CardType.WEAPON));
				} else {
					throw new BadConfigFormatException("Error while parsing config file. Headers are incorrect.");
				}
			}
			
			// pick a random computer player for the human to play
			Random rand = new Random();
			ComputerPlayer removedCPU = computerPlayers.remove(rand.nextInt(computerPlayers.size()));
			HumanPlayer pc = new HumanPlayer(removedCPU.getName(), removedCPU.getColor());
			pc.setLocation(removedCPU.getRow(), removedCPU.getCol());
			humanPlayers.add(pc);
			
		} catch (FileNotFoundException e) {
			throw new BadConfigFormatException("Error while attempting to read the file. File path may be incorrect."+ e);
		} catch (IOException e) {
			throw new BadConfigFormatException("Error while attempting to read the file. File may not be formatted correctly."+ e);
		} catch (Exception e) {
			throw new BadConfigFormatException(e.toString());
		}
	}
	
	public Color getColor(String col) {
		Color color = Color.WHITE;
	    switch (col.toLowerCase()) {
	    case "black":
	        color = Color.BLACK;
	        break;
	    case "blue":
	        color = Color.BLUE;
	        break;
	    case "cyan":
	        color = Color.CYAN;
	        break;
	    case "darkgray":
	        color = Color.DARK_GRAY;
	        break;
	    case "gray":
	        color = Color.GRAY;
	        break;
	    case "green":
	        color = Color.GREEN;
	        break;
	    case "yellow":
	        color = Color.YELLOW;
	        break;
	    case "lightgray":
	        color = Color.LIGHT_GRAY;
	        break;
	    case "magneta":
	        color = Color.MAGENTA;
	        break;
	    case "orange":
	        color = Color.ORANGE;
	        break;
	    case "pink":
	        color = Color.PINK;
	        break;
	    case "red":
	        color = Color.RED;
	        break;
	    case "white":
	        color = Color.WHITE;
	        break;
	        }
	    return color;
	   }

	public void loadLayoutConfig () throws BadConfigFormatException {
		/*
		 * Reads the layout csv file from the data folder
		 * Adds each cell to the board, checks to see that the length is valid
		 */
		gameBoard = new ArrayList<>();
		int maxLength = -1;
		String line;
		
		try {
			BufferedReader setup = new BufferedReader( new InputStreamReader(this.getClass().getResourceAsStream("/data/" + layoutConfigFile)));
			int i = 0;
			while ((line = setup.readLine()) != null) {
				String[] roomCellInfo = line.split(",");
				
				// sets the board cells based on csv file
				// also sets the room cell information
				ArrayList<BoardCell> roomCells = new ArrayList<>();
				for (int j=0; j<roomCellInfo.length; j++) {
					BoardCell currCell = new BoardCell(i, j);
					currCell.setDoorDirection(DoorDirection.NONE);
					currCell.setCenter(false);
					
					if (roomMap.containsKey(roomCellInfo[j].charAt(0))) {
						if (roomCellInfo[j].charAt(0) != outOfBoundsChar  && roomCellInfo[j].charAt(0) != pathwayChar) {
							currCell.setRoom(true);
						} else {
							currCell.setRoom(false);
						}
						currCell.setInitial(roomCellInfo[j].charAt(0));
						currCell.setRoom(this.roomMap.get(currCell.getInitial()));
					} else {
						throw new BadConfigFormatException("Error while loading layout configuration file. " + roomCellInfo[j].charAt(0) + " is not a valid room key");
					}
					if (roomCellInfo[j].length() > 1) {
						setRoomClassVars(roomCellInfo[j], currCell);
					}
					roomCells.add(currCell);
				}
				
				// checks the length of each row in the layout config file
				if (maxLength == -1) {
					maxLength = roomCellInfo.length;
				} else if (maxLength != roomCellInfo.length) {
					throw new BadConfigFormatException();
				}
				
				gameBoard.add(roomCells);
				i++;
			}
		} catch (FileNotFoundException e) {
			throw new BadConfigFormatException("Error while attempting to read the file. File path may be incorrect."+ e);
		} catch (IOException e) {
			throw new BadConfigFormatException("Error while attempting to read the file. File may not be formatted correctly."+ e);
		} catch (Exception e) {
			throw new BadConfigFormatException();
		}
	}
	
	public void setRoomClassVars (String singleCellInfo, BoardCell currCell) {
		/*
		 * Sets the door direction for each door cell and adds the cell to the room local variables if it matters to that room
		 */
		switch (singleCellInfo.charAt(1)) {
		case '>':
			currCell.setDoorway(true);
			currCell.setDoorDirection(DoorDirection.RIGHT);
			break;
		case '<':
			currCell.setDoorway(true);
			currCell.setDoorDirection(DoorDirection.LEFT);
			break;
		case 'v':
			currCell.setDoorway(true);
			currCell.setDoorDirection(DoorDirection.DOWN);
			break;
		case '^':
			currCell.setDoorway(true);
			currCell.setDoorDirection(DoorDirection.UP);
			break;
		case '*':
			roomMap.get(singleCellInfo.charAt(0)).setCenterCell(currCell);
			currCell.setCenter(true);
			break;
		case '#':
			roomMap.get(singleCellInfo.charAt(0)).setLabelCell(currCell);
			currCell.setLabel(true);
			break;
		default:
			roomMap.get(singleCellInfo.charAt(0)).setSecretCell(currCell);
			currCell.setSecretPassage(singleCellInfo.charAt(1));
			break;
		}
	}

	public void calcTargets (BoardCell startCell, int pathLength) {
		/*
		 * Checks each cell adjacent to target cell and adds them to the list recursively
		 * If the cell is not a room and isn't occupied, then it is returned
		 */
		targetsForMove = new HashSet<BoardCell>();
		visitedCells = new HashSet<BoardCell>();
		visitedCells.add(startCell);
		recurOverTargets(startCell, pathLength);
	}

	private void recurOverTargets (BoardCell startCell, int pathLength) {
		/*
		 * Loops over the specified cell and adds all valid adjacencies
		 * Ends if pathLength is 0 or if target is a room
		 */
		addCellAdjacencies(startCell);
		for (BoardCell cell: startCell.getAdjList()) {
			if (!visitedCells.contains(cell)) {
				visitedCells.add(cell);
				// if the cell is a room, then we stop movement and add it as the last remaining move
				if (cell.isRoomCenter()) {
					targetsForMove.add(cell);
				} else if (! cell.isOccupied()) {
					if (pathLength == 1) { // if we are out of moves, then we add the last move made
						targetsForMove.add(cell);
					} else {
						recurOverTargets(cell, pathLength-1);
					}
				}
				
				visitedCells.remove(cell);
			}
		}
	}
	
	public void deal() {
		// adds the full knowledge for each player
		this.addPlayerKnowledge();
		
		// the treesets are pre-randomized using the card comparables
		// removes the cards when they are accessed
		theAnswer.select(roomCards.pollFirst(), personCards.pollFirst(), weaponCards.pollFirst());
		
		// deal each card to each player
		deck.addAll(roomCards);
		deck.addAll(personCards);
		deck.addAll(weaponCards);
		
		ArrayList<Card> drawableDeck = (ArrayList<Card>)deck.clone();
		while (drawableDeck.size() > 0) {
			for (Player player: this.getPlayers()) {
				player.drawCard(drawableDeck.remove(0));
				if (drawableDeck.size() <= 0) break;
			}
		}
	}
	
	public void addPlayerKnowledge() {
		ArrayList<Card> allCards = new ArrayList<>();
		allCards.addAll(roomCards);
		allCards.addAll(personCards);
		allCards.addAll(weaponCards);
		
		for (Player player: this.getPlayers()) {
			for (Card unknown: allCards) {
				player.updateUnknown(unknown);
			}
		}
	}
	
	public boolean checkAccusation(Solution suggestion) {
		if (suggestion.getRoom() == theAnswer.getRoom()) {
			if (suggestion.getPerson() == theAnswer.getPerson()) {
				if (suggestion.getWeapon() == theAnswer.getWeapon()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public Card handleSuggestion(Solution suggestion, Player suggestingPlayer, ArrayList<Player> players) {
		for (int i = players.indexOf(suggestingPlayer) + 1; i < ((6 + players.indexOf(suggestingPlayer)) - 1); i++) {
			Card potentialDisprove = players.get(i % 6).disproveSuggestion(suggestion);
			if (potentialDisprove == null) {
				continue;
			} else {
				players.get(i % 6).updateKnown(potentialDisprove);
				return potentialDisprove;
			}
		}
		return null;
	}
 
	private void addCellAdjacencies (BoardCell targetCell) {
		/*
		 * This must be done in the board class because it knows where all of the cells are and can access information accross cells
		 * Adds the valid adjaent cells
		 */
		int row = targetCell.getRow();
		int col = targetCell.getCol();
		
		// adds the room center as an adjacent cell if the target is a doorway 
		if (targetCell.isDoorway()) {
			switch (targetCell.getDoorDirection()) {
				case UP:
					targetCell.addAdjacency(roomMap.get(gameBoard.get(row-1).get(col).getInitial()).getCenterCell());
					break;
				case DOWN:
					targetCell.addAdjacency(roomMap.get(gameBoard.get(row+1).get(col).getInitial()).getCenterCell());
					break;
				case LEFT:
					targetCell.addAdjacency(roomMap.get(gameBoard.get(row).get(col-1).getInitial()).getCenterCell());
					break;
				case RIGHT:
					targetCell.addAdjacency(roomMap.get(gameBoard.get(row).get(col+1).getInitial()).getCenterCell());
					break;
				default:
					break;
			}
		}
		
		// adds the room center as an adjacent cell if the target is a room center
		// if it is, add doorways and secret passages
		if (targetCell.isRoomCenter()) {
			for (BoardCell doorway: roomMap.get(targetCell.getInitial()).getDoorList()) {
				targetCell.addAdjacency(doorway);
			}
			if (roomMap.get(targetCell.getInitial()).hasSecretCell()) {
				Room secretPassageTarget = roomMap.get(roomMap.get(targetCell.getInitial()).getSecretCell().getSecretPassage());
				targetCell.addAdjacency(secretPassageTarget.getCenterCell());
			}
			
		} else { // if the target is not a room center, then it adds the adjacent cells to the target, provided that they aren't room cells
			if (row > 0 && !gameBoard.get(row-1).get(col).isRoom() && gameBoard.get(row-1).get(col).getInitial() != outOfBoundsChar) {
				targetCell.addAdjacency(gameBoard.get(row-1).get(col));
			}
			if (row < gameBoard.size()-1 && !gameBoard.get(row+1).get(col).isRoom() && gameBoard.get(row+1).get(col).getInitial() != outOfBoundsChar) {
				targetCell.addAdjacency(gameBoard.get(row+1).get(col));
			}
			if (col > 0 && !gameBoard.get(row).get(col-1).isRoom() && gameBoard.get(row).get(col-1).getInitial() != outOfBoundsChar) {
				targetCell.addAdjacency(gameBoard.get(row).get(col-1));
			}
			if (col < gameBoard.get(0).size()-1 && !gameBoard.get(row).get(col+1).isRoom() && gameBoard.get(row).get(col+1).getInitial() != outOfBoundsChar) {
				targetCell.addAdjacency(gameBoard.get(row).get(col+1));
			}
		}
	}
	
	// For testing
	public void defaultSetup() {
		// sets game board and loads config files
		theInstance.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		theInstance.initialize();
		theInstance.deal();
	}

	// Variable setter methods
	public void setConfigFiles (String layout, String setup) {
		this.layoutConfigFile = layout;
		this.setupConfigFile = setup;
	}
	
	// Variable getter methods
	public BoardCell getCell (int row, int col) {
		return gameBoard.get(row).get(col);
	}

	public Room getRoom (char roomKey) {
		if (!roomMap.isEmpty()) {
			return roomMap.get(roomKey);
		} else {
			return new Room ();
		}
		
	}

	public Room getRoom (BoardCell cell) {
		return roomMap.get(cell.getInitial());
	}

	public int getNumRows() {
		return gameBoard.size();
	}

	public int getNumColumns() {
		return gameBoard.get(0).size();
	}

	public Set<BoardCell> getAdjList(int i, int j) {
		this.addCellAdjacencies(gameBoard.get(i).get(j));
		return gameBoard.get(i).get(j).getAdjList();
	}

	public Set<BoardCell> getTargets() {
		return targetsForMove;
	}
	
	public static Board getInstance () {
		return theInstance;
	}
	
	public ArrayList<Card> getDeck() {
		return deck;
	}
	
	public ArrayList<Player> getPlayers() {
		ArrayList <Player> allPlayers = new ArrayList<>();
		allPlayers.addAll(humanPlayers);
		allPlayers.addAll(computerPlayers);
		return allPlayers;
	}
	
	public ArrayList<ComputerPlayer> getComputerPlayers() {
		return computerPlayers;
	}
	
	public ArrayList<HumanPlayer> getHumanPlayers() {
		return humanPlayers;
	}
	
	public Solution getSolution() {
		return theAnswer;
	}

	public TreeSet<Card> getWeaponCards() {
		return weaponCards;
	}
	
	public Card getWeaponCard(String weaponCardName) {
		for (Card card: weaponCards) {
			if (card.getCardName().equals(weaponCardName)) return card;
		}
		if (theAnswer.getWeapon().getCardName().equals(weaponCardName)) return theAnswer.getWeapon();
		else return null;
	}
	
	public TreeSet<Card> getAllWeaponCards() {
		TreeSet<Card>allWeaponCards = (TreeSet)weaponCards.clone();
		allWeaponCards.add(theAnswer.getWeapon());
		return allWeaponCards;
	}

	public TreeSet<Card> getRoomCards() {
		return roomCards;
	}
	
	public Card getRoomCard(String roomCardName) {
		for (Card card: roomCards) {
			if (card.getCardName().equals(roomCardName)) return card;
		}
		if (theAnswer.getRoom().getCardName().equals(roomCardName)) return theAnswer.getRoom();
		else return null;
	}
	
	public TreeSet<Card> getAllRoomCards() {
		TreeSet<Card>allRoomCards = (TreeSet)roomCards.clone();
		allRoomCards.add(theAnswer.getRoom());
		return allRoomCards;
	}

	public TreeSet<Card> getPersonCards() {
		return personCards;
	}
	
	public Card getPersonCard(String personCardName) {
		for (Card card: personCards) {
			if (card.getCardName().equals(personCardName)) return card;
		}
		if (theAnswer.getPerson().getCardName().equals(personCardName)) return theAnswer.getPerson();
		else return null;
	}
	
	public TreeSet<Card> getAllPersonCards() {
		TreeSet<Card>allPersonCards = (TreeSet)personCards.clone();
		allPersonCards.add(theAnswer.getPerson());
		return allPersonCards;
	}
	
	public void setCurrentPlayer(Player currPlayer) {
		this.currPlayer = currPlayer;
	}
	
	public Player getCurrentPlayer() {
		return currPlayer;
	}
	
	public ArrayList<ArrayList<BoardCell>> getBoard() {
		return gameBoard;
	}

	public void setWindowSize(int height, int width) {
		heightGUI = height;
		widthGUI = width;
	}
	
	public int getWidthGUI() {
		return widthGUI;
	}
	
	public int getHeightGUI() {
		return heightGUI;
	}
}
