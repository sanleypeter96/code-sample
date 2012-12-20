package main;

import java.util.concurrent.locks.Lock;

public class Game {
	private static Game m_game;
	private Board m_board; 
	private Players m_players; 
	private Player m_lastPlayer; 
	//the last player is defined as someone who has just placed a piece
	
	public static Game getInstance()
	{
		if (null == m_game)
		{
			m_game = new Game();
		}	
		return m_game; 
	}
	
	public Game()
	{
		m_board = new Board();
		m_players = new Players(Constants.DEFAULT_NUM_PLAYERS);
		m_lastPlayer = m_players.getComputerPlayer(); 
		//assume computer player has just played at the beginning of the game
	}
	
	public Board getBoard()
	{
		return m_board;
	}
	
	public Players getPlayers()
	{
		return m_players;
	}
	
	public void reset()
	{
		m_board.reset();
		m_lastPlayer = m_players.getComputerPlayer(); 
	}
	
	public boolean isLocationEmpty(int row, int column)
	{
		return m_board.getPiece(row, column) == Piece.Empty;
	}
	
	public void switchPlayer()
	{
		m_lastPlayer = m_lastPlayer == m_players.getComputerPlayer() ? 
				m_players.getHumanPlayer() : m_players.getComputerPlayer();
	}	 
	
	/*
	 * Current player is defined as a player who has not yet to play
	 * We would say "it's current player's turn." 
	 */
	public Player getCurrentPlayer()
	{
		return m_lastPlayer == m_players.getComputerPlayer() ? 
				m_players.getHumanPlayer() : m_players.getComputerPlayer();
	}
	
	public Player getLastPlayer()
	{
		return m_lastPlayer; 
	}
	
	public boolean isFull()
	{
		return m_board.isFull();
	}
}
