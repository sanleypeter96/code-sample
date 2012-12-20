package main;

public class Players {
	private Player[] m_players;
	private final static int m_numPlayers = Constants.DEFAULT_NUM_PLAYERS;  
	
	public Players()
	{
		this(m_numPlayers);
	}
	
	public Players(int numPlayers)
	{
		m_players = new Player[numPlayers];
		m_players[0] = new HumanPlayer(Piece.X);
		m_players[1] = new ComputerPlayer(Piece.O);
	}
	
	public void reset()
	{
		for (Player p : m_players)
		{
			p.reset(); 
		}
	}
	
	public void playerXWins()
	{
		updateWinLosses(Piece.X);
	}
	
	public void playerOWins()
	{
		updateWinLosses(Piece.O);
	}
	
	public void updateWinLosses(Piece winningPiece)
	{
		Player winningPlayer = getPlayerByPiece(winningPiece);
		winningPlayer.addWins();
		for (Player p : m_players)
		{
			if (p != winningPlayer)
				p.addLosses();
		}
	}
	
	private Player getPlayerByPiece(Piece piece)
	{
		for (Player p : m_players)
		{
			if (p.getPiece() == piece)
				return p; 
		}
		return null; 
	}
	
	public Player getHumanPlayer()
	{
		return m_players[0];
	}
	
	public Player getComputerPlayer()
	{
		return m_players[1];
	}
	
	public Player getPlayer(int i)
	{
		return m_players[i-1];
	}
	
}
