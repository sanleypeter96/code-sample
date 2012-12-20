package main;

import java.util.Scanner;

public class HumanPlayer extends Player {
	public HumanPlayer(Piece piece) {
		super(piece);
	}

	@Override
	public Location decideNextLocationToPlay(Location loc) {
		return loc; 
	}
}
