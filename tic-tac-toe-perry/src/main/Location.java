package main;

public class Location{
	private int m_row;
	private int m_column;
	public Location(int r, int c) {
		m_row = r;
		m_column = c;
	}
	
	public Location(int number, Board board)
	{
		number = number - 1;
		m_row = number / board.getRows();
		m_column = number % board.getColumns(); 
	}

	public int getRow() {
		return m_row;
	}
	
	public int getColumn() {
		return m_column;
	}

	public boolean equals(Object obj)
	{
		if (obj == null)
			return false; 
		if (obj == this)
			return true; 
		Location other = (Location)obj; 
		return m_row == other.getRow() && m_column == other.getColumn();
	}
	
	public int hashCode()
	{
		String str = m_row + m_column + "";
		return str.hashCode();
	}
}
