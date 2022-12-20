package gui;

import java.awt.*;
import java.util.Random;

import clueGame.*;

public interface QueryForInfoGUI {
	Random rand = new Random();
	
	// Information for colors and fonts
	public static final Color PERSON_COLOR = new Color(163, 226, 124);
	public static final Color WEAPON_COLOR = new Color(76, 195, 248);
	public static final Color ROOM_COLOR = new Color(169, 169, 169);
	public static final Color BACKGROUND_COLOR = Color.GRAY;
	public static final Color BOARD_PATH_COLOR = new Color(16, 98, 16);
	public static final Color BOARD_PATH_TARGET_COLOR = Color.GREEN;
	public static final Color BOARD_ROOM_COLOR = new Color(158, 147, 92);
	public static final Color BOARD_WATER_COLOR = new Color(54, 215, 220);
	public static final Color BOARD_DOOR_COLOR = Color.BLACK;
	public static final Color BOARD_LABEL_COLOR = Color.BLACK;
	public static final Color BOARD_SECRET_PASSAGE_COLOR = new Color(128, 105, 62);
	
	public static final Font LABEL_FONT = new Font("Courier", Font.BOLD, 14);
	
	// Information for default board sizes
	public static final int WIDTH_ON_OPEN = 1394;
	public static final int HEIGHT_ON_OPEN = 760;
	public static final int DEFAULT_BOARD_HEIGHT = 1000;
	public static final int DEFAULT_BOARD_WIDTH = 1000;
	
	// Information for border weights and buffers
	public static final int DOOR_WEIGHT = 3;
	public static final int PLAYER_BUFFER = 2;
	public static final int LABEL_LEFT_SHIFT = 30;
	public static final int STACKED_BUFFER = 5;
	
	// Information for all GUI classes
	Board theInstance = Board.getInstance();
}
