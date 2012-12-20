package main;

import GUI.GUIView;

public class TicTacToeMain {
	public static void main(String[] args)
	{
		runTextGame();
	}
	
	public static void runTextGame()
	{
		GameView gameView = GameView.getInstance();
		gameView.run();
	}
	
	public static void runGUIGame()
	{
		GUIView.getInstance();
	}
}
