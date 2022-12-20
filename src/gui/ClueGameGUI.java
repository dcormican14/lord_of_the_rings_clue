package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import clueGame.Board;

public class ClueGameGUI extends JFrame implements QueryForInfoGUI{
	public static BoardGUI board;
	public static GameControl gcg;
	
	public ClueGameGUI() {
		theInstance.defaultSetup();
		setTitle("Clue Board of Middle Earth");
		setSize(WIDTH_ON_OPEN, HEIGHT_ON_OPEN);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.createLayout();
	}

	private void createLayout() {
		board = new BoardGUI();
		add(board, BorderLayout.CENTER);
		
		gcg = new GameControl();
		add(gcg, BorderLayout.EAST);
		
	}
	
	private void displaySplashScreen() {
		JFrame splashScreen = new JFrame();
		splashScreen.setTitle("Welcome to Clue");
		splashScreen.setSize(350, 150);
		splashScreen.setLayout(new GridLayout(4, 1));
		// add window contents
		splashScreen.add(new JLabel("Hello, welcome to Clue."));
		splashScreen.add(new JLabel("You are " + theInstance.getHumanPlayers().get(0).getName()));
		splashScreen.add(new JLabel("Can you find out who stole the ring before the others do?"));
		
		splashScreen.setVisible(true);
	}

	public static void main(String[] args) {
		ClueGameGUI cg = new ClueGameGUI();
		cg.setVisible(true);
		cg.displaySplashScreen();
	}
}
