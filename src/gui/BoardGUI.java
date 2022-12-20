package gui;

import clueGame.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.border.Border;
import java.util.*;

public class BoardGUI extends JPanel implements QueryForInfoGUI, MouseListener{

	public BoardGUI() {
		this.addMouseListener(this);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// set the height and width each time the board needs to be drawn
		int height, width;
		if((height = this.getHeight()) == 0) height = DEFAULT_BOARD_HEIGHT;
		if((width = this.getWidth()) == 0) width = DEFAULT_BOARD_WIDTH;
		theInstance.setWindowSize(height, width);
		
		this.setBackground(BACKGROUND_COLOR);
		
		// set the base board
		for (ArrayList<BoardCell> cells: theInstance.getBoard()) {
			for (BoardCell cell: cells) {
				// draw each cell
				cell.drawCell(g);
			}
		}
		
		// add decorations and pieces on top
		for (ArrayList<BoardCell> cells: theInstance.getBoard()) {
			for (BoardCell cell: cells) {
				// decorate each cell
				cell.addDecoration(g);
			}
		}
		
		// add players
		for (Player player: theInstance.getPlayers()) {
			player.drawPlayer(g);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// get which cell is selected by the user
		int cellWidthDiff = getWidth() - (int)(getWidth()/theInstance.getBoard().get(0).size()) * theInstance.getBoard().get(0).size();
		int cellHeightDiff = getHeight() - (int)(getHeight()/theInstance.getBoard().size()) * theInstance.getBoard().size();
		
		int cellCol = theInstance.getBoard().get(0).size() * (int)e.getX()/(getWidth()-cellWidthDiff);
		int cellRow = theInstance.getBoard().size() * (int)e.getY()/(getHeight()-cellHeightDiff);
		
		if (GameControl.currPlayer.isHumanPlayer) {
			if  (!GameControl.rollButtonAvailable) {
				if (theInstance.getTargets().contains(theInstance.getCell(cellRow, cellCol))) {
					if(!GameControl.hasMoved) {
						GameControl.hasMoved = true;
						GameControl.currPlayer.setLocation(cellRow, cellCol);
						
						this.revalidate();
						this.repaint();
						
						if (theInstance.getCell(GameControl.currPlayer.getRow(), GameControl.currPlayer.getCol()).isRoomCenter()) {
							MakeGuessGUI.openGuessWindow();
						}
					} else {
						GameControl.openErrorMessageWindow("You have already moved.");
					}
				} else {
					GameControl.openErrorMessageWindow("Invalid target for move.");
				}
			} else {
				GameControl.openErrorMessageWindow("Roll before selecting targets.");
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// Do nothing
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// Do nothing
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// Do nothing
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// Do nothing
		
	}
}