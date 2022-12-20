package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class BoardAdjTargetTest {
	// We make the Board static because we can load it one time and 
	// then do all the tests. 
	private static Board board;
	
	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
	}

	// Ensure that player does not move around within room
	// These cells are LIGHT ORANGE on the planning spreadsheet
	@Test
	public void testAdjacenciesRooms()
	{
		// we want to test a couple of different areas on the map
		// Mirkwood will be tested first, make sure it has two doors and a secret passage
		Set<BoardCell> testList = board.getAdjList(14, 49);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(10, 45))); // door
		assertTrue(testList.contains(board.getCell(16, 45))); // door
		assertTrue(testList.contains(board.getCell(1, 30))); // secret passage
		
		// Isengard will now be tested here, checks for two doors
		testList = board.getAdjList(11, 9);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(8, 12))); // door
		assertTrue(testList.contains(board.getCell(13, 13))); // door
		
		// Final room for test is Helm's Deep with four doors
		testList = board.getAdjList(10, 23);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(9, 18))); // door
		assertTrue(testList.contains(board.getCell(6, 23))); // door
		assertTrue(testList.contains(board.getCell(8, 28))); // door
		assertTrue(testList.contains(board.getCell(13, 26))); // door
	}

	
	// Ensure door locations include their rooms and also additional walkways
	// These cells are LIGHT ORANGE on the planning spreadsheet
	@Test
	public void testAdjacencyDoor()
	{
		// Door to Erebor
		Set<BoardCell> testList = board.getAdjList(7, 46);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(3, 48))); // center of room
		assertTrue(testList.contains(board.getCell(7, 45))); // path
		assertTrue(testList.contains(board.getCell(8, 46))); // path
		assertTrue(testList.contains(board.getCell(7, 47))); // path

		// Door to The Shire
		testList = board.getAdjList(4, 12);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(4, 8))); // center of room
		assertTrue(testList.contains(board.getCell(4, 13))); // path
		assertTrue(testList.contains(board.getCell(5, 12))); // path
		
		// Door to Dead Marshes
		testList = board.getAdjList(22, 33);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(26, 34))); // center of room
		assertTrue(testList.contains(board.getCell(22, 32))); // path
		assertTrue(testList.contains(board.getCell(21, 33))); // path
		assertTrue(testList.contains(board.getCell(22, 34))); // path
	}
	
	// Test a variety of pathway scenarios
	// These tests are Dark Orange on the planning spreadsheet
	@Test
	public void testAdjacencyPathways()
	{
		// Middle pathway with four options around it
		Set<BoardCell> testList = board.getAdjList(3, 38);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(3, 37))); // path
		assertTrue(testList.contains(board.getCell(2, 38))); // path
		assertTrue(testList.contains(board.getCell(3, 39))); // path
		assertTrue(testList.contains(board.getCell(4, 38))); // path
		
		// Side of room but not doorway, three options around it
		testList = board.getAdjList(10, 13);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(10, 14))); // path
		assertTrue(testList.contains(board.getCell(9, 13))); // path
		assertTrue(testList.contains(board.getCell(11, 13))); // path

		// Middle pathway with four options around it
		testList = board.getAdjList(18, 28);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(18, 27))); // path
		assertTrue(testList.contains(board.getCell(17, 28))); // path
		assertTrue(testList.contains(board.getCell(18, 29))); // path
		assertTrue(testList.contains(board.getCell(19, 28))); // path

		// Test bottom edge of board
		testList = board.getAdjList(27,40);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(27, 39))); // path
		assertTrue(testList.contains(board.getCell(26, 40))); // path
		assertTrue(testList.contains(board.getCell(27, 41))); // path
	
	}
	
	
	// Tests out of room center, 1, 3 and 4
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsInDeadMarshes() {
		// test a roll of 1
		board.calcTargets(board.getCell(26, 34), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(22, 33))); // path
		assertTrue(targets.contains(board.getCell(25, 38))); // path	

		// test a roll of 3
		board.calcTargets(board.getCell(26, 34), 3);
		targets= board.getTargets();
		assertEquals(10, targets.size());
		assertTrue(targets.contains(board.getCell(20, 33))); // path
		assertTrue(targets.contains(board.getCell(22, 35))); // path	
		assertTrue(targets.contains(board.getCell(25, 40))); // path
		assertTrue(targets.contains(board.getCell(24, 39))); // path	
		
		// test a roll of 4
		board.calcTargets(board.getCell(26, 34), 4);
		targets= board.getTargets();
		assertEquals(20, targets.size());
		assertTrue(targets.contains(board.getCell(25, 41))); // path
		assertTrue(targets.contains(board.getCell(22, 38))); // path	
		assertTrue(targets.contains(board.getCell(22, 36))); // path
		assertTrue(targets.contains(board.getCell(20, 32))); // path	
	}
	
	@Test
	public void testTargetsInRivendell() {
		// this room has a secret passage
		// test a roll of 1
		board.calcTargets(board.getCell(1, 30), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(14, 49))); // path
		assertTrue(targets.contains(board.getCell(3, 22))); // path
		assertTrue(targets.contains(board.getCell(3, 32))); // path
		
		// test a roll of 3
		board.calcTargets(board.getCell(1, 30), 3);
		targets= board.getTargets();
		assertEquals(11, targets.size());
		assertTrue(targets.contains(board.getCell(14, 49))); // Center of Room
		assertTrue(targets.contains(board.getCell(4, 33))); // path
		assertTrue(targets.contains(board.getCell(3, 20))); // path
		assertTrue(targets.contains(board.getCell(5, 32))); // path
		
		// test a roll of 4
		board.calcTargets(board.getCell(1, 30), 4);
		targets= board.getTargets();
		assertEquals(22, targets.size());
		assertTrue(targets.contains(board.getCell(14, 49))); // Center of Room
		assertTrue(targets.contains(board.getCell(4, 22))); // path
		assertTrue(targets.contains(board.getCell(3, 31))); // path
		assertTrue(targets.contains(board.getCell(6, 32)));	// path
	}

	// Test out of a door
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsAtDoor() {
		// test a roll of 1, at door
		board.calcTargets(board.getCell(21, 26), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(22, 21))); // center of room
		assertTrue(targets.contains(board.getCell(22, 26))); // path
		assertTrue(targets.contains(board.getCell(21, 27))); // path
		assertTrue(targets.contains(board.getCell(20, 26))); // path
		
		// test a roll of 2
		board.calcTargets(board.getCell(21, 26), 2);
		targets= board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(19, 26))); // path
		assertTrue(targets.contains(board.getCell(20, 27))); // path
		assertTrue(targets.contains(board.getCell(21, 28))); // path	
		assertTrue(targets.contains(board.getCell(22, 27))); // path
		assertTrue(targets.contains(board.getCell(23, 26))); // path
		assertTrue(targets.contains(board.getCell(22, 21))); // center of room
		
		// test a roll of 3
		board.calcTargets(board.getCell(21, 26), 3);
		targets= board.getTargets();
		assertEquals(11, targets.size());
		assertTrue(targets.contains(board.getCell(24, 26))); // path
		assertTrue(targets.contains(board.getCell(21, 29))); // path
		assertTrue(targets.contains(board.getCell(20, 28))); // path
		assertTrue(targets.contains(board.getCell(22, 21))); // center of room
	}

	@Test
	public void testTargetsInPathway1() {
		// test a roll of 1
		board.calcTargets(board.getCell(5, 17), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(5, 16)));
		assertTrue(targets.contains(board.getCell(4, 17)));	
		assertTrue(targets.contains(board.getCell(5, 18)));
		assertTrue(targets.contains(board.getCell(6, 17)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(5, 17), 3);
		targets= board.getTargets();
		assertEquals(16, targets.size());
		assertTrue(targets.contains(board.getCell(5, 20)));
		assertTrue(targets.contains(board.getCell(3, 18)));
		assertTrue(targets.contains(board.getCell(4, 15)));	
		
		// test a roll of 4
		board.calcTargets(board.getCell(5, 17), 4);
		targets= board.getTargets();
		assertEquals(21, targets.size());
		assertTrue(targets.contains(board.getCell(5, 21)));
		assertTrue(targets.contains(board.getCell(5, 13)));
		assertTrue(targets.contains(board.getCell(8, 18)));	
	}

	@Test
	public void testTargetsInPathway2() {
		// test a roll of 1
		board.calcTargets(board.getCell(19, 51), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(18, 51)));
		assertTrue(targets.contains(board.getCell(19, 50)));	
		assertTrue(targets.contains(board.getCell(20, 51)));
		
		// test a roll of 2
		board.calcTargets(board.getCell(19, 51), 2);
		targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(18, 50)));
		assertTrue(targets.contains(board.getCell(19, 49)));
		assertTrue(targets.contains(board.getCell(20, 50)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(19, 51), 3);
		targets= board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(18, 49)));
		assertTrue(targets.contains(board.getCell(19, 48)));
		assertTrue(targets.contains(board.getCell(20, 49)));	
	}

	@Test
	// test to make sure occupied locations do not cause problems
	public void testTargetsOccupied() {
		// test a roll of 4 blocked 2 down
		board.getCell(5, 25).setOccupied(true);
		board.calcTargets(board.getCell(3, 25), 2);
		board.getCell(5, 25).setOccupied(false);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(3, 23)));
		assertTrue(targets.contains(board.getCell(3, 27)));
		assertTrue(targets.contains(board.getCell(4, 24)));	
		assertFalse(targets.contains(board.getCell(4, 29)));
	
		// we want to make sure we can get into a room, even if flagged as occupied
		board.getCell(17, 19).setOccupied(true);
		board.getCell(22, 21).setOccupied(true);
		board.calcTargets(board.getCell(17, 19), 1);
		board.getCell(22, 21).setOccupied(false);
		board.getCell(17, 19).setOccupied(false);
		targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(17, 20)));	
		assertTrue(targets.contains(board.getCell(17, 18)));	
		assertTrue(targets.contains(board.getCell(16, 19)));	
		
		// check leaving a room with a blocked doorway
		board.getCell(11, 40).setOccupied(true);
		board.calcTargets(board.getCell(13, 36), 1);
		board.getCell(11, 40).setOccupied(false);
		targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(6, 36)));
		assertTrue(targets.contains(board.getCell(12, 32)));	
		assertTrue(targets.contains(board.getCell(19, 37)));

	}
}
