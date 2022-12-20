package experiment;
import java.util.*;

public class TestBoard {
	public static final int BOARD_HEIGHT = 30;
	public static final int BOARD_WIDTH = 52;
	
	private TestBoardCell[][] gameBoard;
	private Set<TestBoardCell> targetsForMove;
	private Set<TestBoardCell> visitedCells;
	
	public TestBoard () {
		// Board dimensions are accessed with row, col
		// Board is zero indexed
		gameBoard = new TestBoardCell[BOARD_HEIGHT][BOARD_WIDTH];
		
		// Populate gameBoard with null cells
		for (int i=0; i<BOARD_HEIGHT; i++) {
			for (int j=0; j<BOARD_WIDTH; j++) {
				gameBoard[i][j] = new TestBoardCell(i, j);
			}
		}
	}
	
	// Checks each cell adjacent to target cell and adds them to the list recursively
	// If the cell is not a room and isn't occupied, then it is returned
	public void calcTargets (TestBoardCell startCell, int pathLength) {
		targetsForMove = new HashSet<TestBoardCell>();
		visitedCells = new HashSet<TestBoardCell>();
		visitedCells.add(startCell);
		recurOverTargets(startCell, pathLength);
	}
	
	private void recurOverTargets (TestBoardCell startCell, int pathLength) {
		addCellAdjacencies(startCell);
		if (pathLength > 0) { // Check base case
			for (TestBoardCell cell: startCell.getAdjList()) { // Check adjacent cells
				if (!cell.isRoom() && !cell.isOccupied()) { // Checks if we can move to those cells
					if (!visitedCells.contains(cell)) targetsForMove.add(cell); 
					recurOverTargets(cell, pathLength-1);
				}
			}
		}
	}
	
	// This must be done in the board class because it knows where all of the cells are and can access information accross cells
	private void addCellAdjacencies (TestBoardCell targetCell) {
		int row = targetCell.getRow();
		int col = targetCell.getCol();
		
		if (row > 0) targetCell.addAdjacency(gameBoard[row-1][col]);
		if (row < BOARD_HEIGHT-1) targetCell.addAdjacency(gameBoard[row+1][col]);
		if (col > 0) targetCell.addAdjacency(gameBoard[row][col-1]);
		if (col < BOARD_WIDTH-1) targetCell.addAdjacency(gameBoard[row][col+1]);
	}
	
	// Access individual cells
	public TestBoardCell getCell (int row, int col) {
		return gameBoard[row][col];
	}

	public Set<TestBoardCell> getTargetsForMove () {
		return targetsForMove;
	}
}
