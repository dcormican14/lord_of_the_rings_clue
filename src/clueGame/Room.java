package clueGame;
import java.util.*;

public class Room {
	private String name;
	private BoardCell centerCell;
	private BoardCell labelCell;
	private BoardCell secretCell;
	private Set<BoardCell> doorList;
	
	public Room (String name) {
		this.doorList = new HashSet<>();
		this.name = name;
	}

	public Room () {
		this.doorList = new HashSet<>();
		this.name = "NONE";
	}
	
	public boolean hasSecretCell () {
		if (secretCell == null) return false;
		return true;
	}
	
	// Variable setter methods
	public void addDoor (BoardCell door) {
		this.doorList.add(door);
	}
	
	public void setName (String name) {
		this.name = name;
	}
	
	public void setCenterCell (BoardCell centerCell) {
		this.centerCell = centerCell;
	}
	
	public void setLabelCell (BoardCell labelCell) {
		this.labelCell = labelCell;
	}
	
	public void setSecretCell (BoardCell secretCell) {
		this.secretCell = secretCell;
	}
	
	// Variable getter methods
	public String getName () {
		return name;
	}

	public BoardCell getCenterCell () {
		return centerCell;
	}

	public BoardCell getLabelCell () {
		return labelCell;
	}

	public BoardCell getSecretCell () {
		return secretCell;
	}
	
	public Set<BoardCell> getDoorList () {
		return doorList;
	}
}
