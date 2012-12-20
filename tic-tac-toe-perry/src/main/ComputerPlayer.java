package main;

import AI.*;

public class ComputerPlayer extends Player {
	
	private Strategy m_strategy; 
	private Strategy m_defaultStrategy = new SequentialStrategy();  
	
	public ComputerPlayer(Piece piece) {
		super(piece);
		setStrategy(m_defaultStrategy);
	}

	private void setStrategy(Strategy strategy)
	{
		m_strategy = strategy; 
	}
	
	@Override 
	public Location decideNextLocationToPlay(Location loc)
	{
		return m_strategy.decideNextLocationToPlay(loc);
	}
}
