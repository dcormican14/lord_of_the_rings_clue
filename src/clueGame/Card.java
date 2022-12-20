package clueGame;

import java.util.Random;

public class Card implements Comparable<Card>{
	private String cardName;
	private CardType cardType;
	
	public Card(String cardName, CardType cardType) {
		this.cardName = cardName;
		this.cardType = cardType;
	}
	
	//TODO
	public boolean equals(Card target) {
		return true;
	}
	

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public CardType getCardType() {
		return cardType;
	}

	public void setCardType(CardType cardType) {
		this.cardType = cardType;
	}

	@Override
	public int compareTo(Card c) {
		Random rand = new Random();
		return rand.nextInt()*1000-500;
	}
	
}
