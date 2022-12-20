package gui;

import clueGame.*;
import java.awt.*;
import java.util.regex.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;
import java.util.*;

public class MakeAccusationGUI extends JPanel implements QueryForInfoGUI, ActionListener{
	private Solution guess;
	private Card[] personCards;
	private Card[] weaponCards;
	private Card[] roomCards;
	
	private JTextField submissionEntry;
	private JTextField guessResult;
	
	public MakeAccusationGUI() {
		guess = new Solution();
		submissionEntry = new JTextField();
		guessResult = new JTextField();
		
		// Create a layout with 3 rows
		setLayout(new GridLayout(3,1));
		JPanel wrapper = new JPanel(new GridLayout(1,2));
		wrapper.add(this.createRoomSelection());
		wrapper.add(this.createPersonSelection());
		wrapper.add(this.createWeaponSelection());
		add(wrapper);
		
		// space between sections
		JPanel space = new JPanel();
		space.setBackground(BACKGROUND_COLOR);
		add(space);
		
		// add guess and answer fields
		JPanel wrapper2 = new JPanel(new GridLayout(2,1));
		wrapper2.add(this.createFormSubmissionButton());
		wrapper2.add(this.createGuessResultField());
		add(wrapper2);
	}

	private JPanel createRoomSelection() {
		roomCards = theInstance.getAllRoomCards().toArray(new Card[theInstance.getAllRoomCards().size()]);
		
		// create and fill JPanel
		JPanel roomCardPanel = new JPanel(new GridLayout(0, 2));
		for (Card card: roomCards) {
			JButton roomButton = this.createCardButton(card.getCardName(), card.getCardType());
			roomButton.setPreferredSize(new Dimension(75, 40));
			roomButton.addActionListener(this);
			roomButton.setActionCommand("roomButtonPressed");
			roomCardPanel.add(roomButton);
		}
		Border border = BorderFactory.createTitledBorder("Person Cards");
		roomCardPanel.setBorder(border);
		roomCardPanel.setBackground(BACKGROUND_COLOR);
		
		return roomCardPanel;
	}
	
	private JPanel createPersonSelection() {
		personCards = theInstance.getAllPersonCards().toArray(new Card[theInstance.getAllPersonCards().size()]);
		
		// create and fill JPanel
		JPanel personCardPanel = new JPanel(new GridLayout(0, 2));
		for (Card card: personCards) {
			JButton personButton = this.createCardButton(card.getCardName(), card.getCardType());
			personButton.setPreferredSize(new Dimension(75, 40));
			personButton.addActionListener(this);
			personButton.setActionCommand("personButtonPressed");
			personCardPanel.add(personButton);
		}
		Border border = BorderFactory.createTitledBorder("Person Cards");
		personCardPanel.setBorder(border);
		personCardPanel.setBackground(BACKGROUND_COLOR);
		
		return personCardPanel;
	}

	private JPanel createWeaponSelection() {
		weaponCards = theInstance.getAllWeaponCards().toArray(new Card[theInstance.getAllWeaponCards().size()]);
		
		// create and fill JPanel
		JPanel weaponCardPanel = new JPanel(new GridLayout(0, 2));
		for (Card card: weaponCards) {
			JButton weaponButton = this.createCardButton(card.getCardName(), card.getCardType());
			weaponButton.setPreferredSize(new Dimension(75, 40));
			weaponButton.addActionListener(this);
			weaponButton.setActionCommand("weaponButtonPressed");
			weaponCardPanel.add(weaponButton);
		}
		Border border = BorderFactory.createTitledBorder("Weapon Cards");
		weaponCardPanel.setBorder(border);
		weaponCardPanel.setBackground(BACKGROUND_COLOR);
		
		return weaponCardPanel;
	}
	
	
	private JPanel createFormSubmissionButton() {
		submissionEntry.setText("NO ROOM SELECTED, NO PERSON SELECTED, NO WEAPON SELECTED");
		JLabel submissionLabel = new JLabel("Current Accusation: ");
		JButton submissionButton = new JButton("Submit Accusation");
		submissionButton.setBackground(Color.BLACK);
		submissionButton.setForeground(Color.WHITE);
		submissionButton.addActionListener(this);
		submissionButton.setActionCommand("submitGuessButton");
		
		JPanel submissionPanel = new JPanel(new GridLayout(1, 3));
		submissionPanel.setBackground(BACKGROUND_COLOR);
		submissionPanel.add(submissionLabel);
		submissionPanel.add(submissionEntry);
		submissionPanel.add(submissionButton);
		
		return submissionPanel;
	}
	
	private JPanel createGuessResultField() {
		JLabel guessResultLabel = new JLabel("Accusation Result: ");
		guessResult = new JTextField();
		
		JPanel submissionPanel = new JPanel(new GridLayout(1, 2));
		submissionPanel.setBackground(BACKGROUND_COLOR);
		submissionPanel.add(guessResultLabel);
		submissionPanel.add(guessResult);
		
		return submissionPanel;
	}
	
	private JButton createCardButton(String cardName, CardType type) {
		JButton cardTextField = new JButton(cardName);

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

	public Card[] getPersonCards() {
		return personCards;
	}

	public void setPersonCards(Card[] personCards) {
		this.personCards = personCards;
	}

	public Card[] getWeaponCards() {
		return weaponCards;
	}

	public void setWeaponCards(Card[] weaponCards) {
		this.weaponCards = weaponCards;
	}

	public static void openGuessWindow() {
		// temporary JFrame for control buttons
		JFrame tempFrame = new JFrame();
		tempFrame.setTitle("Make an accusation");
		tempFrame.setSize(850, 400);
		
		// add view info to JFrame
		MakeAccusationGUI gui = new MakeAccusationGUI();
		tempFrame.add(gui, BorderLayout.CENTER);
		
		tempFrame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Pattern pattern = Pattern.compile("\\btext.*,");
		Matcher matcher = pattern.matcher(e.getSource().toString());
		matcher.find();
		
		String buttonText = matcher.group(0).substring(5, matcher.group(0).length()-1);

		switch (e.getActionCommand()) {
		case "roomButtonPressed":
			// TODO maybe fix this?
			guess.setRoom(theInstance.getRoomCard(buttonText));
			break;
		case "personButtonPressed":
			guess.setPerson(theInstance.getPersonCard(buttonText));
			break;
		case "weaponButtonPressed":
			guess.setWeapon(theInstance.getWeaponCard(buttonText));
			break;
		case "submitGuessButton":
			if (guess.getPerson() == null || guess.getWeapon() == null || guess.getRoom() == null) {
				GameControl.openErrorMessageWindow("Invalid accusation, make sure to select one room, one person, and one weapon.");
				break;
			}
			
			if (!GameControl.hasGuessed) {
				if(theInstance.checkAccusation(guess)) {
					GameControl.playSoundEffect(2);
					
					JFrame errorScreen = new JFrame();
					errorScreen.setTitle("GAME OVER");
					errorScreen.setSize(350, 100);
					errorScreen.add(new JLabel("You win!"));
					errorScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					errorScreen.setVisible(true);
				} else {
					JFrame errorScreen = new JFrame();
					errorScreen.setTitle("GAME OVER");
					errorScreen.setSize(350, 100);
					errorScreen.add(new JLabel("You lost! The answer was: " + theInstance.getSolution().toString()));
					errorScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					errorScreen.setVisible(true);
				}
			} else {
				GameControl.openErrorMessageWindow("You have already made a guess this round.");
			}
			
			break;
		default:
			break;
		}
		
		String roomSelection, personSelection, weaponSelection;
		if (guess.getRoom() != null) roomSelection = guess.getRoom().getCardName(); else roomSelection = "NO ROOM SELECTED";
		if (guess.getPerson() != null) personSelection = guess.getPerson().getCardName(); else personSelection = "NO PERSON SELECTED";
		if (guess.getWeapon() != null) weaponSelection = guess.getWeapon().getCardName(); else weaponSelection = "NO WEAPON SELECTED";
		
		submissionEntry.setText(roomSelection + ", " + personSelection + ", " + weaponSelection);
		
		this.revalidate();
		this.repaint();
	}
}
