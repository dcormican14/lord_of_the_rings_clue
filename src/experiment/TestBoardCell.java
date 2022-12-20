package experiment;
import java.util.*;

public class TestBoardCell {
	private Set<TestBoardCell> adjacentCells;
	private boolean isRoom;
	private boolean isOccupied;
	private int row;
	private int col;


	public TestBoardCell (int row, int col) {
		adjacentCells = new HashSet<TestBoardCell>();
		this.row = row;
		this.col = col;
	}

	public void addAdjacency (TestBoardCell cell) {
		adjacentCells.add(cell);
	}
	
	public Set<TestBoardCell> getAdjList () {
		return adjacentCells;
	}

	public boolean isRoom() {
		return isRoom;
	}

	public void setRoom (boolean isRoom) {
		this.isRoom = isRoom;
	}
	
	public boolean isOccupied () {
		return isOccupied; 
	}

	public void setOccupied (boolean isOccupied) {
		this.isOccupied = isOccupied;
	}
	
	public int getRow () {
		return row;
	}

	public int getCol () {
		return col;
	}

	@Override
	public String toString () {
		return "[" + row + ", " + col + "]";
	}
}
