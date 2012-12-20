package main;

public class Board {
	
	private Piece[][] m_board; 
	private int m_rows;
	private int m_columns;
	
	private final String[] m_helpText = new String[] {"1|2|3", "4|5|6", "7|8|9"};
	
	public Board()
	{
		this(Constants.DEFAULT_BOARD_ROWS, Constants.DEFAULT_BOARD_COLUMNS);
	}
	
	public Board(int rows, int columns)
	{
		m_rows = rows;
		m_columns = columns; 
		m_board = new Piece[rows][columns];
		reset();
	}
	
	public boolean canPlacePiece(int row, int column, Piece piece)
	{
		if (null == piece) 
			return false;
		
		if (!areValidIndices(row, column)) 
			return false; 
		
		if (m_board[row][column] == null || m_board[row][column] != Piece.Empty)
			return false;
		
		return true; 
	}
	
	public Piece placePieceAndGetWinner(int number, Piece piece)
	{
		Location loc = new Location(number, this);
		return placePieceAndGetWinner(loc.getRow(), loc.getColumn(), piece);
	}
	
	public Piece placePieceAndGetWinner(int row, int column, Piece piece)
	{
		if (!canPlacePiece(row, column, piece))
			return Piece.Empty; 
		
		m_board[row][column] = piece;
		return getWinningPiece(); 
	}
	
	public Piece getWinningPiece()
	{
		return BoardManager.hasWon(m_board);
	}
	
	public void reset()
	{
		for (int i = 0; i < m_rows; i++)
			for (int j = 0; j < m_columns; j++)
			{
				m_board[i][j] = Piece.Empty;
			}
	}

	public void printBoard()
	{
		System.out.println("Board:\t\tMovement Key:");
		int n = m_board.length;
		
		for (int i = 0; i < n; i++)
		{
			for (int j = 0; j < n; j++)
			{
				if (m_board[i][j] == Piece.Empty)
					System.out.print(" ");
				else if (m_board[i][j] == Piece.X)
					System.out.print("X");
				else if (m_board[i][j] == Piece.O)
					System.out.print("O");
				if (j != n -1 )
					System.out.print("|");
			}
			System.out.print("\t\t" + m_helpText[i]);
			System.out.println();
		}
	}
	
	public boolean areValidIndices(int row, int column)
	{
		if (row >= m_rows || column >= m_columns || row < 0 || column < 0)
			return false; 
		
		return true; 
	}
	
	public Piece getPiece(int row, int column)
	{
		if (!areValidIndices(row, column)) return Piece.Empty; 
		return m_board[row][column];
	}

	public int getRows()
	{
		return m_rows;
	}
	
	public int getColumns()
	{
		return m_columns; 
	}
	
	public boolean isFull()
	{
		boolean isFull = true; 
		for (int i = 0; i < m_rows; i++)
			for (int j = 0; j < m_columns; j++)
			{
				if (m_board[i][j] == Piece.Empty)
					isFull = false;
			}
		return isFull; 
	}
}
