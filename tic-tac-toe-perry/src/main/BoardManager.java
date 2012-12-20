package main;

public class BoardManager {
	public static Piece hasWon(Piece[][] board)
	{
		if (board.length == 0) return Piece.Empty;
		int N = board.length;
		Piece color = null; 
		
		for(int i = 0; i < N; i++)
		{
			color = getWinner(board, i, WinCondition.Horizontal);
			if (color != Piece.Empty) return color;
			
			color = getWinner(board, i, WinCondition.Vertical);
			if (color != Piece.Empty) return color; 
		}
		
		color = getWinner(board, -1, WinCondition.Diagonal); 
		if (color != Piece.Empty) return color; 
		
		color = getWinner(board, -1, WinCondition.ReverseDiagonal);
		if (color != Piece.Empty) return color;
		
		return Piece.Empty; 	
	}
	
	private static Piece getWinner(Piece[][] board, int fixed_index, WinCondition condition)
	{	
		Piece winnerPiece = getPiece(board, fixed_index, 0, condition);
		if (winnerPiece == Piece.Empty) return Piece.Empty;
		for (int var = 1; var < board.length; var++)
		{
			if (winnerPiece != getPiece(board, fixed_index, var, condition) ) 
			{
				return Piece.Empty;
			}
		}
		return winnerPiece; 
	}
	
	private static Piece getPiece(Piece[][] board, int index, int var, WinCondition condition)
	{
		if (board.length == 0) return Piece.Empty;
		int m = board.length;
		int n = board[0].length;
		if (condition == WinCondition.Horizontal) 
			return board[index][var]; 
		else if (condition == WinCondition.Vertical)
			return board[var][index]; 
		else if (condition == WinCondition.Diagonal)
			return board[var][var];
		else if (condition == WinCondition.ReverseDiagonal)
			return board[var][n-1-var];
		else 
			return Piece.Empty;
	}
}

enum WinCondition { Horizontal, Vertical, Diagonal, ReverseDiagonal }
