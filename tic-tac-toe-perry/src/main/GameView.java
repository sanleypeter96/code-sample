package main;

import java.util.HashMap;
import java.util.Scanner;

public class GameView {
	
	private static GameView m_gameView; 
	private static HashMap<Location, String> m_locationToPositionMap; 
	private static Game m_game; 
	
	public GameView()
	{
		initializeCommandToPositionMap();
		m_game = Game.getInstance();
	}
	
	private void initializeCommandToPositionMap()
	{
		m_locationToPositionMap = new HashMap<Location, String>();
		m_locationToPositionMap.put(new Location(0,0), "upper left");
		m_locationToPositionMap.put(new Location(0,1), "top center");
		m_locationToPositionMap.put(new Location(0,2), "upper right");
		
		m_locationToPositionMap.put(new Location(1,0), "middle left");
		m_locationToPositionMap.put(new Location(1,1), "center");
		m_locationToPositionMap.put(new Location(1,2), "middle right");
		
		m_locationToPositionMap.put(new Location(2,0), "bottom left");
		m_locationToPositionMap.put(new Location(2,1), "bottom center");
		m_locationToPositionMap.put(new Location(2,2), "bottom right");
	}
	
	public static GameView getInstance()
	{
		if (null == m_gameView)
			m_gameView = new GameView();
		
		return m_gameView; 
	}
	
	public void run()
	{
		Scanner console = new Scanner(System.in);
		String command = ""; 
		int commandNumber = -1; 

		printIntroText();
		while (!command.equalsIgnoreCase("q"))
		{
			m_game.getBoard().printBoard();
			printPrompt();
			try 
			{
				command = console.nextLine();
				commandNumber = Integer.parseInt(command);
			}
			catch (Exception e)
			{
				continue; 
			}
			if (commandNumber < 1 || commandNumber > 9)
				continue;
			
			Location inputLocation = new Location(commandNumber, m_game.getBoard());
			
			HumanPlayer humanPlayer = (HumanPlayer)m_game.getPlayers().getHumanPlayer();
			Location humanLocation = humanPlayer.decideNextLocationToPlay(inputLocation);
			boolean success = humanPlayer.canPlacePiece(humanLocation);
			if (!success)
				continue;
			Piece winner = humanPlayer.placePieceAndGetWinner(humanLocation);
			printAction(humanPlayer, humanLocation);
			if (winner == humanPlayer.getPiece())
			{
				printHumanPlayerIsWinner(winner);
				m_game.getBoard().reset(); 
				continue; 
			}
			
			ComputerPlayer computerPlayer = (ComputerPlayer) m_game.getPlayers().getComputerPlayer();
			Location computerLocation = computerPlayer.decideNextLocationToPlay(inputLocation);

			printAction(computerPlayer, computerLocation);
			
			winner = computerPlayer.placePieceAndGetWinner(computerLocation);
			if (winner == computerPlayer.getPiece())
			{
				printComputerPlayerIsWinner(winner); 
				m_game.getBoard().reset(); 
				continue; 
			}
			
			if (m_game.getBoard().isFull())
			{
				printDraw();
				m_game.getBoard().reset(); 
				continue;
			}
		}
	}

	public static void printIntroText()
	{
		System.out.println("Welcome to Tic-Tac-Toe. Please make your move selection by entering");
		System.out.println("a number 1-9 corresponding to the movement key on the right.");
		System.out.println("Press q anytime to quit.");
		System.out.println(); 
	}
	
	public static void printAction(HumanPlayer humanPlayer, Location humanLocation)
	{
		System.out.println();
		System.out.print("You have put an " 
				+ humanPlayer.getPiece() 
				+  " in the "  
				+ m_locationToPositionMap.get(humanLocation) + ". ");
	}
	
	public static void printAction(ComputerPlayer computerPlayer, Location computerLocation)
	{
		System.out.println("I will put an " 
				+ computerPlayer.getPiece()
				+ " in the "  
				+ m_locationToPositionMap.get(computerLocation) + ". ");
	}
		
	public static void printPrompt()
	{
		System.out.println();
		System.out.println("Where to? ");
	}

	public static void printHumanPlayerIsWinner(Piece winner)
	{
		System.out.println("You have beaten my poor AI!");
		printStartNewGame(); 
	}
	
	public static void printComputerPlayerIsWinner(Piece winner)
	{
		System.out.println("My AI has beaten you!");
		printStartNewGame();
	}
	
	public static void printDraw()
	{
		System.out.println("It is a draw!");
		printStartNewGame();
	}
	
	public static void printStartNewGame()
	{		
		m_game.getBoard().printBoard();
		System.out.println("Starting a new game.");
		System.out.println();
	}
}
