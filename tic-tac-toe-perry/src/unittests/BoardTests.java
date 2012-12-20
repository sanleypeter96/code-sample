package unittests;

import static org.junit.Assert.*;
import main.*;
import org.junit.Test;

public class BoardTests {
	@Test
	public void it_should_not_be_able_to_place_pieces_out_of_range()
	{
		Board board = new Board();
		boolean success = board.canPlacePiece(10, 12, Piece.O);
		assertEquals(success, false);
	}
	
	@Test
	public void it_should_not_be_able_to_place_null_pieces()
	{
		Board board = new Board();
		boolean success = board.canPlacePiece(2, 2, null);
		assertEquals(success, false);
	}
	
	@Test
	public void it_should_accept_movement_key_starting_with_1()
	{
		//1 --> 0,0
		//2 --> 0,1
		//3 --> 0,2
		//4 --> 1,0
		//5 --> 1,1
		//6 --> 1,2
		//7 --> 2,0
		//8 --> 2,1
		//9 --> 2,2
		Board board = new Board();
		board.placePieceAndGetWinner(-1, Piece.X);
		assertEquals(board.getPiece(0,0), Piece.Empty);
		
		board.placePieceAndGetWinner(1, Piece.X);
		assertEquals(board.getPiece(0,0), Piece.X);
		board.placePieceAndGetWinner(2, Piece.O);
		assertEquals(board.getPiece(0,1), Piece.O);
		board.placePieceAndGetWinner(3, Piece.X);
		assertEquals(board.getPiece(0,2), Piece.X);
		
		board.placePieceAndGetWinner(4, Piece.X);
		assertEquals(board.getPiece(1,0), Piece.X);
		board.placePieceAndGetWinner(5, Piece.X);
		assertEquals(board.getPiece(1,1), Piece.X);
		board.placePieceAndGetWinner(6, Piece.X);
		assertEquals(board.getPiece(1,2), Piece.X);
		
		board.reset();
		board.placePieceAndGetWinner(7, Piece.X);
		assertEquals(board.getPiece(2,0), Piece.X);
		board.placePieceAndGetWinner(8, Piece.X);
		assertEquals(board.getPiece(2,1), Piece.X);
		board.placePieceAndGetWinner(9, Piece.X);
		assertEquals(board.getPiece(2,2), Piece.X);
	}
	
}
