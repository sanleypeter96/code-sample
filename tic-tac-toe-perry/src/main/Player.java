package main;

public abstract class Player {
	private Piece m_piece;
	private String m_name;
	private int m_wins;
	private int m_losses;
	
	public abstract Location decideNextLocationToPlay(Location loc); 
	
	public Player(Piece piece)
	{
		m_piece = piece;
	}
	
	public boolean IsWinner()
	{
		Piece winningPiece = Game.getInstance().getBoard().getWinningPiece();
		return winningPiece == m_piece; 
	}
	
	public boolean canPlacePiece(Location location)
	{
		return canPlacePiece(location.getRow(), location.getColumn());
	}
	
	public boolean canPlacePiece(int row, int column)
	{
		return Game.getInstance().getBoard().canPlacePiece(row, column, m_piece);
	}
	
	public Piece placePieceAndGetWinner(Location location)
	{
		return placePieceAndGetWinner(location.getRow(), location.getColumn());
	}
	
	public Piece placePieceAndGetWinner(int row, int column)
	{
		return Game.getInstance().getBoard().placePieceAndGetWinner(row, column, m_piece);
	}
	
	public Piece getPiece()
	{
		return m_piece; 
	}
	
	public String getName()
	{
		return m_name; 
	}
	
	public void setName(String name)
	{
		m_name = name;
	}
	
	public int getWins()
	{
		return m_wins;
	}
	
	public int getLosses()
	{
		return m_losses; 
	}
	
	public void addWins()
	{
		m_wins++;
	}
	
	public void addLosses()
	{
		m_losses++; 
	}
	
	public void reset()
	{
		m_wins = 0;
		m_losses = 0; 
	}
}
