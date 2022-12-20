package clueGame;

import java.util.*;
import java.awt.Color;

public class ComputerPlayer extends Player{
	public ComputerPlayer(String name, Color color) {
		super(name, color);
		isHumanPlayer = false;
	}

	@Override
	public Solution createSuggestion () {
		Board theInstance = Board.getInstance();
		Card room, person, weapon;
		
		// the suggestion can only happen in a room, so we do not need to check if its in a room here
		room = theInstance.getRoomCard(theInstance.getRoom(theInstance.getCell(this.getRow(), this.getCol()).getInitial()).getName());
		if (room == null) {
			Solution suggestion = new Solution();
			suggestion.select(null, null, null);
			return suggestion;
		}
		// get a person and weapon from unknown cards
		ArrayList <Card> personCards = new ArrayList<>();
		ArrayList <Card> weaponCards = new ArrayList<>();
		
		for (Card card: unknownCards) {
			if (card.getCardType().equals(CardType.PERSON)) personCards.add(card);
			else if (card.getCardType().equals(CardType.WEAPON)) weaponCards.add(card);
		}
		Random rand = new Random();
		
		// these will never have a size less than 1 because the solution will always be unknown
		person = personCards.get(rand.nextInt(personCards.size()));
		weapon = weaponCards.get(rand.nextInt(weaponCards.size()));
		
		Solution suggestion = new Solution();
		suggestion.select(room, person, weapon);
		return suggestion;
	}

	@Override
	public Card disproveSuggestion(Solution suggestion) {
		ArrayList<Card> disproveCards = new ArrayList<>();
		for (Card card: hand) {
			if (suggestion.getRoom().getCardName().equals(card.getCardName())) {
				disproveCards.add(card);
			} else if (suggestion.getPerson().getCardName().equals(card.getCardName())) {
				disproveCards.add(card);
			} else if (suggestion.getWeapon().getCardName().equals(card.getCardName())){
				disproveCards.add(card);
			} else {
				// Do nothing
			}
		}
		if (disproveCards.size() == 0) {
			return null;
		} else {
			int random = (int) (Math.random() * (disproveCards.size()));
			return disproveCards.get(random);
		}
	}
	
	@Override
	public BoardCell selectTarget(int roll) {
		Board theInstance = Board.getInstance();
		theInstance.calcTargets(theInstance.getCell(this.getRow(), this.getCol()), roll);
		BoardCell[] validTargets = theInstance.getTargets().toArray(new BoardCell[theInstance.getTargets().size()]);
		
		// Check to see if validTargets contains a room that is in unknown list
		ArrayList<BoardCell> roomTargets = new ArrayList<>();
		for (BoardCell cell: validTargets) {
			if (cell.isRoomCenter()) {
				// if the room is unknown, return that cell
				if (unknownCards.contains(theInstance.getRoom(cell.getInitial()).getName())) return cell;
				roomTargets.add(cell);
			}
		}
		
		Random rand = new Random();
		
		// choose a room cell if all rooms are known
		if (roomTargets.size() > 0) return roomTargets.get(rand.nextInt(roomTargets.size()));
		
		// if none exist, choose randomly
		return validTargets[rand.nextInt(validTargets.length)];
	}
}
