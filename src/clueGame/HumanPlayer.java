package clueGame;
import java.awt.Color;
import java.util.*;

public class HumanPlayer extends Player{
	public HumanPlayer(String name, Color color) {
		super(name, color);
		isHumanPlayer = true;
	}
	
	@Override
	public Solution createSuggestion() {
		// TODO
		return null;
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
		// TODO Auto-generated method stub
		theInstance.calcTargets(theInstance.getCell(this.row, this.col), roll);
		Set<BoardCell> validTargets = theInstance.getTargets();
		for (BoardCell cell: validTargets) cell.setTargeted(true);
		return null;
	}
}
