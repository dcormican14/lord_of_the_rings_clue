package gui;

import clueGame.*;
import java.awt.*;
import java.util.regex.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;
import java.util.*;

public class MakeGuessGUI extends JPanel implements QueryForInfoGUI, ActionListener{
	private Solution guess;
	private String roomCardName;
	private Card[] personCards;
	private Card[] weaponCards;
	
	private JTextField submissionEntry;
	private JTextField guessResult;
	
	public MakeGuessGUI() {
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
	
	// generate the one possible room card
	private JPanel createRoomSelection() {
		// gets the name of the room where the current player is
		roomCardName = theInstance.getRoom(theInstance.getCell(GameControl.currPlayer.getRow(), GameControl.currPlayer.getCol())).getName();

		// create and fill JPanel
		JPanel roomCardPanel = new JPanel(new GridLayout(2, 1));
		
		JButton roomButton = new JButton(roomCardName);
		roomButton.setPreferredSize(new Dimension(75, 40));
		roomButton.setBackground(ROOM_COLOR);
		roomButton.addActionListener(this);
		roomButton.setActionCommand("roomButtonPressed");
		roomCardPanel.add(roomButton);
		
		Border border = BorderFactory.createTitledBorder("Room Card");
		roomCardPanel.setBorder(border);
		roomCardPanel.setBackground(BACKGROUND_COLOR);
		
		return roomCardPanel;
	}
	
	//generate all possible person cards
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
	
	// generate all possible weapon cards
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
	
	// creates guess submission form
	private JPanel createFormSubmissionButton() {
		submissionEntry.setText("NO ROOM SELECTED, NO PERSON SELECTED, NO WEAPON SELECTED");
		JLabel submissionLabel = new JLabel("Current Guess: ");
		JButton submissionButton = new JButton("Submit Guess");
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
	
	// creates the display for guess results
	private JPanel createGuessResultField() {
		JLabel guessResultLabel = new JLabel("Guess Result: ");
		guessResult = new JTextField();
		
		JPanel submissionPanel = new JPanel(new GridLayout(1, 2));
		submissionPanel.setBackground(BACKGROUND_COLOR);
		submissionPanel.add(guessResultLabel);
		submissionPanel.add(guessResult);
		
		return submissionPanel;
	}
	
	// generating card buttons, colors, and texts
	private JButton createCardButton(String cardName, CardType type) {
		JButton cardTextField = new JButton(cardName);
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

	public String getRoomCardName() {
		return roomCardName;
	}

	public void setRoomCardName(String roomCardName) {
		this.roomCardName = roomCardName;
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
		tempFrame.setTitle("Make a Guess");
		tempFrame.setSize(850, 400);
		
		// add view info to JFrame
		MakeGuessGUI gui = new MakeGuessGUI();
		tempFrame.add(gui, BorderLayout.CENTER);
		
		tempFrame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// getting the name of the card for the button that was pressed.
		Pattern pattern = Pattern.compile("\\btext.*,");
		Matcher matcher = pattern.matcher(e.getSource().toString());
		matcher.find();
		
		String buttonText = matcher.group(0).substring(5, matcher.group(0).length()-1);
		
		// building a guess from the player's selected cards
		switch (e.getActionCommand()) {
		case "roomButtonPressed":
			guess.setRoom(theInstance.getRoomCard(buttonText));
			break;
		case "personButtonPressed":
			guess.setPerson(theInstance.getPersonCard(buttonText));
			break;
		case "weaponButtonPressed":
			guess.setWeapon(theInstance.getWeaponCard(buttonText));
			break;
		case "submitGuessButton":
			
			// checking for a valid guess, then validating for all players
			if (guess.getPerson() == null || guess.getWeapon() == null || guess.getRoom() == null) {
				GameControl.openErrorMessageWindow("Invalid guess, make sure to select one room, one person, and one weapon.");
				break;
			}
			
			if (!GameControl.hasGuessed) {
				Card answer = theInstance.handleSuggestion(guess, GameControl.currPlayer, theInstance.getPlayers());
				GameControl.hasGuessed = true;
				
				for (Player player: theInstance.getPlayers()) {
					if (player.getName().equals(guess.getPerson().getCardName())) {
						player.setLocation(GameControl.currPlayer.getRow(), GameControl.currPlayer.getCol());
						ClueGameGUI.board.repaint();
					}
				}
				
				if (answer != null) {
					GameControl.currPlayer.updateKnown(answer);
					guessResult.setText(answer.getCardName());
				} else {
					guessResult.setText("No one could disprove.");
				}
			} else {
				GameControl.openErrorMessageWindow("You have already made a guess this round.");
			}
			
			break;
		default:
			break;
		}
		
		// default printing for when the player hasn't selected an option for that field
		String roomSelection, personSelection, weaponSelection;
		if (guess.getRoom() != null) roomSelection = guess.getRoom().getCardName(); else roomSelection = "NO ROOM SELECTED";
		if (guess.getPerson() != null) personSelection = guess.getPerson().getCardName(); else personSelection = "NO PERSON SELECTED";
		if (guess.getWeapon() != null) weaponSelection = guess.getWeapon().getCardName(); else weaponSelection = "NO WEAPON SELECTED";
		
		// displaying current selection
		submissionEntry.setText(roomSelection + ", " + personSelection + ", " + weaponSelection);
		
		this.revalidate();
		this.repaint();
	}
}
