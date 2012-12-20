package GUI;

import java.util.Observable;
import main.*;

public class GUIGame extends Observable {
	private Game m_game; 

	public GUIGame()
	{
		m_game = Game.getInstance(); 
	}

	public Players getPlayers()
	{
		return m_game.getPlayers();
	}
	
	public void switchPlayer()
	{
		m_game.switchPlayer();
	}
	
	public Player getCurrentPlayer()
	{
		return m_game.getCurrentPlayer();
	}
	
	public Player getLastPlayer()
	{
		return m_game.getLastPlayer();
	}
	
	public boolean isLocationEmpty(int row, int column)
	{
		return m_game.isLocationEmpty(row, column);
	}
	
	public void reset()
	{
		m_game.reset();
	}
	
	public boolean isFull()
	{
		return m_game.isFull();
	}

	public void placePiece(int row, int column)
	{
		Piece piece = m_game.getCurrentPlayer().placePieceAndGetWinner(row, column);
		
		switchPlayer(); 
		setChanged();
		if (piece != Piece.Empty)
		{
			if (piece == Piece.X)
				m_game.getPlayers().playerXWins();
			else 
				m_game.getPlayers().playerOWins();
			
			notifyObservers(1);
		}
		else if (isFull())
		{
			//tie 
			notifyObservers(2);
		}
		else 
		{
			//continuing the game
			notifyObservers();
		}
	}
}
