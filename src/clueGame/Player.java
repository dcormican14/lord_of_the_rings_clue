package clueGame;
import java.util.*;
import java.awt.Color;
import java.awt.Graphics;
import gui.*;

public abstract class Player implements QueryForInfoGUI{
	protected ArrayList<Card> hand;
	protected Set<Card> knownCards;
	protected Set<Card> unknownCards;
	protected String name;
	protected Color color;
	protected int row;
	protected int col;
	public static int stackedPlayerBuffer = 0;
	private int shiftPlayerBuffer;
	private Solution accusationState;
	
	protected boolean lost;
	public boolean isHumanPlayer;
	
	public Player(String name, Color color) {
		knownCards = new HashSet<>();
		unknownCards = new HashSet<>();
		hand = new ArrayList<>();
		this.name = name;
		this.color = color;
		this.lost = false;
		
		shiftPlayerBuffer = stackedPlayerBuffer;
		stackedPlayerBuffer++;
	}
	
	public abstract Card disproveSuggestion(Solution suggestion);
	public abstract Solution createSuggestion();
	public abstract BoardCell selectTarget(int roll);
	
	
	public void drawCard(Card card) {
		hand.add(card);
		knownCards.add(card);
		unknownCards.remove(card);
	}
	
	public void updateKnown(Card seenCard) {
		unknownCards.remove(seenCard);
		knownCards.add(seenCard);
	}
	
	public void updateUnknown(Card unkownCard) {
		unknownCards.add(unkownCard);
	}
	
	public void setLocation(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public ArrayList<Card> getHand() {
		return hand;
	}
	
	public String getName() {
		return name;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public int getRow() {
		return row;
	}
	
	public void setRow(int row) {
		this.row = row;
	}
	
	public int getCol() {
		return col;
	}
	
	public void setCol(int col) {
		this.col = col;
	}
	
	public void setHand(ArrayList<Card> hand) {
		this.hand = hand;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Set<Card> getKnownCards() {
		return knownCards;
	}

	public void setKnownCards(Set<Card> knownCards) {
		this.knownCards = knownCards;
	}

	public Set<Card> getUnknownCards() {
		return unknownCards;
	}

	public void setUnknownCards(Set<Card> unknownCards) {
		this.unknownCards = unknownCards;
	}
	
	public void setPlayerBuffer(int buffer) {
		this.stackedPlayerBuffer = buffer;
	}
	
	public int getPlayerBuffer() {
		return this.stackedPlayerBuffer;
	}
	
	public Solution getAccusationState() {
		return this.accusationState;
	}
	
	public void setAccusationState(Solution accusationState) {
		this.accusationState = accusationState;
	}

	public void drawPlayer(Graphics g) {
		// adds player pieces
		int cellWidth = theInstance.getWidthGUI() / theInstance.getBoard().get(0).size();
		int cellHeight = theInstance.getHeightGUI() / theInstance.getBoard().size();
		int cellXLoc = this.getCol() * cellWidth;
		int cellYLoc = this.getRow() * cellHeight;
		
		int shiftPlayer = 0;
		
		for (Player player: theInstance.getPlayers()) {
			if(!player.getName().equals(this.name)) {
				if (player.getCol() == this.col && player.getRow() == this.row) {
					shiftPlayer = shiftPlayerBuffer*STACKED_BUFFER;
				}
			}
		}
		
		g.setColor(this.color);
		g.fillOval(cellXLoc+PLAYER_BUFFER + shiftPlayer, cellYLoc+PLAYER_BUFFER, cellWidth-PLAYER_BUFFER*2, cellHeight-PLAYER_BUFFER*2);
		
		g.setColor(Color.BLACK);
		g.drawOval(cellXLoc+PLAYER_BUFFER+ shiftPlayer, cellYLoc+PLAYER_BUFFER, cellWidth-PLAYER_BUFFER*2, cellHeight-PLAYER_BUFFER*2);
	}

	public void setLost(boolean lost) {
		this.lost = lost;
		
	}
	
	public boolean getLost() {
		return lost;
	}
}
