package unittests;

import static org.junit.Assert.assertEquals;
import main.*;
import org.junit.Test;

public class BoardManagerTests {
	
	@Test
	public void it_should_return_horizontal_win()
	{
		Piece[][] board = new Piece[][]{
			{Piece.X, Piece.X, Piece.X},
			{Piece.O, Piece.Empty, Piece.Empty},
			{Piece.O, Piece.O, Piece.Empty}};
		Piece pieceWon = BoardManager.hasWon(board);
		assertEquals(pieceWon, Piece.X);
	}
	
	@Test
	public void it_should_return_horizontal_win_middle()
	{
		Piece[][] board = new Piece[][]{
			{Piece.O, Piece.Empty, Piece.O},
			{Piece.X, Piece.X, Piece.X},
			{Piece.Empty, Piece.Empty, Piece.Empty}};
		Piece pieceWon = BoardManager.hasWon(board);
		assertEquals(pieceWon, Piece.X);
	}
	
	@Test
	public void it_should_return_vertical_win()
	{
		Piece[][] board = new Piece[][]{
				{Piece.X, Piece.O, Piece.O},
				{Piece.X, Piece.Empty, Piece.Empty},
				{Piece.X, Piece.O, Piece.Empty}};
		Piece pieceWon = BoardManager.hasWon(board);
		assertEquals(pieceWon, Piece.X);
	}
	
	@Test
	public void it_should_return_diagonal_win()
	{
		Piece[][] board = new Piece[][]{
				{Piece.X, Piece.O, Piece.O},
				{Piece.O, Piece.X, Piece.Empty},
				{Piece.Empty, Piece.Empty, Piece.X}};
		Piece pieceWon = BoardManager.hasWon(board);
		assertEquals(pieceWon, Piece.X);
	}
	
	@Test
	public void testNone()
	{
		Piece[][] board = new Piece[][]{
				{Piece.X, Piece.X, Piece.O},
				{Piece.O, Piece.X, Piece.Empty},
				{Piece.Empty, Piece.Empty, Piece.O}};
		Piece pieceWon = BoardManager.hasWon(board);
		assertEquals(pieceWon, Piece.Empty);
	} 
}
