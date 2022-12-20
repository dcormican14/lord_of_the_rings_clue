package clueGame;

public class Solution {
	private Card room, person, weapon;
	
	public void select (Card room, Card person, Card weapon) {
		this.room = room;
		this.person = person;
		this.weapon = weapon;
	}
	
	public void setRoom(Card room) {
		this.room = room;
	}

	public Card getRoom() {
		return room;
	}
	
	public void setPerson(Card person) {
		this.person = person;
	}

	public Card getPerson() {
		return person;
	}
	
	public void setWeapon(Card weapon) {
		this.weapon = weapon;
	}

	public Card getWeapon() {
		return weapon;
	}
	
	@Override
	public String toString() {
		return this.person.getCardName() + ", " + this.weapon.getCardName() + ", " + this.room.getCardName();
	}
}
