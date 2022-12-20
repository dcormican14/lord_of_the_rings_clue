package test;

import java.util.*;
import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.*;
import experiment.*;

public class BoardTestExp {
	TestBoard gameBoard;
	
	@BeforeEach
	public void setUp() {
		gameBoard = new TestBoard();
	}
	
	@Test
	public void checkAdjacencyLists() {
		// Checks all adjacencies for each corner, and 2 edge cells
		// Top left corner
		TestBoardCell testingCell1 = gameBoard.getCell(0, 0);
		gameBoard.calcTargets(testingCell1, 1);
		assert gameBoard.getTargetsForMove().contains(gameBoard.getCell(1, 0));
		assert gameBoard.getTargetsForMove().contains(gameBoard.getCell(0, 1));
		assertEquals(2, gameBoard.getTargetsForMove().size());
		
		// Top right corner
		TestBoardCell testingCell2 = gameBoard.getCell(0, TestBoard.BOARD_WIDTH-1);
		gameBoard.calcTargets(testingCell2, 1);
		assert gameBoard.getTargetsForMove().contains(gameBoard.getCell(0, TestBoard.BOARD_WIDTH-2));
		assert gameBoard.getTargetsForMove().contains(gameBoard.getCell(1, TestBoard.BOARD_WIDTH-1));
		assertEquals(2, gameBoard.getTargetsForMove().size());
		
		// Bottom right corner
		TestBoardCell testingCell3 = gameBoard.getCell(TestBoard.BOARD_HEIGHT-1, TestBoard.BOARD_WIDTH-1);
		gameBoard.calcTargets(testingCell3, 1);
		assert gameBoard.getTargetsForMove().contains(gameBoard.getCell(TestBoard.BOARD_HEIGHT-1, TestBoard.BOARD_WIDTH-2));
		assert gameBoard.getTargetsForMove().contains(gameBoard.getCell(TestBoard.BOARD_HEIGHT-2, TestBoard.BOARD_WIDTH-1));
		assertEquals(2, gameBoard.getTargetsForMove().size());
		
		// Bottom left corner
		TestBoardCell testingCell4 = gameBoard.getCell(TestBoard.BOARD_HEIGHT-1, 0);
		gameBoard.calcTargets(testingCell4, 1);
		assert gameBoard.getTargetsForMove().contains(gameBoard.getCell(TestBoard.BOARD_HEIGHT-1, 1));
		assert gameBoard.getTargetsForMove().contains(gameBoard.getCell(TestBoard.BOARD_HEIGHT-2, 0));
		assertEquals(2, gameBoard.getTargetsForMove().size());
		
		// Right Edge at the halfway point
		TestBoardCell testingCell5 = gameBoard.getCell(TestBoard.BOARD_HEIGHT/2, TestBoard.BOARD_WIDTH-1);
		gameBoard.calcTargets(testingCell5, 1);
		assert gameBoard.getTargetsForMove().contains(gameBoard.getCell(TestBoard.BOARD_HEIGHT/2, TestBoard.BOARD_WIDTH-2));
		assert gameBoard.getTargetsForMove().contains(gameBoard.getCell(TestBoard.BOARD_HEIGHT/2+1, TestBoard.BOARD_WIDTH-1));
		assert gameBoard.getTargetsForMove().contains(gameBoard.getCell(TestBoard.BOARD_HEIGHT/2-1, TestBoard.BOARD_WIDTH-1));
		assertEquals(3, gameBoard.getTargetsForMove().size());
		
		// Left Edge at the halfway point
		TestBoardCell testingCell6 = gameBoard.getCell(TestBoard.BOARD_HEIGHT/2, 0);
		gameBoard.calcTargets(testingCell6, 1);
		assert gameBoard.getTargetsForMove().contains(gameBoard.getCell(TestBoard.BOARD_HEIGHT/2, 1));
		assert gameBoard.getTargetsForMove().contains(gameBoard.getCell(TestBoard.BOARD_HEIGHT/2+1, 0));
		assert gameBoard.getTargetsForMove().contains(gameBoard.getCell(TestBoard.BOARD_HEIGHT/2-1, 0));
		assertEquals(3, gameBoard.getTargetsForMove().size());
	}
	
	@Test
	public void TestTargetsNormal() {
		// If calcTargets is called, we should see 9 cells that are marked as valid targets
		// Test the movement from the corner with 3 spaces
		TestBoardCell testingCell = gameBoard.getCell(0, 0);
		gameBoard.calcTargets(testingCell, 3);
		Set<TestBoardCell> targets = gameBoard.getTargetsForMove();
		
		assertEquals(9, targets.size());
		assert targets.contains(gameBoard.getCell(3,  0));
		assert targets.contains(gameBoard.getCell(2,  0));
		assert targets.contains(gameBoard.getCell(1,  0));
		
		assert targets.contains(gameBoard.getCell(0,  3));
		assert targets.contains(gameBoard.getCell(0,  2));
		assert targets.contains(gameBoard.getCell(0,  1));
		
		assert targets.contains(gameBoard.getCell(1,  1));
		assert targets.contains(gameBoard.getCell(2,  1));
		assert targets.contains(gameBoard.getCell(1,  2));		
	}
	
	@Test
	public void testTargetsMixed() {
		// We want to make cells not possible to move to to test our movement algorithm
		gameBoard.getCell(0, 2).setOccupied(true); // occupied by another player
		gameBoard.getCell(1, 2).setRoom(true); // made a room
		TestBoardCell testingCell = gameBoard.getCell(0, 3); // starting spot
		
		gameBoard.calcTargets(testingCell, 3);
		Set<TestBoardCell> targets = gameBoard.getTargetsForMove();
		
		assertEquals(10, targets.size());
		assert targets.contains(gameBoard.getCell(1, 3));
		assert targets.contains(gameBoard.getCell(2, 3));
		assert targets.contains(gameBoard.getCell(3, 3));
		
		assert targets.contains(gameBoard.getCell(0, 4));
		assert targets.contains(gameBoard.getCell(1, 4));
		assert targets.contains(gameBoard.getCell(2, 4));
		
		assert targets.contains(gameBoard.getCell(0, 5));
		assert targets.contains(gameBoard.getCell(1, 5));
		
		assert targets.contains(gameBoard.getCell(0, 6));
	}
}
