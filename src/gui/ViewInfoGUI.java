package gui;

import clueGame.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.util.*;

public class ViewInfoGUI extends JPanel implements QueryForInfoGUI{
	private Card[] knownCards;
	private Card[] unknownCards;
	private Card[] handCards;
	
	public ViewInfoGUI() {
		this.createWindow();
	}
	
	public void refreshWindow() {
		this.removeAll();
		this.createWindow();
	}
	
	private void createWindow() {
		// Create a layout with 3 rows
		setLayout(new GridLayout(1,3));
		JPanel wrapper = this.createKnownCards();
		add(wrapper);
		wrapper = createUnknownCards();
		add(wrapper);
		wrapper = createHandCards();
		add(wrapper);
	}

	private JPanel createHandCards() {
		handCards = theInstance.getHumanPlayers().get(0).getHand().toArray(new Card[this.getHumanPlayer().getHand().size()]);

		// create and fill JPanel
		JPanel handCardPanel = new JPanel(new GridLayout(0, 2));
		for (Card card: handCards) {
			JTextField cardTextField = this.createCardBlock(card.getCardName(), card.getCardType());
			handCardPanel.add(cardTextField);
		}
		Border border = BorderFactory.createTitledBorder("Cards In Hand");
		handCardPanel.setBorder(border);
		handCardPanel.setBackground(BACKGROUND_COLOR);
		
		return handCardPanel;
	}
	
	private JPanel createKnownCards() {
		knownCards = this.getHumanPlayer().getKnownCards().toArray(new Card[this.getHumanPlayer().getKnownCards().size()]);
		
		// create and fill JPanel
		JPanel knownCardPanel = new JPanel(new GridLayout(0, 2));
		for (Card card: knownCards) {
			JTextField cardTextField = this.createCardBlock(card.getCardName(), card.getCardType());
			knownCardPanel.add(cardTextField);
		}
		Border border = BorderFactory.createTitledBorder("Known Cards");
		knownCardPanel.setBorder(border);
		knownCardPanel.setBackground(BACKGROUND_COLOR);
		
		return knownCardPanel;
	}

	private JPanel createUnknownCards() {
		unknownCards = this.getHumanPlayer().getUnknownCards().toArray(new Card[this.getHumanPlayer().getUnknownCards().size()]);
		
		// create and fill JPanel
		JPanel unknownCardPanel = new JPanel(new GridLayout(0, 2));
		for (Card card: unknownCards) {
			JTextField cardTextField = this.createCardBlock(card.getCardName(), card.getCardType());
			unknownCardPanel.add(cardTextField);
		}
		Border border = BorderFactory.createTitledBorder("Unknown Cards");
		unknownCardPanel.setBorder(border);
		unknownCardPanel.setBackground(BACKGROUND_COLOR);
		
		return unknownCardPanel;
	}
	
	private JTextField createCardBlock(String cardName, CardType type) {
		JTextField cardTextField = new JTextField(cardName);

		// set colors
		switch (type) {
		case PERSON:
			cardTextField.setBackground(PERSON_COLOR);
			break;
		case WEAPON:
			cardTextField.setBackground(WEAPON_COLOR);
			break;
		case ROOM:
			cardTextField.setBackground(ROOM_COLOR);
			break;
		default:
			break;
		}
		
		return cardTextField;
	}
	
	public Player getHumanPlayer() {
		return theInstance.getHumanPlayers().get(0);
	}

	public Card[] getKnownCards() {
		return knownCards;
	}

	public void setKnownCards(Card[] knownCards) {
		this.knownCards = knownCards;
	}

	public Card[] getUnknownCards() {
		return unknownCards;
	}

	public void setUnknownCards(Card[] unknownCards) {
		this.unknownCards = unknownCards;
	}

	public Card[] getHandCards() {
		return handCards;
	}

	public void setHandCards(Card[] handCards) {
		this.handCards = handCards;
	}

	public void openInfoWindow() {
		// temporary JFrame for control buttons
		JFrame tempFrame = new JFrame();
		tempFrame.setTitle("View Information");
		tempFrame.setSize(600, 400);
		
		// add view info to JFrame
		tempFrame.add(this, BorderLayout.CENTER);
		tempFrame.setVisible(true);
	}
}
