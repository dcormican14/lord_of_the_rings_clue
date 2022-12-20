package gui;

import clueGame.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import java.util.*;

public class GameControl extends JPanel implements QueryForInfoGUI, ActionListener{
	
	public static Player currPlayer;
	public static boolean rollButtonAvailable;
	public static boolean hasMoved;
	public static boolean hasGuessed;
	public static SoundHandler sh = new SoundHandler();
	
	private boolean accusationButtonAvailable;
	private boolean nextButtonAvailable;
	private String guess;
	private String result;
	private int roll;
	private int previousCol;
	private int previousRow;
	
	private JTextField rollTracker;
	private JTextField guessTracker;
	private JTextField guessResultTracker;
	private JTextField turnTracker;
	
	
	
	public GameControl()
	{
		currPlayer = theInstance.getPlayers().get(0);
		
		previousCol = currPlayer.getCol();
		previousRow = currPlayer.getRow();
		rollButtonAvailable = true;
		accusationButtonAvailable = true;
		nextButtonAvailable = true;
		hasMoved = false;
		hasGuessed = false;
		
		this.createWindow();
		GameControl.playBackgroundMusic();
	}
	
	public static void playBackgroundMusic() {
		sh.setFile(0);
		sh.play();
		sh.loop();
	}
	
	public static void stopMusic() {
		sh.stop();
	}
	
	public static void playSoundEffect(int i) {
		sh.setFile(i);
		sh.play();
	}
	
	// setter to update data
	public void refreshWindow() {
		this.removeAll();
		this.createWindow();
	}
	
	private void createWindow() {
		// Create a layout with 2 rows
		setLayout(new GridLayout(1,1));
		JPanel wrapper = createInfoAndButtons();
		add(wrapper);
	}
	
	private JPanel createInfoAndButtons() {
		// creates info button
		JButton infoButton = this.createGenericButton("View Information");
		infoButton.addActionListener(this);
		infoButton.setActionCommand("infoButtonPressed");
		
		JButton rollButton = this.createGenericButton("Roll");
		rollButton.addActionListener(this);
		rollButton.setActionCommand("rollButtonPressed");
		
		JButton guessButton = this.createGenericButton("Make a Guess");
		guessButton.addActionListener(this);
		guessButton.setActionCommand("guessButtonPressed");
		
		JButton accusationButton = this.createGenericButton("Make an Accusation");
		accusationButton.addActionListener(this);
		accusationButton.setActionCommand("accusationButtonPressed");
		
		JButton turnButton = this.createGenericButton("Next Player");
		turnButton.addActionListener(this);
		turnButton.setActionCommand("nextButtonPressed");
		
		// generate the panel
		JPanel buttonPanel = new JPanel(new GridLayout(8, 1));
		buttonPanel.setBackground(BACKGROUND_COLOR);
		
		// add order
		buttonPanel.add(this.createTurnTracker());
		buttonPanel.add(this.createRollTracker());
		buttonPanel.add(this.createGuessTracker());
		buttonPanel.add(infoButton);
		buttonPanel.add(rollButton);
		buttonPanel.add(guessButton);
		buttonPanel.add(accusationButton);
		buttonPanel.add(turnButton);
		
		return buttonPanel;
	}

	private JPanel createTurnTracker() {
		 // set up field information
		 JLabel turnTrackerLabel = new JLabel("Current Player: ");
		 
		 // configure the current player information and colors
		 turnTracker = new JTextField();
		 this.buildTurnTrackerTextField();
		 
		 // generate the panel
		 JPanel turnTrackerPanel = new JPanel();
		 turnTrackerPanel.setBackground(BACKGROUND_COLOR);
		 
		 turnTrackerPanel.add(turnTrackerLabel);
		 turnTrackerPanel.add(turnTracker);
		 
		 return turnTrackerPanel;
	}
	
	private void buildTurnTrackerTextField() {
		turnTracker.setText(currPlayer.getName());
		turnTracker.setBackground(currPlayer.getColor());
		if (currPlayer.getColor().equals(Color.BLACK)) turnTracker.setForeground(Color.WHITE); else turnTracker.setForeground(Color.BLACK);
	}
	 
	private JPanel createGuessTracker() {
		// create first field
		JLabel guessTrackerLabel = new JLabel("Guess: ");
		guessTracker = new JTextField(11);
		
		JPanel guessPanel = new JPanel();
		guessPanel.setBackground(BACKGROUND_COLOR);
		guessPanel.add(guessTrackerLabel);
		guessPanel.add(guessTracker);
		
		// create second field
		JLabel guessResultTrackerLabel = new JLabel("Result: ");
		guessResultTracker = new JTextField(11);
		
		JPanel guessResultPanel = new JPanel();
		guessResultPanel.setBackground(BACKGROUND_COLOR);
		guessResultPanel.add(guessResultTrackerLabel);
		guessResultPanel.add(guessResultTracker);
		
		// create the panel
		JPanel guessAndResultPanel = new JPanel(new GridLayout(2, 1));
		guessAndResultPanel.setBackground(BACKGROUND_COLOR);
		guessAndResultPanel.add(guessPanel);
		guessAndResultPanel.add(guessResultPanel);
		
		return guessAndResultPanel;
	}
	
	 private JPanel createRollTracker() {
		// create first field
		JLabel rollTrackerLabel = new JLabel("Roll: ");
		rollTracker = new JTextField();
		
		JPanel guessPanel = new JPanel(new GridLayout(1, 2));
		guessPanel.setBackground(BACKGROUND_COLOR);
		guessPanel.add(rollTrackerLabel);
		guessPanel.add(rollTracker);
		
		return guessPanel;
	}
	 
	private JButton createGenericButton(String name) {
		JButton button = new JButton(name);
		button.setBackground(Color.BLACK);
		button.setForeground(Color.WHITE);
		button.setPreferredSize(new Dimension(175, 50));
		return button;
	}
	
	public Player getCurrPlayer() {
		return currPlayer;
	}

	public void setCurrPlayer(Player currPlayer) {
		this.currPlayer = currPlayer;
	}

	public String getGuess() {
		return guess;
	}

	public void setGuess(String guess) {
		this.guess = guess;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public static void openErrorMessageWindow(String errorMessage) {
		GameControl.playSoundEffect(3);
		
		JFrame errorScreen = new JFrame();
		errorScreen.setTitle("ERROR");
		errorScreen.setSize(350, 100);
		errorScreen.setLayout(new GridLayout(2, 1));
		
		// add window contents
		errorScreen.add(new JLabel("Invalid selection."));
		errorScreen.add(new JLabel(errorMessage));
		
		errorScreen.setVisible(true);
	}
	
	public void handleComputerTurn() {
		// roll the dice
		Random rand = new Random();
		roll = rand.nextInt(10) + 2;
		rollTracker.setText(((Integer)roll).toString());
		
		// move the CPU
		BoardCell computerTarget = currPlayer.selectTarget(roll);
		currPlayer.setCol(computerTarget.getCol());
		currPlayer.setRow(computerTarget.getRow());
		
		// if only 3 unknown cards, make accusation
		if (currPlayer.getAccusationState() != null) {
			Solution accusation = new Solution();
			for (Card card: currPlayer.getUnknownCards()) {
				switch (card.getCardType()) {
				case PERSON:
					accusation.setPerson(card);
					break;
				case WEAPON: 
					accusation.setWeapon(card);
					break;
				case ROOM: 
					accusation.setRoom(card);
					break;
				default:
					break;
				}
			}
			if(theInstance.checkAccusation(accusation)) {
				JFrame errorScreen = new JFrame();
				errorScreen.setTitle("GAME OVER");
				errorScreen.setSize(350, 100);
				errorScreen.add(new JLabel(currPlayer.getName() + " wins!"));
				errorScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				errorScreen.setVisible(true);
			} else {
				JFrame errorScreen = new JFrame();
				errorScreen.setTitle("REMINDER");
				errorScreen.setSize(350, 100);
				errorScreen.add(new JLabel(currPlayer.getName() + " accused " + accusation.toString() +  " and lost."));
				errorScreen.setVisible(true);
				
				currPlayer.setLost(true);
			}
		} else {
			// if in room, make a guess
			Solution guess;
			if (computerTarget.isRoom()) {
				guess = currPlayer.createSuggestion();
				guessTracker.setText(guess.toString());
				
				Card provenCard = theInstance.handleSuggestion(guess, currPlayer, theInstance.getPlayers());
				
				for (Player player: theInstance.getPlayers()) {
					if (player.getName().equals(guess.getPerson().getCardName())) {
						player.setLocation(currPlayer.getRow(), currPlayer.getCol());
						ClueGameGUI.board.repaint();
					}
				}
				
				if (provenCard != null) {
					guessResultTracker.setText("Guess was disproven."); 
				} else {
					guessResultTracker.setText("No one could disprove.");
					currPlayer.setAccusationState(guess);
				}
			} else {
				guessTracker.setText("");
				guessResultTracker.setText("");
			}

		}
	}
	
	public void handleNextButton() {
		if (currPlayer.isHumanPlayer) {
			// if current player hasn't moved, then we cannot go to the next player
			if (previousCol != currPlayer.getCol() || previousRow != currPlayer.getRow()) {
				int nextPlayerIndex = (theInstance.getPlayers().indexOf(currPlayer) + 1) % theInstance.getPlayers().size();
				previousCol = currPlayer.getCol();
				previousRow = currPlayer.getRow();
				
				currPlayer = theInstance.getPlayers().get(nextPlayerIndex);
				
				// update current player text field
				this.buildTurnTrackerTextField();
				
				// revert target cells and status variables
				for (BoardCell cell: theInstance.getTargets()) cell.setTargeted(false);
				
				hasMoved = false;
				hasGuessed = false;
				rollButtonAvailable = true;
				accusationButtonAvailable = true;
				nextButtonAvailable = true;
			} else {
				GameControl.openErrorMessageWindow("You have not moved yet.");
			}
		} else {
			int nextPlayerIndex = (theInstance.getPlayers().indexOf(currPlayer) + 1) % theInstance.getPlayers().size();
			currPlayer = theInstance.getPlayers().get(nextPlayerIndex);
			
			// update current player text field
			this.buildTurnTrackerTextField();
			previousCol = currPlayer.getCol();
			previousRow = currPlayer.getRow();
			
			
			hasMoved = false;
			hasGuessed = false;
			rollButtonAvailable = true;
			accusationButtonAvailable = true;
			nextButtonAvailable = true;
		}		
		if (!currPlayer.isHumanPlayer && !currPlayer.getLost()) {
			this.handleComputerTurn();
		}
		ClueGameGUI.board.revalidate();
		ClueGameGUI.board.repaint();
	}

	public void handleAccusationButton() {
		if (currPlayer.isHumanPlayer) {
			if (!hasGuessed) {
				MakeAccusationGUI.openGuessWindow();
			} else {
				GameControl.openErrorMessageWindow("You already made a guess this turn.");
			}
		} else {
			GameControl.openErrorMessageWindow("It is not your turn. You cannot make an accusation.");
		}
	}

	public void handleGuessButton() {
		if (currPlayer.isHumanPlayer) {
			if (!hasGuessed) {
				if (theInstance.getCell(currPlayer.getRow(), currPlayer.getCol()).isRoom()) {
					MakeGuessGUI.openGuessWindow();
				} else {
					GameControl.openErrorMessageWindow("You are not in a room. You cannot make a suggestion.");
				}
			} else {
				GameControl.openErrorMessageWindow("You already made a guess this turn.");
			}
		} else {
			GameControl.openErrorMessageWindow("It is not your turn. You cannot make a guess.");
		}
	}
	
	public void handleRollButton() {
		if (currPlayer.isHumanPlayer) {
			if (rollButtonAvailable) {
				rollButtonAvailable = false;
				Random rand = new Random();
				roll = rand.nextInt(10) + 2;
				
				currPlayer.selectTarget(roll);
				rollTracker.setText(((Integer)roll).toString());
			} else {
				GameControl.openErrorMessageWindow("You already rolled this turn.");
			}
			
		} else {
			GameControl.openErrorMessageWindow("It is not your turn. You cannot Roll");
		}
	}

	public void handleViewInfoButton() {
		ViewInfoGUI info = new ViewInfoGUI();
		info.openInfoWindow();
	}
	
	// calls respective button actions when a button is clicked
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "infoButtonPressed":
			this.handleViewInfoButton();
			break;
		case "rollButtonPressed":
			this.handleRollButton();
			ClueGameGUI.board.revalidate();
			ClueGameGUI.board.repaint();
			break;
		case "guessButtonPressed":
			this.handleGuessButton();
			break;
		case "accusationButtonPressed":
			this.handleAccusationButton();
			break;
		case "nextButtonPressed":
			this.handleNextButton();
			break;
		default: 
			break;
		}
		this.revalidate();
		this.repaint();
	}
}
