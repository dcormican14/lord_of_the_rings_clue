package clueGame;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;
import javax.swing.*;

import gui.QueryForInfoGUI;

public class BoardCell extends JPanel implements QueryForInfoGUI {
	private Set<BoardCell> adjacentCells;
	private boolean isRoom;
	private boolean isOccupied;
	private boolean isDoorway;
	private boolean isRoomLabel;
	private boolean isRoomCenter;
	private boolean isTargeted;
	private int row;
	private int col;
	private char initial;
	private char secretPassage;
	private DoorDirection doorDirection;
	private Room room;

	public BoardCell (int row, int col) {
		adjacentCells = new HashSet<BoardCell>();
		this.row = row;
		this.col = col;
	}
	
	public void drawCell(Graphics g) {
		int cellWidth = theInstance.getWidthGUI() / theInstance.getBoard().get(0).size();
		int cellHeight = theInstance.getHeightGUI() / theInstance.getBoard().size();
		int cellXLoc = this.getCol() * cellWidth;
		int cellYLoc = this.getRow() * cellHeight;
		
		// Sets the color
		switch (this.getRoom().getName().toUpperCase()) {
		case "PATH":
			if (this.isTargeted) {
				g.setColor(BOARD_PATH_TARGET_COLOR); 
			} else {
				g.setColor(BOARD_PATH_COLOR);
			}
			g.fill3DRect(cellXLoc, cellYLoc, cellWidth, cellHeight, true);
			break;
		case "WATER":
			g.setColor(BOARD_WATER_COLOR);
			g.fillRect(cellXLoc, cellYLoc, cellWidth, cellHeight);
			break;
		default:
			if (this.isTargeted) g.setColor(BOARD_PATH_TARGET_COLOR); else g.setColor(BOARD_ROOM_COLOR);
			g.fillRect(cellXLoc, cellYLoc, cellWidth, cellHeight);
			break;
		}
	}
	
	// Adds any decoration such as doors and text
	public void addDecoration(Graphics g) {
		int cellWidth = theInstance.getWidthGUI() / theInstance.getBoard().get(0).size();
		int cellHeight = theInstance.getHeightGUI() / theInstance.getBoard().size();
		int cellXLoc = this.getCol() * cellWidth;
		int cellYLoc = this.getRow() * cellHeight;
		
		// adds doors
		if (this.isDoorway()) {
			g.setColor(BOARD_DOOR_COLOR);
			switch (doorDirection) {
			case UP:
				g.fillRect(cellXLoc, cellYLoc - DOOR_WEIGHT, cellWidth, DOOR_WEIGHT);
				break;
			case DOWN:
				g.fillRect(cellXLoc, cellYLoc + cellHeight, cellWidth, DOOR_WEIGHT);
				break;
			case LEFT:
				g.fillRect(cellXLoc - DOOR_WEIGHT, cellYLoc, DOOR_WEIGHT, cellHeight);
				break;
			case RIGHT:
				g.fillRect(cellXLoc + cellWidth, cellYLoc, DOOR_WEIGHT, cellHeight);
				break;
			default:
				break;
			}
		}
		
		if (this.isRoomCenter()) {
			g.setColor(BOARD_LABEL_COLOR);
			g.setFont(LABEL_FONT);
			g.drawString(this.room.getName(), cellXLoc-LABEL_LEFT_SHIFT, cellYLoc);
		}
		
		if ((int)this.secretPassage != 0) {
			g.setColor(BOARD_SECRET_PASSAGE_COLOR);
			g.fillRect(cellXLoc, cellYLoc, cellWidth, cellHeight);
			
			g.setColor(BOARD_LABEL_COLOR);
			g.setFont(LABEL_FONT);
			g.drawString(this.secretPassage + "", cellXLoc + 2, cellYLoc + cellHeight - 2);
		}
	}
	
	// Variable setter methods
	public void addAdjacency (BoardCell cell) {
		adjacentCells.add(cell);
	}
	
	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public void setInitial (char initial) {
		this.initial = initial;
	}
	
	public void setOccupied (boolean isOccupied) {
		this.isOccupied = isOccupied;
	}
	
	public void setCenter (boolean isCenter) {
		this.isRoomCenter = isCenter;
	}
	
	public void setLabel (boolean isLabel) {
		this.isRoomLabel = isLabel;
	}
	
	public void setDoorway (boolean isDoorway) {
		this.isDoorway = isDoorway;
	}
	
	public void setDoorDirection (DoorDirection direction) {
		this.doorDirection = direction;
	}
	
	public void setRoom (boolean isRoom) {
		this.isRoom = isRoom;
	}
	
	public void setSecretPassage (char secretPassage) {
		this.secretPassage = secretPassage;
	}
	
	// variable getter methods
	public Set<BoardCell> getAdjList () {
		return adjacentCells;
	}
	
	public DoorDirection getDoorDirection () {
		return doorDirection;
	}
	
	public char getInitial () {
		return initial;
	}
	
	public char getSecretPassage () {
		return secretPassage;
	}
	
	public int getRow () {
		return row;
	}

	public int getCol () {
		return col;
	}

	public boolean isRoom () {
		return isRoom;
	}
	
	public boolean isOccupied () {
		return isOccupied; 
	}

	public boolean isDoorway () {
		return isDoorway;
	}

	public boolean isLabel () {
		return isRoomLabel;
	}
	
	public boolean isRoomCenter () {
		return isRoomCenter;
	}

	public boolean isTargeted() {
		return isTargeted;
	}

	public void setTargeted(boolean isTargeted) {
		this.isTargeted = isTargeted;
	}
	
}
