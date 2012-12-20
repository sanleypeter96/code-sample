package AI;

import main.Board;
import main.Game;
import main.Location;
import main.Piece;

public class SequentialStrategy implements Strategy {
	
	public Location decideNextLocationToPlay(Location loc)
	{
		//find the first empty cell and put it there
		Board board = Game.getInstance().getBoard();
		for (int i = 0; i < board.getRows(); i++)
			for (int j = 0; j < board.getColumns(); j++)
			{
				if (board.getPiece(i, j) == Piece.Empty)
					return new Location(i,j);
			}
		
		return new Location(0,0);
	}
}
